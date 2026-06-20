package org.example.arrays;

public class SingleNumber {

    /**
     * XOR is associative/commutative with x^x = 0 and x^0 = x, so XOR-ing every
     * element cancels each duplicated pair and leaves only the lone element.
     * Time:  O(n) — one pass. Space: O(1) — a single accumulator.
     */
    public static int singleNumber(int[] nums) {
        int acc = 0;
        for (int x : nums) acc ^= x;
        return acc;
    }

    public static void main(String[] args) {
        System.out.println(singleNumber(new int[]{2, 2, 1}));          // expected: 1
        System.out.println(singleNumber(new int[]{4, 1, 2, 1, 2}));    // expected: 4
        System.out.println(singleNumber(new int[]{1}));               // expected: 1
        System.out.println(singleNumber(new int[]{-1, -1, -3}));       // expected: -3
    }
}
