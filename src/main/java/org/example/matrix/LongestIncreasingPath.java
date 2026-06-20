package org.example.matrix;

public class LongestIncreasingPath {

    private static final int[] DR = {-1, 1, 0, 0};
    private static final int[] DC = {0, 0, -1, 1};

    /**
     * Because every step must be STRICTLY increasing, a path can never revisit a cell
     * (values would have to repeat), so the reachability graph is a DAG and no visited
     * set is needed. The longest increasing path starting at a cell depends only on
     * that cell, so we memoize it: dfs(r, c) = 1 + max over larger neighbours of
     * dfs(neighbour). Each cell is solved once and cached; the answer is the max over
     * all starts.
     * Time:  O(m*n) — every cell's value is computed once and reused thereafter.
     * Space: O(m*n) — memo table plus recursion depth in the worst case.
     */
    public static int longestIncreasingPath(int[][] matrix) {
        int m = matrix.length, n = matrix[0].length;
        int[][] memo = new int[m][n];                    // 0 means "not yet computed"
        int best = 0;
        for (int r = 0; r < m; r++) {
            for (int c = 0; c < n; c++) {
                best = Math.max(best, dfs(matrix, r, c, memo));
            }
        }
        return best;
    }

    private static int dfs(int[][] matrix, int r, int c, int[][] memo) {
        if (memo[r][c] != 0) return memo[r][c];          // cache hit
        int longest = 1;                                 // the cell itself
        for (int d = 0; d < 4; d++) {
            int nr = r + DR[d], nc = c + DC[d];
            if (nr >= 0 && nr < matrix.length && nc >= 0 && nc < matrix[0].length
                    && matrix[nr][nc] > matrix[r][c]) {  // strictly increasing step only
                longest = Math.max(longest, 1 + dfs(matrix, nr, nc, memo));
            }
        }
        memo[r][c] = longest;
        return longest;
    }

    public static void main(String[] args) {
        System.out.println(longestIncreasingPath(new int[][]{{9, 9, 4}, {6, 6, 8}, {2, 1, 1}}));
        // expected: 4 (1 -> 2 -> 6 -> 9)

        System.out.println(longestIncreasingPath(new int[][]{{3, 4, 5}, {3, 2, 6}, {2, 2, 1}}));
        // expected: 4 (3 -> 4 -> 5 -> 6)

        System.out.println(longestIncreasingPath(new int[][]{{1}}));
        // expected: 1
    }
}
