package org.example.intervaldp;

/**
 * LC 877. Stone Game.
 */
public class StoneGame {

    /**
     * State: dp[i][j] = best score difference (current player minus opponent) achievable
     * on piles[i..j] when it is the current player's turn.
     * Transition: take the left pile (piles[i] - dp[i+1][j]) or the right pile
     * (piles[j] - dp[i][j-1]); subtracting the subgame value flips perspective to the
     * opponent. dp[i][i] = piles[i]. Alice wins iff dp[0][n-1] > 0.
     * (O(1) trick: with an even count of piles and a nonzero total, the first player can
     * always claim either all odd-indexed or all even-indexed piles, so Alice always wins —
     * return true. The general DP below also handles arbitrary inputs.)
     * Time:  O(n^2) — fill each cell of the upper triangle once.
     * Space: O(n^2) — the dp table.
     */
    public static boolean stoneGame(int[] piles) {
        int n = piles.length;
        int[][] dp = new int[n][n];
        for (int i = 0; i < n; i++) dp[i][i] = piles[i];    // single pile: take it outright
        for (int len = 2; len <= n; len++) {
            for (int i = 0; i + len - 1 < n; i++) {
                int j = i + len - 1;
                dp[i][j] = Math.max(piles[i] - dp[i + 1][j], // take left, opponent plays i+1..j
                                    piles[j] - dp[i][j - 1]); // take right, opponent plays i..j-1
            }
        }
        return dp[0][n - 1] > 0;
    }

    public static void main(String[] args) {
        System.out.println(stoneGame(new int[]{5, 3, 4, 5}));    // expected: true
        System.out.println(stoneGame(new int[]{3, 7, 2, 3}));    // expected: true
        System.out.println(stoneGame(new int[]{7, 8, 8, 10}));   // expected: true
        System.out.println(stoneGame(new int[]{2, 4}));          // expected: true
    }
}
