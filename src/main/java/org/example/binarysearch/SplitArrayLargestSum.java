package org.example.binarysearch;

public class SplitArrayLargestSum {

    /**
     * Minimizing the largest subarray sum is monotonic in the cap we allow: a
     * bigger cap never needs more pieces, so we binary search the smallest cap
     * achievable with at most k subarrays. Feasibility is a greedy left-to-right
     * sweep — keep extending the current piece until adding the next element
     * would exceed the cap, then start a new piece. The search runs over
     * [max element, total sum]: the cap must fit the biggest single element, and
     * the whole array in one piece is always feasible.
     * Time:  O(n log(sum)) — greedy check scans n elements per binary-search step.
     * Space: O(1)          — running counters only.
     */
    public static int splitArray(int[] nums, int k) {
        int lo = 0;                                      // lower bound: the largest single element
        long hi = 0;                                     // upper bound: everything in one piece
        for (int v : nums) {
            lo = Math.max(lo, v);
            hi += v;
        }
        long left = lo, right = hi;
        while (left < right) {
            long cap = left + (right - left) / 2;
            if (piecesNeeded(nums, cap) <= k) {
                right = cap;                             // cap suffices; try a tighter one
            } else {
                left = cap + 1;                          // too small; needs more than k pieces
            }
        }
        return (int) left;
    }

    // Minimum subarrays whose sums each stay <= cap (greedy is optimal here).
    private static int piecesNeeded(int[] nums, long cap) {
        int pieces = 1;
        long running = 0;
        for (int v : nums) {
            if (running + v > cap) {
                pieces++;                                // current piece is full; open a new one
                running = v;
            } else {
                running += v;
            }
        }
        return pieces;
    }

    public static void main(String[] args) {
        System.out.println(splitArray(new int[]{7, 2, 5, 10, 8}, 2));     // expected: 18
        System.out.println(splitArray(new int[]{1, 2, 3, 4, 5}, 2));      // expected: 9
        System.out.println(splitArray(new int[]{1, 4, 4}, 3));           // expected: 4
        System.out.println(splitArray(new int[]{2, 3, 1, 2, 4, 3}, 5));  // expected: 4
    }
}
