package org.example.arrays;

public class BestTimeToBuyAndSellStock {

    /**
     * The best sell on day i is constrained by the cheapest price seen on or before
     * day i. Track that running minimum as we go and the answer is the largest
     * price - minSoFar gap — one pass, buy must precede sell by construction.
     *
     * Time:  O(n)  — single pass.
     * Space: O(1)  — two scalars.
     */
    public static int maxProfit(int[] prices) {
        int minSoFar = prices[0];
        int best = 0;
        for (int i = 1; i < prices.length; i++) {
            best = Math.max(best, prices[i] - minSoFar);   // sell today against cheapest prior buy
            minSoFar = Math.min(minSoFar, prices[i]);
        }
        return best;
    }

    public static void main(String[] args) {
        System.out.println(maxProfit(new int[]{7, 1, 5, 3, 6, 4})); // expected: 5  (buy 1, sell 6)
        System.out.println(maxProfit(new int[]{7, 6, 4, 3, 1}));   // expected: 0  (only decreases)
        System.out.println(maxProfit(new int[]{2, 4, 1}));         // expected: 2
    }
}
