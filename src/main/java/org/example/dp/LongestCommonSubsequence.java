package org.example.dp;

public class LongestCommonSubsequence {

    /**
     * State: dp[i][j] = LCS length of the first i chars of a and first j chars of b.
     * Transition: if a[i-1] == b[j-1] the two chars pair up, dp[i][j] = dp[i-1][j-1]+1;
     * otherwise drop one end, dp[i][j] = max(dp[i-1][j], dp[i][j-1]). Row/column 0 are
     * empty-prefix base cases of 0. (Two rows suffice to drop to O(min) space, but the
     * full 2-D table is kept here for clarity and easy backtracking of the actual LCS.)
     * Time:  O(m * n)  — fill every cell of the table once.
     * Space: O(m * n)  — the full dp grid.
     */
    public static int longestCommonSubsequence(String a, String b) {
        int m = a.length(), n = b.length();
        int[][] dp = new int[m + 1][n + 1];              // dp[0][*] and dp[*][0] default to 0
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                if (a.charAt(i - 1) == b.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1] + 1;     // matching chars extend the diagonal subproblem
                } else {
                    dp[i][j] = Math.max(dp[i - 1][j], dp[i][j - 1]);
                }
            }
        }
        return dp[m][n];
    }

    public static void main(String[] args) {
        System.out.println(longestCommonSubsequence("abcde", "ace")); // expected: 3
        System.out.println(longestCommonSubsequence("abc", "abc"));   // expected: 3
        System.out.println(longestCommonSubsequence("abc", "def"));   // expected: 0
        System.out.println(longestCommonSubsequence("bl", "yby"));    // expected: 1
    }
}
