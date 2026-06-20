package org.example.advancedgraphs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * LC 1192. Critical Connections in a Network.
 */
public class CriticalConnections {

    /**
     * Key insight: a critical connection is a bridge — an edge whose removal disconnects the
     * graph. Tarjan's DFS assigns each node a discovery time disc[u] and a low-link low[u],
     * the smallest disc reachable from u's subtree via tree edges plus at most one back edge.
     * Edge (u, v) with v a child is a bridge iff low[v] > disc[u]: v's subtree has no back
     * edge climbing to u or above, so that edge is the subtree's only link to the rest.
     *
     * Time:  O(V + E) — one DFS visiting every node and edge once.
     * Space: O(V + E) — adjacency lists, the disc/low arrays, and the recursion stack.
     */
    public static List<List<Integer>> criticalConnections(int n, List<List<Integer>> connections) {
        List<Integer>[] adj = new List[n];
        for (int i = 0; i < n; i++) adj[i] = new ArrayList<>();
        for (List<Integer> c : connections) {
            adj[c.get(0)].add(c.get(1));
            adj[c.get(1)].add(c.get(0));
        }

        int[] disc = new int[n], low = new int[n];
        Arrays.fill(disc, -1); // -1 marks an unvisited node
        List<List<Integer>> bridges = new ArrayList<>();
        int[] timer = {0};

        for (int i = 0; i < n; i++) {
            if (disc[i] == -1) dfs(i, -1, adj, disc, low, timer, bridges);
        }
        return bridges;
    }

    private static void dfs(int u, int parent, List<Integer>[] adj,
                            int[] disc, int[] low, int[] timer, List<List<Integer>> bridges) {
        disc[u] = low[u] = timer[0]++;
        boolean skippedParent = false; // handle parallel edges: only ignore the parent edge once
        for (int v : adj[u]) {
            if (v == parent && !skippedParent) {
                skippedParent = true; // a second u-parent edge is a real back edge, not the tree edge
                continue;
            }
            if (disc[v] == -1) {
                dfs(v, u, adj, disc, low, timer, bridges);
                low[u] = Math.min(low[u], low[v]); // pull up the child's best reach
                if (low[v] > disc[u]) {            // child subtree cannot climb back to u -> bridge
                    bridges.add(List.of(u, v));
                }
            } else {
                low[u] = Math.min(low[u], disc[v]); // back edge: climb toward an ancestor
            }
        }
    }

    public static void main(String[] args) {
        System.out.println(criticalConnections(4, List.of(
                List.of(0, 1), List.of(1, 2), List.of(2, 0), List.of(1, 3))));
        // expected: [[1, 3]]  (the cycle 0-1-2 has no bridge; edge 1-3 is the only critical one)

        System.out.println(criticalConnections(2, List.of(List.of(0, 1))));
        // expected: [[0, 1]]  (the single edge is a bridge)

        System.out.println(criticalConnections(6, List.of(
                List.of(0, 1), List.of(1, 2), List.of(2, 0),
                List.of(1, 3), List.of(3, 4), List.of(4, 5), List.of(5, 3))));
        // expected: [[1, 3]]  (two triangles joined by the single edge 1-3)

        System.out.println(criticalConnections(3, List.of(
                List.of(0, 1), List.of(1, 2), List.of(2, 0))));
        // expected: []  (a pure cycle has no bridges)
    }
}
