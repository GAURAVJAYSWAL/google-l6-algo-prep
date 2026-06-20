package org.example.advancedgraphs;

import java.util.Arrays;
import java.util.PriorityQueue;

/**
 * LC 2577. Minimum Time to Visit a Cell in a Grid.
 */
public class MinimumTimeToVisitCell {

    /**
     * Key insight: each move costs 1 second and a cell can only be entered once time reaches
     * its grid value, so this is a shortest-time search — Dijkstra by arrival time. The twist
     * is waiting: when a neighbor's value is still in the future we burn time by stepping to
     * any adjacent cell and back, +2 per round trip. Since every step flips the parity of both
     * (r+c) and the clock, the earliest legal entry into a future cell is grid[nr][nc], bumped
     * by 1 when its parity disagrees with (nr+nc) — that +1 represents one extra bounce. If
     * both cells neighboring the start require a wait (value > 1), we can never take a first
     * step, so the bottom-right is unreachable and we return -1.
     *
     * Time:  O(m*n log(m*n)) — each cell settles once; heap ops are logarithmic.
     * Space: O(m*n) — the distance grid and the heap.
     */
    public static int minimumTime(int[][] grid) {
        int m = grid.length, n = grid[0].length;
        // The very first move must land on a cell enterable at time 1; if neither can, we are stuck.
        if (grid[0][1] > 1 && grid[1][0] > 1) return -1;

        int[][] dist = new int[m][n];
        for (int[] row : dist) Arrays.fill(row, Integer.MAX_VALUE);
        dist[0][0] = 0;

        PriorityQueue<int[]> pq = new PriorityQueue<>((a, b) -> Integer.compare(a[0], b[0]));
        pq.offer(new int[]{0, 0, 0}); // {time, row, col}
        int[][] dirs = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};

        while (!pq.isEmpty()) {
            int[] cur = pq.poll();
            int t = cur[0], r = cur[1], c = cur[2];
            if (t > dist[r][c]) continue;            // stale heap entry
            if (r == m - 1 && c == n - 1) return t;   // first settle of the target is the optimum

            for (int[] d : dirs) {
                int nr = r + d[0], nc = c + d[1];
                if (nr < 0 || nr >= m || nc < 0 || nc >= n) continue;
                int arrive = t + 1;
                if (grid[nr][nc] > arrive) {
                    arrive = grid[nr][nc];
                    // Adjust to the correct parity for this cell by adding one bounce if needed.
                    if (((arrive - (nr + nc)) & 1) == 1) arrive++;
                }
                if (arrive < dist[nr][nc]) {
                    dist[nr][nc] = arrive;
                    pq.offer(new int[]{arrive, nr, nc});
                }
            }
        }
        return -1; // unreachable (only possible for the blocked-start case, handled above)
    }

    public static void main(String[] args) {
        System.out.println(minimumTime(new int[][]{{0, 1, 3, 2}, {5, 1, 2, 5}, {4, 3, 8, 6}})); // expected: 7
        System.out.println(minimumTime(new int[][]{{0, 2, 4}, {3, 2, 1}, {1, 0, 4}}));           // expected: -1
        System.out.println(minimumTime(new int[][]{{0, 1}, {1, 2}}));                            // expected: 2
        System.out.println(minimumTime(new int[][]{{0, 1, 99}, {99, 1, 99}, {99, 1, 1}}));       // expected: 4 (snake down the value-1 column)
    }
}
