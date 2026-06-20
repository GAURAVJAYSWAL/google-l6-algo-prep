package org.example.prefixsum2d;

/**
 * LC 304. Range Sum Query 2D - Immutable.
 */
public class RangeSumQuery2DImmutable {

    /**
     * Key insight: precompute pre[i][j] = sum of the submatrix from (0,0) to (i-1,j-1)
     * inclusive (one-indexed prefix grid with a zero border). Any axis-aligned region sum
     * is then four prefix lookups via inclusion-exclusion: take the big rectangle, subtract
     * the strip above and the strip to the left, then add back the top-left corner counted
     * twice. The padding row/column removes all boundary special-casing.
     *
     * Time:  constructor O(m*n) to build the grid; sumRegion O(1) — exactly four lookups.
     * Space: O(m*n) — the prefix grid.
     */
    static class NumMatrix {
        private final int[][] pre; // pre[i][j] = sum of matrix[0..i-1][0..j-1]

        public NumMatrix(int[][] matrix) {
            int m = matrix.length, n = matrix[0].length;
            pre = new int[m + 1][n + 1]; // extra zero row/col so i-1, j-1 never go negative
            for (int i = 1; i <= m; i++) {
                for (int j = 1; j <= n; j++) {
                    // current cell + strip above + strip left - doubly counted upper-left block
                    pre[i][j] = matrix[i - 1][j - 1]
                            + pre[i - 1][j]
                            + pre[i][j - 1]
                            - pre[i - 1][j - 1];
                }
            }
        }

        public int sumRegion(int row1, int col1, int row2, int col2) {
            // Shift by +1 into prefix space, then inclusion-exclusion over the four corners.
            return pre[row2 + 1][col2 + 1]
                    - pre[row1][col2 + 1]      // remove rows above the region
                    - pre[row2 + 1][col1]      // remove cols left of the region
                    + pre[row1][col1];         // add back the corner removed twice
        }
    }

    public static void main(String[] args) {
        int[][] matrix = {
                {3, 0, 1, 4, 2},
                {5, 6, 3, 2, 1},
                {1, 2, 0, 1, 5},
                {4, 1, 0, 1, 7},
                {1, 0, 3, 0, 5}
        };
        NumMatrix nm = new NumMatrix(matrix);
        System.out.println(nm.sumRegion(2, 1, 4, 3)); // expected: 8
        System.out.println(nm.sumRegion(1, 1, 2, 2)); // expected: 11
        System.out.println(nm.sumRegion(1, 2, 2, 4)); // expected: 12
        System.out.println(nm.sumRegion(0, 0, 0, 0)); // expected: 3 (single cell)

        NumMatrix one = new NumMatrix(new int[][]{{-1}});
        System.out.println(one.sumRegion(0, 0, 0, 0)); // expected: -1
    }
}
