package org.example.binarysearch;

public class CapacityToShipPackagesWithinDDays {

    /**
     * Days-needed is monotonic in ship capacity: a bigger ship never needs more
     * days, so feasible capacities form a suffix [answer .. sum]. We binary search
     * the smallest capacity whose greedy packing (load in order until the next
     * package overflows, then start a new day) fits within D days. The lower bound
     * is max(weights) — a day must hold its heaviest package — and the upper bound
     * is sum(weights), which ships everything in a single day.
     * Time:  O(n log(sum - max)) — feasibility scans n packages per search step.
     * Space: O(1)                — running counters only.
     */
    public static int shipWithinDays(int[] weights, int days) {
        int lo = 0, hi = 0;
        for (int w : weights) {
            lo = Math.max(lo, w);                        // capacity must fit the single heaviest package
            hi += w;                                     // one giant day ships everything
        }
        while (lo < hi) {
            int mid = lo + (hi - lo) / 2;
            if (daysNeeded(weights, mid) <= days) {
                hi = mid;                                // mid is enough; try a smaller ship
            } else {
                lo = mid + 1;                            // too small; need more capacity
            }
        }
        return lo;
    }

    private static int daysNeeded(int[] weights, int capacity) {
        int days = 1, load = 0;                          // first day already open
        for (int w : weights) {
            if (load + w > capacity) {
                days++;                                  // package overflows today; start a new day
                load = 0;
            }
            load += w;
        }
        return days;
    }

    public static void main(String[] args) {
        System.out.println(shipWithinDays(new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10}, 5)); // expected: 15
        System.out.println(shipWithinDays(new int[]{3, 2, 2, 4, 1, 4}, 3));              // expected: 6
        System.out.println(shipWithinDays(new int[]{1, 2, 3, 1, 1}, 4));                 // expected: 3
        System.out.println(shipWithinDays(new int[]{10, 10, 10}, 3));                    // expected: 10
    }
}
