package org.example.advancedgraphs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.PriorityQueue;

/**
 * LC 743. Network Delay Time.
 */
public class NetworkDelayTime {

    /**
     * Key insight: the time for the signal to reach a node is the shortest-path distance
     * from source k under non-negative edge weights, so Dijkstra computes every node's
     * arrival time. All nodes are reached exactly when the network is fully covered, and
     * the slowest of those shortest arrivals is the answer; if any node is unreachable the
     * signal never fully propagates, so we return -1.
     *
     * Time:  O(E log V) — each edge relaxes at most once, each pop/push is O(log V).
     * Space: O(V + E) — adjacency lists plus the distance array and heap.
     */
    public static int networkDelayTime(int[][] times, int n, int k) {
        List<int[]>[] adj = new List[n + 1]; // 1-indexed nodes; index 0 unused
        for (int i = 1; i <= n; i++) adj[i] = new ArrayList<>();
        for (int[] t : times) adj[t[0]].add(new int[]{t[1], t[2]}); // u -> v with weight w

        int[] dist = new int[n + 1];
        Arrays.fill(dist, Integer.MAX_VALUE);
        dist[k] = 0;

        // Min-heap keyed on tentative distance: always settle the closest unsettled node next.
        PriorityQueue<int[]> pq = new PriorityQueue<>((a, b) -> Integer.compare(a[1], b[1]));
        pq.offer(new int[]{k, 0}); // {node, dist}

        while (!pq.isEmpty()) {
            int[] cur = pq.poll();
            int node = cur[0], d = cur[1];
            if (d > dist[node]) continue; // stale entry — a shorter path already settled this node
            for (int[] edge : adj[node]) {
                int next = edge[0], nd = d + edge[1];
                if (nd < dist[next]) {
                    dist[next] = nd;
                    pq.offer(new int[]{next, nd});
                }
            }
        }

        int max = 0;
        for (int i = 1; i <= n; i++) {
            if (dist[i] == Integer.MAX_VALUE) return -1; // node i never receives the signal
            max = Math.max(max, dist[i]);
        }
        return max; // the last node to receive it bounds the total delay
    }

    public static void main(String[] args) {
        System.out.println(networkDelayTime(
                new int[][]{{2, 1, 1}, {2, 3, 1}, {3, 4, 1}}, 4, 2)); // expected: 2
        System.out.println(networkDelayTime(new int[][]{{1, 2, 1}}, 2, 1)); // expected: 1
        System.out.println(networkDelayTime(new int[][]{{1, 2, 1}}, 2, 2)); // expected: -1 (node 1 unreachable from 2)
        System.out.println(networkDelayTime(
                new int[][]{{1, 2, 1}, {2, 3, 2}, {1, 3, 4}}, 3, 1)); // expected: 3 (1->2->3 = 3 beats 1->3 = 4)
    }
}
