package org.example.stack;

import java.util.ArrayDeque;
import java.util.Deque;

public class BasicCalculator {

    /**
     * Single left-to-right scan with a running result and the current sign (+1/-1).
     * Parentheses are handled without recursion: on '(' we push the result and the
     * sign that applies to the whole group, then start fresh; on ')' we fold the
     * group's value back in by multiplying by the pushed sign and adding the pushed
     * result. Numbers may be multi-digit, so we accumulate while scanning digits.
     *
     * Time:  O(n)  — each character processed once.
     * Space: O(d)  — stack depth equals the parenthesis nesting depth.
     */
    public static int calculate(String s) {
        Deque<Integer> stack = new ArrayDeque<>();
        int result = 0;
        int sign = 1;      // sign that applies to the next number
        int number = 0;    // current multi-digit number being read

        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (Character.isDigit(c)) {
                number = number * 10 + (c - '0');
            } else if (c == '+') {
                result += sign * number;
                number = 0;
                sign = 1;
            } else if (c == '-') {
                result += sign * number;
                number = 0;
                sign = -1;
            } else if (c == '(') {
                // Save context, then evaluate the inner expression from scratch.
                stack.push(result);
                stack.push(sign);
                result = 0;
                sign = 1;
            } else if (c == ')') {
                result += sign * number;   // finish the last number inside the group
                number = 0;
                result *= stack.pop();     // apply the sign that preceded '('
                result += stack.pop();     // add the result accumulated before '('
            }
            // spaces are ignored
        }
        return result + sign * number;     // fold in a trailing number
    }

    public static void main(String[] args) {
        System.out.println(calculate("1 + 1"));                 // expected: 2
        System.out.println(calculate(" 2-1 + 2 "));             // expected: 3
        System.out.println(calculate("(1+(4+5+2)-3)+(6+8)"));   // expected: 23
        System.out.println(calculate("-(3-(2+1))"));            // expected: 0
    }
}
