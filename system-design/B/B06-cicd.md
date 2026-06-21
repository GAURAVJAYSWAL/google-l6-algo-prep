# B06 — Design a CI/CD / build system at scale

Design the build + continuous-integration system that thousands of engineers push to all day: it must turn a commit into built artifacts and test results fast, reliably, and cheaply, even on a giant monorepo. It tests core Developer-Experience platform work — a build graph + dependency model, **incremental builds**, a **remote build cache**, distributed executors, an artifact store, queueing, and flaky-test handling. Google asks it because this is literally how Google ships software (Blaze/Bazel + remote execution + result cache); getting it right makes every engineer faster, so it's a pure Staff-platform problem.

## (Section B only) Lead with this — your résumé hook

"I've built CI/CD in production — Dockerized builds, GitHub Actions pipelines, deploying to ECS — so I've felt the failure modes first-hand: 40-minute pipelines that rebuild everything, flaky tests that erode trust in the signal, and cache misses that cost real money on every PR. I'll design this from the primitive that fixes all three at once — a **content-addressed build graph with a shared remote cache and remote execution** — so that the unit of work is a single deterministic action, identical work is never repeated across engineers, and the system scales by adding stateless executors. I'll spend my depth budget on the cache + execution layer and on flaky-test handling, because those are where DevEx is won or lost."

## 1) Clarify — questions to ask the interviewer

- **Repo model:** Monorepo or many polyrepos? A monorepo demands fine-grained dependency tracking and a shared cache; polyrepos push more weight onto artifact versioning. (I'll assume **monorepo** — that's the hard, Google-shaped version.)
- **Languages / build tools:** One ecosystem or many (Java, Go, C++, JS, Python)? This sets whether I can mandate a hermetic build tool (Bazel-style) or must wrap heterogeneous toolchains.
- **Scale:** How many engineers, commits/day, and CI runs/day? Average vs p99 build time today? This drives executor fleet sizing and cache hit-rate targets.
- **Trigger model:** Per-PR (presubmit), per-merge (postsubmit), nightly, on-demand? Presubmit latency is the DevEx-critical one (engineer is blocked, watching).
- **Latency / SLO:** What's the target for a presubmit "did my change break anything" signal — minutes? This is the number the whole design optimizes.
- **Hermeticity:** Can we *enforce* deterministic, sandboxed builds (no network, declared inputs)? Caching correctness depends entirely on this. If not, what's the migration path?
- **Test taxonomy:** Unit vs integration vs end-to-end? E2E and tests needing real services change isolation and flakiness handling dramatically.
- **Artifact lifetime & compliance:** How long must artifacts/logs be retained? Any provenance/supply-chain (SLSA) or audit requirements?
- **Cost sensitivity:** Is the goal latency at any cost, or cost-efficiency too? Affects how aggressively we cache, dedup, and right-size executors.
- **Multi-region / org:** One region or global engineering org? Cache locality and executor placement become a thing at global scale.

**What the interviewer is signaling:** They want to see that you reach for an **incremental, content-addressed build graph + shared cache + remote execution** instead of "spin a VM and run `make` from scratch." The hermeticity question is the tell — if you ask it, you understand that **caching correctness is a build-determinism problem**, not a cache-eviction problem. Asking about presubmit latency and flaky tests signals you've actually operated CI and know where engineer trust comes from.

## 2) Functional Requirements (FR)

**In scope:**
- A **build graph / dependency model**: targets with declared inputs/outputs and edges; compute exactly what a change affects.
- **Incremental builds:** rebuild and retest only what's transitively affected by a change.
- **Remote build cache:** content-addressed; reuse outputs across engineers and machines (never rebuild identical work).
- **Distributed remote execution:** run build/test actions on a stateless executor fleet, not the dev laptop.
- **Artifact store:** versioned, content-addressed storage for binaries/images with provenance.
- **Queueing & scheduling:** admit, prioritize, and dispatch action graphs across executors with backpressure.
- **Flaky-test handling:** detect, quarantine, and retry non-deterministic tests so they don't block merges or rot the signal.
- **Test result + log storage** and a UI/API for engineers to see status fast.
- **Scale to thousands of engineers** with high concurrency.

**Out of scope (defer):**
- CD release orchestration beyond producing a deployable artifact (canary/rollout is a separate system; I'll note the handoff).
- Source control internals (assume a Git host / monorepo VCS exists).
- IDE / local dev-loop tooling (though the same cache benefits it).
- Code review and merge-queue *policy* (I'll integrate with a merge queue but not design it fully).
- Secrets management internals (assume a vault exists).

## 3) Non-Functional Requirements (NFR)

| Dimension | Target & rationale |
|---|---|
| Scale | ~10,000 engineers; ~100k commits/day; ~500k CI runs/day; bursty (push spikes around standups / pre-deploy). Monorepo with millions of targets. |
| p99 latency | **Presubmit signal ≤ ~10 min p95** for a typical change (the blocking, DevEx-critical path). Full builds amortized by cache to seconds-to-minutes for warm graphs. |
| Availability | **99.9%+** for the cache + execution control plane. CI down = the whole org is blocked, so this is a Tier-1 service. |
| Consistency | **Cache must be correct (never serve a wrong output)** — strong correctness via content-addressing. Cache *availability* can be eventual (a miss is safe, just slower). Build graph metadata strongly consistent within a commit. |
| Durability | Artifacts + provenance: **durable** (releases, rollbacks, audits depend on them). Cache: **best-effort** (rebuildable, evict freely). Logs/results: durable for a retention window. |
| Throughput | Executor fleet must absorb push bursts; target high utilization without long queue times. |
| Security | Hermetic sandboxes (no ambient network/filesystem), per-action isolation, signed provenance (SLSA-style), authZ on cache writes to prevent poisoning. |

## 4) Back-of-envelope estimation

```
ENGINEERS & RUNS
  Engineers                    ~10,000
  CI runs/day                  ~500,000  (presubmit + postsubmit + retries)
  ~ 500k / 86400               ~6 runs/sec average ; peak burst x10 -> ~60/sec
  Actions per run (graph)      ~1,000 build/test actions (typical change)
  Action executions/day        500k * 1,000 = ~5e8 logical actions
    BUT cache hit rate ~90%+  -> only ~5e7 actually EXECUTED/day
    ~ 5e7 / 86400              ~580 executed actions/sec average
    peak x5                    ~3,000 concurrent action executions

EXECUTOR FLEET
  Avg action wall-time         ~10 s (mix of fast compiles + slow tests)
  Concurrency needed (peak)    ~3,000 actions * (10s service) -> a few thousand cores
  At ~16 actions/host          ~200-400 executor hosts at peak (autoscaled down off-peak)

REMOTE CACHE (Content-Addressed Store, CAS)
  Distinct action outputs/day  ~5e7 (mostly cache HITS reusing existing)
  New blobs/day                ~5e6 * avg 1 MB = ~5 TB/day written
  Retain 30 days (hot)         ~150 TB hot CAS  (LRU evicted, rebuildable)
  Action-result metadata       ~5e8/day * ~1 KB = small, in a fast KV (TTL'd)

ARTIFACT STORE (durable releases)
  Release artifacts/day        ~10,000 builds * ~200 MB = ~2 TB/day
  Retain 1 yr                  ~700 TB durable blob (dedup brings it down a lot)

CACHE HIT ECONOMICS
  Without cache: 5e8 actions/day executed -> impossible fleet
  With 90% hit:  5e7 executed -> 10x smaller fleet, 10x faster, 10x cheaper.
  => the remote cache IS the design.
```

The headline: the **cache hit rate is the single most important number** — at 90% it shrinks the executor fleet and presubmit latency by an order of magnitude. Storage is dominated by the durable artifact store; the cache is large but freely evictable.

## 5) API design

```
# Build invocation (from CI trigger or dev machine)
POST /v1/build
  { repo, commit_sha, targets:[...], mode: "presubmit"|"postsubmit",
    priority, requester }
  -> { build_id, action_graph_ref }     # returns a DAG of actions

# Remote Execution (executor <-> control plane), content-addressed
POST /v2/actions:execute
  { action_digest }                     # hash(command + input tree + env + platform)
  -> 200 { result: CACHED, output_digests, stdout_digest, exit=0 }   # cache HIT
  -> 202 { queued, lease }              # cache MISS -> scheduled to an executor

# Content-Addressed Store (CAS) — blobs keyed by their own hash
GET    /cas/{sha256}                    # fetch input/output blob
POST   /cas/{sha256}    (body)          # upload blob (idempotent; key = content)
POST   /cas:findMissingBlobs { digests } -> { missing:[...] }   # batch existence

# Action Cache (ActionDigest -> ActionResult)
GET    /ac/{action_digest}              # was this exact action already run?
PUT    /ac/{action_digest} { result }   # authZ'd write (prevent poisoning)

# Test results / flakiness
GET    /v1/build/{build_id}/results
POST   /v1/tests/{test_id}/quarantine   { reason }

# Artifacts
PUT    /v1/artifacts { build_id, blob_digest, provenance }   # signed
GET    /v1/artifacts/{name}@{version}
```

Design notes: the core protocol is **content-addressed** (Bazel Remote Execution API shape). Cache keys are *digests of fully-declared inputs* — that's what makes a hit safe. CAS uploads are idempotent (key = content hash). Action-cache writes are authZ'd to prevent **cache poisoning**.

## 6) Architecture — request & data flow

### (a) ASCII layered diagram

```
        Engineers / CI triggers (push, PR, merge-queue, nightly)
                              |  POST /v1/build (commit, targets)
                              v
                    [ API Gateway / Build Frontend ]      authN, rate-limit,
                    - resolve targets for this commit     dedupe identical builds
                              |
                              v
                    [ Graph Service ]                     loads BUILD files,
                    - compute affected targets            builds the action DAG,
                    - diff vs base -> minimal rebuild set  assigns ACTION DIGESTS
                              |  action graph (DAG of digests)
                              v
                    [ Scheduler / Queue ]                 priority lanes
                    - presubmit > postsubmit > nightly    (blocked engineer first),
                    - admission control / backpressure    topological dispatch
                       |                          ^
        for each action|                          | results / leases
                       v                          |
              +-----------------+                 |
              | Action Cache    |  HIT (~90%)      |
              | (ActionDigest-> |-----------------+--> skip execution, return outputs
              |  ActionResult)  |
              +--------+--------+
                       | MISS
                       v
              [ Executor Fleet (stateless, sandboxed) ]   <--- autoscaled workers
              - fetch inputs from CAS by digest           hermetic: no net, declared
              - run action in sandbox                     inputs only
              - upload outputs to CAS, write ActionResult
                  |        ^                         |
        fetch     |        | upload outputs          | test events
        inputs    v        |                         v
              [ Content-Addressed Store (CAS) ]   [ Test/Result Service ]
              - blob store keyed by sha256        - store logs, pass/fail
              - dedup across ALL engineers        - flaky detection + quarantine
                  |                                     |
                  | promote release outputs            v
                  v                              [ Flaky DB / Quarantine list ]
              [ Artifact Store (durable) ]
              - versioned binaries / images
              - signed provenance (SLSA)  --> [ CD / Deploy system ] (handoff)

        Cross-cutting:
        [ Metadata DB ] build graph + build state (strongly consistent per commit)
        [ Observability ] queue depth, cache hit rate, p99 build time, flake rate
```

**Write/build path (the main flow).** An engineer pushes a PR; the **Build Frontend** dedupes (identical commit+targets already running? join it) and hands the commit to the **Graph Service**, which loads the BUILD definitions, computes the **transitive affected target set** by diffing against the base, and emits a **DAG of actions**, each stamped with an **action digest** = hash(command + input file tree + env + platform). The **scheduler** walks the DAG topologically, and for every action first consults the **Action Cache**: if some engineer (or a prior run) already executed this exact digest, it's a **hit** (~90%) and we reuse the cached outputs with zero execution. On a **miss**, the action is queued to the **executor fleet**; an executor fetches the declared inputs from the **CAS** by digest, runs the action in a **hermetic sandbox**, uploads outputs back to the CAS, and writes the `ActionResult` to the Action Cache so the *next* engineer hits it for free. Release-worthy outputs are promoted into the durable **Artifact Store** with signed provenance and handed to the CD system.

**Read path (status + cache).** Engineers poll `/build/{id}/results`; the **Test/Result Service** streams pass/fail and logs. Cache reads (Action Cache lookups and CAS fetches) are content-addressed point lookups — the hot path that the whole system's speed depends on. Flaky-test signals flow async into the **Flaky DB**, which the scheduler consults to auto-retry/quarantine without blocking the merge.

**Why content-addressing makes it correct.** Because an action's key includes *all* declared inputs and the exact command/env/platform, a cache hit is provably the same computation — so sharing outputs across thousands of engineers is safe. Hermetic sandboxing (no network, no undeclared files) is what guarantees the declared inputs are the *only* inputs; without it, the cache would silently serve wrong results.

### (b) Mermaid flowchart

```mermaid
flowchart TD
  dev[Engineers / CI triggers] --> fe[Build Frontend: authN + dedupe]
  fe --> graph[Graph Service: affected targets -> action DAG]
  graph --> sched[Scheduler / Priority Queue]
  sched --> ac{Action Cache hit?}
  ac -- hit ~90% --> outputs[Reuse cached outputs]
  ac -- miss --> exec[Executor Fleet - hermetic sandbox]
  exec -- fetch inputs by digest --> cas[(Content-Addressed Store)]
  exec -- upload outputs --> cas
  exec -- write ActionResult --> ac
  exec --> results[Test/Result Service]
  results --> flaky[(Flaky DB / Quarantine)]
  flaky -. retry/quarantine .-> sched
  cas -- promote release --> artifacts[(Artifact Store - durable + provenance)]
  artifacts --> cd[CD / Deploy system]
  sched --- meta[(Metadata DB: graph + build state)]

  subgraph OBS [Observability]
    m1[queue depth] --- m2[cache hit rate] --- m3[p99 build time] --- m4[flake rate]
  end
```

## 7) Data model & storage choices

**Build graph / metadata.**
```
Target { label, rule_type, srcs[], deps[], outs[], visibility }
Action { action_digest, command, input_root_digest, platform, env,
         output_paths[] }       # digest = sha256 of the whole tuple
Build  { build_id, commit_sha, root_targets[], state, started_at, requester }
```
Stored in a **strongly-consistent relational/metadata store** (the graph for one commit must be internally consistent; you can't half-resolve a DAG). The graph is large but structured and queried by relationship — SQL or a graph-aware store fits.

**Action Cache: `action_digest -> ActionResult`.** A **fast KV store** (LSM-tree backed) keyed by the action digest, holding output digests + exit code + stdout/stderr digests. Reads dominate massively and must be sub-millisecond; entries are TTL'd and rebuildable, so an LSM store optimized for high write/read throughput is ideal. Writes are authZ'd (poisoning defense).

**Content-Addressed Store (CAS): `sha256(blob) -> bytes`.** A **blob store** keyed by content hash. Content-addressing gives free **dedup across all engineers** (identical source files / compiler outputs stored once) and trivially safe caching (the key *is* the integrity check). Hot tier on SSD/object store with an LRU/LFU eviction; it's fully rebuildable so durability isn't required — availability and locality are.

**Artifact Store (durable).** Versioned, content-addressed **object storage** for release binaries/images plus **signed provenance** (what inputs/commit/builder produced this). This one *is* durable and retained for a year+ because rollbacks, audits, and supply-chain (SLSA) depend on it.

**Test results & logs.** Logs are large, append-only, write-once → **blob/object storage** with a metadata index (build_id, test_id, status) in a queryable store for the UI. Retained for a window then tiered to cold storage.

**Flaky DB.** Per-test pass/fail history and quarantine state in a KV/time-series store; the flake detector reads it to compute non-determinism rates.

Justification theme: **content-addressing is the backbone** — it gives correctness (key = integrity), dedup (store once), and cache-shareability (safe across engineers) for free, which is exactly why Google's build system is built this way.

## 8) Deep dive

### Deep dive 1: Remote cache + remote execution (the crux)

This is the component that turns a 40-minute build into seconds, and it's where I'd spend the most time.

- **Cache key = action digest = hash(command + input tree + env + platform).** Two builds anywhere in the org that produce the same digest *are* the same computation, so the result is shareable. This is why **hermeticity is non-negotiable**: the sandbox must guarantee no undeclared inputs (no ambient network, no reading system paths), otherwise two "identical" actions could differ and the cache serves a wrong output — the worst possible failure in a build system.
- **Two-level cache:** the **Action Cache** (digest → result metadata) answers "do I even need to run this?"; the **CAS** (content hash → bytes) stores the actual input/output blobs. A hit on the Action Cache means we fetch outputs from CAS and skip execution entirely.
- **Remote execution flow on a miss:** scheduler dispatches the action; executor calls `findMissingBlobs` to fetch only the inputs it doesn't already have locally, runs in the sandbox, uploads outputs, writes the ActionResult. Executors are **stateless** — they hold no build state, so the fleet autoscales freely and a dead executor just means the action is re-leased elsewhere.
- **Cache poisoning defense:** writes to the Action Cache are authZ'd to trusted executors only; untrusted (e.g. PR) builds can *read* the shared cache but write to a separate, untrusted namespace, so a malicious PR can't inject a bad output that a teammate then trusts.
- **Locality at scale:** CAS is replicated per region with a global fallback; executors prefer the local CAS replica to keep input-fetch latency low. A "build event" stream lets the UI show live progress.
- **Why this fixes my résumé pain:** the GitHub-Actions pipelines I built re-ran work per-PR with no cross-engineer sharing; content-addressed remote cache + execution is exactly the primitive that eliminates that waste.

### Deep dive 2: Flaky-test handling (where DevEx trust is won/lost)

A flaky test — one that passes and fails non-deterministically on the same code — is poison: it blocks merges, trains engineers to blindly retry (so real failures get ignored), and destroys trust in the signal. Handling it well is a Staff-level DevEx differentiator.

- **Detection:** for each test, track pass/fail history keyed by *commit digest*. If the **same action digest** (identical inputs) produces different results across runs, it is **provably flaky** — content-addressing gives us this for free (same inputs should = same result). Compute a per-test flake rate.
- **Automatic retries with caution:** retry a failed test a bounded number of times; if it then passes, mark the run as flaky-but-green rather than failing the engineer's PR — but *record* the flake so it's visible, never silently swallowed.
- **Quarantine:** tests above a flake threshold are auto-moved to a **quarantine list** — they still run (for data) but no longer block merges, and the owning team gets a ticket. This unblocks the org immediately while keeping the test owner accountable.
- **Root-cause aids:** capture full sandbox logs, timing, and resource contention on failure; flag tests that depend on wall-clock, network, ordering, or shared state. Hermetic sandboxing dramatically *reduces* flakiness by removing ambient nondeterminism in the first place — the same property that makes caching correct.
- **Don't let retries hide real breakage:** a test that fails *deterministically* on a given digest (same inputs, always fails) is a real failure and must block — distinguish "flaky" (varies on identical inputs) from "broken" (consistent on identical inputs). That distinction is only possible *because* of content-addressing.

## 9) Key tradeoffs

| Decision | Option A | Option B | Choice & why |
|---|---|---|---|
| Build model | Rebuild everything per run | **Incremental on a content-addressed graph** | **Incremental.** Only affected actions run; everything else is a cache hit. The whole 10x latency/cost win. Cost: requires hermetic, fully-declared builds. |
| Cache correctness | Trust timestamps/heuristics | **Content-addressed (digest of inputs)** | **Content-addressed.** Key = integrity; sharing across engineers is provably safe. Cost: builds must be deterministic + sandboxed. |
| Executors | Stateful build machines | **Stateless sandboxed executors** | **Stateless.** Autoscale freely, re-lease on failure, no sticky state. Cost: every input fetched from CAS (mitigated by locality + dedup). |
| Cache consistency | Strongly consistent, HA cache | **Correct-but-best-effort-available** | **Best-effort availability.** A miss is *safe* (just re-execute), so optimize the cache for speed/locality, not for never-missing. Correctness is strong; availability is eventual. |
| Presubmit vs postsubmit | One queue | **Priority lanes (blocked engineer first)** | **Priority lanes.** Presubmit blocks a human and must jump the queue; nightly can wait. Cost: scheduler complexity + starvation guards. |
| Flaky tests | Always block on red | **Retry + quarantine with visibility** | **Retry + quarantine.** Unblocks the org without hiding failures; distinguishes flaky (varies on same digest) from broken (consistent). Cost: must never silently swallow a real failure. |
| Artifact storage | Durable for everything | **Durable artifacts, evictable cache** | **Split.** Releases/provenance durable (rollbacks/audits); cache rebuildable, evict freely. Right-sizes cost. |

## 10) Bottlenecks & failure modes

- **Cache stampede on a fresh commit / cold cache (e.g. a base-library change invalidates everything).** A change deep in the graph can invalidate millions of downstream digests, causing a thundering herd of executions. *Mitigation:* request collapsing / single-flight per action digest (only one executor builds a given digest, others wait on it); gradual base-change rollout; prioritize presubmit so blocked engineers still progress.
- **Executor fleet saturation during push bursts.** Queue depth spikes at standup/pre-deploy. *Mitigation:* admission control + backpressure (reject/queue low-priority work first), autoscale on queue depth, and keep a warm pool. The cache hit rate is the real relief valve — at 90% the fleet only sees 10% of logical work.
- **Hot CAS blob / hot Action Cache key.** A universally-depended-on artifact (the base toolchain) is fetched by every executor. *Mitigation:* replicate hot blobs, local executor caches, peer-to-peer/tree distribution of large common inputs.
- **Cache poisoning.** A malicious or buggy build writes a wrong output others trust. *Mitigation:* authZ'd cache writes (trusted executors only), separate untrusted namespace for PR builds (read shared, write isolated), and integrity verified by content hash on every fetch.
- **Non-hermetic build silently corrupts the cache.** The scariest failure: an action reads an undeclared input, so its digest doesn't capture reality and the cache serves wrong outputs. *Mitigation:* strict sandboxing (no network, declared inputs only), input-completeness checks, and periodic "cache vs cold-build" verification jobs that alarm on mismatch.
- **Metadata DB / scheduler SPOF.** The control plane is Tier-1. *Mitigation:* HA replicas, leader election for the scheduler, and graceful degradation (executors can finish in-flight leases even if the frontend blips).
- **Flaky retries masking real breakage.** *Mitigation:* only retry tests that vary on *identical* digests; deterministic failures always block (covered in deep dive).
- **Cascading failure from a bad toolchain rollout.** A broken compiler image fails every build. *Mitigation:* canary the toolchain on a small target set, fast rollback, and version-pin toolchains as part of the action digest so the change is explicit and revertible.

## 11) Scale 10x / evolution

- **What breaks first: the executor fleet and CAS bandwidth.** 10x engineers → 10x logical actions, but cache hits absorb most of it; the binding constraints become (a) CAS read bandwidth for hot common inputs and (b) raw executor capacity for the genuine 10% misses. Scale executors horizontally (they're stateless) and shard/replicate the CAS more aggressively with peer-to-peer distribution of big shared blobs.
- **Graph computation cost.** On a giant monorepo, computing "what's affected" for every PR gets expensive. Evolve to **incremental graph evaluation** (cache the dependency graph, only recompute the changed subgraph) and finer-grained targets so a change invalidates less.
- **Cache hit rate is the lever, not fleet size.** The highest-leverage scaling move is pushing hit rate from 90% → 95%+ (better target granularity, more determinism, longer cache retention) — that *halves* executions again. Invest there before buying machines.
- **Multi-region engineering org.** Place executor pools and CAS replicas per region; route builds to the nearest pool; keep a global Action Cache so a result computed in one region is reused in another. Artifact store + provenance stay globally durable.
- **Merge-queue integration at scale.** As commit rate rises, "test every PR against tip" becomes a bottleneck; introduce **speculative/batched merge testing** (test batches optimistically, bisect on failure) so CI throughput keeps up with merge rate.
- **Cost governance.** At 10x, build cost is a real budget line; add per-team cost attribution, cache-efficiency dashboards, and policies that reward hermetic, cache-friendly targets — this is the "infra governance" Staff angle.

## 12) Interviewer probes & follow-ups

- **"How is your cache key computed and why is it safe to share across engineers?"** Key = action digest = hash(command + full input tree + env + platform). Two identical digits *are* the same computation, so sharing is provably correct — provided builds are hermetic (no undeclared inputs).
- **"What if a build isn't deterministic?"** Then the cache can serve wrong outputs — the worst failure. I enforce hermetic sandboxes (no network, declared inputs only) and run periodic cache-vs-cold-build verification that alarms on mismatch; non-hermetic targets are flagged and migrated.
- **"How do you do incremental builds — only rebuild what changed?"** The Graph Service computes the transitive affected target set by diffing the change against the base; only those actions run, everything else is a cache hit. Fine-grained targets minimize the invalidated set.
- **"How do you handle flaky tests without either blocking everyone or hiding bugs?"** Detect via differing results on *identical* digests, bounded retries, auto-quarantine above a threshold (still runs, no longer blocks, owner ticketed). Deterministic failures on a digest always block — that's a real bug, not flake.
- **"What happens on a cache miss vs hit, concretely?"** Hit: Action Cache returns the result, fetch outputs from CAS, skip execution. Miss: schedule to an executor, fetch missing inputs from CAS, run in sandbox, upload outputs, write ActionResult so the next engineer hits.
- **"How do you stop cache poisoning from a malicious PR?"** AuthZ'd cache writes (trusted executors only); PR/untrusted builds read the shared cache but write to an isolated namespace, and every fetch is integrity-checked by content hash.
- **"Presubmit is slow during peak — what do you do?"** Priority lanes (presubmit jumps the queue), admission control / backpressure shedding low-priority work, autoscale on queue depth, warm pool. But the real fix is raising the cache hit rate so the fleet sees far less work.
- **"How do executors stay cheap and resilient?"** Stateless + sandboxed: autoscale on demand, re-lease actions on failure, no sticky state. Inputs come from CAS (deduped, locality-optimized).
- **"How do you handle a change to a base library that everything depends on?"** It invalidates a huge subgraph — expect a stampede. Single-flight per digest (build once, others wait), gradual rollout, prioritize blocked engineers, and lean on the warm executor pool.
- **"Where does provenance/supply-chain fit?"** Release artifacts carry signed provenance (inputs, commit, builder) — SLSA-style — stored durably for audits and rollbacks; the cache itself is integrity-checked by content hash.

## 13) 60-minute flow cheat-sheet

| Time | Phase | What to cover |
|---|---|---|
| 0-5 min | Clarify | Monorepo vs polyrepo, languages, scale (engineers/commits), presubmit latency target, **hermeticity** (the key question), test taxonomy, cost sensitivity. |
| 5-8 min | FR / NFR | Build graph, incremental builds, remote cache, remote execution, artifact store, queueing, flaky handling. NFR: Tier-1 availability, cache *correct* but eventually available, presubmit ≤10 min. |
| 8-13 min | Estimation | Hit-rate economics: 5e8 logical actions → 5e7 executed at 90% hit (10x smaller fleet). 150 TB hot CAS, durable artifact store. "The cache is the design." |
| 13-18 min | API + model | Content-addressed Remote Execution API: Action Cache (digest→result) + CAS (hash→bytes). Idempotent uploads, authZ'd writes. |
| 18-32 min | Architecture (centerpiece) | Walk both diagrams: build → graph/affected targets → scheduler → Action Cache hit/miss → executor + CAS → artifact store. Stress content-addressing = correctness + dedup + shareability. |
| 32-45 min | Deep dive | (1) Remote cache + execution: digest keys, hermeticity, poisoning defense, stateless executors. (2) Flaky tests: detect via same-digest divergence, retry, quarantine, never hide real failures. |
| 45-52 min | Tradeoffs + failures | Incremental vs full, content-addressed vs heuristic, stateless executors, priority lanes. Cache stampede on base-lib change, fleet saturation, poisoning, non-hermetic corruption. |
| 52-58 min | Scale 10x | Executors + CAS bandwidth break first; raise hit rate as the real lever; incremental graph eval; multi-region with global Action Cache; merge-queue batching; cost governance. |
| 58-60 min | Wrap | Restate: content-addressed build graph + shared remote cache + remote execution makes the unit of work a deterministic action, so identical work is never repeated and the org scales by adding stateless executors — tying back to the CI/CD you've built. |
