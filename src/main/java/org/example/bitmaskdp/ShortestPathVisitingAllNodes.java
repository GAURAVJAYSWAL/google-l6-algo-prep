package org.example.bitmaskdp;

import java.util.ArrayDeque;
import java.util.Queue;

public class ShortestPathVisitingAllNodes {

    /**
     * Key insight: a "state" is not just the current node but (node, visitedBitmask) — the
     * set of nodes seen so far. Revisiting nodes is allowed, so the graph of plain nodes has
     * cycles, but the state graph does not regress: each edge only ever adds bits to the mask.
     * Edges are unweighted, so BFS over states finds the fewest steps. We seed the queue with
     * every node simultaneously (mask = 1<<node), and the first state whose mask is full
     * ((1<<n)-1) is the answer. A visited set over (node, mask) prevents reprocessing.
     * Time:  O(2^n * n^2) — 2^n masks * n nodes as states, each expanding up to n neighbors.
     * Space: O(2^n * n)   — the visited set and queue over all states.
     */
    public static int shortestPathLength(int[][] graph) {
        int n = graph.length;
        if (n == 1) return 0; // single node: already "all visited" with zero edges
        int fullMask = (1 << n) - 1;

        Queue<int[]> queue = new ArrayDeque<>(); // each entry: {node, mask}
        boolean[][] visited = new boolean[n][1 << n];
        for (int node = 0; node < n; node++) { // multi-source: start from every node at once
            queue.offer(new int[]{node, 1 << node});
            visited[node][1 << node] = true;
        }

        int steps = 0;
        while (!queue.isEmpty()) {
            int size = queue.size();
            for (int i = 0; i < size; i++) {
                int[] cur = queue.poll();
                int node = cur[0], mask = cur[1];
                if (mask == fullMask) return steps; // first full mask reached is the shortest
                for (int next : graph[node]) {
                    int nextMask = mask | (1 << next); // accumulate the newly seen node
                    if (!visited[next][nextMask]) {
                        visited[next][nextMask] = true;
                        queue.offer(new int[]{next, nextMask});
                    }
                }
            }
            steps++;
        }
        return -1; // unreachable for a connected graph, but keeps the method total
    }

    public static void main(String[] args) {
        System.out.println(shortestPathLength(new int[][]{{1, 2, 3}, {0}, {0}, {0}}));
        // expected: 4  (e.g. 1 -> 0 -> 2 -> 0 -> 3)

        System.out.println(shortestPathLength(new int[][]{{1}, {0, 2, 4}, {1, 3, 4}, {2}, {1, 2}}));
        // expected: 4

        System.out.println(shortestPathLength(new int[][]{{}}));
        // expected: 0  (single node)

        System.out.println(shortestPathLength(new int[][]{{1}, {0}}));
        // expected: 1  (0 -> 1)
    }
}
