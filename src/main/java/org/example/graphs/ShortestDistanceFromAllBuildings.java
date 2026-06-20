package org.example.graphs;

import java.util.ArrayDeque;
import java.util.Queue;

/**
 * LC 317. Shortest Distance from All Buildings.
 */
public class ShortestDistanceFromAllBuildings {

    /**
     * Key insight: the best empty lot minimizes the sum of shortest path distances to every
     * building, and on an unweighted grid each shortest distance is a BFS layer. Rather than
     * BFS from every candidate lot, we BFS once per building and accumulate, into each empty
     * cell, the running distance sum plus a count of how many buildings reached it. A valid
     * answer must be reachable by ALL buildings; tracking reach counts lets us discard cells
     * blocked from some building, and the minimum accumulated distance among fully reached
     * cells is the answer. We BFS from buildings (few) instead of empties (many).
     *
     * Time:  O(B * m * n) — one grid-wide BFS per building B.
     * Space: O(m * n) — the distance-sum and reach-count grids plus the BFS queue.
     */
    public static int shortestDistance(int[][] grid) {
        int m = grid.length, n = grid[0].length;
        int[][] dist = new int[m][n];   // summed distance from all buildings reached so far
        int[][] reach = new int[m][n];  // how many buildings can reach this empty cell
        int totalBuildings = 0;
        int[][] dirs = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};

        for (int r = 0; r < m; r++) {
            for (int c = 0; c < n; c++) {
                if (grid[r][c] != 1) continue;
                totalBuildings++;
                boolean[][] visited = new boolean[m][n]; // fresh per building so each contributes at most once
                Queue<int[]> queue = new ArrayDeque<>();
                queue.offer(new int[]{r, c});
                visited[r][c] = true;
                int steps = 0;

                while (!queue.isEmpty()) {
                    steps++; // distance to cells discovered on the next layer
                    for (int sz = queue.size(); sz > 0; sz--) {
                        int[] cell = queue.poll();
                        for (int[] d : dirs) {
                            int nr = cell[0] + d[0], nc = cell[1] + d[1];
                            // Only walk into in-bounds, unvisited, empty land.
                            if (nr < 0 || nc < 0 || nr >= m || nc >= n) continue;
                            if (visited[nr][nc] || grid[nr][nc] != 0) continue;
                            visited[nr][nc] = true;
                            dist[nr][nc] += steps;
                            reach[nr][nc]++;
                            queue.offer(new int[]{nr, nc});
                        }
                    }
                }
            }
        }

        int best = Integer.MAX_VALUE;
        for (int r = 0; r < m; r++) {
            for (int c = 0; c < n; c++) {
                // Only cells reachable from every building are valid meeting points.
                if (grid[r][c] == 0 && reach[r][c] == totalBuildings) {
                    best = Math.min(best, dist[r][c]);
                }
            }
        }
        return best == Integer.MAX_VALUE ? -1 : best; // no cell reaches all buildings
    }

    public static void main(String[] args) {
        int[][] g1 = {
                {1, 0, 2, 0, 1},
                {0, 0, 0, 0, 0},
                {0, 0, 1, 0, 0}
        };
        System.out.println(shortestDistance(g1)); // expected: 7

        int[][] g2 = {{1, 0}};
        System.out.println(shortestDistance(g2)); // expected: 1

        int[][] g3 = {{1}};
        System.out.println(shortestDistance(g3)); // expected: -1 (no empty land)

        int[][] g4 = {{1, 2}};
        System.out.println(shortestDistance(g4)); // expected: -1 (obstacle blocks the only empty path)
    }
}
