package org.example.intervals;

import java.util.Arrays;

/**
 * LC 435 — Non-overlapping Intervals (Medium).
 * Remove the minimum number of intervals so the rest are pairwise non-overlapping.
 */
public class NonOverlappingIntervals {

    /**
     * Classic activity selection: sort by END coordinate and greedily keep the interval that finishes
     * earliest, since the earliest finish leaves the most room for later intervals — any optimal schedule
     * can be rewritten to start this way. Removals = total - kept.
     * Time:  O(n log n) — sort dominates; the sweep is linear.
     * Space: O(1) — only the running "last kept end" and a counter (besides the sort's own scratch).
     */
    int eraseOverlapIntervals(int[][] intervals) {
        if (intervals.length == 0) return 0;
        Arrays.sort(intervals, (a, b) -> Integer.compare(a[1], b[1]));

        int kept = 1;                 // the first (earliest-finishing) interval is always kept
        int lastEnd = intervals[0][1];
        for (int i = 1; i < intervals.length; i++) {
            // Touching at the boundary (start == lastEnd) does NOT overlap, so keep it.
            if (intervals[i][0] >= lastEnd) {
                kept++;
                lastEnd = intervals[i][1];
            }
            // Otherwise it overlaps the earlier-finishing kept interval; drop it (lastEnd unchanged).
        }
        return intervals.length - kept;
    }

    public static void main(String[] args) {
        NonOverlappingIntervals s = new NonOverlappingIntervals();

        // [1,2],[2,3],[3,4],[1,3]: keep [1,2],[2,3],[3,4]; drop [1,3] → 1.
        System.out.println(s.eraseOverlapIntervals(
                new int[][]{{1, 2}, {2, 3}, {3, 4}, {1, 3}})); // expected: 1

        // [1,2],[1,2],[1,2]: keep one, drop two → 2.
        System.out.println(s.eraseOverlapIntervals(
                new int[][]{{1, 2}, {1, 2}, {1, 2}})); // expected: 2

        // [1,2],[2,3]: boundary-touching, none removed → 0.
        System.out.println(s.eraseOverlapIntervals(
                new int[][]{{1, 2}, {2, 3}})); // expected: 0
    }
}
