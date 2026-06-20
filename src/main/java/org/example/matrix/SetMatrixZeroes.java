package org.example.matrix;

import java.util.Arrays;

public class SetMatrixZeroes {

    /**
     * Instead of an O(m+n) side table to remember which rows/columns to zero, reuse
     * the matrix's own first row and first column as those markers: a zero at (r, c)
     * sets matrix[r][0] and matrix[0][c]. The catch is the cell (0,0) is shared by
     * both markers, so the first row's and first column's own original state is
     * captured in two boolean flags first. We then zero the interior from the
     * markers, and finally apply the two flags — handling the first row/column last
     * so their marker values aren't clobbered before they're read.
     * Time:  O(m*n) — a constant number of passes over the grid.
     * Space: O(1)   — markers live inside the matrix; only two boolean flags extra.
     */
    public static void setZeroes(int[][] matrix) {
        int m = matrix.length, n = matrix[0].length;
        boolean firstRowZero = false, firstColZero = false;

        // Does the first row / first column originally contain a zero?
        for (int c = 0; c < n; c++) if (matrix[0][c] == 0) firstRowZero = true;
        for (int r = 0; r < m; r++) if (matrix[r][0] == 0) firstColZero = true;

        // Stamp markers in row 0 / col 0 for every interior zero.
        for (int r = 1; r < m; r++) {
            for (int c = 1; c < n; c++) {
                if (matrix[r][c] == 0) {
                    matrix[r][0] = 0;
                    matrix[0][c] = 0;
                }
            }
        }

        // Zero the interior wherever its row-marker or col-marker fired.
        for (int r = 1; r < m; r++) {
            for (int c = 1; c < n; c++) {
                if (matrix[r][0] == 0 || matrix[0][c] == 0) matrix[r][c] = 0;
            }
        }

        // Apply the saved flags to the first row/column last.
        if (firstRowZero) Arrays.fill(matrix[0], 0);
        if (firstColZero) for (int r = 0; r < m; r++) matrix[r][0] = 0;
    }

    public static void main(String[] args) {
        int[][] a = {{1, 1, 1}, {1, 0, 1}, {1, 1, 1}};
        setZeroes(a);
        System.out.println(Arrays.deepToString(a));
        // expected: [[1, 0, 1], [0, 0, 0], [1, 0, 1]]

        int[][] b = {{0, 1, 2, 0}, {3, 4, 5, 2}, {1, 3, 1, 5}};
        setZeroes(b);
        System.out.println(Arrays.deepToString(b));
        // expected: [[0, 0, 0, 0], [0, 4, 5, 0], [0, 3, 1, 0]]

        int[][] c = {{1, 2, 3}, {4, 5, 6}};
        setZeroes(c);
        System.out.println(Arrays.deepToString(c));
        // expected: [[1, 2, 3], [4, 5, 6]] (no zeros, unchanged)
    }
}
