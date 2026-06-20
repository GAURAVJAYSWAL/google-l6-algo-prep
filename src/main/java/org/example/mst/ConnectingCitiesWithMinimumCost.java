package org.example.mst;

import java.util.Arrays;

/**
 * LC 1135. Connecting Cities With Minimum Cost.
 */
public class ConnectingCitiesWithMinimumCost {

    /**
     * Key insight: linking every city for the least money is a Minimum Spanning Tree over a sparse,
     * explicitly-listed edge set, which is exactly Kruskal's territory. Sort the connections by cost
     * ascending and walk them greedily: an edge is accepted only when its endpoints lie in different
     * components (union-find says so in near-constant time), which both avoids cycles and is safe by
     * the cut property. After accepting n-1 edges the tree spans all cities; if we run out of edges
     * before reaching n-1 the graph was disconnected, so no spanning tree exists and we return -1.
     *
     * Time:  O(E log E) — sorting the connections dominates; each union/find is effectively constant.
     * Space: O(n) — the parent and rank arrays of the union-find.
     */
    public static int minimumCost(int n, int[][] connections) {
        Arrays.sort(connections, (a, b) -> Integer.compare(a[2], b[2])); // cheapest edges first

        UnionFind uf = new UnionFind(n + 1); // cities are 1-indexed; slot 0 is unused
        int total = 0, accepted = 0;

        for (int[] c : connections) {
            if (uf.union(c[0], c[1])) {       // joined two distinct components -> keep this edge
                total += c[2];
                if (++accepted == n - 1) return total; // a spanning tree needs exactly n-1 edges
            }
        }
        return -1; // some city was never reachable -> graph not connected
    }

    /** Disjoint-set with path compression and union by rank. */
    static class UnionFind {
        private final int[] parent;
        private final int[] rank;

        UnionFind(int size) {
            parent = new int[size];
            rank = new int[size];
            for (int i = 0; i < size; i++) parent[i] = i;
        }

        int find(int x) {
            while (parent[x] != x) {
                parent[x] = parent[parent[x]]; // path compression (halving)
                x = parent[x];
            }
            return x;
        }

        /** Returns true iff a and b were in different sets and have now been merged. */
        boolean union(int a, int b) {
            int ra = find(a), rb = find(b);
            if (ra == rb) return false; // already connected
            if (rank[ra] < rank[rb]) { int t = ra; ra = rb; rb = t; }
            parent[rb] = ra;
            if (rank[ra] == rank[rb]) rank[ra]++;
            return true;
        }
    }

    public static void main(String[] args) {
        int[][] c1 = {{1, 2, 5}, {1, 3, 6}, {2, 3, 1}};
        System.out.println(minimumCost(3, c1)); // expected: 6 (edges 2-3 cost 1 and 1-2 cost 5)

        int[][] c2 = {{1, 2, 3}, {3, 4, 4}};
        System.out.println(minimumCost(4, c2)); // expected: -1 (cities {1,2} and {3,4} stay split)

        int[][] c3 = {{1, 2, 1}};
        System.out.println(minimumCost(2, c3)); // expected: 1

        int[][] c4 = {{1, 2, 6}, {1, 3, 1}, {2, 3, 5}, {2, 4, 3}, {3, 4, 4}};
        System.out.println(minimumCost(4, c4)); // expected: 8 (1-3, 2-4, 3-4)
    }
}
