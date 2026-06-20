package org.example.bitmath;

/**
 * LC 1071. Greatest Common Divisor of Strings.
 */
public class GreatestCommonDivisorOfStrings {

    /**
     * Key insight: some base string divides both iff str1+str2 == str2+str1 — concatenation
     * commutes only when both are powers of one common period. Given that, the divisor's length
     * is exactly gcd(len1, len2) (the largest tile length that evenly partitions both), so the
     * answer is the prefix of that length. Euclid's algorithm computes the gcd.
     *
     * Time:  O(m + n) — the two concatenation-equality checks dominate; gcd is O(log) and the
     *                   prefix copy is O(gcd).
     * Space: O(m + n) — the concatenations built for the commutativity test.
     */
    public static String gcdOfStrings(String str1, String str2) {
        if (!(str1 + str2).equals(str2 + str1)) return "";   // no common period exists
        int g = gcd(str1.length(), str2.length());
        return str1.substring(0, g);                         // the tile that repeats to form both
    }

    // Euclid's algorithm: gcd(a, b) = gcd(b, a mod b).
    private static int gcd(int a, int b) {
        while (b != 0) {
            int t = a % b;
            a = b;
            b = t;
        }
        return a;
    }

    public static void main(String[] args) {
        System.out.println(gcdOfStrings("ABCABC", "ABC"));      // expected: ABC
        System.out.println(gcdOfStrings("ABABAB", "ABAB"));     // expected: AB
        System.out.println(gcdOfStrings("LEET", "CODE"));       // expected: (empty)
        System.out.println(gcdOfStrings("ABABABAB", "ABAB"));   // expected: ABAB
    }
}
