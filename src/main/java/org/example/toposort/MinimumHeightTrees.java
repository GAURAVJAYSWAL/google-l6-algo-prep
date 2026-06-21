package org.example.toposort;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.List;

/**
 * LC 310. Minimum Height Trees.
 */
public class MinimumHeightTrees {

    /**
     * Key insight: rooting at a graph centroid minimises tree height, and a tree has at most two
     * centroids. We find them by topological leaf-trimming: every node of degree 1 is a leaf that
     * can only worsen height, so strip all current leaves layer by layer (like peeling an onion).
     * Whatever survives when 2 or fewer nodes remain are exactly the 1 or 2 centroids.
     *
     * Time:  O(n) — each node and undirected edge is removed once during trimming.
     * Space: O(n) — adjacency lists, the degree array, and the leaf frontier.
     */
    public static List<Integer> findMinHeightTrees(int n, int[][] edges) {
        if (n == 1) return List.of(0);          // single node is its own centroid; it has no edges
        if (n == 2) return List.of(0, 1);       // both endpoints of the lone edge are centroids

        List<List<Integer>> adj = new ArrayList<>();
        for (int i = 0; i < n; i++) adj.add(new ArrayList<>());
        int[] degree = new int[n];
        for (int[] e : edges) {
            adj.get(e[0]).add(e[1]);
            adj.get(e[1]).add(e[0]);
            degree[e[0]]++;
            degree[e[1]]++;
        }

        Deque<Integer> leaves = new ArrayDeque<>();
        for (int i = 0; i < n; i++) {
            if (degree[i] == 1) leaves.offer(i); // current outermost layer
        }

        int remaining = n;
        while (remaining > 2) {
            int layerSize = leaves.size();       // trim a whole layer before recomputing
            remaining -= layerSize;
            for (int i = 0; i < layerSize; i++) {
                int leaf = leaves.poll();
                for (int next : adj.get(leaf)) {
                    if (--degree[next] == 1) leaves.offer(next); // neighbour becomes a new leaf
                }
            }
        }

        return new ArrayList<>(leaves);          // the 1 or 2 surviving centroids
    }

    public static void main(String[] args) {
        System.out.println(findMinHeightTrees(4, new int[][]{{1, 0}, {1, 2}, {1, 3}}));
        // expected: [1] (star centred on 1)
        System.out.println(findMinHeightTrees(6, new int[][]{{3, 0}, {3, 1}, {3, 2}, {3, 4}, {5, 4}}));
        // expected: [3, 4] (two centroids)
        System.out.println(findMinHeightTrees(1, new int[][]{}));
        // expected: [0]
        System.out.println(findMinHeightTrees(2, new int[][]{{0, 1}}));
        // expected: [0, 1]
    }
}
