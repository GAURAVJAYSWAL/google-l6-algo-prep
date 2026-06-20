package org.example.intervaldp;

import java.util.Arrays;

/**
 * LC 312. Burst Balloons.
 */
public class BurstBalloons {

    /**
     * State: dp[i][j] = max coins from bursting every balloon strictly between virtual
     * boundaries i and j (exclusive), on a padded array with 1s at both ends.
     * Transition: pick k as the LAST balloon to burst in (i, j); when it pops, its only
     * surviving neighbours are i and j, contributing vals[i]*vals[k]*vals[j], so
     * dp[i][j] = max over i<k<j of dp[i][k] + vals[i]*vals[k]*vals[j] + dp[k][j].
     * Choosing the last (not first) burst makes the two subranges independent.
     * Time:  O(n^3) — O(n^2) intervals, each scanning O(n) split points.
     * Space: O(n^2) — the dp table.
     */
    public static int maxCoins(int[] nums) {
        int n = nums.length;
        int[] vals = new int[n + 2];
        vals[0] = vals[n + 1] = 1;                          // padding so edge bursts see a 1
        for (int i = 0; i < n; i++) vals[i + 1] = nums[i];

        int[][] dp = new int[n + 2][n + 2];
        for (int len = 2; len <= n + 1; len++) {            // span between boundaries i and j
            for (int i = 0; i + len <= n + 1; i++) {
                int j = i + len;
                for (int k = i + 1; k < j; k++) {           // k is the last balloon popped in (i, j)
                    int coins = dp[i][k] + vals[i] * vals[k] * vals[j] + dp[k][j];
                    if (coins > dp[i][j]) dp[i][j] = coins;
                }
            }
        }
        return dp[0][n + 1];
    }

    public static void main(String[] args) {
        System.out.println(maxCoins(new int[]{3, 1, 5, 8}));  // expected: 167
        System.out.println(maxCoins(new int[]{1, 5}));        // expected: 10
        System.out.println(maxCoins(new int[]{7}));           // expected: 7
        System.out.println(maxCoins(new int[]{9, 76, 64}));   // expected: 44416
        System.out.println(Arrays.toString(new int[]{}));     // expected: [] (empty input -> 0 coins)
    }
}
