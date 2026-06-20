package org.example.intervals;

import java.util.Arrays;
import java.util.List;

public class MinimumTimeDifference {

    /**
     * Convert each "HH:MM" to minutes-since-midnight (0..1439) and sort. The closest pair on
     * a 24-hour clock is either two adjacent values after sorting, or the wraparound pair
     * (last and first), whose distance is 1440 minus their direct gap. With > 1440 entries
     * a collision is forced by pigeonhole, so the answer is 0 — but the sort handles that
     * naturally without a special case.
     * Time:  O(n log n) — dominated by the sort. Space: O(n) — the minutes array.
     */
    public static int findMinDifference(List<String> timePoints) {
        int[] mins = new int[timePoints.size()];
        for (int i = 0; i < mins.length; i++) {
            String t = timePoints.get(i);
            int h = (t.charAt(0) - '0') * 10 + (t.charAt(1) - '0');
            int m = (t.charAt(3) - '0') * 10 + (t.charAt(4) - '0');
            mins[i] = h * 60 + m;
        }
        Arrays.sort(mins);

        int best = Integer.MAX_VALUE;
        for (int i = 1; i < mins.length; i++) {
            best = Math.min(best, mins[i] - mins[i - 1]);
        }
        // Wraparound: distance from the latest time forward to the earliest time next day.
        int wrap = 1440 - mins[mins.length - 1] + mins[0];
        return Math.min(best, wrap);
    }

    public static void main(String[] args) {
        System.out.println(findMinDifference(List.of("23:59", "00:00"))); // expected: 1 (wraparound)
        System.out.println(findMinDifference(List.of("00:00", "23:59", "00:00"))); // expected: 0 (duplicate)
        System.out.println(findMinDifference(List.of("01:01", "02:01", "03:00"))); // expected: 59
        System.out.println(findMinDifference(List.of("12:12", "00:13"))); // expected: 719
    }
}
