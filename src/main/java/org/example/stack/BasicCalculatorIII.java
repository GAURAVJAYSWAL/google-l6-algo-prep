package org.example.stack;

import java.util.ArrayDeque;
import java.util.Deque;

public class BasicCalculatorIII {

    /**
     * Precedence is handled by deferring addition: push numbers onto a stack, but apply
     * multiplication and division immediately against the stack top so the higher-precedence
     * operators are already resolved by the time we sum the stack at the end. The pending
     * operator (the one BEFORE the current number) decides what to do: plus and minus push
     * (negating for minus), while times and divide rewrite the stack top in place.
     * Parentheses are evaluated by recursion: a '(' spawns a sub-evaluation from the matching
     * ')', and its scalar result is treated as an ordinary operand. A shared index lets the
     * recursion resume where the sub-expression stopped.
     *
     * Time:  O(n)  — each character is consumed once across all recursion levels.
     * Space: O(n)  — operand stack plus recursion depth bounded by parenthesis nesting.
     */
    private int pos;  // shared cursor into the expression, advanced across recursive calls

    public int calculate(String s) {
        pos = 0;
        return eval(s);
    }

    // Evaluates from the current pos up to (and consuming) the matching ')' or end of string.
    private int eval(String s) {
        Deque<Integer> stack = new ArrayDeque<>();
        int num = 0;
        char op = '+';   // operator pending in front of the number currently being built

        while (pos < s.length()) {
            char c = s.charAt(pos++);
            if (Character.isDigit(c)) {
                num = num * 10 + (c - '0');
            } else if (c == '(') {
                num = eval(s);   // recurse; sub-expression's value becomes this operand
            }

            // Apply the pending op when we hit an operator, a ')', or the very end.
            boolean atEnd = (pos == s.length());
            if ((!Character.isDigit(c) && c != ' ' && c != '(') || atEnd) {
                switch (op) {
                    case '+' -> stack.push(num);
                    case '-' -> stack.push(-num);
                    case '*' -> stack.push(stack.pop() * num);   // bind tighter than +/-
                    case '/' -> stack.push(stack.pop() / num);
                }
                op = c;          // remember this operator for the next number
                num = 0;
                if (c == ')') break;   // finished this parenthesised scope
            }
        }

        int sum = 0;
        for (int v : stack) sum += v;   // all */ already resolved, so just total the additive terms
        return sum;
    }

    public static void main(String[] args) {
        BasicCalculatorIII calc = new BasicCalculatorIII();
        System.out.println(calc.calculate("1+1"));                         // expected: 2
        System.out.println(calc.calculate("6-4/2"));                       // expected: 4
        System.out.println(calc.calculate("2*(5+5*2)/3+(6/2+8)"));         // expected: 21
        System.out.println(calc.calculate("(2+6*3+5-(3*14/7+2)*5)+3"));    // expected: -12
    }
}
