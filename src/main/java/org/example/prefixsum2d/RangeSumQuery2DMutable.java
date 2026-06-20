package org.example.prefixsum2d;

/**
 * LC 308. Range Sum Query 2D - Mutable.
 */
public class RangeSumQuery2DMutable {

    /**
     * Key insight: a 2D Binary Indexed (Fenwick) tree generalizes the 1D BIT — node (i,j)
     * owns a rectangle whose height is the low bit of i and width the low bit of j. A point
     * update walks UP both dimensions (i += i&amp;-i, j += j&amp;-j), and a prefix query down to
     * (0,0) walks DOWN both (i -= i&amp;-i). A region sum is then inclusion-exclusion over four
     * such prefix rectangles, mirroring the immutable case but with O(log m * log n) lookups.
     *
     * Time:  update O(log m * log n); sumRegion O(log m * log n); construction O(m*n*log m*log n).
     * Space: O(m*n) — the BIT plus a copy of the values for delta computation.
     */
    static class NumMatrix {
        private final int m, n;
        private final int[][] tree; // 1-indexed 2D Fenwick tree
        private final int[][] vals; // current cell values, to compute deltas on update

        public NumMatrix(int[][] matrix) {
            m = matrix.length;
            n = matrix[0].length;
            tree = new int[m + 1][n + 1];
            vals = new int[m][n];
            for (int i = 0; i < m; i++) {
                for (int j = 0; j < n; j++) update(i, j, matrix[i][j]);
            }
        }

        public void update(int row, int col, int val) {
            int delta = val - vals[row][col];
            vals[row][col] = val;
            // Propagate the delta to every node that covers this cell.
            for (int i = row + 1; i <= m; i += i & -i) {
                for (int j = col + 1; j <= n; j += j & -j) {
                    tree[i][j] += delta;
                }
            }
        }

        public int sumRegion(int row1, int col1, int row2, int col2) {
            // Inclusion-exclusion over four corner prefixes (boundaries shifted into 1-indexed space).
            return prefix(row2 + 1, col2 + 1)
                    - prefix(row1, col2 + 1)
                    - prefix(row2 + 1, col1)
                    + prefix(row1, col1);
        }

        // Sum of the submatrix from (0,0) up to the 1-indexed boundary (r, c) exclusive of r, c.
        private int prefix(int r, int c) {
            int sum = 0;
            for (int i = r; i > 0; i -= i & -i) {
                for (int j = c; j > 0; j -= j & -j) {
                    sum += tree[i][j];
                }
            }
            return sum;
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
        nm.update(3, 2, 2);                            // cell (3,2): 0 -> 2
        System.out.println(nm.sumRegion(2, 1, 4, 3)); // expected: 10
        System.out.println(nm.sumRegion(1, 1, 2, 2)); // expected: 11

        NumMatrix small = new NumMatrix(new int[][]{{1, 2}, {3, 4}});
        System.out.println(small.sumRegion(0, 0, 1, 1)); // expected: 10
        small.update(0, 0, 10);                           // [[10,2],[3,4]]
        System.out.println(small.sumRegion(0, 0, 0, 0)); // expected: 10
    }
}
