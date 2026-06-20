package org.example.matrix;

public class Battleships {

    /**
     * Ships are straight (1xk or kx1) and never adjacent, so each ship has exactly one
     * "top-left" cell: an 'X' whose neighbour directly above and directly to the left
     * are both not 'X' (or off-grid). Counting only those cells counts each ship once,
     * in a single pass with no marking of visited cells.
     * Time:  O(m*n) — one scan of the board.
     * Space: O(1)   — only a counter; the board is not modified.
     */
    public static int countBattleships(char[][] board) {
        int count = 0;
        for (int r = 0; r < board.length; r++) {
            for (int c = 0; c < board[0].length; c++) {
                if (board[r][c] != 'X') continue;
                // Skip cells that continue a ship from above or from the left.
                if (r > 0 && board[r - 1][c] == 'X') continue;
                if (c > 0 && board[r][c - 1] == 'X') continue;
                count++;                                 // this is a ship's top-left cell
            }
        }
        return count;
    }

    public static void main(String[] args) {
        char[][] a = {
            {'X', '.', '.', 'X'},
            {'.', '.', '.', 'X'},
            {'.', '.', '.', 'X'}
        };
        System.out.println(countBattleships(a)); // expected: 2

        char[][] b = {{'.'}};
        System.out.println(countBattleships(b)); // expected: 0

        char[][] c = {
            {'X', 'X', 'X', '.', 'X'},
            {'.', '.', '.', '.', 'X'},
            {'X', '.', 'X', '.', '.'}
        };
        System.out.println(countBattleships(c)); // expected: 4
    }
}
