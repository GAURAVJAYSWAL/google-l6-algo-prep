package org.example.differencearray;

import java.util.Arrays;

/**
 * LC 1109. Corporate Flight Bookings.
 */
public class CorporateFlightBookings {

    /**
     * Key insight: a booking [first, last, seats] adds the same `seats` to every flight in
     * the inclusive range [first, last]. Rather than looping that range per booking, stamp a
     * difference array: +seats at the start and -seats just past the end, then one prefix-sum
     * pass turns those boundary marks back into the per-flight totals. Flights are 1-indexed,
     * so we shift to 0-indexed and let the -seats land at `last` (which is `last-1`+1).
     *
     * Time:  O(n + m) — m bookings stamp two endpoints each, then an O(n) sweep over flights.
     * Space: O(n) — the difference array, reused as the answer.
     */
    public static int[] corpFlightBookings(int[][] bookings, int n) {
        int[] res = new int[n]; // doubles as the difference array, then the prefix-summed answer
        for (int[] b : bookings) {
            int first = b[0], last = b[1], seats = b[2];
            res[first - 1] += seats;       // begin adding seats from flight `first`
            if (last < n) res[last] -= seats; // stop after flight `last`; guard the right edge
        }
        for (int i = 1; i < n; i++) {
            res[i] += res[i - 1]; // prefix sum recovers cumulative seats per flight
        }
        return res;
    }

    public static void main(String[] args) {
        System.out.println(Arrays.toString(
                corpFlightBookings(new int[][]{{1, 2, 10}, {2, 3, 20}, {2, 5, 25}}, 5)));
        // expected: [10, 55, 45, 25, 25]
        System.out.println(Arrays.toString(
                corpFlightBookings(new int[][]{{1, 2, 10}, {2, 2, 15}}, 2)));
        // expected: [10, 25]
        System.out.println(Arrays.toString(
                corpFlightBookings(new int[][]{{1, 1, 5}}, 1)));
        // expected: [5] (single flight, booking touches the last flight)
        System.out.println(Arrays.toString(
                corpFlightBookings(new int[][]{{1, 3, 4}}, 3)));
        // expected: [4, 4, 4]
    }
}
