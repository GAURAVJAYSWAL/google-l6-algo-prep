package org.example.dp;

public class CoinChangeII {

    /**
     * State: dp[a] = number of distinct combinations summing to amount a using the coins
     * processed so far. Transition while folding in a coin c: dp[a] += dp[a - c] for
     * a from c upward. The crucial ordering is the COINS in the outer loop and the
     * amount inner — this introduces each coin once, so combinations are counted
     * without regard to order ({1,2} and {2,1} collapse to one). Swapping the loops
     * would count permutations instead. Base case dp[0] = 1: the empty combination.
     * Time:  O(amount * coins) — each (coin, amount) pair relaxed once.
     * Space: O(amount) — single rolling row reused across coins.
     */
    public static int change(int amount, int[] coins) {
        int[] dp = new int[amount + 1];
        dp[0] = 1;                                  // one way to make 0: take nothing
        for (int c : coins) {                       // coins OUTER => combinations, not permutations
            for (int a = c; a <= amount; a++) {
                dp[a] += dp[a - c];
            }
        }
        return dp[amount];
    }

    public static void main(String[] args) {
        System.out.println(change(5, new int[]{1, 2, 5}));
        // expected: 4 ({5},{2,2,1},{2,1,1,1},{1,1,1,1,1})

        System.out.println(change(3, new int[]{2}));
        // expected: 0

        System.out.println(change(10, new int[]{10}));
        // expected: 1

        System.out.println(change(0, new int[]{1, 2}));
        // expected: 1
    }
}
