package org.example.trees;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class InvertBinaryTree {

    static class TreeNode {
        int val;
        TreeNode left, right;
        TreeNode(int val) { this.val = val; }
        TreeNode(int val, TreeNode left, TreeNode right) {
            this.val = val; this.left = left; this.right = right;
        }
    }

    /**
     * Inverting a tree is mirroring it, and mirroring is structurally recursive: swap a
     * node's two children, then mirror each child. Because the swap commutes with the
     * recursion (invert-then-swap == swap-then-invert), processing order is irrelevant —
     * the whole subtree is correctly mirrored once both children are.
     * Time:  O(n) — one swap per node.
     * Space: O(h) — recursion stack, h the tree height.
     */
    public TreeNode invertTree(TreeNode root) {
        if (root == null) return null;
        TreeNode left = invertTree(root.left);            // mirror each side first
        TreeNode right = invertTree(root.right);
        root.left = right;                                // then swap the mirrored sides
        root.right = left;
        return root;
    }

    private static List<Integer> bfs(TreeNode root) {
        List<Integer> order = new ArrayList<>();
        if (root == null) return order;
        Queue<TreeNode> q = new LinkedList<>();
        q.add(root);
        while (!q.isEmpty()) {
            TreeNode n = q.poll();
            order.add(n.val);
            if (n.left != null) q.add(n.left);
            if (n.right != null) q.add(n.right);
        }
        return order;
    }

    public static void main(String[] args) {
        InvertBinaryTree sol = new InvertBinaryTree();
        TreeNode t1 = new TreeNode(4,
                new TreeNode(2, new TreeNode(1), new TreeNode(3)),
                new TreeNode(7, new TreeNode(6), new TreeNode(9)));
        System.out.println(bfs(sol.invertTree(t1)));     // expected: [4, 7, 2, 9, 6, 3, 1]
        TreeNode t2 = new TreeNode(2, new TreeNode(1), new TreeNode(3));
        System.out.println(bfs(sol.invertTree(t2)));     // expected: [2, 3, 1]
        System.out.println(bfs(sol.invertTree(null)));   // expected: []
    }
}
