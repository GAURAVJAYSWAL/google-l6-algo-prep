package org.example.slidingwindow;

import java.util.HashMap;
import java.util.Map;

public class LongestSubstringWithoutRepeating {

    /**
     * Slide a window [left, right]; keep a map of char -> last index seen. When the
     * current char was seen inside the window, jump left straight past its previous
     * occurrence instead of crawling one step at a time — that one jump keeps the
     * whole scan linear.
     *
     * Time:  O(n)  — right advances once per char, left only ever moves forward.
     * Space: O(min(n, alphabet))  — map holds at most one entry per distinct char.
     */
    public static int lengthOfLongestSubstring(String s) {
        Map<Character, Integer> lastSeen = new HashMap<>();
        int best = 0, left = 0;

        for (int right = 0; right < s.length(); right++) {
            char c = s.charAt(right);
            // Only jump left forward — a stale index outside the window must not pull it back.
            if (lastSeen.containsKey(c) && lastSeen.get(c) >= left) {
                left = lastSeen.get(c) + 1;
            }
            lastSeen.put(c, right);
            best = Math.max(best, right - left + 1);
        }
        return best;
    }

    public static void main(String[] args) {
        System.out.println(lengthOfLongestSubstring("abcabcbb")); // expected: 3  ("abc")
        System.out.println(lengthOfLongestSubstring("bbbbb"));    // expected: 1  ("b")
        System.out.println(lengthOfLongestSubstring("pwwkew"));   // expected: 3  ("wke")
        System.out.println(lengthOfLongestSubstring(""));         // expected: 0
    }
}
