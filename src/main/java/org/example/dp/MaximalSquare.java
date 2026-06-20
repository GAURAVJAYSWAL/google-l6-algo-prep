package org.example.dp;

public class MaximalSquare {

    /**
     * State: dp[i][j] = side length of the largest all-ones square whose BOTTOM-RIGHT
     * corner is cell (i, j). A square of side k ending here requires three squares of
     * side k-1 ending just above, just left, and up-left to all coexist, so the
     * transition when the cell is '1' is dp[i][j] = 1 + min(dp[i-1][j], dp[i][j-1],
     * dp[i-1][j-1]); a '0' cell forces dp[i][j] = 0. The answer is the max side seen,
     * squared. We pad with a zero border row/column so edge cells need no special case.
     * Time:  O(m*n) — each cell computed in O(1) from three neighbours.
     * Space: O(m*n) — the dp table (can be reduced to one row, kept clear here).
     */
    public static int maximalSquare(char[][] matrix) {
        int m = matrix.length, n = matrix[0].length;
        int[][] dp = new int[m + 1][n + 1];        // row/col 0 are the zero border
        int maxSide = 0;
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                if (matrix[i - 1][j - 1] == '1') {
                    dp[i][j] = 1 + Math.min(dp[i - 1][j],
                            Math.min(dp[i][j - 1], dp[i - 1][j - 1]));
                    maxSide = Math.max(maxSide, dp[i][j]);
                }
            }
        }
        return maxSide * maxSide;                  // problem asks for area, not side
    }

    public static void main(String[] args) {
        char[][] a = {
                {'1', '0', '1', '0', '0'},
                {'1', '0', '1', '1', '1'},
                {'1', '1', '1', '1', '1'},
                {'1', '0', '0', '1', '0'}
        };
        System.out.println(maximalSquare(a));
        // expected: 4 (a 2x2 square)

        char[][] b = {{'0', '1'}, {'1', '0'}};
        System.out.println(maximalSquare(b));
        // expected: 1

        char[][] c = {{'0'}};
        System.out.println(maximalSquare(c));
        // expected: 0
    }
}
