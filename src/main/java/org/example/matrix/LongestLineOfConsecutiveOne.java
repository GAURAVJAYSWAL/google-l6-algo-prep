package org.example.matrix;

public class LongestLineOfConsecutiveOne {

    /**
     * A maximal line of 1s ends at some cell, oriented horizontally, vertically, or
     * along one of the two diagonals. For each orientation the run ending at (r, c) is
     * just 1 + the run ending at the predecessor in that direction, so four DP layers
     * — each reading only an already-computed neighbour (left, up, up-left, up-right)
     * — give every cell's four run lengths in one forward sweep. The global maximum
     * over all cells and all four directions is the answer.
     * Time:  O(m*n) — constant work (four transitions) per cell.
     * Space: O(m*n) — four DP tables; reducible to O(n) rolling rows.
     */
    public static int longestLine(int[][] mat) {
        if (mat.length == 0 || mat[0].length == 0) return 0;
        int m = mat.length, n = mat[0].length;
        int[][] horiz = new int[m][n], vert = new int[m][n];
        int[][] diag = new int[m][n], anti = new int[m][n];
        int best = 0;

        for (int r = 0; r < m; r++) {
            for (int c = 0; c < n; c++) {
                if (mat[r][c] == 0) continue;            // a 0 breaks every run; leave at 0
                horiz[r][c] = (c > 0 ? horiz[r][c - 1] : 0) + 1;                 // from the left
                vert[r][c]  = (r > 0 ? vert[r - 1][c] : 0) + 1;                  // from above
                diag[r][c]  = (r > 0 && c > 0 ? diag[r - 1][c - 1] : 0) + 1;     // from up-left
                anti[r][c]  = (r > 0 && c < n - 1 ? anti[r - 1][c + 1] : 0) + 1; // from up-right
                best = Math.max(best, Math.max(horiz[r][c], vert[r][c]));
                best = Math.max(best, Math.max(diag[r][c], anti[r][c]));
            }
        }
        return best;
    }

    public static void main(String[] args) {
        System.out.println(longestLine(new int[][]{{0, 1, 1, 0}, {0, 1, 0, 0}, {0, 1, 0, 1}}));
        // expected: 3 (vertical run of three 1s in column 1)

        System.out.println(longestLine(new int[][]{{1, 1, 1, 1}, {0, 1, 1, 0}, {0, 0, 0, 1}}));
        // expected: 4 (top row)

        System.out.println(longestLine(new int[][]{{1, 0, 0, 0}, {0, 1, 0, 0}, {0, 0, 1, 0}, {0, 0, 0, 1}}));
        // expected: 4 (main diagonal)

        System.out.println(longestLine(new int[][]{{0}}));
        // expected: 0
    }
}
