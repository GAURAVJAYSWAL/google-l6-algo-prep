package org.example.advancedgraphs;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

/**
 * LC 2101. Detonate the Maximum Bombs.
 */
public class DetonateTheMaximumBombs {

    /**
     * Key insight: detonation is directed and asymmetric — bomb a triggers bomb b iff b lies
     * within a's blast radius, which need not be mutual (a big bomb can reach a small one but
     * not vice versa). So build a directed graph with edge a -> b when b is inside a's radius,
     * then the bombs set off by manually detonating a is exactly the set reachable from a.
     * Run a traversal from every starting bomb and take the largest reachable count.
     *
     * Time:  O(n^3) — O(n^2) edges to build, and an O(n + edges) = O(n^2) traversal from each of n bombs.
     * Space: O(n^2) — the adjacency lists in the worst (dense) case.
     */
    public static int maximumDetonation(int[][] bombs) {
        int n = bombs.length;
        List<Integer>[] adj = new List[n];
        for (int i = 0; i < n; i++) adj[i] = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (i == j) continue;
                long dx = (long) bombs[i][0] - bombs[j][0];
                long dy = (long) bombs[i][1] - bombs[j][1];
                long r = bombs[i][2];
                // Compare squared distance against squared radius in long to dodge int overflow.
                if (dx * dx + dy * dy <= r * r) adj[i].add(j); // j is within i's blast -> i triggers j
            }
        }

        int best = 0;
        for (int i = 0; i < n; i++) best = Math.max(best, reachable(i, adj, n));
        return best;
    }

    private static int reachable(int start, List<Integer>[] adj, int n) {
        boolean[] seen = new boolean[n];
        Deque<Integer> stack = new ArrayDeque<>();
        stack.push(start);
        seen[start] = true;
        int count = 0;
        while (!stack.isEmpty()) {
            int u = stack.pop();
            count++;
            for (int v : adj[u]) {
                if (!seen[v]) {
                    seen[v] = true; // mark on push so a bomb is never counted twice
                    stack.push(v);
                }
            }
        }
        return count;
    }

    public static void main(String[] args) {
        System.out.println(maximumDetonation(new int[][]{{2, 1, 3}, {6, 1, 4}})); // expected: 2
        System.out.println(maximumDetonation(new int[][]{{1, 1, 5}, {10, 10, 5}})); // expected: 1 (too far apart)
        System.out.println(maximumDetonation(
                new int[][]{{1, 1, 5}, {10, 10, 5}, {5, 5, 25}, {2, 2, 5}})); // expected: 4 (bomb at (5,5) r=25 reaches all)
        System.out.println(maximumDetonation(
                new int[][]{{1, 2, 3}, {2, 3, 1}, {3, 4, 2}, {4, 5, 3}, {5, 6, 4}})); // expected: 5 (chain reaction)
    }
}
