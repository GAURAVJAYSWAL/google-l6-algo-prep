package org.example.intervals;

import java.util.TreeMap;

public class MyCalendarI {

    /**
     * Keep booked events in a TreeMap keyed by start time. A half-open [start, end) booking
     * conflicts with an existing event iff it overlaps the neighbor immediately before it
     * (floorKey's end > start) or the neighbor immediately after it (ceilingKey's start < end).
     * Both neighbors are reachable in O(log n), so each booking is logarithmic.
     * Time:  O(log n) per book — two TreeMap navigations. Space: O(n) — one entry per event.
     */
    private final TreeMap<Integer, Integer> calendar = new TreeMap<>(); // start -> end

    public boolean book(int start, int end) {
        Integer prev = calendar.floorKey(start);    // latest event starting at or before `start`
        if (prev != null && calendar.get(prev) > start) return false;
        Integer next = calendar.ceilingKey(start);  // earliest event starting at or after `start`
        if (next != null && next < end) return false;
        calendar.put(start, end);
        return true;
    }

    public static void main(String[] args) {
        MyCalendarI cal = new MyCalendarI();
        System.out.println(cal.book(10, 20)); // expected: true
        System.out.println(cal.book(15, 25)); // expected: false (overlaps [10,20))
        System.out.println(cal.book(20, 30)); // expected: true  (touching at 20 is allowed)

        MyCalendarI cal2 = new MyCalendarI();
        System.out.println(cal2.book(47, 50)); // expected: true
        System.out.println(cal2.book(33, 41)); // expected: true
        System.out.println(cal2.book(39, 45)); // expected: false (overlaps [33,41))
    }
}
