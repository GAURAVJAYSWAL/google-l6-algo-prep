package org.example.dp;

public class EditDistance {

    /**
     * State: dp[i][j] = minimum edits to turn the first i chars of word1 into the first j
     * chars of word2. If the current characters match, no operation is needed and we
     * inherit dp[i-1][j-1]. Otherwise we take 1 + the cheapest of three moves:
     *   delete  -> dp[i-1][j]   (drop word1's i-th char),
     *   insert  -> dp[i][j-1]   (add word2's j-th char),
     *   replace -> dp[i-1][j-1] (swap word1's i-th into word2's j-th).
     * Base row/column encode building a string from empty by pure inserts/deletes.
     * Time:  O(m*n) — each cell from three neighbours in O(1).
     * Space: O(m*n) — full table (reducible to two rows).
     */
    public static int minDistance(String word1, String word2) {
        int m = word1.length(), n = word2.length();
        int[][] dp = new int[m + 1][n + 1];
        for (int i = 0; i <= m; i++) dp[i][0] = i;  // delete i chars to reach ""
        for (int j = 0; j <= n; j++) dp[0][j] = j;  // insert j chars from ""
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                if (word1.charAt(i - 1) == word2.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1];     // characters align, free move
                } else {
                    dp[i][j] = 1 + Math.min(dp[i - 1][j - 1],         // replace
                            Math.min(dp[i - 1][j],                    // delete
                                    dp[i][j - 1]));                   // insert
                }
            }
        }
        return dp[m][n];
    }

    public static void main(String[] args) {
        System.out.println(minDistance("horse", "ros"));
        // expected: 3

        System.out.println(minDistance("intention", "execution"));
        // expected: 5

        System.out.println(minDistance("", "abc"));
        // expected: 3

        System.out.println(minDistance("same", "same"));
        // expected: 0
    }
}
