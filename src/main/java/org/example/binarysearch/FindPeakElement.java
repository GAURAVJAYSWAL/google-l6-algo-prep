package org.example.binarysearch;

public class FindPeakElement {

    /**
     * With nums[-1] = nums[n] = -infinity and no equal neighbors, the slope at
     * mid points toward a peak: if nums[mid] < nums[mid+1] the array is still
     * rising, so a peak must exist to the right; otherwise we are at or past a
     * peak and one exists at mid or to its left. Following the ascent always
     * lands on some peak.
     * Time:  O(log n) — halve the range each step.
     * Space: O(1)     — two indices.
     */
    public static int findPeakElement(int[] nums) {
        int lo = 0, hi = nums.length - 1;
        while (lo < hi) {                                // converge to a single index
            int mid = lo + (hi - lo) / 2;               // mid+1 stays in bounds since lo < hi
            if (nums[mid] < nums[mid + 1]) {
                lo = mid + 1;                            // ascending: a peak lies to the right
            } else {
                hi = mid;                                // descending (or peak): keep mid as candidate
            }
        }
        return lo;
    }

    public static void main(String[] args) {
        System.out.println(findPeakElement(new int[]{1, 2, 3, 1}));          // expected: 2
        System.out.println(findPeakElement(new int[]{1, 2, 1, 3, 5, 6, 4})); // expected: 5 (index of a peak; 1 also valid)
        System.out.println(findPeakElement(new int[]{1}));                   // expected: 0
        System.out.println(findPeakElement(new int[]{5, 4, 3, 2, 1}));       // expected: 0
    }
}
