package org.example.backtracking;

import java.util.ArrayList;
import java.util.List;

public class LetterCombinations {

    private static final String[] MAP = {
            "",     // 0
            "",     // 1
            "abc",  // 2
            "def",  // 3
            "ghi",  // 4
            "jkl",  // 5
            "mno",  // 6
            "pqrs", // 7
            "tuv",  // 8
            "wxyz"  // 9
    };

    /**
     * Each digit independently contributes one letter to the combination, so a
     * combination is one path through the digit->letters tree. Backtrack: at depth
     * d pick every letter for digits[d], recurse to d+1, then un-pick. The leaf at
     * depth == len is one complete combination.
     * Time:  O(4^n * n) — up to 4 choices per digit, n work to copy each leaf.
     * Space: O(n) — recursion depth and the path buffer (output excluded).
     */
    public static List<String> letterCombinations(String digits) {
        List<String> out = new ArrayList<>();
        if (digits.isEmpty()) return out;        // empty input yields no combinations
        backtrack(digits, 0, new StringBuilder(), out);
        return out;
    }

    private static void backtrack(String digits, int idx, StringBuilder path, List<String> out) {
        if (idx == digits.length()) {            // consumed every digit -> a full combination
            out.add(path.toString());
            return;
        }
        String letters = MAP[digits.charAt(idx) - '0'];
        for (int i = 0; i < letters.length(); i++) {
            path.append(letters.charAt(i));      // choose
            backtrack(digits, idx + 1, path, out);
            path.deleteCharAt(path.length() - 1); // un-choose
        }
    }

    public static void main(String[] args) {
        System.out.println(letterCombinations("23"));
        // expected: [ad, ae, af, bd, be, bf, cd, ce, cf]

        System.out.println(letterCombinations(""));
        // expected: []

        System.out.println(letterCombinations("2"));
        // expected: [a, b, c]

        System.out.println(letterCombinations("7").size());
        // expected: 4
    }
}
