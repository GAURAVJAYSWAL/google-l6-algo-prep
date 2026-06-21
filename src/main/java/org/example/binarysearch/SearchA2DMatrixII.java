package org.example.binarysearch;

public class SearchA2DMatrixII {

    /**
     * Rows and columns are each sorted ascending, so the top-right corner is the
     * max of its row and the min of its column: a perfect pivot. If it exceeds
     * target the whole column is too big (drop it, move left); if it is smaller
     * the whole row is too small (drop it, move down). Each step removes a full
     * row or column, so no full binary search is needed.
     * Time:  O(m + n) — at most m down-moves plus n left-moves.
     * Space: O(1)     — two indices.
     */
    public static boolean searchMatrix(int[][] matrix, int target) {
        int row = 0, col = matrix[0].length - 1;         // start at the top-right corner
        while (row < matrix.length && col >= 0) {
            int val = matrix[row][col];
            if (val == target) {
                return true;
            } else if (val > target) {
                col--;                                   // current column's smallest still too big
            } else {
                row++;                                   // current row's largest still too small
            }
        }
        return false;
    }

    public static void main(String[] args) {
        int[][] m = {
            {1, 4, 7, 11, 15},
            {2, 5, 8, 12, 19},
            {3, 6, 9, 16, 22},
            {10, 13, 14, 17, 24},
            {18, 21, 23, 26, 30}
        };
        System.out.println(searchMatrix(m, 5));                 // expected: true
        System.out.println(searchMatrix(m, 20));                // expected: false
        System.out.println(searchMatrix(new int[][]{{-5}}, -5)); // expected: true
        System.out.println(searchMatrix(new int[][]{{-1, 3}}, 3)); // expected: true
    }
}
