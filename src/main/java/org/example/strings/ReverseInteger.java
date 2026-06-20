package org.example.strings;

public class ReverseInteger {

    /**
     * Pop the last digit (x % 10) and push it onto the reversed result
     * (result * 10 + digit). The trap is overflow: check BEFORE multiplying by 10
     * whether result has already grown past Integer.MAX_VALUE / 10 (or the boundary
     * digit case), so we never actually overflow a 32-bit int. Works uniformly for
     * negatives since Java's % keeps the sign.
     *
     * Time:  O(log|x|)  — one iteration per decimal digit.
     * Space: O(1)  — a single accumulator.
     */
    public static int reverse(int x) {
        int result = 0;
        while (x != 0) {
            int digit = x % 10;   // sign-preserving in Java (e.g. -123 % 10 == -3)
            x /= 10;

            // Would result * 10 + digit blow past the 32-bit range? Stop first.
            if (result > Integer.MAX_VALUE / 10 || (result == Integer.MAX_VALUE / 10 && digit > 7)) {
                return 0;
            }
            if (result < Integer.MIN_VALUE / 10 || (result == Integer.MIN_VALUE / 10 && digit < -8)) {
                return 0;
            }
            result = result * 10 + digit;
        }
        return result;
    }

    public static void main(String[] args) {
        System.out.println(reverse(123));        // expected: 321
        System.out.println(reverse(-123));       // expected: -321
        System.out.println(reverse(120));        // expected: 21
        System.out.println(reverse(1534236469)); // expected: 0  (reversed value overflows int)
    }
}
