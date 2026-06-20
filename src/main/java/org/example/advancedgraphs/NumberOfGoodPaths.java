package org.example.advancedgraphs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * LC 2421. Number of Good Paths.
 */
public class NumberOfGoodPaths {

    /**
     * Key insight: a good path's endpoints share the maximum value on the path, so process
     * nodes in increasing value and grow components with union-find. When we add a node of
     * value v, we union it only with already-added neighbors (those have value <= v), so the
     * component formed at value v contains exactly the nodes reachable through values <= v.
     * Among them, every pair of value-v nodes is a good path: if a component currently holds
     * c nodes of value v, that contributes c*(c-1)/2 paths. The N trivial single-node paths
     * are seeded up front, so c == 1 components correctly add nothing extra.
     *
     * Time:  O(N log N + E * alpha) — sort dominates; unions are near-constant amortized.
     * Space: O(N + E) — adjacency lists, DSU arrays, and the value buckets.
     */
    public static int numberOfGoodPaths(int[] vals, int[][] edges) {
        int n = vals.length;
        List<Integer>[] adj = new List[n];
        for (int i = 0; i < n; i++) adj[i] = new ArrayList<>();
        for (int[] e : edges) {
            adj[e[0]].add(e[1]);
            adj[e[1]].add(e[0]);
        }

        int[] parent = new int[n], rank = new int[n];
        for (int i = 0; i < n; i++) parent[i] = i;

        // Buckets of nodes grouped and ordered by value, so we can process values ascending.
        Map<Integer, List<Integer>> byValue = new HashMap<>();
        for (int i = 0; i < n; i++) byValue.computeIfAbsent(vals[i], k -> new ArrayList<>()).add(i);

        int good = n; // every single node is a good path on its own
        List<Integer> values = new ArrayList<>(byValue.keySet());
        values.sort(null);

        for (int v : values) {
            for (int node : byValue.get(v)) {
                for (int nei : adj[node]) {
                    if (vals[nei] <= v) union(node, nei, parent, rank); // join lower/equal neighbors
                }
            }
            // After all value-v nodes are connected, tally good paths whose max is v per component.
            Map<Integer, Integer> perRoot = new HashMap<>();
            for (int node : byValue.get(v)) {
                int root = find(node, parent);
                perRoot.merge(root, 1, Integer::sum);
            }
            for (int c : perRoot.values()) {
                good += c * (c - 1) / 2; // pair up the value-v nodes sharing a component
            }
        }
        return good;
    }

    private static int find(int x, int[] parent) {
        while (parent[x] != x) {
            parent[x] = parent[parent[x]]; // path halving keeps the tree flat
            x = parent[x];
        }
        return x;
    }

    private static void union(int a, int b, int[] parent, int[] rank) {
        int ra = find(a, parent), rb = find(b, parent);
        if (ra == rb) return;
        if (rank[ra] < rank[rb]) { int t = ra; ra = rb; rb = t; }
        parent[rb] = ra;
        if (rank[ra] == rank[rb]) rank[ra]++;
    }

    public static void main(String[] args) {
        System.out.println(numberOfGoodPaths(
                new int[]{1, 3, 2, 1, 3},
                new int[][]{{0, 1}, {0, 2}, {2, 3}, {2, 4}})); // expected: 6
        System.out.println(numberOfGoodPaths(
                new int[]{1, 1, 2, 2, 3},
                new int[][]{{0, 1}, {1, 2}, {2, 3}, {2, 4}})); // expected: 7
        System.out.println(numberOfGoodPaths(new int[]{1}, new int[][]{})); // expected: 1
        System.out.println(numberOfGoodPaths(
                new int[]{2, 1, 4},
                new int[][]{{0, 1}, {1, 2}})); // expected: 3 (only the 3 trivial single-node paths)
    }
}
