package org.example.stringmatching;

/**
 * LC 214. Shortest Palindrome.
 */
public class ShortestPalindrome {

    /**
     * Key insight: we may only prepend characters, so the answer is fixed once we know the LONGEST
     * prefix of s that is already a palindrome — everything after it must be mirrored and added in
     * front. Finding that longest palindromic prefix reduces to a string-matching trick: form
     * t = s + '#' + reverse(s) and run KMP's failure (prefix) function over it. The separator blocks
     * any overlap across the join, so lps[last] equals the length of the longest prefix of s that is
     * also a suffix of reverse(s) — which is precisely the longest palindromic prefix of s. The
     * remaining suffix, reversed, is prepended to produce the shortest palindrome.
     *
     * Time:  O(n) — building t and its prefix-function table are both linear.
     * Space: O(n) — the concatenated string t and its lps array.
     */
    public static String shortestPalindrome(String s) {
        int n = s.length();
        if (n == 0) return "";

        String rev = new StringBuilder(s).reverse().toString();
        String combined = s + "#" + rev;          // '#' prevents matches spanning the boundary
        int[] lps = prefixFunction(combined);
        int palLen = lps[combined.length() - 1];  // longest palindromic prefix length of s

        // The tail s[palLen..n) is not part of the palindromic prefix; mirror it to the front.
        String suffixToAdd = new StringBuilder(s.substring(palLen)).reverse().toString();
        return suffixToAdd + s;
    }

    /** KMP failure function: lps[i] = length of the longest proper prefix that is also a suffix of t[0..i]. */
    private static int[] prefixFunction(String t) {
        int[] lps = new int[t.length()];
        int len = 0; // length of the current matched prefix
        for (int i = 1; i < t.length(); i++) {
            while (len > 0 && t.charAt(i) != t.charAt(len)) len = lps[len - 1]; // fall back on mismatch
            if (t.charAt(i) == t.charAt(len)) len++;
            lps[i] = len;
        }
        return lps;
    }

    public static void main(String[] args) {
        System.out.println(shortestPalindrome("aacecaaa")); // expected: aaacecaaa
        System.out.println(shortestPalindrome("abcd"));     // expected: dcbabcd
        System.out.println(shortestPalindrome(""));         // expected: (empty)
        System.out.println(shortestPalindrome("aba"));      // expected: aba (already a palindrome)
    }
}
