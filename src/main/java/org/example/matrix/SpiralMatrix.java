package org.example.matrix;

import java.util.ArrayList;
import java.util.List;

public class SpiralMatrix {

    /**
     * Maintain four shrinking boundaries — top, bottom, left, right — and peel one
     * edge per step: walk the top row left-to-right, the right column top-to-bottom,
     * then (guarding against a single remaining row/column) the bottom row
     * right-to-left and the left column bottom-to-top. After each edge its boundary
     * moves inward; the loop ends when the boundaries cross.
     * Time:  O(m*n) — each cell is emitted exactly once.
     * Space: O(1)   — beyond the required output list.
     */
    public static List<Integer> spiralOrder(int[][] matrix) {
        List<Integer> result = new ArrayList<>();
        if (matrix.length == 0) return result;
        int top = 0, bottom = matrix.length - 1;
        int left = 0, right = matrix[0].length - 1;

        while (top <= bottom && left <= right) {
            for (int c = left; c <= right; c++) result.add(matrix[top][c]);
            top++;
            for (int r = top; r <= bottom; r++) result.add(matrix[r][right]);
            right--;
            if (top <= bottom) {                         // a bottom row still remains
                for (int c = right; c >= left; c--) result.add(matrix[bottom][c]);
                bottom--;
            }
            if (left <= right) {                         // a left column still remains
                for (int r = bottom; r >= top; r--) result.add(matrix[r][left]);
                left++;
            }
        }
        return result;
    }

    public static void main(String[] args) {
        System.out.println(spiralOrder(new int[][]{{1, 2, 3}, {4, 5, 6}, {7, 8, 9}}));
        // expected: [1, 2, 3, 6, 9, 8, 7, 4, 5]

        System.out.println(spiralOrder(new int[][]{{1, 2, 3, 4}, {5, 6, 7, 8}, {9, 10, 11, 12}}));
        // expected: [1, 2, 3, 4, 8, 12, 11, 10, 9, 5, 6, 7]

        System.out.println(spiralOrder(new int[][]{{7}, {9}, {6}}));
        // expected: [7, 9, 6]
    }
}
