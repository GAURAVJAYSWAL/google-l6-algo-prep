package org.example.graphs;

import java.util.ArrayDeque;
import java.util.Queue;

/**
 * LC 994. Rotting Oranges.
 */
public class RottingOranges {

    /**
     * Key insight: rot spreads to all 4-adjacent fresh oranges simultaneously each minute, which is
     * exactly a multi-source BFS where every initially rotten orange is a level-0 source. The number
     * of BFS layers needed to drain the queue equals the minutes until the last fresh orange rots.
     * Counting fresh oranges up front lets us distinguish "all rotted" from "some unreachable": if
     * any fresh orange remains after the BFS settles, no time suffices, so we return -1.
     *
     * Time:  O(m * n) — each cell is enqueued and processed at most once.
     * Space: O(m * n) — the BFS queue in the worst case where everything starts rotten.
     */
    public static int orangesRotting(int[][] grid) {
        int m = grid.length, n = grid[0].length;
        Queue<int[]> queue = new ArrayDeque<>();
        int fresh = 0;

        for (int r = 0; r < m; r++) {
            for (int c = 0; c < n; c++) {
                if (grid[r][c] == 2) queue.offer(new int[]{r, c}); // all sources start together
                else if (grid[r][c] == 1) fresh++;
            }
        }
        if (fresh == 0) return 0; // nothing to rot, zero minutes

        int[][] dirs = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};
        int minutes = 0;

        while (!queue.isEmpty() && fresh > 0) {
            minutes++; // one full ring of spread
            for (int sz = queue.size(); sz > 0; sz--) {
                int[] cell = queue.poll();
                for (int[] d : dirs) {
                    int nr = cell[0] + d[0], nc = cell[1] + d[1];
                    if (nr < 0 || nc < 0 || nr >= m || nc >= n || grid[nr][nc] != 1) continue;
                    grid[nr][nc] = 2; // mark rotten so it is not processed twice
                    fresh--;
                    queue.offer(new int[]{nr, nc});
                }
            }
        }
        return fresh == 0 ? minutes : -1; // leftover fresh oranges are unreachable
    }

    public static void main(String[] args) {
        int[][] g1 = {{2, 1, 1}, {1, 1, 0}, {0, 1, 1}};
        System.out.println(orangesRotting(g1)); // expected: 4

        int[][] g2 = {{2, 1, 1}, {0, 1, 1}, {1, 0, 1}};
        System.out.println(orangesRotting(g2)); // expected: -1 (bottom-left orange is isolated)

        int[][] g3 = {{0, 2}};
        System.out.println(orangesRotting(g3)); // expected: 0 (no fresh oranges)
    }
}
