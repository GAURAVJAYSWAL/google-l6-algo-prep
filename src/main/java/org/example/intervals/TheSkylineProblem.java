package org.example.intervals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TreeMap;

public class TheSkylineProblem {

    /**
     * Sweep a vertical line across all critical x-coordinates (the left and right edges
     * of buildings). Maintain a multiset of currently-active heights; a left edge inserts
     * its height, a right edge removes it. A key point is emitted whenever the tallest
     * active height changes after processing an x — that new max is the skyline's height
     * there. Encoding left edges as negative heights and sorting by (x, height) guarantees
     * that at a shared x all starts are processed before ends and taller starts first,
     * which prevents spurious points. A TreeMap serves as the height multiset so the
     * current max is its last key in O(log n).
     * Time:  O(n log n) — sort plus a log-time multiset op per edge.
     * Space: O(n) — the edge list and the active-height multiset.
     */
    public static List<List<Integer>> getSkyline(int[][] buildings) {
        // Edge = {x, signedHeight}: start heights are stored negative to sort them first.
        int[][] edges = new int[buildings.length * 2][2];
        int k = 0;
        for (int[] b : buildings) {
            edges[k++] = new int[]{b[0], -b[2]};   // left edge (start)
            edges[k++] = new int[]{b[1], b[2]};    // right edge (end)
        }
        Arrays.sort(edges, (a, b) -> a[0] != b[0] ? Integer.compare(a[0], b[0])
                                                  : Integer.compare(a[1], b[1]));

        List<List<Integer>> result = new ArrayList<>();
        TreeMap<Integer, Integer> active = new TreeMap<>(); // height -> count (multiset)
        active.put(0, 1);                                   // ground level keeps the max well-defined
        int prevMax = 0;

        for (int[] e : edges) {
            int x = e[0], h = e[1];
            if (h < 0) {
                active.merge(-h, 1, Integer::sum);          // building starts: add its height
            } else {
                int c = active.get(h);                      // building ends: drop one copy
                if (c == 1) active.remove(h); else active.put(h, c - 1);
            }
            int curMax = active.lastKey();
            if (curMax != prevMax) {                        // tallest changed: this x is a key point
                result.add(List.of(x, curMax));
                prevMax = curMax;
            }
        }
        return result;
    }

    public static void main(String[] args) {
        System.out.println(getSkyline(new int[][]{{2, 9, 10}, {3, 7, 15}, {5, 12, 12}, {15, 20, 10}, {19, 24, 8}}));
        // expected: [[2, 10], [3, 15], [7, 12], [12, 0], [15, 10], [20, 8], [24, 0]]
        System.out.println(getSkyline(new int[][]{{0, 2, 3}, {2, 5, 3}}));
        // expected: [[0, 3], [5, 0]]  (adjacent equal-height buildings merge)
        System.out.println(getSkyline(new int[][]{{1, 2, 1}, {1, 2, 2}, {1, 2, 3}}));
        // expected: [[1, 3], [2, 0]]  (same span, only tallest shows)
        System.out.println(getSkyline(new int[][]{{0, 5, 7}}));
        // expected: [[0, 7], [5, 0]]
    }
}
