package org.example.graphs;

import java.util.Arrays;

/**
 * LC 733. Flood Fill.
 */
public class FloodFill {

    /**
     * Key insight: recolor the 4-connected region of cells sharing the start cell's
     * original color via DFS. The one subtlety is the no-op case: if the new color
     * equals the original, repainting changes nothing and the visited check (cell ==
     * original) never fails, causing infinite recursion — so we guard against it up front.
     *
     * Time:  O(m*n) — each cell of the target region is visited once.
     * Space: O(m*n) — worst-case recursion depth when the whole image is one color.
     */
    public static int[][] floodFill(int[][] image, int sr, int sc, int color) {
        int original = image[sr][sc];
        if (original != color) {           // guard: avoids infinite recursion when repaint is a no-op
            fill(image, sr, sc, original, color);
        }
        return image;
    }

    private static void fill(int[][] image, int r, int c, int original, int color) {
        if (r < 0 || c < 0 || r >= image.length || c >= image[0].length || image[r][c] != original) return;
        image[r][c] = color;
        fill(image, r + 1, c, original, color);
        fill(image, r - 1, c, original, color);
        fill(image, r, c + 1, original, color);
        fill(image, r, c - 1, original, color);
    }

    public static void main(String[] args) {
        int[][] a = {{1, 1, 1}, {1, 1, 0}, {1, 0, 1}};
        System.out.println(Arrays.deepToString(floodFill(a, 1, 1, 2)));
        // expected: [[2, 2, 2], [2, 2, 0], [2, 0, 1]]

        int[][] b = {{0, 0, 0}, {0, 0, 0}};
        System.out.println(Arrays.deepToString(floodFill(b, 0, 0, 0)));
        // expected: [[0, 0, 0], [0, 0, 0]]

        int[][] c = {{0, 0, 0}, {0, 1, 0}};
        System.out.println(Arrays.deepToString(floodFill(c, 1, 1, 2)));
        // expected: [[0, 0, 0], [0, 2, 0]]
    }
}
