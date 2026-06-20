package org.example.knapsack;

public class PartitionEqualSubsetSum {

    /**
     * Key insight: an equal two-way split exists iff some subset sums to total/2 — a 0/1
     * subset-sum (knapsack) problem. State: dp[s] = can we hit exactly sum s using items seen
     * so far. Transition per item v: dp[s] |= dp[s - v]. Because each item is usable at most
     * once, we sweep s from HIGH to LOW so dp[s - v] still refers to the PREVIOUS item's row
     * (a low->high sweep would let one item be reused). Early-out when total is odd.
     * Time:  O(n * sum) — each item relaxes every capacity up to total/2 once.
     * Space: O(sum)     — a single boolean row indexed by subset sum.
     */
    public static boolean canPartition(int[] nums) {
        int total = 0;
        for (int v : nums) total += v;
        if ((total & 1) == 1) return false;       // odd total can't split into two equal halves
        int half = total / 2;

        boolean[] dp = new boolean[half + 1];
        dp[0] = true;                             // empty subset sums to 0
        for (int v : nums) {
            for (int s = half; s >= v; s--) {     // high->low keeps each item single-use
                dp[s] = dp[s] || dp[s - v];
            }
            if (dp[half]) return true;            // short-circuit once the target is reachable
        }
        return dp[half];
    }

    public static void main(String[] args) {
        System.out.println(canPartition(new int[]{1, 5, 11, 5}));  // expected: true  ([1,5,5] vs [11])
        System.out.println(canPartition(new int[]{1, 2, 3, 5}));   // expected: false (total 11 is odd)
        System.out.println(canPartition(new int[]{2, 2, 2, 2}));   // expected: true  ([2,2] vs [2,2])
        System.out.println(canPartition(new int[]{1, 2, 5}));      // expected: false (total 8, no subset sums to 4)
    }
}
