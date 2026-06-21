package org.example.binarysearch;

public class SearchInRotatedSortedArrayII {

    /**
     * Same as the no-duplicates version: one half around mid is sorted and we
     * shrink toward the half that could contain target. Duplicates break the
     * sorted-half test only when nums[lo] == nums[mid] == nums[hi] — we cannot
     * tell which side is sorted — so we conservatively drop both endpoints by one
     * and retry. That degenerate step makes the worst case O(n) (e.g. all equal).
     * Time:  O(log n) average, O(n) worst — duplicates can force linear shrinking.
     * Space: O(1)                          — iterative, a few indices.
     */
    public static boolean search(int[] nums, int target) {
        int lo = 0, hi = nums.length - 1;
        while (lo <= hi) {
            int mid = lo + (hi - lo) / 2;
            if (nums[mid] == target) {
                return true;
            }
            if (nums[lo] == nums[mid] && nums[mid] == nums[hi]) {
                lo++;                                    // ambiguous: can't pick a sorted half, trim both ends
                hi--;
            } else if (nums[lo] <= nums[mid]) {          // left half [lo..mid] is sorted
                if (nums[lo] <= target && target < nums[mid]) {
                    hi = mid - 1;                        // target is in the sorted left half
                } else {
                    lo = mid + 1;
                }
            } else {                                     // right half [mid..hi] is sorted
                if (nums[mid] < target && target <= nums[hi]) {
                    lo = mid + 1;                        // target is in the sorted right half
                } else {
                    hi = mid - 1;
                }
            }
        }
        return false;
    }

    public static void main(String[] args) {
        System.out.println(search(new int[]{2, 5, 6, 0, 0, 1, 2}, 0));   // expected: true
        System.out.println(search(new int[]{2, 5, 6, 0, 0, 1, 2}, 3));   // expected: false
        System.out.println(search(new int[]{1, 0, 1, 1, 1}, 0));         // expected: true
        System.out.println(search(new int[]{1, 1, 1, 1, 1}, 2));         // expected: false
    }
}
