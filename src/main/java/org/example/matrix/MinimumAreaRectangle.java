package org.example.matrix;

import java.util.HashSet;
import java.util.Set;

public class MinimumAreaRectangle {

    /**
     * For an axis-aligned rectangle, any two points that differ in BOTH x and y can
     * serve as a diagonal; the rectangle is then determined and its remaining two
     * corners are (p1.x, p2.y) and (p2.x, p1.y). So hash every point, try each
     * unordered pair as a diagonal, and if both implied corners are present, the area
     * |dx|*|dy| is a candidate. Encoding a point as x*40001+y gives O(1) membership.
     * Time:  O(n^2) — every pair of points is examined, each check O(1).
     * Space: O(n)   — the hash set of points.
     */
    public static int minAreaRect(int[][] points) {
        Set<Integer> seen = new HashSet<>();
        for (int[] p : points) seen.add(encode(p[0], p[1]));

        int best = Integer.MAX_VALUE;
        for (int i = 0; i < points.length; i++) {
            for (int j = i + 1; j < points.length; j++) {
                int x1 = points[i][0], y1 = points[i][1];
                int x2 = points[j][0], y2 = points[j][1];
                if (x1 == x2 || y1 == y2) continue;      // need a true diagonal, not an edge
                // The other two corners share one coordinate with each diagonal endpoint.
                if (seen.contains(encode(x1, y2)) && seen.contains(encode(x2, y1))) {
                    best = Math.min(best, Math.abs(x1 - x2) * Math.abs(y1 - y2));
                }
            }
        }
        return best == Integer.MAX_VALUE ? 0 : best;
    }

    // Coordinates are in [0, 40000]; 40001 as a radix makes the encoding collision-free.
    private static int encode(int x, int y) {
        return x * 40001 + y;
    }

    public static void main(String[] args) {
        System.out.println(minAreaRect(new int[][]{{1, 1}, {1, 3}, {3, 1}, {3, 3}, {2, 2}}));
        // expected: 4

        System.out.println(minAreaRect(new int[][]{{1, 1}, {1, 3}, {3, 1}, {3, 3}, {4, 1}, {4, 3}}));
        // expected: 2

        System.out.println(minAreaRect(new int[][]{{0, 0}, {1, 1}}));
        // expected: 0 (no rectangle possible)
    }
}
