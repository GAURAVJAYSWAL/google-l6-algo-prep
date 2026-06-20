package org.example.arrays;

public class MajorityElement {

    /**
     * Boyer-Moore voting: a candidate kept by net votes survives because the
     * majority element (> n/2) outnumbers every other element combined, so the
     * count can never be driven below zero by the time it is the last standing.
     * When count hits 0 we adopt the current element as the new candidate.
     * Time:  O(n) — one pass. Space: O(1) — candidate + counter.
     */
    public static int majorityElement(int[] nums) {
        int candidate = nums[0], count = 0;
        for (int x : nums) {
            if (count == 0) candidate = x;          // previous candidate fully cancelled out
            count += (x == candidate) ? 1 : -1;
        }
        return candidate;                            // guaranteed to exist by problem statement
    }

    public static void main(String[] args) {
        System.out.println(majorityElement(new int[]{3, 2, 3}));                    // expected: 3
        System.out.println(majorityElement(new int[]{2, 2, 1, 1, 1, 2, 2}));         // expected: 2
        System.out.println(majorityElement(new int[]{1}));                          // expected: 1
        System.out.println(majorityElement(new int[]{6, 5, 5}));                     // expected: 5
    }
}
