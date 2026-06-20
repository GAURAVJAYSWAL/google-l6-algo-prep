package org.example.design;

import java.util.HashMap;
import java.util.Map;

/**
 * LC 2013. Detect Squares.
 */
public class DetectSquares {

    /**
     * Key insight: an axis-aligned square is fixed once we pick the query point and a
     * second point on the SAME vertical line (same x); the side length is the y-gap,
     * which then forces the two remaining corners. So we only iterate candidates that
     * share the query's x. Counting duplicate points with a multiset lets a query
     * multiply the three other corners' counts — that product is exactly how many
     * distinct squares those repeated points form.
     *
     * Time:  add O(1); count O(K) where K = points sharing the query's x-coordinate.
     * Space: O(N) — one multiset entry per distinct point added.
     */
    static class Squares {
        // Encode (x, y) into one long key; values up to 1000 fit comfortably.
        private final Map<Long, Integer> pointCount = new HashMap<>();
        // For each x, the multiset of y values present, so we can scan a column fast.
        private final Map<Integer, Map<Integer, Integer>> byColumn = new HashMap<>();

        public Squares() {
        }

        public void add(int[] point) {
            int x = point[0], y = point[1];
            pointCount.merge(key(x, y), 1, Integer::sum);
            byColumn.computeIfAbsent(x, k -> new HashMap<>()).merge(y, 1, Integer::sum);
        }

        public int count(int[] point) {
            int qx = point[0], qy = point[1];
            Map<Integer, Integer> column = byColumn.get(qx);
            if (column == null) return 0;

            int total = 0;
            for (Map.Entry<Integer, Integer> e : column.entrySet()) {
                int y = e.getKey();
                if (y == qy) continue;            // need a genuine vertical side
                int side = y - qy;                // signed side length picks one square
                int cntVertical = e.getValue();
                // The two far corners sit one side-length left and right of the column.
                int left = pointCount.getOrDefault(key(qx - side, qy), 0)
                        * pointCount.getOrDefault(key(qx - side, y), 0);
                int right = pointCount.getOrDefault(key(qx + side, qy), 0)
                        * pointCount.getOrDefault(key(qx + side, y), 0);
                total += cntVertical * (left + right);
            }
            return total;
        }

        private static long key(int x, int y) {
            return ((long) x << 20) | (y & 0xFFFFF);
        }
    }

    public static void main(String[] args) {
        Squares sq = new Squares();
        sq.add(new int[]{3, 10});
        sq.add(new int[]{11, 2});
        sq.add(new int[]{3, 2});
        System.out.println(sq.count(new int[]{11, 10}));  // expected: 1
        System.out.println(sq.count(new int[]{14, 8}));   // expected: 0 (no matching corners)
        sq.add(new int[]{11, 2});                          // duplicate point
        System.out.println(sq.count(new int[]{11, 10}));  // expected: 2 (duplicate doubles it)

        Squares sq2 = new Squares();
        sq2.add(new int[]{0, 0});
        sq2.add(new int[]{0, 5});
        sq2.add(new int[]{5, 0});
        sq2.add(new int[]{5, 5});
        System.out.println(sq2.count(new int[]{0, 0}));   // expected: 1 (unit-free square via the other 3)
    }
}
