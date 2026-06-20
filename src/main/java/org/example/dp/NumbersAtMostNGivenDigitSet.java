package org.example.dp;

/**
 * LC 902. Numbers At Most N Given Digit Set.
 */
public class NumbersAtMostNGivenDigitSet {

    /**
     * Key insight: count positive integers, built only from the allowed digits, that are &lt;= N.
     * Split by digit-length. (1) Any number with FEWER digits than N is automatically smaller, and
     * with d allowed digits there are d^len such numbers of length len (leading digit is free
     * because all allowed digits are 1..9); sum d^len for len = 1 .. (digits(N) - 1). (2) For
     * numbers with EXACTLY digits(N) digits we walk N position by position keeping the prefix tight
     * (equal to N so far): at position i, every allowed digit strictly LESS than N's digit lets the
     * remaining (L-i-1) slots be filled freely, contributing (count of smaller allowed digits) *
     * d^(L-i-1); if N's digit is itself allowed we can stay tight and move on, otherwise no
     * exact-length number can match N's prefix and we stop. (3) Finally +1 if N itself is fully
     * constructible from the digit set (the tight walk reached the end), since N &lt;= N counts.
     *
     * Time:  O(L * d) — L = number of digits in N, each scanning the d allowed digits once.
     * Space: O(L) — the powers-of-d table (the digit string is O(L) too).
     */
    public static int atMostNGivenDigitSet(String[] digits, int n) {
        char[] num = Integer.toString(n).toCharArray();
        int len = num.length;
        int d = digits.length;

        // powers[k] = d^k = how many free numbers fill k unconstrained slots.
        int[] powers = new int[len + 1];
        powers[0] = 1;
        for (int k = 1; k <= len; k++) powers[k] = powers[k - 1] * d;

        int count = 0;

        // (1) All shorter lengths 1 .. len-1 are unconditionally smaller than N.
        for (int shorterLen = 1; shorterLen < len; shorterLen++) {
            count += powers[shorterLen];
        }

        // (2) Same-length numbers, kept tight against N from the most significant digit.
        for (int i = 0; i < len; i++) {
            boolean prefixCanStayTight = false;
            for (String ds : digits) {
                char digit = ds.charAt(0);
                if (digit < num[i]) {
                    // This position goes strictly below N, so the rest is free.
                    count += powers[len - i - 1];
                } else if (digit == num[i]) {
                    // We can match N here and remain tight for the next position.
                    prefixCanStayTight = true;
                }
            }
            if (!prefixCanStayTight) {
                return count;          // N's digit isn't in the set: no exact-length match can continue
            }
        }

        // (3) The tight walk reached the end => N is itself constructible and counts.
        return count + 1;
    }

    public static void main(String[] args) {
        System.out.println(atMostNGivenDigitSet(new String[]{"1", "3", "5", "7"}, 100)); // expected: 20
        System.out.println(atMostNGivenDigitSet(new String[]{"1", "4", "9"}, 1000000000)); // expected: 29523
        System.out.println(atMostNGivenDigitSet(new String[]{"7"}, 8));                  // expected: 1
        System.out.println(atMostNGivenDigitSet(new String[]{"3", "4", "8"}, 4));        // expected: 2  (3 and 4)
    }
}
