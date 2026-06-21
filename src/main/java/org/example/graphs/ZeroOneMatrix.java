package org.example.graphs;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;

/**
 * LC 542. 01 Matrix.
 */
public class ZeroOneMatrix {

    /**
     * Key insight: this is a shortest-distance-to-nearest-source problem, so run BFS
     * from ALL zeros at once instead of from each one. Seed the queue with every zero at
     * distance 0 and mark every one as unvisited; the wavefront then expands one ring
     * per round, so the first time it touches a one, it has arrived from the closest
     * zero — that arrival distance is the answer. (Running a separate BFS from each one
     * would cost far more and recompute the same wavefront repeatedly.) Marking a cell
     * visited the instant it is enqueued keeps every cell processed exactly once.
     *
     * Time:  O(rows·cols) — each cell is enqueued and dequeued at most once.
     * Space: O(rows·cols) — the queue can hold a whole ring of the grid.
     */
    public int[][] updateMatrix(int[][] mat) {
        int rows = mat.length, cols = mat[0].length;
        int[][] dist = new int[rows][cols];
        Deque<int[]> queue = new ArrayDeque<>();

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (mat[r][c] == 0) {
                    dist[r][c] = 0;
                    queue.offer(new int[]{r, c}); // every zero is a simultaneous source
                } else {
                    dist[r][c] = -1;              // -1 == not yet reached
                }
            }
        }

        int[][] dirs = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};
        while (!queue.isEmpty()) {
            int[] cell = queue.poll();
            int r = cell[0], c = cell[1];
            for (int[] d : dirs) {
                int nr = r + d[0], nc = c + d[1];
                // First visit to a one fixes its distance: one more than its discoverer.
                if (nr >= 0 && nr < rows && nc >= 0 && nc < cols && dist[nr][nc] == -1) {
                    dist[nr][nc] = dist[r][c] + 1;
                    queue.offer(new int[]{nr, nc});
                }
            }
        }
        return dist;
    }

    public static void main(String[] args) {
        ZeroOneMatrix s = new ZeroOneMatrix();

        int[][] m1 = {{0, 0, 0}, {0, 1, 0}, {0, 0, 0}};
        // The lone 1 sits one step from the zeros all around it.
        System.out.println(Arrays.deepToString(s.updateMatrix(m1)));
        // expected: [[0, 0, 0], [0, 1, 0], [0, 0, 0]]

        int[][] m2 = {{0, 0, 0}, {0, 1, 0}, {1, 1, 1}};
        // Center 1 is 1 away; the bottom corners are 2 away from the nearest zero.
        System.out.println(Arrays.deepToString(s.updateMatrix(m2)));
        // expected: [[0, 0, 0], [0, 1, 0], [1, 2, 1]]

        int[][] m3 = {{0, 1, 1, 1}};
        // Distances grow along the row away from the single zero.
        System.out.println(Arrays.deepToString(s.updateMatrix(m3)));
        // expected: [[0, 1, 2, 3]]
    }
}
