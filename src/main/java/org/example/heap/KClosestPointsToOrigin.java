package org.example.heap;

import java.util.Arrays;
import java.util.PriorityQueue;

public class KClosestPointsToOrigin {

    /**
     * Keep a max-heap of the k closest points seen so far, keyed on squared distance (no
     * sqrt needed — it is monotonic, so comparisons are exact and integer-cheap). For each
     * new point, if it is nearer than the current farthest (heap top) we swap it in. The
     * heap stays at size k, so we never sort all n points. (Quickselect alternative:
     * partition around the k-th smallest distance for O(n) average time, but it mutates the
     * array and degrades to O(n^2) on bad pivots; the heap gives a stable O(n log k).)
     *
     * Time:  O(n log k) — n points, each an O(log k) heap op.
     * Space: O(k) — the heap holds at most k points.
     */
    public static int[][] kClosest(int[][] points, int k) {
        // Max-heap by squared distance: the worst (farthest) of the current best k sits on top.
        PriorityQueue<int[]> heap = new PriorityQueue<>((a, b) -> Long.compare(dist(b), dist(a)));
        for (int[] p : points) {
            heap.offer(p);
            if (heap.size() > k) {
                heap.poll();   // evict the farthest — it cannot be in the closest k
            }
        }
        int[][] result = new int[k][];
        for (int i = 0; i < k; i++) result[i] = heap.poll();
        return result;
    }

    private static long dist(int[] p) {
        return (long) p[0] * p[0] + (long) p[1] * p[1]; // squared distance; long guards against overflow
    }

    public static void main(String[] args) {
        System.out.println(Arrays.deepToString(kClosest(new int[][]{{1, 3}, {-2, 2}}, 1)));
        // expected: [[-2, 2]]
        System.out.println(Arrays.deepToString(kClosest(new int[][]{{3, 3}, {5, -1}, {-2, 4}}, 2)));
        // expected: [[3, 3], [-2, 4]]   (order within the k may vary)
        System.out.println(Arrays.deepToString(kClosest(new int[][]{{0, 0}}, 1)));
        // expected: [[0, 0]]
        System.out.println(Arrays.deepToString(kClosest(new int[][]{{1, 1}, {2, 2}, {3, 3}}, 3)));
        // expected: [[1, 1], [2, 2], [3, 3]]   (all points, order may vary)
    }
}
