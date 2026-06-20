package org.example.graphs;

/**
 * LC 1254. Number of Closed Islands.
 */
public class NumberOfClosedIslands {

    /**
     * Key insight: a closed island is land (0) fully surrounded by water (1) and not
     * touching the border. Any island reachable from the border is therefore NOT
     * closed, so we first flood-fill (sink) every border-connected land cell. After
     * that, every remaining land blob is guaranteed interior, and a single scan counts
     * those blobs exactly as in the standard island count.
     *
     * Time:  O(m*n) — border flooding plus the interior scan each touch a cell O(1) times.
     * Space: O(m*n) — worst-case DFS recursion depth.
     */
    public static int closedIsland(int[][] grid) {
        if (grid == null || grid.length == 0) return 0;
        int m = grid.length, n = grid[0].length;

        // Flood every land cell that touches the border; these can never be closed.
        for (int r = 0; r < m; r++) {
            flood(grid, r, 0);
            flood(grid, r, n - 1);
        }
        for (int c = 0; c < n; c++) {
            flood(grid, 0, c);
            flood(grid, m - 1, c);
        }

        // Whatever land remains is interior; count maximal blobs.
        int count = 0;
        for (int r = 0; r < m; r++) {
            for (int c = 0; c < n; c++) {
                if (grid[r][c] == 0) {
                    count++;
                    flood(grid, r, c);
                }
            }
        }
        return count;
    }

    private static void flood(int[][] grid, int r, int c) {
        if (r < 0 || c < 0 || r >= grid.length || c >= grid[0].length || grid[r][c] != 0) return;
        grid[r][c] = 1; // convert land to water to mark visited
        flood(grid, r + 1, c);
        flood(grid, r - 1, c);
        flood(grid, r, c + 1);
        flood(grid, r, c - 1);
    }

    public static void main(String[] args) {
        int[][] g1 = {
                {1, 1, 1, 1, 1, 1, 1, 0},
                {1, 0, 0, 0, 0, 1, 1, 0},
                {1, 0, 1, 0, 1, 1, 1, 0},
                {1, 0, 0, 0, 0, 1, 0, 1},
                {1, 1, 1, 1, 1, 1, 1, 0}
        };
        System.out.println(closedIsland(g1)); // expected: 2

        int[][] g2 = {
                {0, 0, 1, 0, 0},
                {0, 1, 0, 1, 0},
                {0, 1, 1, 1, 0}
        };
        System.out.println(closedIsland(g2)); // expected: 1

        int[][] g3 = {
                {1, 1, 1, 1, 1, 1, 1},
                {1, 0, 0, 0, 0, 0, 1},
                {1, 0, 1, 1, 1, 0, 1},
                {1, 0, 1, 0, 1, 0, 1},
                {1, 0, 1, 1, 1, 0, 1},
                {1, 0, 0, 0, 0, 0, 1},
                {1, 1, 1, 1, 1, 1, 1}
        };
        System.out.println(closedIsland(g3)); // expected: 2
    }
}
