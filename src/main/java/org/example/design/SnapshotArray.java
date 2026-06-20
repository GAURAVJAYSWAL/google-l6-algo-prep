package org.example.design;

import java.util.ArrayList;
import java.util.List;

/**
 * LC 1146. Snapshot Array.
 */
public class SnapshotArray {

    /**
     * Key insight: snapshotting the whole array on every snap() would be O(length)
     * per snap and explode memory. Instead each index keeps its OWN timeline — a list
     * of (snapId, value) records appended only when that index actually CHANGES. The
     * records are naturally sorted by snapId (we only ever append the current id), so
     * get(index, snapId) is a binary search for the last record whose id is <= the
     * queried snapshot: that record's value is what the index held at that time.
     * snap() just hands back the current id and bumps it, touching no per-index data.
     *
     * Time:  set O(log k) to dedupe the latest record for the index; get O(log k);
     *        snap O(1) — where k = number of distinct snapshots that changed the index.
     * Space: O(total writes) — one record per actual value change, not per snap.
     */
    static class Snapshots {
        // One timeline per index: records[i] holds (snapId, value) pairs in id order.
        private final List<List<int[]>> records;
        private int snapId = 0;

        public Snapshots(int length) {
            records = new ArrayList<>(length);
            for (int i = 0; i < length; i++) {
                // Seed every index with value 0 at snapshot 0 so any early get() resolves.
                List<int[]> timeline = new ArrayList<>();
                timeline.add(new int[]{0, 0});
                records.add(timeline);
            }
        }

        public void set(int index, int val) {
            List<int[]> timeline = records.get(index);
            int[] last = timeline.get(timeline.size() - 1);
            // Multiple sets within the same snapshot collapse onto one record.
            if (last[0] == snapId) {
                last[1] = val;
            } else {
                timeline.add(new int[]{snapId, val});
            }
        }

        public int snap() {
            return snapId++;   // return the id just sealed, then advance
        }

        public int get(int index, int snapId) {
            List<int[]> timeline = records.get(index);
            // Find the rightmost record with recorded id <= queried snapId.
            int lo = 0, hi = timeline.size() - 1, ans = 0;
            while (lo <= hi) {
                int mid = (lo + hi) >>> 1;
                if (timeline.get(mid)[0] <= snapId) {
                    ans = timeline.get(mid)[1];
                    lo = mid + 1;
                } else {
                    hi = mid - 1;
                }
            }
            return ans;
        }
    }

    public static void main(String[] args) {
        Snapshots a = new Snapshots(3);
        a.set(0, 5);
        int snap0 = a.snap();                 // snapshot 0 seals value 5 at index 0
        a.set(0, 6);
        System.out.println(a.get(0, snap0));  // expected: 5  (value at snapshot 0, ignoring later set)
        System.out.println(a.get(0, a.snap())); // expected: 6  (snapshot 1 sees the new value)

        Snapshots b = new Snapshots(2);
        System.out.println(b.snap());         // expected: 0  (first snapshot id)
        b.set(1, 7);
        b.set(1, 9);                          // overwrites within the same snapshot window
        System.out.println(b.snap());         // expected: 1
        System.out.println(b.get(1, 1));      // expected: 9  (last write before snapshot 1)
        System.out.println(b.get(0, 0));      // expected: 0  (never set -> seeded default)
        System.out.println(b.get(1, 0));      // expected: 0  (writes happened after snapshot 0)
    }
}
