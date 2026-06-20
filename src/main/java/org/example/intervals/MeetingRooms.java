package org.example.intervals;

import java.util.Arrays;

public class MeetingRooms {

    /**
     * One person can attend every meeting iff no two meetings overlap. After sorting by
     * start time, overlap can only occur between adjacent meetings, so a single pass that
     * checks each meeting's start against the previous meeting's end suffices.
     * Time:  O(n log n) — dominated by the sort. Space: O(1) — in-place, ignoring sort.
     */
    public static boolean canAttendMeetings(int[][] intervals) {
        Arrays.sort(intervals, (a, b) -> Integer.compare(a[0], b[0]));
        for (int i = 1; i < intervals.length; i++) {
            if (intervals[i][0] < intervals[i - 1][1]) return false; // starts before predecessor ends
        }
        return true;
    }

    public static void main(String[] args) {
        System.out.println(canAttendMeetings(new int[][]{{0, 30}, {5, 10}, {15, 20}})); // expected: false
        System.out.println(canAttendMeetings(new int[][]{{7, 10}, {2, 4}}));            // expected: true
        System.out.println(canAttendMeetings(new int[][]{{1, 5}, {5, 10}}));            // expected: true (touching is OK)
        System.out.println(canAttendMeetings(new int[][]{}));                           // expected: true
    }
}
