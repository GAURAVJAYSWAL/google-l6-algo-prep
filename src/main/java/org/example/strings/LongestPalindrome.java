package org.example.strings;

public class LongestPalindrome {

    /**
     * A palindrome mirrors around its center, so every character it uses (except at
     * most one lone center) must appear an EVEN number of times. Thus from the raw
     * counts we can greedily claim (count/2)*2 of each letter to fill the two mirrored
     * halves; if any letter had a leftover odd one, exactly one such odd char may sit
     * in the very middle, granting a single +1. Letters are case-sensitive, so 'A' and
     * 'a' are distinct buckets — we index by raw char code.
     *
     * Time:  O(n)    — one pass to count, fixed-size sweep to total.
     * Space: O(1)    — 128-slot ASCII frequency table.
     */
    public static int longestPalindrome(String s) {
        int[] freq = new int[128];                            // covers both 'A'-'Z' and 'a'-'z'
        for (int k = 0; k < s.length(); k++) {
            freq[s.charAt(k)]++;
        }
        int length = 0;
        boolean hasOdd = false;
        for (int c : freq) {
            length += (c / 2) * 2;                            // use all complete pairs
            if (c % 2 == 1) hasOdd = true;                    // some leftover singleton exists
        }
        return hasOdd ? length + 1 : length;                  // one odd char may occupy the center
    }

    public static void main(String[] args) {
        System.out.println(longestPalindrome("abccccdd")); // expected: 7   ("dccaccd")
        System.out.println(longestPalindrome("a"));        // expected: 1
        System.out.println(longestPalindrome("bb"));       // expected: 2
        System.out.println(longestPalindrome("Aa"));       // expected: 1   (case-sensitive: no pair)
    }
}
