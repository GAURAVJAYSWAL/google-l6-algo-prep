package org.example.strings;

public class LongestPalindromicSubstring {

    /**
     * Every palindrome has a center; there are 2n-1 of them (n single chars for odd
     * lengths, n-1 gaps for even lengths). Expand outward from each center while the
     * two ends match, and keep the longest span seen. This needs no extra table — O(1)
     * space — unlike the DP formulation.
     *
     * Time:  O(n^2)  — 2n-1 centers, each expansion O(n) in the worst case.
     * Space: O(1)  — only index bookkeeping.
     */
    public static String longestPalindrome(String s) {
        if (s.length() < 1) return "";
        int start = 0, end = 0;   // best palindrome bounds, inclusive

        for (int i = 0; i < s.length(); i++) {
            int odd = expand(s, i, i);       // centered on a single char
            int even = expand(s, i, i + 1);  // centered between two chars
            int len = Math.max(odd, even);
            if (len > end - start + 1) {
                // Recover bounds from center i and length; works for both parities.
                start = i - (len - 1) / 2;
                end = i + len / 2;
            }
        }
        return s.substring(start, end + 1);
    }

    /** Grows the window outward while characters match; returns the palindrome length. */
    private static int expand(String s, int left, int right) {
        while (left >= 0 && right < s.length() && s.charAt(left) == s.charAt(right)) {
            left--;
            right++;
        }
        return right - left - 1;   // last matched span (pointers overshot by one each)
    }

    public static void main(String[] args) {
        System.out.println(longestPalindrome("babad")); // expected: bab (or aba)
        System.out.println(longestPalindrome("cbbd"));  // expected: bb
        System.out.println(longestPalindrome("a"));     // expected: a
        System.out.println(longestPalindrome("ac"));    // expected: a (or c)
    }
}
