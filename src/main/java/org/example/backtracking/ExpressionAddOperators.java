package org.example.backtracking;

import java.util.ArrayList;
import java.util.List;

public class ExpressionAddOperators {

    /**
     * Walk the digit string and, after each prefix, choose the next operand and the
     * operator before it. The only subtlety is '*' precedence: carry the value of
     * the LAST operand so multiplication can undo its prior contribution —
     * cur = cur - prev + prev * operand, and the new "prev" becomes prev * operand.
     * '+'/'-' just fold into the running value and reset prev to the signed operand.
     * A leading-zero operand longer than one digit is illegal, so stop extending it;
     * everything is long to survive intermediate overflow.
     * Time:  O(4^n) — at each of n-1 gaps: split or not, times three operators.
     * Space: O(n) — recursion depth and the expression buffer (output excluded).
     */
    public static List<String> addOperators(String num, int target) {
        List<String> out = new ArrayList<>();
        if (num.isEmpty()) return out;
        backtrack(num, target, 0, 0, 0, new StringBuilder(), out);
        return out;
    }

    private static void backtrack(String num, int target, int idx,
                                  long cur, long prev, StringBuilder expr, List<String> out) {
        if (idx == num.length()) {
            if (cur == target) out.add(expr.toString());
            return;
        }
        int exprLen = expr.length();             // remember length to truncate on un-choose
        long operand = 0;
        for (int i = idx; i < num.length(); i++) {
            // A multi-digit operand may not start with '0'; allow the single digit "0" only.
            if (i > idx && num.charAt(idx) == '0') break;
            operand = operand * 10 + (num.charAt(i) - '0');

            if (idx == 0) {                      // first operand takes no leading operator
                expr.append(operand);
                backtrack(num, target, i + 1, operand, operand, expr, out);
                expr.setLength(exprLen);
            } else {
                expr.append('+').append(operand);
                backtrack(num, target, i + 1, cur + operand, operand, expr, out);
                expr.setLength(exprLen);

                expr.append('-').append(operand);
                backtrack(num, target, i + 1, cur - operand, -operand, expr, out);
                expr.setLength(exprLen);

                expr.append('*').append(operand); // reverse prev's effect, then apply it times operand
                backtrack(num, target, i + 1, cur - prev + prev * operand, prev * operand, expr, out);
                expr.setLength(exprLen);
            }
        }
    }

    public static void main(String[] args) {
        System.out.println(addOperators("123", 6));
        // expected: [1+2+3, 1*2*3]

        System.out.println(addOperators("232", 8));
        // expected: [2+3*2, 2*3+2]

        System.out.println(addOperators("105", 5));
        // expected: [1*0+5, 10-5]

        System.out.println(addOperators("00", 0));
        // expected: [0+0, 0-0, 0*0]

        System.out.println(addOperators("3456237490", 9191));
        // expected: []
    }
}
