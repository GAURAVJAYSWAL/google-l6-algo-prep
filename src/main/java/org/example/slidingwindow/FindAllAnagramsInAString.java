package org.example.slidingwindow;

import java.util.ArrayList;
import java.util.List;

/**
 * LC 438 — Find All Anagrams in a String (Medium).
 * Return all start indices where a window of s is a permutation of p.
 */
public class FindAllAnagramsInAString {

    /**
     * Slide a fixed window of length p.length(): each step is exactly one char entering and one leaving,
     * so instead of re-comparing 26 buckets we maintain `matches`, the number of letters whose window count
     * exactly equals the need — when a single bucket's update crosses or lands on equality we adjust matches
     * by ±1, and matches == 26 means the window is an anagram.
     * Time:  O(n) — n window steps, each O(1) work.
     * Space: O(1) — two fixed 26-element frequency arrays.
     */
    List<Integer> findAnagrams(String s, String p) {
        List<Integer> result = new ArrayList<>();
        int n = s.length(), m = p.length();
        if (m > n) return result;

        int[] need = new int[26];
        int[] window = new int[26];
        for (int i = 0; i < m; i++) need[p.charAt(i) - 'a']++;

        // A letter trivially "matches" while both counts are 0; pre-seed matches for letters absent from p.
        int matches = 0;
        for (int c = 0; c < 26; c++) if (need[c] == 0) matches++;

        for (int i = 0; i < n; i++) {
            int in = s.charAt(i) - 'a';
            // Add s[i]: equality is lost if we were equal, regained if this brings us to need.
            if (window[in] == need[in]) matches--;
            window[in]++;
            if (window[in] == need[in]) matches++;

            int left = i - m;            // char that just fell out of the window
            if (left >= 0) {
                int out = s.charAt(left) - 'a';
                if (window[out] == need[out]) matches--;
                window[out]--;
                if (window[out] == need[out]) matches++;
            }

            if (matches == 26) result.add(i - m + 1);
        }
        return result;
    }

    public static void main(String[] args) {
        FindAllAnagramsInAString s = new FindAllAnagramsInAString();

        // "cbaebabacd", p="abc": anagrams "cba"@0 and "bac"@6.
        System.out.println(s.findAnagrams("cbaebabacd", "abc")); // expected: [0, 6]

        // "abab", p="ab": "ab"@0, "ba"@1, "ab"@2.
        System.out.println(s.findAnagrams("abab", "ab")); // expected: [0, 1, 2]

        // p longer than s → no windows.
        System.out.println(s.findAnagrams("a", "aa")); // expected: []
    }
}
