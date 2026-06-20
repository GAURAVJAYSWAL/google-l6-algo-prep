package org.example.mst;

/**
 * LC 1584. Min Cost to Connect All Points.
 */
public class MinCostToConnectAllPoints {

    /**
     * Key insight: connecting all points with minimum total wire is a Minimum Spanning Tree on the
     * complete graph whose edge weights are pairwise Manhattan distances. Because that graph is dense
     * (E = n^2), Prim's algorithm with a plain "minimum-distance-to-tree" array beats a binary heap:
     * each of the n iterations scans the array to pull the closest unattached point (O(n)) and then
     * relaxes every remaining point against the newcomer (O(n)), giving O(n^2) with no heap overhead.
     * minDist[j] always holds the cheapest single edge linking point j to the current tree.
     *
     * A heap variant (lazy Prim or Kruskal with union-find over all n^2 edges) costs O(n^2 log n)
     * here, so it is strictly worse for the dense implicit graph — the array form is the right call.
     *
     * Time:  O(n^2) — n rounds, each doing a linear extract-min and a linear relaxation pass.
     * Space: O(n) — the minDist array and the inTree marker.
     */
    public static int minCostConnectPoints(int[][] points) {
        int n = points.length;
        if (n <= 1) return 0;

        int[] minDist = new int[n];          // cheapest edge from point i to the growing tree
        boolean[] inTree = new boolean[n];
        java.util.Arrays.fill(minDist, Integer.MAX_VALUE);
        minDist[0] = 0;                      // seed the tree with point 0
        int total = 0;

        for (int iter = 0; iter < n; iter++) {
            // Extract the unattached point closest to the tree (linear scan — dense graph).
            int u = -1;
            for (int j = 0; j < n; j++) {
                if (!inTree[j] && (u == -1 || minDist[j] < minDist[u])) u = j;
            }
            inTree[u] = true;
            total += minDist[u];

            // Relax every remaining point against the newly attached u.
            for (int v = 0; v < n; v++) {
                if (!inTree[v]) {
                    int d = Math.abs(points[u][0] - points[v][0]) + Math.abs(points[u][1] - points[v][1]);
                    if (d < minDist[v]) minDist[v] = d;
                }
            }
        }
        return total;
    }

    public static void main(String[] args) {
        int[][] p1 = {{0, 0}, {2, 2}, {3, 10}, {5, 2}, {7, 0}};
        System.out.println(minCostConnectPoints(p1)); // expected: 20

        int[][] p2 = {{3, 12}, {-2, 5}, {-4, 1}};
        System.out.println(minCostConnectPoints(p2)); // expected: 18

        int[][] p3 = {{0, 0}};
        System.out.println(minCostConnectPoints(p3)); // expected: 0 (single point, nothing to connect)

        int[][] p4 = {{0, 0}, {1, 1}, {1, 0}, {-1, 1}};
        System.out.println(minCostConnectPoints(p4)); // expected: 4
    }
}
