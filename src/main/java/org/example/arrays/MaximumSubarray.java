package org.example.arrays;

public class MaximumSubarray {

    /**
     * Kadane's: the best subarray ending at i either extends the best ending at
     * i-1 or restarts at nums[i] — whichever is larger. A negative running sum
     * can only hurt what follows, so we drop it and start fresh.
     *
     * Time:  O(n)  — single pass.
     * Space: O(1)  — two running scalars.
     */
    public static int maxSubArray(int[] nums) {
        int best = nums[0];
        int current = nums[0];
        for (int i = 1; i < nums.length; i++) {
            current = Math.max(nums[i], current + nums[i]);   // extend or restart
            best = Math.max(best, current);
        }
        return best;
    }

    public static void main(String[] args) {
        System.out.println(maxSubArray(new int[]{-2, 1, -3, 4, -1, 2, 1, -5, 4})); // expected: 6  ([4,-1,2,1])
        System.out.println(maxSubArray(new int[]{1}));                             // expected: 1
        System.out.println(maxSubArray(new int[]{5, 4, -1, 7, 8}));               // expected: 23
        System.out.println(maxSubArray(new int[]{-3, -1, -2}));                   // expected: -1
    }
}
