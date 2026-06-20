package org.example.graphs;

import java.util.ArrayList;
import java.util.List;

/**
 * LC 417. Pacific Atlantic Water Flow.
 */
public class PacificAtlanticWaterFlow {

    /**
     * Key insight: water flows to equal-or-lower neighbors, so asking "which cells reach
     * an ocean" is awkward forward but trivial in reverse — start at the ocean's border
     * cells and climb to equal-or-HIGHER neighbors, marking everything reachable. Running
     * this reverse flood from the Pacific borders and the Atlantic borders separately
     * yields two reachability masks; their intersection is the answer.
     *
     * Time:  O(m*n) — each cell is visited at most once per ocean's DFS.
     * Space: O(m*n) — two boolean masks plus worst-case recursion depth.
     */
    public static List<List<Integer>> pacificAtlantic(int[][] heights) {
        List<List<Integer>> result = new ArrayList<>();
        if (heights == null || heights.length == 0) return result;
        int m = heights.length, n = heights[0].length;
        boolean[][] pacific = new boolean[m][n];
        boolean[][] atlantic = new boolean[m][n];

        // Pacific hugs the top row and left column; Atlantic the bottom row and right column.
        for (int r = 0; r < m; r++) {
            climb(heights, r, 0, pacific);
            climb(heights, r, n - 1, atlantic);
        }
        for (int c = 0; c < n; c++) {
            climb(heights, 0, c, pacific);
            climb(heights, m - 1, c, atlantic);
        }

        for (int r = 0; r < m; r++) {
            for (int c = 0; c < n; c++) {
                if (pacific[r][c] && atlantic[r][c]) result.add(List.of(r, c));
            }
        }
        return result;
    }

    private static void climb(int[][] heights, int r, int c, boolean[][] reached) {
        reached[r][c] = true;
        int[][] dirs = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};
        for (int[] d : dirs) {
            int nr = r + d[0], nc = c + d[1];
            if (nr < 0 || nc < 0 || nr >= heights.length || nc >= heights[0].length) continue;
            if (reached[nr][nc]) continue;
            if (heights[nr][nc] < heights[r][c]) continue; // reverse flow: only move to >= cells
            climb(heights, nr, nc, reached);
        }
    }

    public static void main(String[] args) {
        int[][] h1 = {
                {1, 2, 2, 3, 5},
                {3, 2, 3, 4, 4},
                {2, 4, 5, 3, 1},
                {6, 7, 1, 4, 5},
                {5, 1, 1, 2, 4}
        };
        System.out.println(pacificAtlantic(h1));
        // expected: [[0, 4], [1, 3], [1, 4], [2, 2], [3, 0], [3, 1], [4, 0]]

        int[][] h2 = {{1}};
        System.out.println(pacificAtlantic(h2)); // expected: [[0, 0]]

        int[][] h3 = {{2, 1}, {1, 2}};
        System.out.println(pacificAtlantic(h3)); // expected: [[0, 0], [0, 1], [1, 0], [1, 1]]
    }
}
