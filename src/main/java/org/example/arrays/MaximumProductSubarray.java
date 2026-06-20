package org.example.arrays;

public class MaximumProductSubarray {

    /**
     * Unlike sum-Kadane, a negative factor swaps the roles of best and worst, so
     * the running MIN can become the new MAX after a sign flip. We carry both the
     * max and min product ending at i; on a negative nums[i] we swap them before
     * extending so each still tracks its correct extreme.
     *
     * Time:  O(n)  — single pass.
     * Space: O(1)  — running max/min scalars.
     */
    public static int maxProduct(int[] nums) {
        int best = nums[0];
        int curMax = nums[0];
        int curMin = nums[0];
        for (int i = 1; i < nums.length; i++) {
            if (nums[i] < 0) {                 // negative flips which extreme is which
                int tmp = curMax;
                curMax = curMin;
                curMin = tmp;
            }
            curMax = Math.max(nums[i], curMax * nums[i]);   // extend or restart
            curMin = Math.min(nums[i], curMin * nums[i]);
            best = Math.max(best, curMax);
        }
        return best;
    }

    public static void main(String[] args) {
        System.out.println(maxProduct(new int[]{2, 3, -2, 4}));   // expected: 6  ([2,3])
        System.out.println(maxProduct(new int[]{-2, 0, -1}));     // expected: 0
        System.out.println(maxProduct(new int[]{-2, 3, -4}));     // expected: 24 (whole array)
        System.out.println(maxProduct(new int[]{-2}));            // expected: -2
    }
}
