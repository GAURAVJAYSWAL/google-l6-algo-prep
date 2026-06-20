package org.example.intervals;

import java.util.Map;
import java.util.TreeMap;

public class RangeModule {

    /**
     * Maintain a set of disjoint half-open intervals as a TreeMap start->end. Every
     * operation pivots on floorKey, which finds the interval that could contain or abut a
     * boundary. addRange swallows all entries that touch or overlap [left,right) — found by
     * walking from floorKey(left) — into one widened interval. removeRange clips the
     * straddling intervals at the cut boundaries (re-inserting the trimmed left/right
     * remnants) and deletes everything strictly inside. queryRange is covered iff a single
     * interval starting at or before left already extends to >= right.
     * Time:  O(log n) amortized per op — each interval is inserted/removed O(1) times overall.
     * Space: O(n) — one entry per disjoint interval currently tracked.
     */
    private final TreeMap<Integer, Integer> ranges = new TreeMap<>(); // start -> end, disjoint

    public void addRange(int left, int right) {
        // Start the merge from the interval just left of (or containing) `left`.
        Integer start = ranges.floorKey(left);
        if (start == null || ranges.get(start) < left) start = left; // no left neighbor reaches us

        // Absorb every interval whose start <= right (they touch or overlap the new range).
        Map.Entry<Integer, Integer> e = ranges.floorEntry(right);
        if (e != null) right = Math.max(right, e.getValue());

        // Drop all entries inside [start, right) — they're now subsumed by one interval.
        ranges.subMap(start, true, right, false).clear();
        ranges.put(start, right);
    }

    public boolean queryRange(int left, int right) {
        Integer start = ranges.floorKey(left);                       // interval that could cover `left`
        return start != null && ranges.get(start) >= right;
    }

    public void removeRange(int left, int right) {
        // Interval straddling the right cut: keep its tail [right, end) if any.
        Map.Entry<Integer, Integer> hi = ranges.lowerEntry(right);
        if (hi != null && hi.getValue() > right) ranges.put(right, hi.getValue());

        // Interval straddling the left cut: keep its head [start, left) if any.
        Map.Entry<Integer, Integer> lo = ranges.lowerEntry(left);
        if (lo != null && lo.getValue() > left) ranges.put(lo.getKey(), left);

        // Everything whose start is in [left, right) is fully removed.
        ranges.subMap(left, true, right, false).clear();
    }

    public static void main(String[] args) {
        RangeModule rm = new RangeModule();
        rm.addRange(10, 20);
        rm.removeRange(14, 16);
        System.out.println(rm.queryRange(10, 14)); // expected: true
        System.out.println(rm.queryRange(13, 15)); // expected: false (14..16 was removed)
        System.out.println(rm.queryRange(16, 17)); // expected: true

        RangeModule rm2 = new RangeModule();
        rm2.addRange(5, 8);
        rm2.addRange(6, 10);                       // merges into [5, 10)
        System.out.println(rm2.queryRange(5, 10)); // expected: true
        rm2.removeRange(7, 9);
        System.out.println(rm2.queryRange(8, 9));  // expected: false
    }
}
