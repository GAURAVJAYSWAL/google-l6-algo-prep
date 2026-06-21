# A09 — Design a web crawler

This is the most quintessentially Google prompt in the set: take the open web — effectively unbounded, adversarial, and constantly changing — and pull it down completely, politely, and freshly with a fleet of distributed workers. It tests scale (billions of pages), a URL frontier that prioritizes what to fetch next, politeness and `robots.txt` compliance, dedup of both URLs and content, a freshness re-crawl policy, and resilience against crawler traps. Google asks it because the crawler is the front of the entire Search pipeline (crawl → index → serve), and a Staff engineer must own the full ingest loop, not just the HTTP fetch.

## 1) Clarify — questions to ask the interviewer

- **Scope of the web:** The whole open web, a single large site, or a bounded vertical (news, shopping)? This sets frontier size, politeness pressure, and storage by orders of magnitude. I will assume **open-web scale (billions of pages)** unless told otherwise.
- **What is the output?** Raw HTML to a blob store for a downstream indexer to parse, or do we also extract links/text/structured data inline? I will assume we store raw content and emit discovered links back into the frontier.
- **Scale & rate:** Target pages/day to crawl and re-crawl? Total corpus size to maintain? This drives worker count and bandwidth. I'll assume **~10B pages in the corpus, ~1B fetches/day**.
- **Freshness requirements:** Is this a one-shot crawl or a *continuous* crawl that must keep the corpus fresh? News must be minutes-fresh; a static doc can be re-crawled monthly. Continuous + adaptive freshness is the interesting case — I'll assume it.
- **Politeness constraints:** Must we honor `robots.txt`, crawl-delay, and `Crawl-delay` headers, and cap per-host concurrency? (Always yes for a real crawler — getting this wrong gets you IP-banned and is an ethics/legal issue.)
- **Content types:** HTML only, or also PDFs, images, JS-rendered SPAs? Rendering JS needs a headless-browser tier and changes the cost model 10–50×. I'll assume HTML-first with a separate render tier for JS-heavy pages.
- **Politeness vs coverage priority:** When a high-value page lives on a slow/rate-limited host, do we prioritize freshness or back off? This is a real tension.
- **Consistency/dedup needs:** Is approximate dedup (a small false-positive rate via Bloom filter) acceptable, or must "have I seen this URL" be exact? Approximate is almost always fine at this scale.

**What the interviewer is signaling:** A web crawler has no single correct shape — it is a chain of tradeoffs (politeness vs throughput, freshness vs cost, exact vs approximate dedup, breadth-first coverage vs priority). The questions that separate L6 from L5 are **politeness** (juniors forget per-host rate limiting and get the fleet banned), **trap avoidance** (infinite calendars, session-id URLs), and **adaptive re-crawl** (re-crawling everything uniformly is wasteful). Surfacing those unprompted signals you have thought about operating a crawler, not just sketching one.

## 2) Functional Requirements (FR)

**In scope:**
- Fetch a seed set of URLs and recursively discover and fetch linked URLs (BFS-style expansion of the web graph).
- A **URL frontier** that decides *what to crawl next* by priority (importance, freshness need) while respecting per-host politeness.
- Honor `robots.txt` (allow/disallow rules, crawl-delay) and per-host concurrency/rate limits.
- **Dedup**: don't re-fetch a URL already seen (URL dedup); don't store the same content twice under different URLs (content dedup).
- Store fetched content durably for the downstream indexer.
- **Freshness / re-crawl**: revisit pages on a schedule that adapts to how often each page changes.
- **Trap avoidance**: detect and escape infinite/near-infinite URL spaces and malicious mazes.
- Distributed, fault-tolerant workers that scale horizontally and resume after crash.

**Out of scope (defer):**
- Parsing/indexing the content into an inverted index (that is the next stage; we hand off raw content).
- Ranking / PageRank computation (consumes our link graph, separate system).
- JS rendering internals of the headless browser tier (we treat it as a pluggable fetcher).
- Spam/quality classification of pages (a downstream signal).
- Per-page ACL / authenticated crawling (we crawl the public web).

## 3) Non-Functional Requirements (NFR)

| Dimension | Target & rationale |
|---|---|
| Scale | ~10B-page corpus; ~1B fetches/day (~12K fetches/sec sustained, higher peak). Thousands of worker processes across regions. |
| Throughput | Sustain ~12K pages/sec; design headroom to 5× for backfill bursts. Bandwidth, not CPU, is usually the ceiling. |
| Politeness | Hard cap per-host concurrency (often 1–2 in-flight) and honor crawl-delay; never overwhelm a site. This is a correctness + reputational requirement. |
| Availability | 99.9% for the crawl pipeline. The frontier and seen-set are durable; a worker crash loses at most one in-flight fetch (retried). |
| Consistency | **Eventual / best-effort.** Dedup is approximate (Bloom filter, small false-positive rate). The corpus is a derived, ever-updating snapshot, not a transactional store. |
| Durability | 11 nines on stored content + the frontier's durable backlog. Content is the product; losing it means re-crawling (polite, slow, costly). |
| Freshness | Adaptive: high-change pages (news) re-crawled in minutes–hours; static pages in days–weeks. Driven by observed change rate. |
| Politeness compliance | 100% `robots.txt` adherence; cached and refreshed per host. A single violation can get the whole IP range blocked. |

## 4) Back-of-envelope estimation

```
Corpus & fetch rate
  Corpus size:                10e9 pages
  Re-crawl whole corpus / month -> 10e9 / 30 days
                              ~ 3.3e8 pages/day just to refresh
  Plus discovery of new pages -> assume ~1e9 fetches/day total
  Fetches/sec sustained:      1e9 / 86,400 ~ 11,600 /sec  (~12K)
  Peak (backfill/burst):      ~50K /sec  -> size for 5x

Bandwidth
  Avg fetched page (HTML+resources we keep): ~100 KB compressed -> ~500 KB raw
  Ingest bandwidth:           12K * 500 KB ~ 6 GB/s  (~48 Gbps) sustained
  This is the real ceiling -> distribute workers across many egress points

Storage (raw content)
  10e9 pages * 100 KB compressed   ~ 1 PB   for one snapshot
  Keep a few historical versions   -> a few PB
  Link graph: 10e9 pages * ~50 outlinks * ~16 B (id pairs) ~ 8 TB

Frontier size
  Pending URLs at any time can exceed the corpus (lots of undiscovered links):
  assume ~10e9 - 50e9 URLs queued -> too big for RAM
  -> frontier is DISK-backed (e.g. log-structured queues), hot heads in memory

Dedup "seen URL" set
  URLs seen: ~50e9 distinct
  Exact set (store full URL/hash): 50e9 * ~20 B ~ 1 TB  -> sharded, partly on disk
  Bloom filter alternative: 50e9 keys, 1% FP, ~9.6 bits/key
                              -> 50e9 * 9.6 / 8 ~ 60 GB  in RAM, sharded
  -> Bloom filter for the fast path, backed by exact store for confirmation

Worker count
  If one worker sustains ~50 fetches/sec (network-bound, polite waits dominate),
  12K / 50 ~ 240 workers sustained; size to ~1,000+ for peak + per-host stalls
```

## 5) API design

```
# Internal control plane (operators / seed management)
POST /v1/seeds:add
  body: { urls: ["https://..."], priority: 0..10, recrawl_hint: "news|static|auto" }
  -> { accepted, frontier_offset }

GET  /v1/crawl/stats
  -> { fetches_per_sec, frontier_depth, freshness_lag_p50, robots_cache_hit_rate,
       per_region: [...] }

# Frontier service (consumed by fetcher workers)
GET  /v1/frontier/next?worker_id=&host_budget=
  -> { url, host, politeness: {crawl_delay_ms, max_inflight}, priority, attempt }
     # returns only URLs whose host is currently within its politeness budget

POST /v1/frontier/result
  body: { url, status, content_hash, discovered_links[], fetch_latency_ms,
          next_recrawl_at }
  -> { ok }

# Robots service
GET  /v1/robots?host=
  -> { allow_rules[], disallow_rules[], crawl_delay_ms, fetched_at, ttl }

# Content store (write by workers, read by indexer)
PUT  /v1/content/{content_hash}    body: <raw bytes + headers + fetch metadata>
GET  /v1/content/{content_hash}
```

## 6) Architecture — request & data flow

### (a) ASCII layered diagram

```
                         Seed URLs / operator console
                                     |
                                     v
                        +======= URL Frontier =======+      the "what to fetch next" brain
                        |  priority queues (per-tier) |
                        |  + per-host politeness queue |     disk-backed; hot heads in RAM
                        |  back-queues keyed by HOST   |     one host -> one back-queue
                        +==============================+
                                     |  (pull: only hosts within politeness budget)
                                     v
                        [ Frontier Dispatcher / Selector ]   enforces crawl-delay, max-inflight/host
                          /          |              \
                         v           v               v
                  [ Fetcher    [ Fetcher    ...  [ Fetcher        stateless, autoscaled
                    Worker 1 ]   Worker 2 ]       Worker K ]      across regions / egress IPs
                       |             |                |
            +----------+    DNS cache + Robots cache (per host)   avoid re-resolving, re-fetching robots
            |          |             |                |
            |          v             v                v
            |   [ HTML fetch ]  [ JS-render tier ]  (headless browser for SPAs, separate pool)
            |          |             |                |
            |          +------+------+----------------+
            |                 v
            |        [ Content Processor ]  normalize, extract links, compute:
            |          - URL canonicalization        - content fingerprint (SimHash + SHA-256)
            |                 |
            |     +-----------+-----------+--------------------+
            |     v                       v                    v
            | [ URL Dedup ]         [ Content Dedup ]   [ Discovered links ]
            | Bloom filter +        SimHash near-dup +   canonicalize -> back to FRONTIER
            | exact seen-store      exact hash store          |
            |     | new?                  | new content?      |
            |     | yes                   | yes               v
            |     +-----------+-----------+            (enqueue with priority + politeness)
            |                 v
            |        [ Content Store (blob) ]  raw HTML + headers + fetch meta, by content_hash
            |                 |
            |                 v
            |        [ Link Graph Store ]  (url -> outlinks), feeds PageRank/indexer
            |                 |
            |                 v
            |        [ Indexer pipeline ]  (downstream, out of scope here)
            |
            +--> [ Freshness Scheduler ]  per-URL change-history -> next_recrawl_at -> re-enqueue
                   (adaptive: observed-change-rate model)            into FRONTIER over time
```

**Read/crawl path (the fetch loop):** The **Frontier** holds every URL we intend to fetch, organized into two layers: *front queues* by priority tier (importance / freshness need) and *back queues* keyed by host. The **Dispatcher** pulls the next URL only from a host that is currently within its politeness budget (no other in-flight fetch to that host, and crawl-delay elapsed), then hands it to a **Fetcher Worker**. The worker resolves DNS (cached), checks the **robots cache** (fetched once per host, TTL'd), and fetches — HTML directly, or via the **JS-render tier** for SPA pages. The **Content Processor** canonicalizes the URL, computes a content fingerprint, runs **URL dedup** (Bloom filter fast-path, exact store to confirm) and **content dedup** (SimHash near-duplicate detection so the same article under 50 URLs is stored once). New content is written to the **blob store**; discovered outlinks are canonicalized and pushed *back into the frontier* with a computed priority; the link is recorded in the **link graph**.

**Freshness loop (continuous):** Every fetched URL gets a `next_recrawl_at` from the **Freshness Scheduler**, which models each page's observed change rate — a page that changed on the last 3 visits gets re-crawled soon; one unchanged for months gets a long interval. The scheduler re-enqueues URLs into the frontier when their time comes, so the crawl never "finishes" — it converges to a steady state that mirrors the web's actual churn while spending fetch budget where change actually happens.

### (b) Mermaid flowchart

```mermaid
flowchart TD
  seed[Seed URLs / operator] --> frontier[[URL Frontier priority + per-host back-queues]]
  frontier --> disp{{Dispatcher politeness gate: crawl-delay + max-inflight/host}}
  disp --> w1[Fetcher Worker 1]
  disp --> w2[Fetcher Worker 2]
  disp --> wK[Fetcher Worker K]
  w1 --> robots[(Robots + DNS cache per host)]
  w1 --> fetch[HTML fetch]
  w1 --> render[JS-render tier headless]
  fetch --> proc[Content Processor canonicalize + fingerprint]
  render --> proc
  proc --> udedup{URL dedup Bloom + exact}
  proc --> cdedup{Content dedup SimHash near-dup}
  proc --> links[Discovered links canonicalize]
  links --> frontier
  cdedup -->|new content| blob[(Content Store blob)]
  proc --> graph[(Link Graph url to outlinks)]
  blob --> indexer[Indexer pipeline downstream]
  graph --> indexer
  proc --> fresh[Freshness Scheduler change-rate model]
  fresh -. re-enqueue next_recrawl .-> frontier
```

## 7) Data model & storage choices

- **URL Frontier:** a set of **disk-backed log-structured queues** (the corpus of pending URLs vastly exceeds RAM), with a two-tier design — *front queues* by priority and *back queues* one-per-host. Justification: priority queues decide *what's important*; per-host back queues are what make **politeness** mechanically enforceable (the dispatcher simply never pulls two URLs from the same host's queue concurrently). Hot heads cached in RAM. This is the Mercator-style frontier and it is the right primitive — politeness and priority are structural, not bolted on.
- **Seen-URL set (dedup):** a sharded **Bloom filter** in RAM for the fast "have I seen this?" check (~60 GB for 50B URLs at 1% FP), backed by an exact key-value store (sharded by URL hash) to confirm on a Bloom hit and to survive restarts. Justification: at 50B URLs an exact in-RAM set is too large; a Bloom filter trades a tiny, *safe-direction* false-positive rate (we might skip a genuinely new URL — acceptable) for 15× memory savings. False negatives are impossible, so we never double-fetch.
- **Content store:** a **blob store** keyed by **content hash (SHA-256)**, holding raw bytes + response headers + fetch metadata. Justification: content-addressing gives free exact-duplicate collapsing (same bytes → same key → stored once) and immutability. Cheap, durable, sequential-write-friendly — exactly what petabyte-scale archival wants.
- **Content fingerprint for near-dup:** a **SimHash** (or MinHash) per page stored alongside, so two pages that are 95% identical (boilerplate-heavy site templates, syndicated articles) collapse even when their bytes differ. Exact SHA-256 catches byte-identical; SimHash catches near-identical.
- **Link graph:** `url_id -> [outlink_ids]`, stored column-oriented / adjacency-list, feeding PageRank and the indexer. Append-mostly, read in bulk batch jobs.
- **Robots + DNS cache:** per-host KV with TTL — `host -> {rules, crawl_delay, fetched_at}` and `host -> resolved_ip`. Avoids re-fetching `robots.txt` and re-resolving DNS on every URL of a host (a host can have millions of URLs).
- **Per-URL crawl history:** `url -> [(fetched_at, content_hash, changed?)]`, the input to the adaptive freshness model.

## 8) Deep dive

**The URL Frontier + politeness (the crux).** Everything good or bad about a crawler comes from the frontier. The hard problem is serving two masters at once: **priority** (crawl important/fresh pages first) and **politeness** (never hammer one host). The clean solution is a two-stage queue. *Front queues* are F priority bands; a URL's importance (static rank estimate + freshness urgency) picks its band, and a biased selector pulls from higher bands more often. The output feeds *back queues* — there are B back queues, each pinned to a single host at a time, and a **min-heap keyed by each host's next-eligible time** (now + crawl-delay). The dispatcher pops the heap: the host whose politeness window has opened soonest. This makes politeness an *invariant of the data structure*, not a check that can be forgotten — a host is in exactly one back queue, so at most one of its URLs is ever in flight, and the heap guarantees we wait out its crawl-delay. To scale beyond one machine, the frontier is **sharded by host hash**, so all of a host's politeness state lives on one shard (no cross-shard coordination to enforce per-host limits) — and a heavy host never splits its rate budget across shards.

**Dedup at two levels (URL and content).** *URL dedup* answers "should I even fetch this?" — checked against the sharded Bloom filter before enqueue, so the same link discovered from a thousand pages is enqueued once. The Bloom filter's false positives are in the *safe* direction (occasionally skip a new URL), and false negatives are impossible, so we never re-fetch — critical, because re-fetching wastes politeness budget and bandwidth. *Content dedup* answers "is this page actually new content?" — the same article is reachable via `?utm=`, `?sessionid=`, mobile vs desktop, and mirror domains. Exact SHA-256 collapses byte-identical copies for free (content-addressed store); **SimHash** collapses near-duplicates (same article, different ads/timestamps) by Hamming distance, so we don't index the web's enormous boilerplate redundancy. Getting both levels right is what keeps a 10B-page corpus from ballooning to 50B near-identical blobs.

**Trap avoidance.** The open web is adversarial: infinite calendars (`/2026/01/01`, `/2026/01/02`, …), session-id URLs that mint a new URL every request, faceted-search filter explosions, and deliberate spider traps. Defenses: (1) **per-URL depth limit** and **per-host page budget** — no single host can consume unbounded frontier; (2) **URL pattern detection** — if a host generates thousands of URLs differing only in a numeric/date segment with near-duplicate content (caught by SimHash), down-rank or stop that pattern; (3) **canonicalization** — strip known tracking params, normalize, and honor `rel=canonical` so trap variants dedup away; (4) **content-hash loop detection** — if new URLs keep yielding already-seen content hashes, the host is a maze; quarantine it.

## 9) Key tradeoffs

| Decision | Choice & rationale |
|---|---|
| CAP | **AP** — the crawl pipeline favors availability and progress over consistency. A briefly-stale frontier or dedup set just means a small chance of a redundant fetch; never block the fleet for consistency. |
| Consistency model | Eventual / best-effort. Dedup is *approximate* (Bloom + SimHash). The corpus is an ever-converging snapshot, not transactional. |
| Partitioning | Frontier and seen-set **sharded by host hash** so all politeness + dedup state for a host is co-located — no cross-shard coordination to enforce per-host rate limits. |
| Dedup: exact vs approximate | **Approximate (Bloom + SimHash).** Exact 50B-URL membership in RAM is infeasible; Bloom's safe-direction error and SimHash near-dup are the right trade — saves ~15× memory and collapses boilerplate. |
| Politeness vs throughput | **Politeness wins, always.** Per-host caps throttle individual sites; we recover aggregate throughput by *breadth* (crawling many hosts in parallel), not by hammering any one. |
| Re-crawl policy | **Adaptive** (per-page change-rate model) over uniform. Uniform re-crawl wastes budget on static pages and starves fast-changing ones. |
| Sync vs async | Fetch loop is async/pipelined (network-bound, lots of polite waiting); content processing and indexing fully decoupled via the content store + link graph. |
| HTML vs JS render | HTML-first (cheap); route only detected JS-heavy pages to the expensive headless-render pool. Rendering everything is 10–50× costlier. |

## 10) Bottlenecks & failure modes

- **Politeness starvation / one giant host:** a single huge site (millions of URLs) can monopolize attention or, conversely, be starved by its own crawl-delay. *Mitigation:* per-host page budget + the host-sharded heap ensures fair rotation; very large hosts get a dedicated higher-throughput agreement where permitted.
- **Frontier hot shard:** hashing by host can overload the shard owning a mega-host. *Mitigation:* secondary split of a hot host's URLs into sub-queues *within* its shard (preserving the single-host politeness invariant); monitor and rebalance.
- **DNS / robots fetch storm:** discovering a new host triggers DNS + `robots.txt` fetches; a burst of new hosts can overwhelm resolvers. *Mitigation:* aggressive DNS + robots caching with TTL, a bounded resolver pool, and negative caching for dead hosts.
- **Crawler trap (infinite space):** *Mitigation:* depth limit, per-host budget, URL-pattern + content-hash loop detection, canonicalization (covered in deep dive).
- **Bloom filter saturation:** as the seen-set grows past the filter's design capacity, false-positive rate climbs and we start skipping real URLs. *Mitigation:* monitor fill ratio; roll to a larger filter / scalable Bloom filter; the exact backing store catches confirmations.
- **Thundering herd on re-crawl:** the freshness scheduler re-enqueuing a large batch at the same instant spikes load. *Mitigation:* jitter `next_recrawl_at`; smooth the scheduler's emission rate.
- **Worker crash mid-fetch:** *Mitigation:* in-flight URLs are leased with a timeout; an un-acked lease returns to the frontier and is retried — at most one fetch lost, never the URL.
- **Content store / bandwidth ceiling:** ingest bandwidth (~48 Gbps) is the real limit. *Mitigation:* distribute workers across regions/egress points; compress on the wire; back-pressure the frontier when the store lags.

## 11) Scale 10x / evolution

- **What breaks first:** the **frontier's single-host sharding** and **bandwidth**. At 100B fetches/day, both the per-host hot-shard problem and aggregate egress (~480 Gbps) become acute.
- **Fix 1 — multi-region, geo-partitioned crawl:** shard the frontier by **(region, host)** and crawl each host from the geographically nearest region — lower latency, distributed egress, and respects that many sites serve region-local content.
- **Fix 2 — hierarchical frontier:** a global frontier of *hosts* (which host to visit) over per-host local frontiers (which URL within the host), so the global scheduler reasons at host granularity (millions) instead of URL granularity (tens of billions).
- **Fix 3 — smarter freshness budget:** as the corpus grows, uniform re-crawl is hopeless; lean harder on the change-rate model and on *signals* (sitemaps, RSS, change-notification pings, HTTP `If-Modified-Since` / ETag) to fetch only what changed — turning expensive full re-crawls into cheap conditional GETs.
- **Storage 10×:** tier the content store — recent/hot snapshots on fast storage, historical versions on cold archival; dedup harder (SimHash threshold tuning) to fight redundancy.
- **Trap/spam 10×:** at scale, adversarial content farms dominate the long tail; add a learned quality/spam pre-filter *before* spending fetch budget, so the frontier deprioritizes known-junk hosts.

## 12) Interviewer probes & follow-ups

- **"How do you enforce politeness across a distributed fleet?"** Shard the frontier by host hash so *all* of a host's URLs and its politeness state live on one shard; that shard keeps a min-heap of hosts by next-eligible time and never dispatches two URLs of the same host concurrently. Politeness becomes a data-structure invariant, not a distributed lock.
- **"Bloom filter or exact set for dedup?"** Bloom for the fast path (60 GB vs ~1 TB), backed by an exact store. Its false positives are *safe* (occasionally skip a new URL); false negatives are impossible, so we never double-fetch and never waste politeness budget. The exact store confirms on a Bloom hit and survives restarts.
- **"How do you decide what to re-crawl and when?"** A per-page change-rate model: track the outcome of the last K visits (changed/unchanged) and set `next_recrawl_at` adaptively — fast for frequently-changing pages, slow for static ones. Use sitemaps/ETags/`If-Modified-Since` to make most re-crawls cheap conditional GETs.
- **"How do you avoid crawler traps?"** Depth limits, per-host page budgets, URL canonicalization (strip tracking params, honor `rel=canonical`), URL-pattern detection, and content-hash loop detection — if new URLs keep returning already-seen content, quarantine the host.
- **"Same article under 50 URLs — how do you store it once?"** Content-addressed blob store (SHA-256) collapses byte-identical copies for free; SimHash collapses near-duplicates (different ads/timestamps). URL canonicalization prevents most of the variants from ever being enqueued.
- **"What's the real bottleneck?"** Bandwidth, not CPU — ~48 Gbps sustained ingest. We scale by breadth (many hosts × many regional egress points), compress on the wire, and back-pressure the frontier when the content store lags.
- **"How do you crawl JavaScript-rendered pages?"** Detect JS-heavy pages (thin HTML, framework signatures) and route only those to a separate headless-browser pool — rendering everything is 10–50× costlier, so it's a targeted tier, not the default.
- **"How do you handle `robots.txt` correctly?"** Fetch once per host, cache with TTL, honor allow/disallow + crawl-delay; on `robots.txt` fetch failure, be conservative (treat as disallow or back off) rather than crawl blindly.

## 13) 60-minute flow cheat-sheet

| Time | Phase | What to do |
|---|---|---|
| 0–6 min | Clarify | Nail scope (open web vs site), continuous-vs-one-shot, scale, politeness, JS rendering, exact-vs-approximate dedup. |
| 6–10 min | FR/NFR | In/out scope; NFR table; commit to AP / eventual + 100% robots compliance + adaptive freshness. |
| 10–16 min | Estimation | Fetches/sec → bandwidth ceiling → 1 PB storage → frontier size → Bloom-filter sizing → worker count. |
| 16–22 min | API | Frontier next/result, robots service, content store; emphasize the politeness fields. |
| 22–38 min | Architecture | Draw the layered diagram; **walk the fetch loop then the freshness loop**; emphasize frontier two-tier queues + dedup. |
| 38–50 min | Deep dive | URL frontier + politeness invariant AND two-level dedup; cover trap avoidance. |
| 50–56 min | Tradeoffs + failures | Host-hash sharding, Bloom vs exact, adaptive re-crawl, hot shard, trap, bandwidth — each with a mitigation. |
| 56–60 min | Scale 10× | Geo-partitioned + hierarchical frontier; conditional-GET freshness; what breaks first and why. |
