package org.example.dp;

public class UniquePaths {

    /**
     * State: dp[i][j] = number of paths to cell (i,j) moving only right or down. Each
     * cell is reached from above or from the left, so dp[i][j] = dp[i-1][j] + dp[i][j-1],
     * with the top row and left column all 1 (a single straight-line path). Processing
     * row by row, dp[j] already holds the value from the row above; adding dp[j-1] (the
     * cell to the left, just updated) collapses the grid to a single rolling row.
     * (Closed form: the answer is C(m+n-2, m-1) — choosing which of the m+n-2 moves go down.)
     * Time:  O(m * n)  — visit every cell once.
     * Space: O(n)      — one row of width n.
     */
    public static int uniquePaths(int m, int n) {
        int[] dp = new int[n];
        java.util.Arrays.fill(dp, 1);                    // top row: exactly one path to each cell
        for (int i = 1; i < m; i++) {
            for (int j = 1; j < n; j++) {
                dp[j] += dp[j - 1];                       // dp[j] (from above) + dp[j-1] (from the left)
            }
        }
        return dp[n - 1];
    }

    public static void main(String[] args) {
        System.out.println(uniquePaths(3, 7));   // expected: 28
        System.out.println(uniquePaths(3, 2));   // expected: 3
        System.out.println(uniquePaths(1, 1));   // expected: 1
        System.out.println(uniquePaths(3, 3));   // expected: 6
    }
}
