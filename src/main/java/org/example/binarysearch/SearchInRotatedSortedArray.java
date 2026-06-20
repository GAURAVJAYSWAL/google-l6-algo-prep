package org.example.binarysearch;

public class SearchInRotatedSortedArray {

    /**
     * A rotated sorted array always has at least one sorted half around mid: if
     * nums[lo] <= nums[mid], the left half is sorted; otherwise the right half
     * is. We test whether target lies inside the sorted half (a clean range
     * check) and shrink toward it, discarding the other half each step.
     * Time:  O(log n) — halve the search space every iteration.
     * Space: O(1)     — iterative, a few indices.
     */
    public static int search(int[] nums, int target) {
        int lo = 0, hi = nums.length - 1;
        while (lo <= hi) {
            int mid = lo + (hi - lo) / 2;
            if (nums[mid] == target) {
                return mid;
            }
            if (nums[lo] <= nums[mid]) {                 // left half [lo..mid] is sorted
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
        return -1;
    }

    public static void main(String[] args) {
        System.out.println(search(new int[]{4, 5, 6, 7, 0, 1, 2}, 0));   // expected: 4
        System.out.println(search(new int[]{4, 5, 6, 7, 0, 1, 2}, 3));   // expected: -1
        System.out.println(search(new int[]{1}, 1));                     // expected: 0
        System.out.println(search(new int[]{5, 1, 3}, 5));               // expected: 0
    }
}
