package org.example.intervals;

import java.util.Arrays;
import java.util.PriorityQueue;

public class MeetingRoomsIII {

    /**
     * Greedily assign meetings in start order using two heaps: {@code available} holds
     * idle room indices (lowest index wins, per the tie-break rule), and {@code busy}
     * holds (freeTime, index) pairs ordered by when each room frees up. For each meeting,
     * first release every room whose freeTime <= the meeting's start. If a room is free,
     * take the lowest-indexed one and keep the meeting's true length; otherwise wait for
     * the earliest-freeing room and shift the meeting later, so it runs for its full
     * duration starting at that room's freeTime. Counting assignments per room and
     * returning the max (lowest index on ties) gives the busiest room. Times need long
     * because delayed meetings can push free-times past int range.
     * Time:  O(n log n + n log rooms) — sort plus heap ops. Space: O(n + rooms) — heaps and counts.
     */
    public static int mostBooked(int n, int[][] meetings) {
        Arrays.sort(meetings, (a, b) -> Integer.compare(a[0], b[0]));

        long[] count = new long[n];
        PriorityQueue<Integer> available = new PriorityQueue<>();        // idle room indices
        PriorityQueue<long[]> busy = new PriorityQueue<>(                // {freeTime, roomIndex}
                (a, b) -> a[0] != b[0] ? Long.compare(a[0], b[0]) : Long.compare(a[1], b[1]));
        for (int r = 0; r < n; r++) available.add(r);

        for (int[] m : meetings) {
            long start = m[0], end = m[1];
            // Return every room that has finished by the time this meeting starts.
            while (!busy.isEmpty() && busy.peek()[0] <= start) {
                available.add((int) busy.poll()[1]);
            }
            if (!available.isEmpty()) {
                int room = available.poll();                             // lowest free index
                count[room]++;
                busy.add(new long[]{end, room});
            } else {
                long[] earliest = busy.poll();                           // wait for the next room to free
                int room = (int) earliest[1];
                count[room]++;
                long duration = end - start;
                busy.add(new long[]{earliest[0] + duration, room});      // delayed: keep full duration
            }
        }

        int best = 0;
        for (int r = 1; r < n; r++) {
            if (count[r] > count[best]) best = r;                        // ties keep the lower index
        }
        return best;
    }

    public static void main(String[] args) {
        System.out.println(mostBooked(2, new int[][]{{0, 10}, {1, 5}, {2, 7}, {3, 4}})); // expected: 0
        System.out.println(mostBooked(3, new int[][]{{1, 20}, {2, 10}, {3, 5}, {4, 9}, {6, 8}})); // expected: 1
        System.out.println(mostBooked(2, new int[][]{{0, 10}, {1, 2}, {12, 14}, {13, 15}})); // expected: 0
        System.out.println(mostBooked(4, new int[][]{{18, 19}, {3, 12}, {17, 19}, {2, 13}, {7, 10}})); // expected: 0
    }
}
