package org.example.intervals;

import java.util.Arrays;
import java.util.PriorityQueue;

public class MeetingRoomsII {

    /**
     * Process meetings in start order. A min-heap of end times holds the rooms currently
     * in use; before assigning a starting meeting, free every room whose meeting has
     * already ended (heap top <= this start). The heap's peak size over the sweep is the
     * maximum number of simultaneously-busy rooms, i.e. the minimum rooms needed.
     * Time:  O(n log n) — sort plus n heap ops. Space: O(n) — the heap of end times.
     */
    public static int minMeetingRooms(int[][] intervals) {
        if (intervals.length == 0) return 0;
        Arrays.sort(intervals, (a, b) -> Integer.compare(a[0], b[0]));

        PriorityQueue<Integer> ends = new PriorityQueue<>(); // end times of in-progress meetings
        for (int[] meeting : intervals) {
            if (!ends.isEmpty() && ends.peek() <= meeting[0]) {
                ends.poll();                                 // a room freed up before this meeting starts
            }
            ends.add(meeting[1]);
        }
        return ends.size();                                  // rooms never freed = concurrent peak
    }

    public static void main(String[] args) {
        System.out.println(minMeetingRooms(new int[][]{{0, 30}, {5, 10}, {15, 20}})); // expected: 2
        System.out.println(minMeetingRooms(new int[][]{{7, 10}, {2, 4}}));            // expected: 1
        System.out.println(minMeetingRooms(new int[][]{{1, 5}, {5, 10}, {2, 7}}));    // expected: 2
        System.out.println(minMeetingRooms(new int[][]{{1, 5}, {1, 5}, {1, 5}}));     // expected: 3
    }
}
