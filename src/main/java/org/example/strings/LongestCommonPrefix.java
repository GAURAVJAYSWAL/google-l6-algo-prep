package org.example.strings;

public class LongestCommonPrefix {

    /**
     * Vertical scanning: compare character position 0 across all strings, then
     * position 1, and so on. The moment a string is too short or a character
     * disagrees, the prefix is exactly what we've matched so far. The first
     * string bounds the answer, so we walk its characters as the column index.
     *
     * Time:  O(S)  — S = total characters; worst case we touch every one.
     * Space: O(1)  — only indices, prefix sliced from the first string.
     */
    public static String longestCommonPrefix(String[] strs) {
        if (strs == null || strs.length == 0) return "";
        for (int col = 0; col < strs[0].length(); col++) {
            char c = strs[0].charAt(col);
            for (int row = 1; row < strs.length; row++) {
                // Out of bounds or mismatch ends the common prefix at `col`.
                if (col == strs[row].length() || strs[row].charAt(col) != c) {
                    return strs[0].substring(0, col);
                }
            }
        }
        return strs[0];   // first string is itself the common prefix
    }

    public static void main(String[] args) {
        System.out.println(longestCommonPrefix(new String[]{"flower", "flow", "flight"})); // expected: fl
        System.out.println(longestCommonPrefix(new String[]{"dog", "racecar", "car"}));     // expected: (empty)
        System.out.println(longestCommonPrefix(new String[]{"interview", "interview"}));    // expected: interview
        System.out.println(longestCommonPrefix(new String[]{"a"}));                          // expected: a
    }
}
