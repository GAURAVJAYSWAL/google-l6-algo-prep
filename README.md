# 📚 Google Staff (L6) — Algorithm & Data-Structure Prep

Clean, **interview-grade Java solutions** to every problem in the Google L6 prep tracker — **197 problems across 29 patterns** — each with the *key insight*, complexity, gotchas, and how to **explain it out loud**.

### 🔗 [**▶ Open the interactive study site**](https://gauravjayswal.github.io/google-l6-algo-prep/) &nbsp;·&nbsp; [📊 Prep tracker (xlsx)](google_l6_prep_tracker.xlsx)

![problems](https://img.shields.io/badge/problems-197-4f46e5) ![patterns](https://img.shields.io/badge/patterns-29-0ea5e9) ![language](https://img.shields.io/badge/Java-25-orange) ![build](https://img.shields.io/badge/mvn%20compile-passing-16a34a)

---

## What's inside

- **`docs/index.html`** — a single, searchable **study site**: one rich card per problem (key insight → step-by-step approach → complexity → ⚠️ gotchas → 🗣️ "explain out loud"), color-coded difficulty & priority badges, and a link to both the runnable Java and the LeetCode problem. Browse it live on **[GitHub Pages](https://gauravjayswal.github.io/google-l6-algo-prep/)**.
- **`src/main/java/org/example/<pattern>/`** — the solutions. **One self-contained class per problem**, with a Javadoc stating the key insight + `Time:`/`Space:`, clean optimal code, and a `main` with `// expected:` test cases.
- **`google_l6_prep_tracker.xlsx`** — the tracker (Problems · Study Plan · Progress), every row linked to its code and LeetCode page.

## Run a solution

```bash
mvn compile
java -cp target/classes org.example.dp.CoinChange      # or any class below
```

Every class has a runnable `main`; the whole project compiles clean on **Java 25** (`mvn compile`).

## Chapters — 29 patterns, 197 problems

| Group | Pattern | # | Code |
|---|---|--:|---|
| **Foundations** | 👣 Two-Pointer Warm-ups | 5 | [`phase1`](src/main/java/org/example/phase1) |
| | 🧮 Arrays & Hashing | 18 | [`arrays`](src/main/java/org/example/arrays) |
| | 📊 2D Prefix Sum & Range Queries | 2 | [`prefixsum2d`](src/main/java/org/example/prefixsum2d) |
| | ➕ Difference Array | 2 | [`differencearray`](src/main/java/org/example/differencearray) |
| | 🪟 Sliding Window | 4 | [`slidingwindow`](src/main/java/org/example/slidingwindow) |
| | 🔤 Strings | 16 | [`strings`](src/main/java/org/example/strings) |
| | 🧬 String Matching (KMP / Rabin-Karp) | 3 | [`stringmatching`](src/main/java/org/example/stringmatching) |
| | 🥞 Stack & Parsing | 5 | [`stack`](src/main/java/org/example/stack) |
| **Search & Structures** | 🔎 Binary Search | 8 | [`binarysearch`](src/main/java/org/example/binarysearch) |
| | 🔗 Linked Lists | 10 | [`linkedlist`](src/main/java/org/example/linkedlist) |
| | 🌳 Trees | 9 | [`trees`](src/main/java/org/example/trees) |
| | 🎄 Tree DP / Rerooting | 3 | [`treedp`](src/main/java/org/example/treedp) |
| | 🌲 Tries | 3 | [`trie`](src/main/java/org/example/trie) |
| | ⛰️ Heap / Priority Queue | 6 | [`heap`](src/main/java/org/example/heap) |
| | 🗂️ Ordered-Set / Data-Stream | 2 | [`orderedset`](src/main/java/org/example/orderedset) |
| **Intervals & Backtracking** | 📅 Intervals & Line Sweep | 10 | [`intervals`](src/main/java/org/example/intervals) |
| | 🔙 Backtracking | 9 | [`backtracking`](src/main/java/org/example/backtracking) |
| **Grids & Graphs** | 🔲 Matrix | 7 | [`matrix`](src/main/java/org/example/matrix) |
| | 🕸️ Graphs | 19 | [`graphs`](src/main/java/org/example/graphs) |
| | 🚀 Advanced Graphs (L6) | 6 | [`advancedgraphs`](src/main/java/org/example/advancedgraphs) |
| | 🌉 Minimum Spanning Tree | 2 | [`mst`](src/main/java/org/example/mst) |
| **Optimization** | 🤑 Greedy | 3 | [`greedy`](src/main/java/org/example/greedy) |
| | 📐 Dynamic Programming | 18 | [`dp`](src/main/java/org/example/dp) |
| | 🎒 0/1 Knapsack & Subset-Sum | 2 | [`knapsack`](src/main/java/org/example/knapsack) |
| | 🎈 Interval DP | 3 | [`intervaldp`](src/main/java/org/example/intervaldp) |
| | 🎭 Bitmask / Subset DP | 3 | [`bitmaskdp`](src/main/java/org/example/bitmaskdp) |
| **Math & Design** | 🔢 Bit & Math | 8 | [`bitmath`](src/main/java/org/example/bitmath) |
| | 🌴 Segment Tree / BIT | 2 | [`segmenttree`](src/main/java/org/example/segmenttree) |
| | 🏗️ Design | 9 | [`design`](src/main/java/org/example/design) |

## Priority legend

🟥 Staff-tagged · 🟪 L6 differentiator · 🟦 Google signature · ⭐ Google fav · 🔹 Google-tagged · ⚪ Core · ◻️ Gap-fill · 🆕 Recent 2025-26 · 📌 Reported L6

## House style

Every solution is the kind a strong L6 candidate would write and defend: the optimal approach, a one-paragraph "why it works", explicit Time/Space, and pointed comments only on the tricky steps. Helper types (`TreeNode`, `ListNode`, tries, graph nodes) are nested `static` classes so each file stands alone.

---

*Built from the Google L6 Prep Tracker — sources: Prep Tracker, Deep Research Adds, Study Plan & Progress tabs.*
