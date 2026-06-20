package org.example.arrays;

public class MissingNumber {

    /**
     * XOR every index 0..n together with every value; each present number cancels
     * its matching index (x^x = 0), so what remains is the one index that has no
     * value — the missing number. Seeding the accumulator with n folds in the
     * top index that the value loop never reaches.
     * Time:  O(n) — one pass. Space: O(1).
     * (Gauss n(n+1)/2 minus the sum also works but can overflow for large n.)
     */
    public static int missingNumber(int[] nums) {
        int acc = nums.length;                       // covers the index == n that has no array slot
        for (int i = 0; i < nums.length; i++) {
            acc ^= i ^ nums[i];                      // cancel index i against value nums[i]
        }
        return acc;
    }

    public static void main(String[] args) {
        System.out.println(missingNumber(new int[]{3, 0, 1}));            // expected: 2
        System.out.println(missingNumber(new int[]{0, 1}));             // expected: 2
        System.out.println(missingNumber(new int[]{9, 6, 4, 2, 3, 5, 7, 0, 1})); // expected: 8
        System.out.println(missingNumber(new int[]{0}));                // expected: 1
    }
}
