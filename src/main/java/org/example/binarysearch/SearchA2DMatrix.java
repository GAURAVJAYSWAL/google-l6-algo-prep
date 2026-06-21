package org.example.binarysearch;

public class SearchA2DMatrix {

    /**
     * Each row is sorted and every row's first value exceeds the previous row's
     * last, so reading the matrix row-by-row yields one fully sorted sequence of
     * m*n values. We binary search that virtual array and map a flat index back
     * to a cell via (index / cols, index % cols), never materializing it.
     * Time:  O(log(m*n)) — halve the flattened space every iteration.
     * Space: O(1)        — index arithmetic only.
     */
    public static boolean searchMatrix(int[][] matrix, int target) {
        int rows = matrix.length, cols = matrix[0].length;
        int lo = 0, hi = rows * cols - 1;
        while (lo <= hi) {
            int mid = lo + (hi - lo) / 2;
            int val = matrix[mid / cols][mid % cols];    // unflatten the 1-D index into (row, col)
            if (val == target) {
                return true;
            } else if (val < target) {
                lo = mid + 1;
            } else {
                hi = mid - 1;
            }
        }
        return false;
    }

    public static void main(String[] args) {
        int[][] m = {{1, 3, 5, 7}, {10, 11, 16, 20}, {23, 30, 34, 60}};
        System.out.println(searchMatrix(m, 3));                 // expected: true
        System.out.println(searchMatrix(m, 13));                // expected: false
        System.out.println(searchMatrix(new int[][]{{1}}, 1));  // expected: true
        System.out.println(searchMatrix(new int[][]{{1, 1}}, 0)); // expected: false
    }
}
