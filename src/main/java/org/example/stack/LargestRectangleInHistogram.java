package org.example.stack;

import java.util.ArrayDeque;
import java.util.Deque;

public class LargestRectangleInHistogram {

    /**
     * For each bar, the largest rectangle using it as the limiting height stretches
     * from the first shorter bar on its left to the first shorter bar on its right.
     * A stack of indices with strictly increasing heights finds both boundaries in
     * one pass: when the incoming bar is shorter, every taller bar we pop is being
     * "closed off" on its right (by i) and on its left (by the new stack top), so
     * its maximal width is exactly known at pop time. A trailing zero-height sentinel
     * flushes whatever remains on the stack.
     *
     * Time:  O(n)  — every index is pushed and popped at most once.
     * Space: O(n)  — the monotonic stack.
     */
    public static int largestRectangleArea(int[] heights) {
        int n = heights.length;
        Deque<Integer> stack = new ArrayDeque<>(); // indices of bars in increasing height
        int best = 0;

        for (int i = 0; i <= n; i++) {
            int h = (i == n) ? 0 : heights[i];     // virtual 0 bar past the end forces a full flush
            while (!stack.isEmpty() && heights[stack.peek()] >= h) {
                int height = heights[stack.pop()];
                // Left boundary is the new top (or -1 if empty); right boundary is i.
                int leftExclusive = stack.isEmpty() ? -1 : stack.peek();
                int width = i - leftExclusive - 1;
                best = Math.max(best, height * width);
            }
            stack.push(i);
        }
        return best;
    }

    public static void main(String[] args) {
        System.out.println(largestRectangleArea(new int[]{2, 1, 5, 6, 2, 3})); // expected: 10 (5 and 6 over width 2)
        System.out.println(largestRectangleArea(new int[]{2, 4}));            // expected: 4
        System.out.println(largestRectangleArea(new int[]{2, 1, 2}));         // expected: 3
        System.out.println(largestRectangleArea(new int[]{6, 7, 5, 2, 4, 5, 9, 3})); // expected: 16
    }
}
