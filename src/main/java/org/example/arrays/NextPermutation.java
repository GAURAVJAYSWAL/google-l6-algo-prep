package org.example.arrays;

import java.util.Arrays;

public class NextPermutation {

    /**
     * The next permutation is the smallest bump over the current one. Scan from
     * the right for the first index i where nums[i] < nums[i+1] (the pivot); the
     * suffix after it is non-increasing (already the largest arrangement). Swap
     * the pivot with the smallest suffix element still greater than it, then
     * reverse the suffix to its smallest (ascending) order. No pivot => fully
     * descending, so reverse the whole array to the first permutation.
     * Time:  O(n) — at most two right-to-left scans plus a reverse.
     * Space: O(1) — done in place.
     */
    public static void nextPermutation(int[] nums) {
        int n = nums.length;
        int i = n - 2;
        while (i >= 0 && nums[i] >= nums[i + 1]) i--;   // find first ascending pivot from right

        if (i >= 0) {
            int j = n - 1;
            while (nums[j] <= nums[i]) j--;             // smallest suffix element > pivot
            swap(nums, i, j);
        }
        reverse(nums, i + 1, n - 1);                    // suffix becomes ascending (smallest)
    }

    private static void swap(int[] nums, int a, int b) {
        int t = nums[a];
        nums[a] = nums[b];
        nums[b] = t;
    }

    private static void reverse(int[] nums, int lo, int hi) {
        while (lo < hi) swap(nums, lo++, hi--);
    }

    public static void main(String[] args) {
        int[] a = {1, 2, 3};
        nextPermutation(a);
        System.out.println(Arrays.toString(a)); // expected: [1, 3, 2]

        int[] b = {3, 2, 1};
        nextPermutation(b);
        System.out.println(Arrays.toString(b)); // expected: [1, 2, 3]

        int[] c = {1, 1, 5};
        nextPermutation(c);
        System.out.println(Arrays.toString(c)); // expected: [1, 5, 1]

        int[] d = {1, 3, 2};
        nextPermutation(d);
        System.out.println(Arrays.toString(d)); // expected: [2, 1, 3]
    }
}
