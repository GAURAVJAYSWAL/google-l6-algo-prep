package org.example.strings;

public class StringToInteger {

    /**
     * The parse itself is trivial (skip spaces, read one optional sign, fold digits as
     * result = result*10 + d); the real problem is never letting that fold overflow a
     * 32-bit int. Before each multiply-add we test whether result has already reached
     * Integer.MAX_VALUE/10: if it's strictly larger, or equal with an incoming digit
     * past the boundary (8 for the negative limit, 7 for the positive), the next step
     * WOULD overflow — so we clamp to the appropriate limit immediately. Doing the
     * check pre-emptively means we work purely in int and never rely on long/parse
     * tricks to catch the overflow after the fact.
     *
     * Time:  O(n)    — single left-to-right scan, stopping at the first non-digit.
     * Space: O(1)    — a sign flag and one int accumulator.
     */
    public static int myAtoi(String s) {
        int i = 0, n = s.length();
        while (i < n && s.charAt(i) == ' ') i++;              // 1. discard leading spaces

        int sign = 1;
        if (i < n && (s.charAt(i) == '+' || s.charAt(i) == '-')) {
            if (s.charAt(i) == '-') sign = -1;                // 2. at most one sign
            i++;
        }

        int result = 0;
        while (i < n && s.charAt(i) >= '0' && s.charAt(i) <= '9') {
            int digit = s.charAt(i) - '0';
            // 3. Detect overflow BEFORE it happens, then clamp to the signed limit.
            if (result > Integer.MAX_VALUE / 10
                    || (result == Integer.MAX_VALUE / 10 && digit > (sign == 1 ? 7 : 8))) {
                return sign == 1 ? Integer.MAX_VALUE : Integer.MIN_VALUE;
            }
            result = result * 10 + digit;
            i++;
        }
        return sign * result;                                 // safe: result is within [0, 2147483648-ish] bound by the clamp
    }

    public static void main(String[] args) {
        System.out.println(myAtoi("42"));                  // expected: 42
        System.out.println(myAtoi("   -042"));             // expected: -42  (spaces, sign, leading zeros)
        System.out.println(myAtoi("4193 with words"));     // expected: 4193 (stops at first non-digit)
        System.out.println(myAtoi("-91283472332"));        // expected: -2147483648 (clamped to Integer.MIN_VALUE)
        System.out.println(myAtoi("words and 987"));       // expected: 0    (first non-space is non-digit)
    }
}
