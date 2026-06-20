package org.example.dp;

public class HouseRobberII {

    /**
     * The houses form a circle, so the first and last are adjacent and cannot both be
     * robbed. That single constraint splits into two independent linear problems: rob
     * the range [0..n-2] (allow the first, forbid the last) or [1..n-1] (forbid the
     * first, allow the last); the answer is the max of the two. Each range is solved
     * by the standard House Robber recurrence best = max(skip, rob + loot[i-2]).
     * Time:  O(n)  — two linear scans.
     * Space: O(1)  — rolling variables in the helper.
     */
    public static int rob(int[] nums) {
        int n = nums.length;
        if (n == 1) return nums[0];                      // no wrap-around to worry about
        // Exclude last vs exclude first to break the circular adjacency.
        return Math.max(robLine(nums, 0, n - 2), robLine(nums, 1, n - 1));
    }

    /** Linear House Robber over the inclusive index range [lo, hi]. */
    private static int robLine(int[] nums, int lo, int hi) {
        int prev2 = 0, prev1 = 0;
        for (int i = lo; i <= hi; i++) {
            int cur = Math.max(prev1, prev2 + nums[i]);  // skip house i, or rob it plus best from i-2
            prev2 = prev1;
            prev1 = cur;
        }
        return prev1;
    }

    public static void main(String[] args) {
        System.out.println(rob(new int[]{2, 3, 2}));      // expected: 3
        System.out.println(rob(new int[]{1, 2, 3, 1}));   // expected: 4
        System.out.println(rob(new int[]{1, 2, 3}));      // expected: 3
        System.out.println(rob(new int[]{200, 3, 140, 20, 10})); // expected: 340
    }
}
