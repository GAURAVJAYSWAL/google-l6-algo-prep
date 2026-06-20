package org.example.design;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * LC 155. Min Stack.
 */
public class MinStack {

    /**
     * Key insight: the minimum can only change at push/pop boundaries, so alongside
     * each value we remember the minimum of the stack AT THE MOMENT it was pushed. The
     * top of this auxiliary stack is therefore always the current minimum, and popping
     * the main stack pops the matching min in lockstep — no rescanning needed.
     *
     * Time:  O(1) for push, pop, top, getMin — all are single stack operations.
     * Space: O(n) — the auxiliary min stack mirrors the value stack's depth.
     */
    static class Stack {
        private final Deque<Integer> values = new ArrayDeque<>();
        private final Deque<Integer> mins = new ArrayDeque<>();   // running minimum per level

        public Stack() {
        }

        public void push(int val) {
            values.push(val);
            // Carry forward the smaller of the new value and the previous minimum.
            mins.push(mins.isEmpty() ? val : Math.min(val, mins.peek()));
        }

        public void pop() {
            values.pop();
            mins.pop();
        }

        public int top() {
            return values.peek();
        }

        public int getMin() {
            return mins.peek();
        }
    }

    public static void main(String[] args) {
        Stack s = new Stack();
        s.push(-2);
        s.push(0);
        s.push(-3);
        System.out.println(s.getMin());   // expected: -3
        s.pop();
        System.out.println(s.top());      // expected: 0
        System.out.println(s.getMin());   // expected: -2

        Stack s2 = new Stack();
        s2.push(5);
        s2.push(5);
        s2.push(3);
        System.out.println(s2.getMin());  // expected: 3
        s2.pop();
        System.out.println(s2.getMin());  // expected: 5 (duplicate 5 still present)
        s2.pop();
        System.out.println(s2.getMin());  // expected: 5
    }
}
