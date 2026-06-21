package org.example.trees;

public class BalancedBinaryTree {

    static class TreeNode {
        int val;
        TreeNode left, right;
        TreeNode(int val) { this.val = val; }
        TreeNode(int val, TreeNode left, TreeNode right) {
            this.val = val; this.left = left; this.right = right;
        }
    }

    /**
     * Checking balance and measuring height are the same post-order pass: a node is
     * balanced iff both subtrees are balanced AND their heights differ by ≤ 1. We fuse
     * the two by letting the height function return a -1 sentinel meaning "already
     * unbalanced", which then poisons every ancestor and short-circuits the rest of the
     * walk — avoiding the O(n²) of recomputing height at every node.
     * Time:  O(n) — each node's height computed exactly once.
     * Space: O(h) — recursion stack, h the tree height.
     */
    public boolean isBalanced(TreeNode root) {
        return height(root) != -1;
    }

    private int height(TreeNode node) {
        if (node == null) return 0;
        int left = height(node.left);
        if (left == -1) return -1;                        // left subtree already failed
        int right = height(node.right);
        if (right == -1) return -1;
        if (Math.abs(left - right) > 1) return -1;        // this node fails — poison upward
        return 1 + Math.max(left, right);
    }

    public static void main(String[] args) {
        BalancedBinaryTree sol = new BalancedBinaryTree();
        TreeNode t1 = new TreeNode(3,
                new TreeNode(9),
                new TreeNode(20, new TreeNode(15), new TreeNode(7)));
        System.out.println(sol.isBalanced(t1));          // expected: true
        // left-heavy skew of depth 4 on one side, depth 1 on the other
        TreeNode t2 = new TreeNode(1,
                new TreeNode(2,
                        new TreeNode(3, new TreeNode(4), new TreeNode(4)),
                        new TreeNode(3)),
                new TreeNode(2));
        System.out.println(sol.isBalanced(t2));          // expected: false
        System.out.println(sol.isBalanced(null));        // expected: true
    }
}
