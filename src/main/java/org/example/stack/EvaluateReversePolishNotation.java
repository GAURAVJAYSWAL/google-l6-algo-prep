package org.example.stack;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * LeetCode 150 — Evaluate Reverse Polish Notation (Medium).
 *
 * Evaluate an arithmetic expression given in postfix (Reverse Polish) notation.
 * Valid operators are +, -, *, /; division truncates toward zero.
 */
public class EvaluateReversePolishNotation {

    /**
     * Postfix removes the need for precedence rules and parentheses: by the time an
     * operator is read, its two operands are guaranteed to be the most recently produced
     * values, sitting on top of the stack. The only subtlety is order — the SECOND value
     * popped is the left-hand operand, which matters for non-commutative '-' and '/'.
     * Time:  O(n) — each token is pushed and popped a constant number of times.
     * Space: O(n) — the stack holds up to O(n) operands for an all-numbers prefix.
     */
    public int evalRPN(String[] tokens) {
        Deque<Integer> stack = new ArrayDeque<>();
        for (String token : tokens) {
            switch (token) {
                case "+", "-", "*", "/" -> {
                    int right = stack.pop();        // most recent operand
                    int left = stack.pop();         // the one beneath it is the left operand
                    stack.push(apply(left, right, token));
                }
                default -> stack.push(Integer.parseInt(token));
            }
        }
        return stack.pop();
    }

    private int apply(int left, int right, String op) {
        return switch (op) {
            case "+" -> left + right;
            case "-" -> left - right;
            case "*" -> left * right;
            case "/" -> left / right;            // integer division truncates toward zero
            default -> throw new IllegalArgumentException("Unknown operator: " + op);
        };
    }

    public static void main(String[] args) {
        EvaluateReversePolishNotation s = new EvaluateReversePolishNotation();

        // ((2 + 1) * 3) = 9
        System.out.println(s.evalRPN(new String[]{"2", "1", "+", "3", "*"}));    // expected: 9

        // (4 + (13 / 5)) = 4 + 2 = 6
        System.out.println(s.evalRPN(new String[]{"4", "13", "5", "/", "+"}));   // expected: 6

        // Order-sensitive: 9 - 6 then ... full trace below -> 22
        // 10 6 9 3 + -11 * / * 17 + 5 +
        // 9 + 3 = 12; 12 * -11 = -132; 6 / -132 = 0 (trunc); 0 * 10 = 0; 0 + 17 = 17; 17 + 5 = 22
        System.out.println(s.evalRPN(new String[]{
                "10", "6", "9", "3", "+", "-11", "*", "/", "*", "17", "+", "5", "+"})); // expected: 22

        // Truncation toward zero for negatives: -7 / 2 = -3 (not -4)
        System.out.println(s.evalRPN(new String[]{"-7", "2", "/"}));             // expected: -3
    }
}
