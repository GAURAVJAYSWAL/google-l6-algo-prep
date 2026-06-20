package org.example.bitmaskdp;

import java.util.Arrays;

public class PartitionToKEqualSumSubsets {

    /**
     * Key insight: each subset must sum to target = total/k, so we fill buckets ONE AT A TIME
     * rather than juggling all k simultaneously — once a bucket hits target we "close" it and
     * start the next. Approach chosen: backtracking with sorting descending plus two prunes.
     * (1) Sort descending so large, hard-to-place numbers fail fast near the root. (2) When a
     * number cannot start a fresh empty bucket (cur == 0 after the attempt), no other empty
     * bucket can hold it either, so we abandon immediately. We also skip a candidate if the
     * running bucket would overflow target. These prunes make the exponential search tractable.
     * Time:  O(k * 2^n) worst case — n placement decisions across k buckets, heavily pruned.
     * Space: O(n)       — recursion depth plus the used[] flags.
     */
    public static boolean canPartitionKSubsets(int[] nums, int k) {
        int total = Arrays.stream(nums).sum();
        if (total % k != 0) return false;        // cannot split evenly
        int target = total / k;
        Arrays.sort(nums);
        if (nums[nums.length - 1] > target) return false; // a single element overshoots a bucket
        return backtrack(nums, new boolean[nums.length], nums.length - 1, k, 0, target);
    }

    // Fill the current bucket starting from index `start` going downward; close it at target, then recurse for the next bucket.
    private static boolean backtrack(int[] nums, boolean[] used, int start, int bucketsLeft, int cur, int target) {
        if (bucketsLeft == 1) return true;       // last bucket auto-completes (sums are exact by construction)
        if (cur == target) {                     // bucket full -> open a fresh one, scanning from the top again
            return backtrack(nums, used, nums.length - 1, bucketsLeft - 1, 0, target);
        }
        for (int i = start; i >= 0; i--) {
            if (used[i] || cur + nums[i] > target) continue; // skip taken or overflowing candidates
            used[i] = true;
            if (backtrack(nums, used, i - 1, bucketsLeft, cur + nums[i], target)) return true;
            used[i] = false;
            if (cur == 0) break; // this number couldn't even start an empty bucket -> no empty bucket fits it; abandon
        }
        return false;
    }

    public static void main(String[] args) {
        System.out.println(canPartitionKSubsets(new int[]{4, 3, 2, 3, 5, 2, 1}, 4));
        // expected: true  (target 5 each: {5},{1,4},{2,3},{2,3})

        System.out.println(canPartitionKSubsets(new int[]{1, 2, 3, 4}, 3));
        // expected: false (total 10 not divisible by 3)

        System.out.println(canPartitionKSubsets(new int[]{2, 2, 2, 2, 3, 4, 5}, 4));
        // expected: false

        System.out.println(canPartitionKSubsets(new int[]{4, 4, 6, 2, 3, 8, 10, 2, 10, 7}, 4));
        // expected: true  (target 14 each)
    }
}
