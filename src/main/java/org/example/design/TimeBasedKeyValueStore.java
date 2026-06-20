package org.example.design;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * LC 981. Time Based Key-Value Store.
 */
public class TimeBasedKeyValueStore {

    /**
     * Key insight: the problem guarantees set() is called with strictly increasing
     * timestamps per key, so each key's history is already sorted just by appending.
     * That turns get(key, t) — "the value stored at the largest timestamp <= t" — into
     * a plain binary search over that key's timestamp list, no sorting needed at query
     * time. We store timestamps and values in parallel lists to keep the search tight.
     *
     * Time:  set O(1) amortized (append); get O(log n) — binary search within one key.
     * Space: O(n) — every set keeps one (timestamp, value) record.
     */
    static class TimeMap {
        private final Map<String, List<int[]>> stamps = new HashMap<>();   // key -> timestamps (sorted ascending)
        private final Map<String, List<String>> vals = new HashMap<>();    // key -> values, index-aligned with stamps

        public TimeMap() {
        }

        public void set(String key, String value, int timestamp) {
            stamps.computeIfAbsent(key, k -> new ArrayList<>()).add(new int[]{timestamp});
            vals.computeIfAbsent(key, k -> new ArrayList<>()).add(value);
        }

        public String get(String key, int timestamp) {
            List<int[]> ts = stamps.get(key);
            if (ts == null) return "";
            // Rightmost record whose timestamp is <= the query; "" if all are newer.
            int lo = 0, hi = ts.size() - 1, ansIdx = -1;
            while (lo <= hi) {
                int mid = (lo + hi) >>> 1;
                if (ts.get(mid)[0] <= timestamp) {
                    ansIdx = mid;
                    lo = mid + 1;
                } else {
                    hi = mid - 1;
                }
            }
            return ansIdx == -1 ? "" : vals.get(key).get(ansIdx);
        }
    }

    public static void main(String[] args) {
        TimeMap m = new TimeMap();
        m.set("foo", "bar", 1);
        System.out.println(m.get("foo", 1));   // expected: bar  (exact timestamp match)
        System.out.println(m.get("foo", 3));   // expected: bar  (largest timestamp <= 3 is 1)
        m.set("foo", "bar2", 4);
        System.out.println(m.get("foo", 4));   // expected: bar2 (exact match at the newer write)
        System.out.println(m.get("foo", 5));   // expected: bar2 (carries forward past t=4)
        System.out.println(m.get("foo", 0));   // expected: ""   (nothing stored at or before 0)
        System.out.println(m.get("none", 1));  // expected: ""   (unknown key)
    }
}
