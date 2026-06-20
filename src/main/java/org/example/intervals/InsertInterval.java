package org.example.intervals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class InsertInterval {

    /**
     * The input is already sorted and non-overlapping, so the answer splits into three
     * runs: intervals ending before newInterval starts (copied as-is), intervals that
     * touch newInterval (collapsed into one by widening newInterval's bounds), and
     * intervals starting after newInterval ends (copied as-is). No sort needed.
     * Time:  O(n) — single linear scan. Space: O(n) — the output list.
     */
    public static int[][] insert(int[][] intervals, int[] newInterval) {
        List<int[]> result = new ArrayList<>();
        int i = 0, n = intervals.length;
        int start = newInterval[0], end = newInterval[1];

        // Phase 1: everything strictly before the new interval.
        while (i < n && intervals[i][1] < start) {
            result.add(intervals[i++]);
        }
        // Phase 2: absorb every interval that overlaps the (growing) new interval.
        while (i < n && intervals[i][0] <= end) {
            start = Math.min(start, intervals[i][0]);
            end = Math.max(end, intervals[i][1]);
            i++;
        }
        result.add(new int[]{start, end});
        // Phase 3: everything strictly after.
        while (i < n) {
            result.add(intervals[i++]);
        }
        return result.toArray(new int[0][]);
    }

    public static void main(String[] args) {
        System.out.println(Arrays.deepToString(insert(new int[][]{{1, 3}, {6, 9}}, new int[]{2, 5})));
        // expected: [[1, 5], [6, 9]]
        System.out.println(Arrays.deepToString(
                insert(new int[][]{{1, 2}, {3, 5}, {6, 7}, {8, 10}, {12, 16}}, new int[]{4, 8})));
        // expected: [[1, 2], [3, 10], [12, 16]]
        System.out.println(Arrays.deepToString(insert(new int[][]{}, new int[]{5, 7})));
        // expected: [[5, 7]]
        System.out.println(Arrays.deepToString(insert(new int[][]{{1, 5}}, new int[]{6, 8})));
        // expected: [[1, 5], [6, 8]]
    }
}
