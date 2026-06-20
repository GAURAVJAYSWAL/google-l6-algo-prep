package org.example.bitmath;

/**
 * LC 371. Sum of Two Integers.
 */
public class SumOfTwoIntegers {

    /**
     * Key insight: addition splits into two independent bitwise parts — a XOR b is the sum
     * ignoring carries, and (a AND b) &lt;&lt; 1 is exactly the carry bits shifted into their next
     * column. Feeding the carry back as the new b and repeating until no carry remains
     * reproduces the ripple-carry adder. Java's two's-complement ints make this work for
     * negatives unchanged.
     *
     * Time:  O(1) — at most 32 iterations, one per bit width.
     * Space: O(1) — only scalar registers.
     */
    public static int getSum(int a, int b) {
        while (b != 0) {
            int carry = (a & b) << 1; // bits where both are 1 carry into the next position
            a = a ^ b;                // sum of the columns without carrying
            b = carry;                // re-add the carry on the next pass
        }
        return a;
    }

    public static void main(String[] args) {
        System.out.println(getSum(1, 2));      // expected: 3
        System.out.println(getSum(2, 3));      // expected: 5
        System.out.println(getSum(-2, 3));     // expected: 1
        System.out.println(getSum(-5, -7));    // expected: -12
    }
}
