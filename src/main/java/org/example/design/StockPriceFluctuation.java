package org.example.design;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * LC 2034. Stock Price Fluctuation.
 */
public class StockPriceFluctuation {

    /**
     * Key insight: records arrive out of order and any past timestamp can be
     * corrected, so we cannot just track a running min/max. Keep the source of truth
     * {@code timeToPrice} (timestamp -> latest price) and mirror it as a multiset
     * {@code priceCounts} (price -> how many timestamps hold it) in a TreeMap, which
     * gives O(log n) min/max. On an update we decrement the OLD price's count (the
     * stale value the corrected timestamp used to carry) before adding the new one,
     * so the multiset always reflects current prices only. {@code latestTime} tracks
     * the largest timestamp seen for {@code current()}.
     *
     * Time:  update/min/max O(log n); current O(1).
     * Space: O(n) — one entry per distinct timestamp.
     */
    static class StockPrice {
        private final Map<Integer, Integer> timeToPrice = new HashMap<>();
        private final TreeMap<Integer, Integer> priceCounts = new TreeMap<>();
        private int latestTime = 0;

        public StockPrice() {
        }

        public void update(int timestamp, int price) {
            if (timeToPrice.containsKey(timestamp)) {
                int old = timeToPrice.get(timestamp);
                // Retire the stale price this timestamp previously contributed.
                int cnt = priceCounts.get(old) - 1;
                if (cnt == 0) priceCounts.remove(old);
                else priceCounts.put(old, cnt);
            }
            timeToPrice.put(timestamp, price);
            priceCounts.merge(price, 1, Integer::sum);
            latestTime = Math.max(latestTime, timestamp);
        }

        public int current() {
            return timeToPrice.get(latestTime);
        }

        public int maximum() {
            return priceCounts.lastKey();
        }

        public int minimum() {
            return priceCounts.firstKey();
        }
    }

    public static void main(String[] args) {
        StockPrice s = new StockPrice();
        s.update(1, 10);
        s.update(2, 5);
        System.out.println(s.current());   // expected: 5  (latest timestamp is 2)
        System.out.println(s.maximum());   // expected: 10
        s.update(1, 3);                    // correct the price at t=1 from 10 to 3
        System.out.println(s.maximum());   // expected: 5  (10 is gone)
        s.update(4, 2);
        System.out.println(s.current());   // expected: 2  (latest timestamp is now 4)
        System.out.println(s.maximum());   // expected: 5
        System.out.println(s.minimum());   // expected: 2
    }
}
