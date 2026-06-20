package org.example.dp;

public class UniquePathsII {

    /**
     * State: dp[j] = number of ways to reach cell (i, j) moving only right/down, rolled
     * over rows so dp holds the previous row when we start a new one. Transition for a
     * free cell is dp[j] += dp[j - 1] (paths from above already sit in dp[j], paths from
     * the left are in the just-updated dp[j-1]); an OBSTACLE forces dp[j] = 0 since no
     * path may terminate on it. Seed dp[0] = 1 for the start cell (unless it is blocked).
     * Time:  O(m*n) — every cell relaxed once.
     * Space: O(n) — a single rolling row.
     */
    public static int uniquePathsWithObstacles(int[][] grid) {
        int n = grid[0].length;
        int[] dp = new int[n];
        dp[0] = grid[0][0] == 1 ? 0 : 1;            // blocked start => zero paths overall
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < n; j++) {
                if (grid[i][j] == 1) {
                    dp[j] = 0;                      // unreachable: no path ends on an obstacle
                } else if (j > 0) {
                    dp[j] += dp[j - 1];             // dp[j] = from-above, dp[j-1] = from-left
                }
                // j == 0 on a free cell: only the from-above value carries over, untouched
            }
        }
        return dp[n - 1];
    }

    public static void main(String[] args) {
        System.out.println(uniquePathsWithObstacles(
                new int[][]{{0, 0, 0}, {0, 1, 0}, {0, 0, 0}}));
        // expected: 2

        System.out.println(uniquePathsWithObstacles(new int[][]{{0, 1}, {0, 0}}));
        // expected: 1

        System.out.println(uniquePathsWithObstacles(new int[][]{{1}}));
        // expected: 0 (start blocked)

        System.out.println(uniquePathsWithObstacles(new int[][]{{0, 0}, {1, 1}, {0, 0}}));
        // expected: 0 (row of obstacles cuts the grid)
    }
}
