package org.example.binarysearch;

public class MedianOfTwoSortedArrays {

    /**
     * The median splits the merged array into a left half and a right half of
     * equal size where every left element <= every right element. We only need
     * to choose how many of the left-half slots come from array A; the rest come
     * from B. Binary search that count on the SMALLER array: a partition is
     * valid when maxLeftA <= minRightB and maxLeftB <= minRightA. ±infinity
     * sentinels handle partitions that fall at an array's edge.
     * Time:  O(log(min(m, n))) — binary search the partition on the shorter array.
     * Space: O(1)              — constant scalars.
     */
    public static double findMedianSortedArrays(int[] a, int[] b) {
        if (a.length > b.length) {
            return findMedianSortedArrays(b, a);         // ensure a is the smaller array
        }
        int m = a.length, n = b.length;
        int half = (m + n + 1) / 2;                      // size of the combined left half
        int lo = 0, hi = m;
        while (lo <= hi) {
            int i = lo + (hi - lo) / 2;                  // elements taken from a on the left
            int j = half - i;                            // remaining left slots filled from b

            int maxLeftA  = (i == 0) ? Integer.MIN_VALUE : a[i - 1];
            int minRightA = (i == m) ? Integer.MAX_VALUE : a[i];
            int maxLeftB  = (j == 0) ? Integer.MIN_VALUE : b[j - 1];
            int minRightB = (j == n) ? Integer.MAX_VALUE : b[j];

            if (maxLeftA <= minRightB && maxLeftB <= minRightA) {
                int maxLeft = Math.max(maxLeftA, maxLeftB);
                if (((m + n) & 1) == 1) {
                    return maxLeft;                      // odd total: left half holds the median
                }
                int minRight = Math.min(minRightA, minRightB);
                return (maxLeft + minRight) / 2.0;       // even total: average the two middles
            } else if (maxLeftA > minRightB) {
                hi = i - 1;                              // took too much from a; pull i left
            } else {
                lo = i + 1;                              // took too little from a; push i right
            }
        }
        throw new IllegalArgumentException("Input arrays are not sorted.");
    }

    public static void main(String[] args) {
        System.out.println(findMedianSortedArrays(new int[]{1, 3}, new int[]{2}));        // expected: 2.0
        System.out.println(findMedianSortedArrays(new int[]{1, 2}, new int[]{3, 4}));     // expected: 2.5
        System.out.println(findMedianSortedArrays(new int[]{}, new int[]{1}));            // expected: 1.0
        System.out.println(findMedianSortedArrays(new int[]{1, 2, 3, 4}, new int[]{5}));  // expected: 3.0
    }
}
