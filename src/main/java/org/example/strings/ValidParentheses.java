package org.example.strings;

import java.util.ArrayDeque;
import java.util.Deque;

public class ValidParentheses {

    /**
     * On every opener, push the closer we expect to see next; on every closer, it must
     * match the most recent expectation (LIFO) or the string is invalid. Pushing the
     * expected closer (rather than the opener) makes the match a single equality check.
     * A non-empty stack at the end means unmatched openers remain.
     *
     * Time:  O(n)  — each char is pushed/popped at most once.
     * Space: O(n)  — stack can hold up to n/2 pending closers.
     */
    public static boolean isValid(String s) {
        Deque<Character> stack = new ArrayDeque<>();

        for (char c : s.toCharArray()) {
            switch (c) {
                case '(' -> stack.push(')');
                case '[' -> stack.push(']');
                case '{' -> stack.push('}');
                // Closer: must equal the top expectation; empty stack means nothing to close.
                default -> {
                    if (stack.isEmpty() || stack.pop() != c) return false;
                }
            }
        }
        return stack.isEmpty();   // leftover openers => unbalanced
    }

    public static void main(String[] args) {
        System.out.println(isValid("()"));       // expected: true
        System.out.println(isValid("()[]{}"));   // expected: true
        System.out.println(isValid("(]"));       // expected: false
        System.out.println(isValid("([)]"));     // expected: false
        System.out.println(isValid("{[]}"));     // expected: true
    }
}
