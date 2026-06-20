package org.example.arrays;

import java.util.HashSet;
import java.util.Set;

public class ContainsDuplicate {

    /**
     * Stream the array into a set; the first value that fails to insert (already
     * present) is a duplicate, so we can bail immediately rather than finishing.
     *
     * Time:  O(n)  — one pass, O(1) set add/contains.
     * Space: O(n)  — set holds up to n distinct values.
     */
    public static boolean containsDuplicate(int[] nums) {
        Set<Integer> seen = new HashSet<>();
        for (int num : nums) {
            if (!seen.add(num)) return true;   // add returns false when already present
        }
        return false;
    }

    public static void main(String[] args) {
        System.out.println(containsDuplicate(new int[]{1, 2, 3, 1}));              // expected: true
        System.out.println(containsDuplicate(new int[]{1, 2, 3, 4}));             // expected: false
        System.out.println(containsDuplicate(new int[]{1, 1, 1, 3, 3, 4, 3, 2})); // expected: true
    }
}
