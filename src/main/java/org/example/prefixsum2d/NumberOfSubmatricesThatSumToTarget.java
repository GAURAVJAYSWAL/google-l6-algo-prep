package org.example.prefixsum2d;

import java.util.HashMap;
import java.util.Map;

/**
 * LC 1074. Number of Submatrices That Sum to Target.
 */
public class NumberOfSubmatricesThatSumToTarget {

    /**
     * Key insight: a submatrix is pinned by its top and bottom rows and its left and
     * right columns. Fix the vertical band (top, bottom): collapsing each column to its
     * sum over those rows turns the 2-D shape into a 1-D array colSum[], and any
     * submatrix in this band is just a contiguous subarray of colSum[]. Counting
     * subarrays with sum == target is the classic "subarray sum equals k" — walk a
     * running prefix sum and, at each position, add the number of earlier prefixes equal
     * to (prefix − target), since each such earlier prefix marks a left boundary that
     * closes a window summing to exactly target. Summing this over all O(rows²) bands
     * covers every submatrix exactly once. As bottom advances we add its row into
     * colSum[] in place rather than rebuilding the band from scratch.
     *
     * Time:  O(rows²·cols) — O(rows²) bands, each scanned across cols once.
     * Space: O(cols) — the colSum array plus the prefix-count map (≤ cols+1 entries).
     */
    public int numSubmatrixSumTarget(int[][] matrix, int target) {
        int rows = matrix.length, cols = matrix[0].length;
        int count = 0;
        for (int top = 0; top < rows; top++) {
            int[] colSum = new int[cols];
            for (int bottom = top; bottom < rows; bottom++) {
                // Extend the band by one row: fold matrix[bottom] into the column sums.
                for (int c = 0; c < cols; c++) colSum[c] += matrix[bottom][c];
                count += countSubarrays(colSum, target);
            }
        }
        return count;
    }

    /** Number of contiguous subarrays of arr summing to target (subarray-sum-equals-k). */
    private int countSubarrays(int[] arr, int target) {
        Map<Integer, Integer> seen = new HashMap<>();
        seen.put(0, 1); // empty prefix: enables windows that start at index 0
        int prefix = 0, count = 0;
        for (int v : arr) {
            prefix += v;
            // Each earlier prefix == prefix - target closes one window summing to target.
            count += seen.getOrDefault(prefix - target, 0);
            seen.merge(prefix, 1, Integer::sum);
        }
        return count;
    }

    public static void main(String[] args) {
        NumberOfSubmatricesThatSumToTarget s = new NumberOfSubmatricesThatSumToTarget();

        int[][] m1 = {{0, 1, 0}, {1, 1, 1}, {0, 1, 0}};
        // The four 1×1 cells equal to 0, plus four 1×3/3×1 "plus arms" sum to 0 -> 4.
        System.out.println(s.numSubmatrixSumTarget(m1, 0)); // expected: 4

        int[][] m2 = {{1, -1}, {-1, 1}};
        // Five submatrices sum to 0: the two rows, the two columns, and the whole 2×2.
        System.out.println(s.numSubmatrixSumTarget(m2, 0)); // expected: 5

        int[][] m3 = {{904}};
        System.out.println(s.numSubmatrixSumTarget(m3, 0)); // expected: 0  (only cell is 904)

        int[][] m4 = {{1, 2}, {3, 4}};
        // Submatrices summing to 3: the single cell 3, and the top row 1+2.
        System.out.println(s.numSubmatrixSumTarget(m4, 3)); // expected: 2
    }
}
