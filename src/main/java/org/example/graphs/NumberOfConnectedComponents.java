package org.example.graphs;

/**
 * LC 323. Number of Connected Components in an Undirected Graph.
 */
public class NumberOfConnectedComponents {

    /**
     * Key insight: start by assuming all n nodes are isolated (n components) and let each edge
     * potentially merge two of them. An edge only reduces the component count when its endpoints
     * were in different sets; union-find tells us this in near-constant time, and decrementing the
     * count on every successful merge leaves exactly the number of connected components at the end.
     *
     * Alternative: build adjacency lists and run a DFS/BFS, counting one traversal per unvisited
     * node — also O(V + E) but needs explicit visited tracking and the adjacency structure.
     *
     * Time:  O(n + E * α(n)) — initialization is linear; each edge is a near-constant union.
     * Space: O(n) — the parent and rank arrays.
     */
    public static int countComponents(int n, int[][] edges) {
        int[] parent = new int[n];
        int[] rank = new int[n];
        for (int i = 0; i < n; i++) parent[i] = i;
        int components = n; // every node starts on its own

        for (int[] e : edges) {
            if (union(parent, rank, e[0], e[1])) components--; // merged two distinct components
        }
        return components;
    }

    private static int find(int[] parent, int x) {
        while (parent[x] != x) {
            parent[x] = parent[parent[x]]; // path compression (halving)
            x = parent[x];
        }
        return x;
    }

    private static boolean union(int[] parent, int[] rank, int a, int b) {
        int ra = find(parent, a), rb = find(parent, b);
        if (ra == rb) return false; // already connected -> no merge
        if (rank[ra] < rank[rb]) { int t = ra; ra = rb; rb = t; }
        parent[rb] = ra;
        if (rank[ra] == rank[rb]) rank[ra]++;
        return true;
    }

    public static void main(String[] args) {
        System.out.println(countComponents(5, new int[][]{{0, 1}, {1, 2}, {3, 4}})); // expected: 2
        System.out.println(countComponents(5, new int[][]{{0, 1}, {1, 2}, {2, 3}, {3, 4}})); // expected: 1
        System.out.println(countComponents(4, new int[][]{}));                       // expected: 4 (no edges)
        System.out.println(countComponents(3, new int[][]{{0, 1}, {0, 2}, {1, 2}})); // expected: 1 (extra edge in a cycle)
    }
}
