package org.example.graphs;

import java.util.PriorityQueue;

/**
 * LC 778. Swim in Rising Water.
 */
public class SwimInRisingWater {

    /**
     * Key insight: at time t every cell with elevation <= t is flooded, so a path is usable at time
     * t iff its single highest cell is <= t. We therefore want the path from top-left to
     * bottom-right whose maximum elevation is smallest — a minimax (bottleneck) shortest path.
     * Dijkstra works if we redefine a path's cost as the max elevation along it instead of a sum:
     * always expand the reachable cell with the smallest "max-so-far", and the first time we pop
     * the bottom-right corner that value is the answer (any cheaper route would have popped first).
     *
     * Time:  O(n^2 log n) — each of the n^2 cells is settled once via a heap of size O(n^2).
     * Space: O(n^2) — the visited grid and the priority queue.
     */
    public static int swimInWater(int[][] grid) {
        int n = grid.length;
        boolean[][] visited = new boolean[n][n];
        int[][] dirs = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};

        // Min-heap ordered by the worst (highest) elevation needed to reach a cell.
        PriorityQueue<int[]> pq = new PriorityQueue<>((a, b) -> Integer.compare(a[0], b[0]));
        pq.offer(new int[]{grid[0][0], 0, 0}); // {maxElevationSoFar, row, col}
        visited[0][0] = true;

        while (!pq.isEmpty()) {
            int[] top = pq.poll();
            int time = top[0], r = top[1], c = top[2];
            if (r == n - 1 && c == n - 1) return time; // first settle of the corner is the minimax time

            for (int[] d : dirs) {
                int nr = r + d[0], nc = c + d[1];
                if (nr < 0 || nc < 0 || nr >= n || nc >= n || visited[nr][nc]) continue;
                visited[nr][nc] = true;
                // The bottleneck to reach the neighbor is the larger of the path-so-far and its own depth.
                pq.offer(new int[]{Math.max(time, grid[nr][nc]), nr, nc});
            }
        }
        return -1; // unreachable on a connected grid; defensive only
    }

    public static void main(String[] args) {
        System.out.println(swimInWater(new int[][]{{0, 2}, {1, 3}})); // expected: 3

        int[][] grid = {
                {0, 1, 2, 3, 4},
                {24, 23, 22, 21, 5},
                {12, 13, 14, 15, 16},
                {11, 17, 18, 19, 20},
                {10, 9, 8, 7, 6}
        };
        System.out.println(swimInWater(grid)); // expected: 16

        System.out.println(swimInWater(new int[][]{{0}})); // expected: 0 (already at the corner)
    }
}
