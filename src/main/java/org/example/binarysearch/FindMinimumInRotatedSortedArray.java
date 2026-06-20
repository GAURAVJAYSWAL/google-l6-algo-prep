package org.example.binarysearch;

public class FindMinimumInRotatedSortedArray {

    /**
     * The minimum is the single "drop" point of the rotation. Comparing mid to
     * the RIGHT end tells us which side the drop is on: if nums[mid] > nums[hi],
     * the array dips somewhere to the right of mid, so the min is in (mid, hi];
     * otherwise mid itself could be the min and the answer is in [lo, mid].
     * Comparing to hi (not lo) sidesteps the fully-sorted ambiguity.
     * Time:  O(log n) — halve the range each step.
     * Space: O(1)     — two indices.
     */
    public static int findMin(int[] nums) {
        int lo = 0, hi = nums.length - 1;
        while (lo < hi) {                                // converge to a single survivor
            int mid = lo + (hi - lo) / 2;
            if (nums[mid] > nums[hi]) {
                lo = mid + 1;                            // drop is strictly right of mid
            } else {
                hi = mid;                                // mid may be the min; keep it
            }
        }
        return nums[lo];
    }

    public static void main(String[] args) {
        System.out.println(findMin(new int[]{3, 4, 5, 1, 2}));        // expected: 1
        System.out.println(findMin(new int[]{4, 5, 6, 7, 0, 1, 2}));  // expected: 0
        System.out.println(findMin(new int[]{11, 13, 15, 17}));       // expected: 11 (no rotation)
        System.out.println(findMin(new int[]{2, 1}));                 // expected: 1
    }
}
