package org.example.graphs;

import java.util.Arrays;

/**
 * LC 787. Cheapest Flights Within K Stops.
 */
public class CheapestFlightsWithinKStops {

    /**
     * Key insight: "at most K stops" means a path of at most K+1 edges, and Bellman-Ford's i-th
     * relaxation round establishes the cheapest cost reachable using at most i edges. So running
     * exactly K+1 rounds yields the cheapest price under the stop limit. The subtlety: each round
     * must relax from the PREVIOUS round's costs, otherwise a flight relaxed earlier in the same
     * round could chain into another flight and sneak in extra stops. Cloning dist per round
     * enforces the "exactly one more edge this round" boundary.
     *
     * Time:  O(K * E) — K+1 rounds, each scanning every flight once.
     * Space: O(n) — two cost arrays of size n (no adjacency structure needed).
     */
    public static int findCheapestPrice(int n, int[][] flights, int src, int dst, int k) {
        int[] dist = new int[n];
        Arrays.fill(dist, Integer.MAX_VALUE);
        dist[src] = 0;

        // K stops allow K+1 flights, hence K+1 relaxation rounds.
        for (int round = 0; round <= k; round++) {
            int[] next = dist.clone(); // relax off the prior round only, bounding the edge count
            for (int[] f : flights) {
                int from = f[0], to = f[1], price = f[2];
                if (dist[from] == Integer.MAX_VALUE) continue; // source not yet reachable this far
                next[to] = Math.min(next[to], dist[from] + price);
            }
            dist = next;
        }
        return dist[dst] == Integer.MAX_VALUE ? -1 : dist[dst]; // unreachable within the stop limit
    }

    public static void main(String[] args) {
        int[][] f1 = {{0, 1, 100}, {1, 2, 100}, {2, 0, 100}, {1, 3, 600}, {2, 3, 200}};
        System.out.println(findCheapestPrice(4, f1, 0, 3, 1)); // expected: 700 (0->1->3)

        int[][] f2 = {{0, 1, 100}, {1, 2, 100}, {0, 2, 500}};
        System.out.println(findCheapestPrice(3, f2, 0, 2, 1)); // expected: 200 (0->1->2)
        System.out.println(findCheapestPrice(3, f2, 0, 2, 0)); // expected: 500 (only the direct flight fits)

        int[][] f3 = {{0, 1, 50}};
        System.out.println(findCheapestPrice(3, f3, 0, 2, 5)); // expected: -1 (no route to node 2)
    }
}
