package org.example.graphs;

/**
 * LC 261. Graph Valid Tree.
 */
public class GraphValidTree {

    /**
     * Key insight: a tree on n nodes is exactly a connected, acyclic graph, and such a
     * graph always has precisely n-1 edges. So we first reject anything without n-1 edges
     * (too few cannot connect, too many forces a cycle). Given n-1 edges, "acyclic" and
     * "connected" become equivalent, so union-find suffices: if any edge joins two nodes
     * already in the same set we have a cycle; otherwise all unions succeed and the graph
     * is a valid tree.
     *
     * Time:  O(n * α(n)) — near-linear via union by rank and path compression.
     * Space: O(n) — the parent and rank arrays.
     */
    public static boolean validTree(int n, int[][] edges) {
        if (edges.length != n - 1) return false; // wrong edge count can never be a tree

        int[] parent = new int[n];
        int[] rank = new int[n];
        for (int i = 0; i < n; i++) parent[i] = i;

        for (int[] e : edges) {
            if (!union(parent, rank, e[0], e[1])) return false; // both ends already connected -> cycle
        }
        return true; // n-1 edges with no cycle implies fully connected
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
        if (ra == rb) return false; // same component -> merging would create a cycle
        if (rank[ra] < rank[rb]) { int t = ra; ra = rb; rb = t; }
        parent[rb] = ra;
        if (rank[ra] == rank[rb]) rank[ra]++;
        return true;
    }

    public static void main(String[] args) {
        System.out.println(validTree(5, new int[][]{{0, 1}, {0, 2}, {0, 3}, {1, 4}})); // expected: true
        System.out.println(validTree(5, new int[][]{{0, 1}, {1, 2}, {2, 3}, {1, 3}, {1, 4}})); // expected: false (cycle + extra edge)
        System.out.println(validTree(4, new int[][]{{0, 1}, {2, 3}})); // expected: false (disconnected, too few edges)
        System.out.println(validTree(1, new int[][]{}));               // expected: true (single node)
    }
}
