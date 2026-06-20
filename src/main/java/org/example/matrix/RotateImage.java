package org.example.matrix;

import java.util.Arrays;

public class RotateImage {

    /**
     * A 90-degree clockwise rotation maps cell (r, c) to (c, n-1-r). That same
     * mapping decomposes into two in-place passes: transpose (swap across the main
     * diagonal, sending (r, c) -> (c, r)) followed by reversing each row (sending
     * (c, r) -> (c, n-1-r)). Both passes touch only existing cells, so no extra
     * matrix is needed.
     * Time:  O(n^2) — every cell is visited a constant number of times.
     * Space: O(1)   — all swaps happen in place.
     */
    public static void rotate(int[][] matrix) {
        int n = matrix.length;
        // Transpose: only the upper triangle (c > r) to avoid swapping back.
        for (int r = 0; r < n; r++) {
            for (int c = r + 1; c < n; c++) {
                int tmp = matrix[r][c];
                matrix[r][c] = matrix[c][r];
                matrix[c][r] = tmp;
            }
        }
        // Reverse each row in place.
        for (int r = 0; r < n; r++) {
            for (int lo = 0, hi = n - 1; lo < hi; lo++, hi--) {
                int tmp = matrix[r][lo];
                matrix[r][lo] = matrix[r][hi];
                matrix[r][hi] = tmp;
            }
        }
    }

    public static void main(String[] args) {
        int[][] a = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        rotate(a);
        System.out.println(Arrays.deepToString(a));
        // expected: [[7, 4, 1], [8, 5, 2], [9, 6, 3]]

        int[][] b = {{5, 1, 9, 11}, {2, 4, 8, 10}, {13, 3, 6, 7}, {15, 14, 12, 16}};
        rotate(b);
        System.out.println(Arrays.deepToString(b));
        // expected: [[15, 13, 2, 5], [14, 3, 4, 1], [12, 6, 8, 9], [16, 7, 10, 11]]

        int[][] c = {{1}};
        rotate(c);
        System.out.println(Arrays.deepToString(c));
        // expected: [[1]]
    }
}
