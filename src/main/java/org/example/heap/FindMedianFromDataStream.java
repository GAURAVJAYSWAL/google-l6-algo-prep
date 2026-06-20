package org.example.heap;

import java.util.PriorityQueue;

/**
 * LC 295. Find Median from Data Stream.
 */
public class FindMedianFromDataStream {

    /**
     * Split the stream into two halves by value: a max-heap holds the smaller half (its top
     * is the largest of the lows) and a min-heap holds the larger half (its top is the
     * smallest of the highs). Keeping the two tops adjacent in sorted order means the median
     * is either the larger heap's top (odd count) or the average of both tops (even count).
     * The size invariant low.size() == high.size() or low.size() == high.size()+1 makes the
     * median an O(1) read after each O(log n) insert.
     *
     * Time:  addNum O(log n) — one or two heap ops; findMedian O(1) — just inspect tops.
     * Space: O(n) — every element lives in exactly one of the two heaps.
     */
    private final PriorityQueue<Integer> low;   // max-heap: smaller half
    private final PriorityQueue<Integer> high;  // min-heap: larger half

    public FindMedianFromDataStream() {
        low = new PriorityQueue<>((a, b) -> Integer.compare(b, a)); // reverse order = max-heap
        high = new PriorityQueue<>();                               // natural order = min-heap
    }

    public void addNum(int num) {
        // Route into low, then hand low's max to high so high holds only the genuinely larger values.
        low.offer(num);
        high.offer(low.poll());
        // Rebalance: low is allowed to be at most one larger than high.
        if (high.size() > low.size()) {
            low.offer(high.poll());
        }
    }

    public double findMedian() {
        if (low.size() > high.size()) {
            return low.peek();                          // odd count: the extra element is the middle
        }
        return (low.peek() + high.peek()) / 2.0;        // even count: average the two middles
    }

    public static void main(String[] args) {
        FindMedianFromDataStream m = new FindMedianFromDataStream();
        m.addNum(1);
        m.addNum(2);
        System.out.println(m.findMedian()); // expected: 1.5
        m.addNum(3);
        System.out.println(m.findMedian()); // expected: 2.0

        FindMedianFromDataStream m2 = new FindMedianFromDataStream();
        m2.addNum(-1);
        System.out.println(m2.findMedian()); // expected: -1.0
        m2.addNum(-2);
        System.out.println(m2.findMedian()); // expected: -1.5
        m2.addNum(-3);
        System.out.println(m2.findMedian()); // expected: -2.0
        m2.addNum(-4);
        System.out.println(m2.findMedian()); // expected: -2.5
    }
}
