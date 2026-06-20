package org.example.arrays;

import java.util.HashSet;
import java.util.Set;

public class LongestConsecutiveSequence {

    /**
     * Dump everything into a set for O(1) membership, then only begin counting at a
     * value that is a sequence START — i.e. num-1 is absent. That guard means each
     * element is walked exactly once across all sequences, giving O(n) despite the
     * inner while loop.
     *
     * Time:  O(n)  — each value is the inner-loop subject at most once.
     * Space: O(n)  — the set.
     */
    public static int longestConsecutive(int[] nums) {
        Set<Integer> set = new HashSet<>();
        for (int num : nums) set.add(num);

        int longest = 0;
        for (int num : set) {
            if (set.contains(num - 1)) continue;   // not a start; skip to avoid recounting

            int length = 1;
            int next = num + 1;
            while (set.contains(next)) {           // walk the run upward
                length++;
                next++;
            }
            longest = Math.max(longest, length);
        }
        return longest;
    }

    public static void main(String[] args) {
        System.out.println(longestConsecutive(new int[]{100, 4, 200, 1, 3, 2}));        // expected: 4  ([1,2,3,4])
        System.out.println(longestConsecutive(new int[]{0, 3, 7, 2, 5, 8, 4, 6, 0, 1})); // expected: 9
        System.out.println(longestConsecutive(new int[]{}));                            // expected: 0
    }
}
