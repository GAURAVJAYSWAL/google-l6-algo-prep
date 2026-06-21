package org.example.matrix;

import java.util.Arrays;

/**
 * LC 85. Maximal Rectangle.
 */
public class MaximalRectangle {

    /**
     * Key insight: a rectangle of 1s is bounded below by some row, so sweep rows
     * top→bottom and keep a column histogram — heights[c] is the run of consecutive
     * 1s ending at the current row in column c (reset to 0 the moment a 0 breaks the
     * run). Every maximal all-1s rectangle has SOME row as its base, so the answer is
     * the max over all rows of "largest rectangle in this row's histogram". That sub-
     * problem is LC 84: a monotonic increasing stack lets each bar discover, when a
     * shorter bar pops it, exactly how far left and right it can extend while it stays
     * the shortest bar — and shortest-bar × width is the largest rectangle capped by it.
     *
     * Time:  O(rows·cols) — each row builds the histogram in O(cols) and each bar is
     *        pushed and popped from the stack at most once.
     * Space: O(cols) — the heights array and the index stack.
     */
    public int maximalRectangle(char[][] matrix) {
        if (matrix == null || matrix.length == 0 || matrix[0].length == 0) return 0;
        int cols = matrix[0].length;
        int[] heights = new int[cols];
        int best = 0;
        for (char[] row : matrix) {
            for (int c = 0; c < cols; c++) {
                // Extend the column's run, or break it back to 0 on a wall.
                heights[c] = row[c] == '1' ? heights[c] + 1 : 0;
            }
            best = Math.max(best, largestInHistogram(heights));
        }
        return best;
    }

    private int largestInHistogram(int[] heights) {
        int n = heights.length;
        int[] stack = new int[n + 1]; // holds indices of bars in strictly increasing height
        int top = -1;                 // stack pointer
        int best = 0;
        // Sentinel pass at i == n (height 0) flushes every remaining bar.
        for (int i = 0; i <= n; i++) {
            int h = i == n ? 0 : heights[i];
            // Current bar h ends the run of any taller bar on the stack: pop and measure it.
            while (top >= 0 && heights[stack[top]] >= h) {
                int height = heights[stack[top--]];
                // Left edge is the bar now below top (exclusive); right edge is i (exclusive).
                int width = top < 0 ? i : i - stack[top] - 1;
                best = Math.max(best, height * width);
            }
            stack[++top] = i;
        }
        return best;
    }

    public static void main(String[] args) {
        MaximalRectangle s = new MaximalRectangle();

        char[][] m1 = {
            {'1', '0', '1', '0', '0'},
            {'1', '0', '1', '1', '1'},
            {'1', '1', '1', '1', '1'},
            {'1', '0', '0', '1', '0'}
        };
        // Rows 1–2, cols 2–4 form a 2×3 block of 1s -> area 6.
        System.out.println(s.maximalRectangle(m1)); // expected: 6

        char[][] m2 = {{'0'}};
        System.out.println(s.maximalRectangle(m2)); // expected: 0

        char[][] m3 = {{'1', '1'}, {'1', '1'}};
        System.out.println(s.maximalRectangle(m3)); // expected: 4

        char[][] m4 = {};
        System.out.println(s.maximalRectangle(m4)); // expected: 0

        System.out.println(Arrays.deepToString(m3)); // expected: [[1, 1], [1, 1]] (input unchanged)
    }
}
