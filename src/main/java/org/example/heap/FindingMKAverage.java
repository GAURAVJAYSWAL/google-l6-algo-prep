package org.example.heap;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.TreeMap;

/**
 * LC 1825. Finding MK Average.
 */
public class FindingMKAverage {

    /**
     * The MKAverage looks at only the last m elements, drops the k smallest and k largest,
     * and averages the rest. Keep the window's m values partitioned into three ordered
     * multisets by rank: `bottom` (the k smallest), `mid` (the middle m-2k), and `top` (the
     * k largest). Each multiset caches its element count and running sum, so the answer is
     * simply mid.sum / mid.count in O(1). The only real work is keeping the partition correct
     * as values stream in and age out: insert/delete touches one multiset, then a rebalance
     * shuffles a bounded number of boundary elements across the cuts to restore the rank
     * split. A TreeMap<value,count> acts as the multiset, giving O(log n) min/max access.
     *
     * Invariant (once size == m): bottom holds the k smallest values, top the k largest,
     * mid the rest; bottom.size == k, top.size == k, mid.size == m - 2k. Every value in
     * bottom <= every value in mid <= every value in top.
     *
     * Time:  addElement O(log m) — a few TreeMap ops; calculateMKAverage O(1) — cached sum.
     * Space: O(m) — the stream window plus the three multisets over those m values.
     */
    private final int m;
    private final int k;
    private final Deque<Integer> window = new ArrayDeque<>(); // the last m elements, oldest at head

    private final Multiset bottom = new Multiset(); // k smallest
    private final Multiset mid = new Multiset();     // middle m - 2k
    private final Multiset top = new Multiset();     // k largest

    public FindingMKAverage(int m, int k) {
        this.m = m;
        this.k = k;
    }

    public void addElement(int num) {
        window.offerLast(num);
        // New value provisionally lands in mid; rebalance then settles it into the right tier.
        mid.add(num);

        if (window.size() > m) {
            int old = window.pollFirst();
            removeValue(old); // evict the value that just aged out of the window
        }
        rebalance();
    }

    public int calculateMKAverage() {
        if (window.size() < m) return -1;
        return (int) (mid.sum / mid.count); // integer mean of the middle band
    }

    /** Remove one occurrence of value from whichever tier currently holds it. */
    private void removeValue(int val) {
        // Tiers are ordered, so a value below bottom's max belongs to bottom, above top's min to top.
        if (!bottom.isEmpty() && val <= bottom.max()) {
            bottom.remove(val);
        } else if (!top.isEmpty() && val >= top.min()) {
            top.remove(val);
        } else {
            mid.remove(val);
        }
    }

    /**
     * Restore the rank partition. Done in two phases: first fix sizes (bottom and top must
     * each hold exactly k while the window is full), then fix order across the two cut lines.
     */
    private void rebalance() {
        if (window.size() < m) return; // partition only meaningful once we have m elements

        // --- Phase 1: size correction. Pull from mid up to k in bottom and k in top. ---
        while (bottom.count < k) bottom.add(mid.pollMin());
        while (bottom.count > k) mid.add(bottom.pollMax());
        while (top.count < k) top.add(mid.pollMax());
        while (top.count > k) mid.add(top.pollMin());

        // --- Phase 2: order correction across the bottom|mid and mid|top boundaries. ---
        // If bottom's largest exceeds mid's smallest, they sit on the wrong sides — swap them.
        while (!mid.isEmpty() && bottom.max() > mid.min()) {
            int hi = bottom.pollMax();
            int lo = mid.pollMin();
            bottom.add(lo);
            mid.add(hi);
        }
        // Symmetrically across the mid|top boundary.
        while (!mid.isEmpty() && top.min() < mid.max()) {
            int lo = top.pollMin();
            int hi = mid.pollMax();
            top.add(hi);
            mid.add(lo);
        }
    }

    /** Ordered multiset backed by a value->count TreeMap, caching size and sum. */
    private static final class Multiset {
        private final TreeMap<Integer, Integer> counts = new TreeMap<>();
        long count = 0; // total number of elements (with multiplicity)
        long sum = 0;   // sum of all elements (with multiplicity)

        void add(int val) {
            counts.merge(val, 1, Integer::sum);
            count++;
            sum += val;
        }

        void remove(int val) {
            int c = counts.get(val);
            if (c == 1) counts.remove(val);
            else counts.put(val, c - 1);
            count--;
            sum -= val;
        }

        boolean isEmpty() { return count == 0; }

        int min() { return counts.firstKey(); }

        int max() { return counts.lastKey(); }

        int pollMin() {
            int v = counts.firstKey();
            remove(v);
            return v;
        }

        int pollMax() {
            int v = counts.lastKey();
            remove(v);
            return v;
        }
    }

    public static void main(String[] args) {
        // Example from the problem statement.
        FindingMKAverage obj = new FindingMKAverage(3, 1);
        obj.addElement(3);
        System.out.println(obj.calculateMKAverage()); // expected: -1  (fewer than m elements)
        obj.addElement(1);
        obj.addElement(10);
        System.out.println(obj.calculateMKAverage()); // expected: 3   (drop 1 and 10, mid = {3})
        obj.addElement(5);
        obj.addElement(5);
        obj.addElement(5);
        System.out.println(obj.calculateMKAverage()); // expected: 5   (window {5,5,5}, mid = {5})

        // Larger window: m=5, k=1. After 5 elements drop one min and one max, average the middle 3.
        FindingMKAverage obj2 = new FindingMKAverage(5, 1);
        for (int v : new int[]{1, 2, 3, 4, 5}) obj2.addElement(v);
        System.out.println(obj2.calculateMKAverage()); // expected: 3   (drop 1 and 5, mid = {2,3,4})
        obj2.addElement(100);
        System.out.println(obj2.calculateMKAverage()); // expected: 4   (window {2,3,4,5,100}, drop 2 and 100, mid = {3,4,5})
    }
}
