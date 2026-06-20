package org.example.binarysearch;

public class KokoEatingBananas {

    /**
     * Hours-needed is monotonic in eating speed k: a faster Koko never needs more
     * hours, so feasible speeds form a suffix [answer .. maxPile]. We binary
     * search the smallest k whose total hours <= h. A pile of size p at speed k
     * costs ceil(p / k) hours, computed as (p + k - 1) / k to avoid floating point.
     * Time:  O(n log maxPile) — feasibility scans n piles per binary-search step.
     * Space: O(1)             — running counters only.
     */
    public static int minEatingSpeed(int[] piles, int h) {
        int lo = 1, hi = 0;
        for (int p : piles) {
            hi = Math.max(hi, p);                        // max speed worth trying is the largest pile
        }
        while (lo < hi) {
            int mid = lo + (hi - lo) / 2;
            if (hoursNeeded(piles, mid) <= h) {
                hi = mid;                                // mid works; search for something smaller
            } else {
                lo = mid + 1;                            // too slow; need a faster speed
            }
        }
        return lo;
    }

    private static long hoursNeeded(int[] piles, int k) {
        long hours = 0;
        for (int p : piles) {
            hours += (p + k - 1) / k;                    // ceil(p / k) without floating point
        }
        return hours;
    }

    public static void main(String[] args) {
        System.out.println(minEatingSpeed(new int[]{3, 6, 7, 11}, 8));            // expected: 4
        System.out.println(minEatingSpeed(new int[]{30, 11, 23, 4, 20}, 5));      // expected: 30
        System.out.println(minEatingSpeed(new int[]{30, 11, 23, 4, 20}, 6));      // expected: 23
        System.out.println(minEatingSpeed(new int[]{1000000000}, 2));             // expected: 500000000
    }
}
