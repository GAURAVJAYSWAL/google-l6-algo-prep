package org.example.binarysearch;

import java.util.Arrays;

public class FindFirstAndLastPosition {

    /**
     * The target occupies a contiguous block in a sorted array, so its bounds are
     * two halving searches. lowerBound finds the first index with value >= target;
     * if that slot actually holds the target it is the first occurrence.
     * lowerBound(target + 1) finds the first index strictly past the block, so
     * its predecessor is the last occurrence.
     * Time:  O(log n) — two binary searches.
     * Space: O(1)     — index arithmetic only.
     */
    public static int[] searchRange(int[] nums, int target) {
        int first = lowerBound(nums, target);
        if (first == nums.length || nums[first] != target) {
            return new int[]{-1, -1};                    // target never appears
        }
        int last = lowerBound(nums, target + 1) - 1;     // one before the first value > target
        return new int[]{first, last};
    }

    // Smallest index i in [0, n] such that nums[i] >= key (n if none qualify).
    private static int lowerBound(int[] nums, int key) {
        int lo = 0, hi = nums.length;                    // hi is exclusive: search [lo, hi)
        while (lo < hi) {
            int mid = lo + (hi - lo) / 2;
            if (nums[mid] < key) {
                lo = mid + 1;                            // mid too small; first valid is to the right
            } else {
                hi = mid;                                // mid qualifies; keep it as a candidate
            }
        }
        return lo;
    }

    public static void main(String[] args) {
        System.out.println(Arrays.toString(searchRange(new int[]{5, 7, 7, 8, 8, 10}, 8))); // expected: [3, 4]
        System.out.println(Arrays.toString(searchRange(new int[]{5, 7, 7, 8, 8, 10}, 6))); // expected: [-1, -1]
        System.out.println(Arrays.toString(searchRange(new int[]{}, 0)));                  // expected: [-1, -1]
        System.out.println(Arrays.toString(searchRange(new int[]{2, 2, 2}, 2)));           // expected: [0, 2]
    }
}
