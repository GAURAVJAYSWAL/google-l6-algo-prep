package org.example.treedp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * LC 834. Sum of Distances in Tree.
 */
public class SumOfDistancesInTree {

    /**
     * Rerooting in two DFS passes. Pass 1 (post-order from node 0) computes count[v] =
     * subtree size and ans[0] = sum of distances from the root, accumulating
     * ans[0] += ans[child] + count[child] (every subtree node is one edge farther).
     * Pass 2 (pre-order) shifts the root from a parent to a child: the count[child] nodes
     * below move one step closer and the other n-count[child] nodes move one step farther,
     * so ans[child] = ans[parent] - count[child] + (n - count[child]).
     * Time:  O(n) — two linear DFS traversals over a tree of n-1 edges.
     * Space: O(n) — adjacency lists, count/ans arrays, and recursion stack.
     */
    public static int[] sumOfDistancesInTree(int n, int[][] edges) {
        List<List<Integer>> adj = new ArrayList<>();
        for (int i = 0; i < n; i++) adj.add(new ArrayList<>());
        for (int[] e : edges) {                             // undirected tree edges
            adj.get(e[0]).add(e[1]);
            adj.get(e[1]).add(e[0]);
        }
        int[] count = new int[n];                           // count[v] = size of v's subtree
        int[] ans = new int[n];
        if (n == 1) return ans;                             // single node: distance 0

        postOrder(0, -1, adj, count, ans);                  // fills count + ans[0]
        preOrder(0, -1, n, adj, count, ans);                // reroots to every other node
        return ans;
    }

    private static void postOrder(int node, int parent, List<List<Integer>> adj, int[] count, int[] ans) {
        count[node] = 1;
        for (int child : adj.get(node)) {
            if (child == parent) continue;
            postOrder(child, node, adj, count, ans);
            count[node] += count[child];
            ans[node] += ans[child] + count[child];         // child's subtree sits one edge deeper
        }
    }

    private static void preOrder(int node, int parent, int n, List<List<Integer>> adj, int[] count, int[] ans) {
        for (int child : adj.get(node)) {
            if (child == parent) continue;
            // count[child] nodes get nearer by 1; the remaining n-count[child] get farther by 1
            ans[child] = ans[node] - count[child] + (n - count[child]);
            preOrder(child, node, n, adj, count, ans);
        }
    }

    public static void main(String[] args) {
        System.out.println(Arrays.toString(
                sumOfDistancesInTree(6, new int[][]{{0, 1}, {0, 2}, {2, 3}, {2, 4}, {2, 5}})));
        // expected: [8, 12, 6, 10, 10, 10]

        System.out.println(Arrays.toString(
                sumOfDistancesInTree(1, new int[][]{})));     // expected: [0]

        System.out.println(Arrays.toString(
                sumOfDistancesInTree(2, new int[][]{{1, 0}})));  // expected: [1, 1]

        System.out.println(Arrays.toString(
                sumOfDistancesInTree(3, new int[][]{{0, 1}, {1, 2}})));  // expected: [3, 2, 3]
    }
}
