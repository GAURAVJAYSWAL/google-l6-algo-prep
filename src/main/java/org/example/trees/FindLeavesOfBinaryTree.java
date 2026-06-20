package org.example.trees;

import java.util.ArrayList;
import java.util.List;

public class FindLeavesOfBinaryTree {

    static class TreeNode {
        int val;
        TreeNode left, right;
        TreeNode(int val) { this.val = val; }
        TreeNode(int val, TreeNode left, TreeNode right) {
            this.val = val; this.left = left; this.right = right;
        }
    }

    /**
     * The order in which repeated leaf-stripping removes a node equals its height from
     * the bottom: a node leaves on round h = 1 + max(child heights), with leaves at
     * height 0. Computing that height bottom-up in one post-order pass lets us bucket
     * each node by its removal round directly, avoiding the naive O(n^2) re-pruning.
     * Time:  O(n) — single post-order visit per node.
     * Space: O(h) — recursion stack (output excluded), h the tree height.
     */
    public List<List<Integer>> findLeaves(TreeNode root) {
        List<List<Integer>> result = new ArrayList<>();
        height(root, result);
        return result;
    }

    private int height(TreeNode node, List<List<Integer>> result) {
        if (node == null) return -1;                     // so a leaf computes height 0
        int h = 1 + Math.max(height(node.left, result), height(node.right, result));
        if (h == result.size()) result.add(new ArrayList<>());  // first node seen at this height
        result.get(h).add(node.val);                     // bucket node by its removal round
        return h;
    }

    public static void main(String[] args) {
        FindLeavesOfBinaryTree sol = new FindLeavesOfBinaryTree();

        // [1,2,3,4,5]
        TreeNode t1 = new TreeNode(1,
                new TreeNode(2, new TreeNode(4), new TreeNode(5)),
                new TreeNode(3));
        System.out.println(sol.findLeaves(t1));        // expected: [[4, 5, 3], [2], [1]]

        System.out.println(sol.findLeaves(new TreeNode(1)));  // expected: [[1]]

        // [1,2,null,3] — a left-leaning chain
        TreeNode t3 = new TreeNode(1, new TreeNode(2, new TreeNode(3), null), null);
        System.out.println(sol.findLeaves(t3));        // expected: [[3], [2], [1]]
    }
}
