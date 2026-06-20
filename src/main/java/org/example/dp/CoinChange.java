package org.example.dp;

import java.util.Arrays;

public class CoinChange {

    /**
     * Unbounded knapsack on the amount axis. State: dp[a] = fewest coins summing to
     * exactly a. Transition: to make amount a, try each coin c as the LAST coin used,
     * leaving subproblem a-c; dp[a] = min over c of dp[a-c] + 1. Coins are reusable,
     * so we sweep amounts ascending and read dp[a-c] of the SAME row, which already
     * accounts for using c any number of times. Unreachable amounts stay at infinity.
     * Time:  O(amount * coins)  — each amount tries every coin once.
     * Space: O(amount)          — one dp row indexed by amount.
     */
    public static int coinChange(int[] coins, int amount) {
        int[] dp = new int[amount + 1];
        Arrays.fill(dp, amount + 1);                     // sentinel "infinity": no solution can exceed `amount` coins
        dp[0] = 0;                                       // zero coins make amount 0
        for (int a = 1; a <= amount; a++) {
            for (int c : coins) {
                if (c <= a) {
                    dp[a] = Math.min(dp[a], dp[a - c] + 1);
                }
            }
        }
        return dp[amount] > amount ? -1 : dp[amount];    // still sentinel => amount is unreachable
    }

    public static void main(String[] args) {
        System.out.println(coinChange(new int[]{1, 2, 5}, 11));  // expected: 3
        System.out.println(coinChange(new int[]{2}, 3));         // expected: -1
        System.out.println(coinChange(new int[]{1}, 0));         // expected: 0
        System.out.println(coinChange(new int[]{1, 5, 10}, 18)); // expected: 5
    }
}
