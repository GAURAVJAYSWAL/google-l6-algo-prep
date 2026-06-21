package org.example.binarysearch;

public class FindTheDuplicateNumber {

    /**
     * Values lie in [1, n] with exactly one duplicated. Binary search the VALUE
     * range, not indices: for a candidate mid, count how many array entries are
     * <= mid. Without duplicates that count is exactly mid; if it exceeds mid then
     * some value in [1, mid] repeats, so the duplicate sits in the low half.
     * This reads the array read-only (no mutation, no extra space).
     * Floyd's tortoise-and-hare cycle detection solves it in O(n) by treating
     * value-as-next-index, but this binary search is simpler to reason about.
     * Time:  O(n log n) — log n value steps, each a full O(n) count pass.
     * Space: O(1)       — running counters only.
     */
    public static int findDuplicate(int[] nums) {
        int lo = 1, hi = nums.length - 1;                // candidate values range over [1, n]
        while (lo < hi) {
            int mid = lo + (hi - lo) / 2;
            int count = 0;
            for (int v : nums) {
                if (v <= mid) {
                    count++;
                }
            }
            if (count > mid) {
                hi = mid;                                // too many small values: duplicate is in [lo, mid]
            } else {
                lo = mid + 1;                            // counts behave normally below; look higher
            }
        }
        return lo;
    }

    public static void main(String[] args) {
        System.out.println(findDuplicate(new int[]{1, 3, 4, 2, 2}));    // expected: 2
        System.out.println(findDuplicate(new int[]{3, 1, 3, 4, 2}));    // expected: 3
        System.out.println(findDuplicate(new int[]{1, 1}));            // expected: 1
        System.out.println(findDuplicate(new int[]{2, 2, 2, 2, 2}));    // expected: 2
    }
}
