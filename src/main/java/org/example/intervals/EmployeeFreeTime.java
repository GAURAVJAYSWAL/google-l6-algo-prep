package org.example.intervals;

import java.util.ArrayList;
import java.util.List;

public class EmployeeFreeTime {

    /** Self-contained interval type (mirrors LeetCode's Interval). */
    public static class Interval {
        public int start, end;
        public Interval(int start, int end) { this.start = start; this.end = end; }
        @Override public String toString() { return "[" + start + ", " + end + "]"; }
    }

    /**
     * Common free time is the gaps left after merging *all* employees' busy intervals into
     * one timeline. Flatten every interval into a single list, sort by start, then sweep:
     * track the furthest end reached so far, and whenever the next interval starts after
     * that end, the span between them is a gap shared by everyone. Widening the running end
     * by max handles intervals nested inside earlier ones.
     * Time:  O(n log n) — sort over all n intervals. Space: O(n) — the flattened list.
     */
    public static List<Interval> employeeFreeTime(List<List<Interval>> schedule) {
        List<Interval> all = new ArrayList<>();
        for (List<Interval> emp : schedule) all.addAll(emp);
        all.sort((a, b) -> Integer.compare(a.start, b.start));

        List<Interval> free = new ArrayList<>();
        int end = all.get(0).end;                       // furthest busy point reached so far
        for (int i = 1; i < all.size(); i++) {
            Interval cur = all.get(i);
            if (cur.start > end) {
                free.add(new Interval(end, cur.start)); // a gap nobody is busy in
                end = cur.end;
            } else {
                end = Math.max(end, cur.end);           // overlap/contained: extend the busy frontier
            }
        }
        return free;
    }

    private static List<Interval> emp(int... pts) {
        List<Interval> list = new ArrayList<>();
        for (int i = 0; i < pts.length; i += 2) list.add(new Interval(pts[i], pts[i + 1]));
        return list;
    }

    public static void main(String[] args) {
        System.out.println(employeeFreeTime(List.of(emp(1, 2, 5, 6), emp(1, 3), emp(4, 10))));
        // expected: [[3, 4]]
        System.out.println(employeeFreeTime(List.of(emp(1, 3, 6, 7), emp(2, 4), emp(2, 5, 9, 12))));
        // expected: [[5, 6], [7, 9]]
        System.out.println(employeeFreeTime(List.of(emp(1, 10), emp(2, 3))));
        // expected: []  (second is contained, no shared gap)
        System.out.println(employeeFreeTime(List.of(emp(1, 2), emp(3, 4), emp(5, 6))));
        // expected: [[2, 3], [4, 5]]
    }
}
