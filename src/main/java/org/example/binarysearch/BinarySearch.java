package org.example.binarysearch;

/**
 * LC 704 — Binary Search (Easy).
 *
 * The canonical sorted-array binary search and the reference template the other
 * binary-search problems in this package build on.
 */
public class BinarySearch {

    /**
     * Maintain the inclusive invariant: if {@code target} exists, it is always inside
     * {@code [lo, hi]}. Each step inspects {@code mid}; because the array is sorted,
     * comparing {@code nums[mid]} to the target lets us discard the half that provably
     * cannot contain it, halving the search window until it is found or collapses.
     * Time:  O(log n) — the window halves every iteration.
     * Space: O(1) — only two indices are tracked.
     */
    public int search(int[] nums, int target) {
        int lo = 0, hi = nums.length - 1;
        while (lo <= hi) {                  // closed interval: empty exactly when lo > hi
            int mid = lo + (hi - lo) / 2;   // avoid lo+hi overflow for large indices
            if (nums[mid] == target) {
                return mid;
            } else if (nums[mid] < target) {
                lo = mid + 1;               // target is strictly right of mid
            } else {
                hi = mid - 1;               // target is strictly left of mid
            }
        }
        return -1;
    }

    public static void main(String[] args) {
        BinarySearch bs = new BinarySearch();

        System.out.println(bs.search(new int[]{-1, 0, 3, 5, 9, 12}, 9));   // expected: 4
        System.out.println(bs.search(new int[]{-1, 0, 3, 5, 9, 12}, 2));   // expected: -1
        System.out.println(bs.search(new int[]{5}, 5));                    // expected: 0
        System.out.println(bs.search(new int[]{2, 5}, 2));                 // expected: 0
    }
}
