package org.example.backtracking;

/**
 * LeetCode 79 — Word Search (Medium).
 *
 * Given an m x n grid of characters and a word, return whether the word can be spelled
 * by a path of orthogonally adjacent cells, where each cell is used at most once.
 */
public class WordSearch {

    /**
     * Standard DFS, but visited-tracking is folded into the board itself: before recursing we
     * overwrite the current cell with a sentinel ('#') so no path can step on it twice, then
     * restore the original character on the way out. This in-place mark/restore makes each
     * backtracking frame O(1) extra space, and bailing the moment word.charAt(index) mismatches
     * prunes the vast majority of dead branches before they ever expand.
     * Time:  O(m·n·4^L) — m·n DFS roots, each branching up to 4 ways for L = word length.
     * Space: O(L) — recursion depth equals the matched prefix length; no separate visited grid.
     */
    public boolean exist(char[][] board, String word) {
        if (word.isEmpty()) return true;
        int rows = board.length, cols = board[0].length;
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (dfs(board, word, 0, r, c)) return true;
            }
        }
        return false;
    }

    private boolean dfs(char[][] board, String word, int index, int r, int c) {
        if (index == word.length()) return true;                 // whole word matched
        if (r < 0 || r >= board.length || c < 0 || c >= board[0].length) return false;
        if (board[r][c] != word.charAt(index)) return false;     // prune on first mismatch

        char saved = board[r][c];
        board[r][c] = '#';                                       // mark visited in place
        boolean found = dfs(board, word, index + 1, r + 1, c)
                || dfs(board, word, index + 1, r - 1, c)
                || dfs(board, word, index + 1, r, c + 1)
                || dfs(board, word, index + 1, r, c - 1);
        board[r][c] = saved;                                     // restore on backtrack
        return found;
    }

    public static void main(String[] args) {
        WordSearch s = new WordSearch();
        char[][] board = {
                {'A', 'B', 'C', 'E'},
                {'S', 'F', 'C', 'S'},
                {'A', 'D', 'E', 'E'}
        };

        System.out.println(s.exist(board, "ABCCED"));   // expected: true
        System.out.println(s.exist(board, "SEE"));      // expected: true
        System.out.println(s.exist(board, "ABCB"));     // expected: false  (would reuse 'B')

        // Single-row board, word longer than any non-reusing path can spell.
        char[][] line = {{'a', 'a'}};
        System.out.println(s.exist(line, "aaa"));       // expected: false
    }
}
