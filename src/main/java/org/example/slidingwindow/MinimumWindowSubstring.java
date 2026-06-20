package org.example.slidingwindow;

import java.util.HashMap;
import java.util.Map;

public class MinimumWindowSubstring {

    /**
     * Expand right to acquire characters; track how many distinct required chars are
     * fully satisfied via a single `formed` counter. The moment all are satisfied,
     * shrink from the left as far as possible, recording the smallest valid window.
     * The `formed == required` check is O(1), so validity is never re-scanned.
     *
     * Time:  O(|s| + |t|)  — each pointer traverses s once; building need is O(|t|).
     * Space: O(|s| + |t|)  — two frequency maps bounded by the distinct chars involved.
     */
    public static String minWindow(String s, String t) {
        if (s.length() < t.length() || t.isEmpty()) return "";

        Map<Character, Integer> need = new HashMap<>();
        for (char c : t.toCharArray()) need.merge(c, 1, Integer::sum);

        int required = need.size();   // distinct chars that must each reach their target count
        int formed = 0;               // how many of those are currently satisfied
        Map<Character, Integer> window = new HashMap<>();

        int bestLen = Integer.MAX_VALUE, bestStart = 0;
        int left = 0;

        for (int right = 0; right < s.length(); right++) {
            char c = s.charAt(right);
            window.merge(c, 1, Integer::sum);
            // A char becomes "formed" exactly when its count hits the needed count (not after).
            if (need.containsKey(c) && window.get(c).intValue() == need.get(c).intValue()) {
                formed++;
            }

            // Window is valid: greedily drop chars from the left while it stays valid.
            while (formed == required) {
                if (right - left + 1 < bestLen) {
                    bestLen = right - left + 1;
                    bestStart = left;
                }
                char leftChar = s.charAt(left);
                window.merge(leftChar, -1, Integer::sum);
                if (need.containsKey(leftChar) && window.get(leftChar) < need.get(leftChar)) {
                    formed--;   // dropping this char breaks validity — stop shrinking
                }
                left++;
            }
        }
        return bestLen == Integer.MAX_VALUE ? "" : s.substring(bestStart, bestStart + bestLen);
    }

    public static void main(String[] args) {
        System.out.println(minWindow("ADOBECODEBANC", "ABC")); // expected: BANC
        System.out.println(minWindow("a", "a"));               // expected: a
        System.out.println(minWindow("a", "aa"));              // expected: (empty)
        System.out.println(minWindow("aa", "aa"));             // expected: aa
    }
}
