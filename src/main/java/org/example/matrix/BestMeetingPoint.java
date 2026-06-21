package org.example.matrix;

import java.util.ArrayList;
import java.util.List;

/**
 * LC 296. Best Meeting Point.
 */
public class BestMeetingPoint {

    /**
     * Key insight: Manhattan distance |r1-r2| + |c1-c2| splits cleanly into an
     * independent x-cost and y-cost, so we can choose the meeting row and column
     * separately. For one axis the task is "pick p minimizing Σ|x_i - p|", and the
     * minimizer is the MEDIAN, not the mean: moving p one step toward the side holding
     * more points shortens that many distances and lengthens fewer, so the sum keeps
     * dropping until equally many points lie on each side — exactly the median. (The
     * mean minimizes squared deviations, a different objective.) We never sort: scanning
     * rows outermost yields row indices already ascending, and scanning columns
     * outermost yields column indices already ascending.
     *
     * Time:  O(rows·cols) — two passes over the grid to gather coordinates.
     * Space: O(number of 1s) — the two coordinate lists.
     */
    public int minTotalDistance(int[][] grid) {
        if (grid == null || grid.length == 0 || grid[0].length == 0) return 0;
        int rows = grid.length, cols = grid[0].length;

        List<Integer> rowIdx = new ArrayList<>();
        // Outer loop over rows -> row indices are appended in ascending order.
        for (int r = 0; r < rows; r++)
            for (int c = 0; c < cols; c++)
                if (grid[r][c] == 1) rowIdx.add(r);

        List<Integer> colIdx = new ArrayList<>();
        // Outer loop over columns -> column indices are appended in ascending order.
        for (int c = 0; c < cols; c++)
            for (int r = 0; r < rows; r++)
                if (grid[r][c] == 1) colIdx.add(c);

        return distanceToMedian(rowIdx) + distanceToMedian(colIdx);
    }

    /** sorted is ascending; sum of absolute deviations from its median. */
    private int distanceToMedian(List<Integer> sorted) {
        int total = 0;
        // Pair smallest with largest inward: each pair contributes its full span,
        // which equals both points' distance to any median lying between them.
        int lo = 0, hi = sorted.size() - 1;
        while (lo < hi) {
            total += sorted.get(hi) - sorted.get(lo);
            lo++;
            hi--;
        }
        return total;
    }

    public static void main(String[] args) {
        BestMeetingPoint s = new BestMeetingPoint();

        int[][] g1 = {
            {1, 0, 0, 0, 1},
            {0, 0, 0, 0, 0},
            {0, 0, 1, 0, 0}
        };
        // Median row=0, median col=2: |0-0|+|0-2| + |0-0|+|4-2| + |2-0|+|2-2| = 2+2+2 = 6.
        System.out.println(s.minTotalDistance(g1)); // expected: 6

        int[][] g2 = {{1, 1}};
        // Two adjacent people: meet at either -> total distance 1.
        System.out.println(s.minTotalDistance(g2)); // expected: 1

        int[][] g3 = {
            {1, 0, 0},
            {0, 0, 0},
            {0, 0, 1}
        };
        // Median row between 0 and 2, median col between 0 and 2: 2 + 2 = 4.
        System.out.println(s.minTotalDistance(g3)); // expected: 4

        int[][] g4 = {{0, 0}, {0, 0}};
        System.out.println(s.minTotalDistance(g4)); // expected: 0  (no people)
    }
}
