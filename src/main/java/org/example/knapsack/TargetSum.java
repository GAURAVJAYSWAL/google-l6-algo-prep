package org.example.knapsack;

public class TargetSum {

    /**
     * Key insight: split nums into a positive set P (assigned '+') and negative set N ('-').
     * Then sum(P) - sum(N) = target and sum(P) + sum(N) = total, so sum(P) = (total + target)/2.
     * Counting sign assignments reduces to COUNTING subsets that sum to that fixed value — a 0/1
     * subset-sum count DP. State: dp[s] = number of subsets summing to exactly s. Transition per
     * item v: dp[s] += dp[s - v], swept HIGH to LOW so each item is counted at most once.
     * Edge cases: if (total + target) is odd or |target| > total, no assignment exists (answer 0).
     * Time:  O(n * S) — S = (total + target)/2, each item relaxes capacities once.
     * Space: O(S)     — a single count row indexed by subset sum.
     */
    public static int findTargetSumWays(int[] nums, int target) {
        int total = 0;
        for (int v : nums) total += v;
        // Need sum(P) = (total + target)/2 to be a non-negative integer; otherwise impossible.
        if (Math.abs(target) > total || ((total + target) & 1) == 1) return 0;
        int s = (total + target) / 2;

        int[] dp = new int[s + 1];
        dp[0] = 1;                                // exactly one way to form sum 0: choose nothing
        for (int v : nums) {
            for (int c = s; c >= v; c--) {        // high->low: count each item at most once
                dp[c] += dp[c - v];
            }
        }
        return dp[s];
    }

    public static void main(String[] args) {
        System.out.println(findTargetSumWays(new int[]{1, 1, 1, 1, 1}, 3));  // expected: 5
        System.out.println(findTargetSumWays(new int[]{1}, 1));              // expected: 1
        System.out.println(findTargetSumWays(new int[]{1}, 2));              // expected: 0  (|target| > total)
        System.out.println(findTargetSumWays(new int[]{1, 2, 1}, 0));        // expected: 2  (+1-2+1, -1+2-1)
    }
}
