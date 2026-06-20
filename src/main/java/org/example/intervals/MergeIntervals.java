package org.example.intervals;

import java.util.Arrays;

public class MergeIntervals {

    /**
     * Sort by start; once sorted, intervals that overlap are contiguous, so a single
     * sweep merges them: the incoming interval extends the current run iff its start
     * is <= the current run's end, in which case we stretch the end to the max of the two.
     * Time:  O(n log n) — dominated by the sort. Space: O(n) — the output list.
     */
    public static int[][] merge(int[][] intervals) {
        if (intervals.length == 0) return new int[0][];
        Arrays.sort(intervals, (a, b) -> Integer.compare(a[0], b[0]));

        int[][] merged = new int[intervals.length][2];
        int size = 0;
        merged[size++] = new int[]{intervals[0][0], intervals[0][1]};

        for (int i = 1; i < intervals.length; i++) {
            int[] last = merged[size - 1];
            if (intervals[i][0] <= last[1]) {
                last[1] = Math.max(last[1], intervals[i][1]); // overlap: absorb into current run
            } else {
                merged[size++] = new int[]{intervals[i][0], intervals[i][1]};
            }
        }
        return Arrays.copyOf(merged, size);
    }

    public static void main(String[] args) {
        System.out.println(Arrays.deepToString(merge(new int[][]{{1, 3}, {2, 6}, {8, 10}, {15, 18}})));
        // expected: [[1, 6], [8, 10], [15, 18]]
        System.out.println(Arrays.deepToString(merge(new int[][]{{1, 4}, {4, 5}})));
        // expected: [[1, 5]]
        System.out.println(Arrays.deepToString(merge(new int[][]{{1, 4}, {2, 3}})));
        // expected: [[1, 4]]  (fully contained)
        System.out.println(Arrays.deepToString(merge(new int[][]{{1, 4}, {0, 4}})));
        // expected: [[0, 4]]
    }
}
