package org.example.backtracking;

import java.util.ArrayList;
import java.util.List;

public class NQueens {

    /**
     * Place exactly one queen per row, so a solution is a choice of column for each
     * row. A new queen at (row, col) is safe iff its column and both diagonals are
     * unoccupied. Encode the two diagonal families by their invariants: cells on a
     * "\" diagonal share (row - col), cells on a "/" diagonal share (row + col).
     * O(1) set membership turns each safety check into three lookups.
     * Time:  O(n!) — at most n columns in row 0, n-1 in row 1, and so on.
     * Space: O(n) — recursion depth, three sets, and the placement array.
     */
    public static List<List<String>> solveNQueens(int n) {
        List<List<String>> out = new ArrayList<>();
        boolean[] cols = new boolean[n];
        boolean[] diag = new boolean[2 * n - 1];     // index by (row - col) + (n - 1)
        boolean[] anti = new boolean[2 * n - 1];     // index by (row + col)
        backtrack(0, n, new int[n], cols, diag, anti, out);
        return out;
    }

    private static void backtrack(int row, int n, int[] queenCol,
                                  boolean[] cols, boolean[] diag, boolean[] anti,
                                  List<List<String>> out) {
        if (row == n) {                              // a queen safely in every row
            out.add(build(queenCol, n));
            return;
        }
        for (int col = 0; col < n; col++) {
            int d = row - col + n - 1, a = row + col;
            if (cols[col] || diag[d] || anti[a]) continue;   // column or a diagonal is attacked
            cols[col] = diag[d] = anti[a] = true;            // choose
            queenCol[row] = col;
            backtrack(row + 1, n, queenCol, cols, diag, anti, out);
            cols[col] = diag[d] = anti[a] = false;           // un-choose
        }
    }

    private static List<String> build(int[] queenCol, int n) {
        List<String> board = new ArrayList<>(n);
        for (int r = 0; r < n; r++) {
            char[] rowChars = new char[n];
            for (int c = 0; c < n; c++) rowChars[c] = (queenCol[r] == c) ? 'Q' : '.';
            board.add(new String(rowChars));
        }
        return board;
    }

    public static void main(String[] args) {
        for (List<String> board : solveNQueens(4)) {
            board.forEach(System.out::println);
            System.out.println();
        }
        // expected (2 solutions):
        // .Q..   ..Q.
        // ...Q   Q...
        // Q...   ...Q
        // ..Q.   .Q..

        System.out.println(solveNQueens(1));   // expected: [[Q]]
        System.out.println(solveNQueens(2).size()); // expected: 0
        System.out.println(solveNQueens(3).size()); // expected: 0
        System.out.println(solveNQueens(8).size()); // expected: 92
    }
}
