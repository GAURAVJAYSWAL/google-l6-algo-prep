package org.example.graphs;

import java.util.Arrays;

/**
 * LC 1101. The Earliest Moment When Everyone Become Friends.
 */
public class EarliestMomentEveryoneFriends {

    /**
     * Key insight: friendship is the transitive closure of the introductions, so the group is
     * "fully acquainted" exactly when its people form a single connected component. Processing
     * logs in increasing timestamp order and unioning each pair, the moment the component count
     * first drops to 1 is the earliest such time — earlier introductions could not have done it,
     * and later ones are irrelevant. If we exhaust the logs still split, it never happens (-1).
     *
     * Time:  O(L log L + L * α(n)) — sorting dominates; each union is near-constant.
     * Space: O(n) — the parent and rank arrays (sorting the L*3 logs is in place).
     */
    public static int earliestAcq(int[][] logs, int n) {
        Arrays.sort(logs, (a, b) -> Integer.compare(a[0], b[0])); // earliest introductions first

        int[] parent = new int[n];
        int[] rank = new int[n];
        for (int i = 0; i < n; i++) parent[i] = i;
        int components = n; // everyone starts in their own component

        for (int[] log : logs) {
            if (union(parent, rank, log[1], log[2])) {
                if (--components == 1) return log[0]; // first time the whole group connects
            }
        }
        return -1; // some people never became acquainted
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
        if (ra == rb) return false; // already acquainted -> no new merge
        if (rank[ra] < rank[rb]) { int t = ra; ra = rb; rb = t; }
        parent[rb] = ra;
        if (rank[ra] == rank[rb]) rank[ra]++;
        return true;
    }

    public static void main(String[] args) {
        int[][] logs1 = {
                {20190101, 0, 1}, {20190104, 3, 4}, {20190107, 2, 3},
                {20190211, 1, 5}, {20190224, 2, 4}, {20190301, 0, 3},
                {20190312, 1, 2}, {20190322, 4, 5}
        };
        System.out.println(earliestAcq(logs1, 6)); // expected: 20190301

        int[][] logs2 = {
                {0, 2, 0}, {1, 0, 1}, {3, 0, 3}, {4, 1, 2}, {7, 3, 1}
        };
        System.out.println(earliestAcq(logs2, 4)); // expected: 3

        int[][] logs3 = {{0, 0, 1}, {1, 2, 3}};
        System.out.println(earliestAcq(logs3, 4)); // expected: -1 (two pairs never join)
    }
}
