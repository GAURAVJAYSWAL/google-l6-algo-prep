package org.example.differencearray;

/**
 * LC 1094. Car Pooling.
 */
public class CarPooling {

    /**
     * Key insight: every trip [passengers, from, to] adds load on the half-open interval
     * [from, to). Instead of touching every stop in each interval, record only the two
     * boundaries in a difference array: +passengers at `from`, -passengers at `to`. A single
     * prefix-sum sweep then reconstructs the live occupancy at each stop; if it ever exceeds
     * capacity the ride is infeasible. Stops are bounded by 0..1000, so a fixed-size array
     * works.
     *
     * Time:  O(n + R) — n trips to mark boundaries, R=1001 stops to sweep.
     * Space: O(R) — the difference array over the bounded stop range.
     */
    public static boolean carPooling(int[][] trips, int capacity) {
        int[] diff = new int[1001]; // index = stop location (0..1000)
        for (int[] t : trips) {
            diff[t[1]] += t[0]; // board at `from`
            diff[t[2]] -= t[0]; // alight at `to` (passengers gone before this stop "departs")
        }
        int onboard = 0;
        for (int load : diff) {        // running prefix sum = passengers currently in the car
            onboard += load;
            if (onboard > capacity) return false;
        }
        return true;
    }

    public static void main(String[] args) {
        System.out.println(carPooling(new int[][]{{2, 1, 5}, {3, 3, 7}}, 4));
        // expected: false (at stop 3 there are 2+3 = 5 > 4)
        System.out.println(carPooling(new int[][]{{2, 1, 5}, {3, 3, 7}}, 5));
        // expected: true
        System.out.println(carPooling(new int[][]{{2, 1, 5}, {3, 5, 7}}, 3));
        // expected: true (first group leaves exactly when the second boards)
        System.out.println(carPooling(new int[][]{{3, 2, 7}, {3, 7, 9}, {8, 3, 9}}, 11));
        // expected: true
    }
}
