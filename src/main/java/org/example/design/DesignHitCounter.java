package org.example.design;

/**
 * LC 362. Design Hit Counter.
 */
public class DesignHitCounter {

    /**
     * Key insight (chosen approach): a fixed circular buffer of 300 one-second buckets.
     * Bucket i serves every timestamp t with t % 300 == i, storing the LAST timestamp
     * that wrote it alongside a hit count. When a hit or query lands on a bucket whose
     * stored timestamp is more than 300 seconds stale, that bucket belongs to an earlier
     * window and is lazily reset before use — so expiry costs nothing until touched.
     * getHits(t) then sums only buckets whose stored timestamp is within the last 300s.
     *
     * Chosen over a queue of timestamps because memory is bounded at O(300) = O(1)
     * regardless of traffic volume, whereas a raw timestamp queue grows with the number
     * of hits in the window (fine for sparse traffic, but unbounded for bursts). Both
     * give the same asymptotic getHits cost; the buffer just caps space hard.
     *
     * Time:  hit O(1); getHits O(300) = O(1) — a fixed scan over the ring.
     * Space: O(300) = O(1) — two parallel arrays of fixed size, independent of traffic.
     */
    static class HitCounter {
        private static final int WINDOW = 300;
        private final int[] times = new int[WINDOW];   // last timestamp that wrote each bucket
        private final int[] hits = new int[WINDOW];    // hit count for that bucket's current second

        public HitCounter() {
        }

        public void hit(int timestamp) {
            int i = timestamp % WINDOW;
            if (times[i] != timestamp) {
                // Bucket holds a stale second (or is unused): start its count fresh.
                times[i] = timestamp;
                hits[i] = 1;
            } else {
                hits[i]++;
            }
        }

        public int getHits(int timestamp) {
            int total = 0;
            for (int i = 0; i < WINDOW; i++) {
                // Count a bucket only if its timestamp is inside the trailing 300s window.
                if (timestamp - times[i] < WINDOW) {
                    total += hits[i];
                }
            }
            return total;
        }
    }

    public static void main(String[] args) {
        HitCounter c = new HitCounter();
        c.hit(1);
        c.hit(2);
        c.hit(3);
        System.out.println(c.getHits(4));     // expected: 3  (all three within 300s)
        c.hit(300);
        System.out.println(c.getHits(300));   // expected: 4  (hits at 1,2,3,300 all in window)
        System.out.println(c.getHits(301));   // expected: 3  (t=1 now exactly 300s old -> dropped)
        System.out.println(c.getHits(302));   // expected: 2  (t=1 and t=2 dropped)

        HitCounter c2 = new HitCounter();
        c2.hit(1);
        c2.hit(1);
        c2.hit(1);                            // three hits in the same second
        System.out.println(c2.getHits(1));    // expected: 3  (same-second hits accumulate)
        System.out.println(c2.getHits(301));  // expected: 0  (second 1 has fully expired)
    }
}
