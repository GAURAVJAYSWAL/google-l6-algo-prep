package org.example.trees;

public class MaximumDepthOfBinaryTree {

    static class TreeNode {
        int val;
        TreeNode left, right;
        TreeNode(int val) { this.val = val; }
        TreeNode(int val, TreeNode left, TreeNode right) {
            this.val = val; this.left = left; this.right = right;
        }
    }

    /**
     * Depth is a pure post-order fold: a node's depth is one more than the deeper of
     * its two children, and an empty subtree contributes 0. There is no cross-subtree
     * interaction, so the single recurrence fully defines the answer at the root.
     * Time:  O(n) — one visit per node.
     * Space: O(h) — recursion stack, h the tree height.
     */
    public int maxDepth(TreeNode root) {
        if (root == null) return 0;
        return 1 + Math.max(maxDepth(root.left), maxDepth(root.right));
    }

    public static void main(String[] args) {
        MaximumDepthOfBinaryTree sol = new MaximumDepthOfBinaryTree();
        TreeNode t1 = new TreeNode(3,
                new TreeNode(9),
                new TreeNode(20, new TreeNode(15), new TreeNode(7)));
        System.out.println(sol.maxDepth(t1));            // expected: 3
        System.out.println(sol.maxDepth(new TreeNode(1, null, new TreeNode(2))));  // expected: 2
        System.out.println(sol.maxDepth(null));          // expected: 0
    }
}
