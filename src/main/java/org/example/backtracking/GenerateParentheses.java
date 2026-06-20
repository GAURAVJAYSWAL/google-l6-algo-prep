package org.example.backtracking;

import java.util.ArrayList;
import java.util.List;

public class GenerateParentheses {

    /**
     * Build the string left to right, tracking how many '(' and ')' have been
     * placed. The two rules that guarantee only valid strings are emitted: add '('
     * only while open < n (budget remains), and add ')' only while close < open (a
     * close must have an unmatched open to pair with). A leaf of length 2n is valid
     * by construction, so no separate validation is needed.
     * Time:  O(4^n / sqrt(n)) — the count of leaves is the n-th Catalan number.
     * Space: O(n) — recursion depth and the path buffer (output excluded).
     */
    public static List<String> generateParenthesis(int n) {
        List<String> out = new ArrayList<>();
        backtrack(n, 0, 0, new StringBuilder(), out);
        return out;
    }

    private static void backtrack(int n, int open, int close, StringBuilder path, List<String> out) {
        if (path.length() == 2 * n) {            // used all 2n slots -> balanced string
            out.add(path.toString());
            return;
        }
        if (open < n) {                          // can still open a new pair
            path.append('(');
            backtrack(n, open + 1, close, path, out);
            path.deleteCharAt(path.length() - 1);
        }
        if (close < open) {                      // only close an outstanding open
            path.append(')');
            backtrack(n, open, close + 1, path, out);
            path.deleteCharAt(path.length() - 1);
        }
    }

    public static void main(String[] args) {
        System.out.println(generateParenthesis(3));
        // expected: [((())), (()()), (())(), ()(()), ()()()]

        System.out.println(generateParenthesis(1));
        // expected: [()]

        System.out.println(generateParenthesis(2));
        // expected: [(()), ()()]

        System.out.println(generateParenthesis(4).size());
        // expected: 14
    }
}
