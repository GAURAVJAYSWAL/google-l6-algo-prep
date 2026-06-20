package org.example.orderedset;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

/**
 * LC 480. Sliding Window Median.
 */
public class SlidingWindowMedian {

    /**
     * Key insight: maintain the window as two heaps — a max-heap (low, smaller half) and a min-heap
     * (high, larger half) — so the median is low's top (odd k) or the average of both tops (even k).
     * Heaps cannot remove an arbitrary leaving element in O(log k), so we use LAZY deletion: record
     * the outgoing value in a delayed-removal map and only physically discard it once it surfaces to
     * a heap top. Effective sizes (raw size minus pending deletions on that heap) drive rebalancing.
     * Median averaging is done as low + (high-low)/2 in long to dodge int overflow on large values.
     *
     * Time:  O(n log k) — each slide is O(log k) heap work plus amortized lazy cleanup.
     * Space: O(k) — heaps and the pending-deletion map track only window-sized data.
     */
    public static double[] medianSlidingWindow(int[] nums, int k) {
        PriorityQueue<Integer> low = new PriorityQueue<>(Comparator.reverseOrder()); // max-heap: smaller half
        PriorityQueue<Integer> high = new PriorityQueue<>();                          // min-heap: larger half
        Map<Integer, Integer> pending = new HashMap<>();                              // value -> times marked for removal

        double[] result = new double[nums.length - k + 1];
        int lowSize = 0, highSize = 0;   // logical sizes, excluding still-queued ghost entries

        for (int i = 0; i < nums.length; i++) {
            // --- insert nums[i] into the correct half ---
            if (low.isEmpty() || nums[i] <= low.peek()) {
                low.offer(nums[i]);
                lowSize++;
            } else {
                high.offer(nums[i]);
                highSize++;
            }

            // --- remove the element leaving the window (lazily) ---
            if (i >= k) {
                int out = nums[i - k];
                pending.merge(out, 1, Integer::sum);
                if (out <= low.peek()) lowSize--; else highSize--;   // compare against current median boundary
            }

            // --- rebalance so low has lowSize == highSize or highSize+1 ---
            if (lowSize > highSize + 1) {
                high.offer(low.poll());
                lowSize--; highSize++;
            } else if (lowSize < highSize) {
                low.offer(high.poll());
                lowSize++; highSize--;
            }

            // Top-of-heap may be a ghost after a poll/insert; purge before any peek that matters.
            prune(low, pending);
            prune(high, pending);

            if (i >= k - 1) {
                result[i - k + 1] = median(low, high, lowSize, highSize);
            }
        }
        return result;
    }

    // Average without overflow: low + half the gap, both tops being valid (heaps pruned beforehand).
    private static double median(PriorityQueue<Integer> low, PriorityQueue<Integer> high,
                                 int lowSize, int highSize) {
        if (lowSize > highSize) {
            return low.peek();
        }
        long a = low.peek(), b = high.peek();
        return a + (b - a) / 2.0;   // == (a+b)/2 but never overflows int range
    }

    // Discards values sitting at the heap top that have outstanding deletions.
    private static void prune(PriorityQueue<Integer> heap, Map<Integer, Integer> pending) {
        while (!heap.isEmpty()) {
            int top = heap.peek();
            Integer cnt = pending.get(top);
            if (cnt == null) break;
            heap.poll();
            if (cnt == 1) pending.remove(top); else pending.put(top, cnt - 1);
        }
    }

    public static void main(String[] args) {
        System.out.println(java.util.Arrays.toString(
                medianSlidingWindow(new int[]{1, 3, -1, -3, 5, 3, 6, 7}, 3)));
        // expected: [1.0, -1.0, -1.0, 3.0, 5.0, 6.0]
        System.out.println(java.util.Arrays.toString(
                medianSlidingWindow(new int[]{1, 2, 3, 4, 2, 3, 1, 4, 2}, 3)));
        // expected: [2.0, 3.0, 3.0, 3.0, 2.0, 3.0, 2.0]
        System.out.println(java.util.Arrays.toString(
                medianSlidingWindow(new int[]{1, 4, 2, 3}, 4)));
        // expected: [2.5]
        System.out.println(java.util.Arrays.toString(
                medianSlidingWindow(new int[]{2147483647, 2147483647}, 2)));
        // expected: [2.147483647E9]  (no int overflow on the average)
    }
}
