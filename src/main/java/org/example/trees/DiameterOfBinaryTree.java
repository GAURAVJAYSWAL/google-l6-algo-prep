package org.example.trees;

public class DiameterOfBinaryTree {

    static class TreeNode {
        int val;
        TreeNode left, right;
        TreeNode(int val) { this.val = val; }
        TreeNode(int val, TreeNode left, TreeNode right) {
            this.val = val; this.left = left; this.right = right;
        }
    }

    private int best = 0;

    /**
     * The longest path between any two nodes "turns" at a single highest node; there it
     * spans the full depth on both sides, i.e. leftHeight + rightHeight edges. So one
     * post-order pass computes each node's height and, in passing, folds that turning
     * width into a global max — the diameter is the largest such width over all nodes.
     * Time:  O(n) — one visit per node.
     * Space: O(h) — recursion stack, h the tree height.
     */
    public int diameterOfBinaryTree(TreeNode root) {
        best = 0;
        height(root);
        return best;
    }

    private int height(TreeNode node) {
        if (node == null) return 0;
        int left = height(node.left);
        int right = height(node.right);
        best = Math.max(best, left + right);              // path through this node, in edges
        return 1 + Math.max(left, right);
    }

    public static void main(String[] args) {
        DiameterOfBinaryTree sol = new DiameterOfBinaryTree();
        TreeNode t1 = new TreeNode(1,
                new TreeNode(2, new TreeNode(4), new TreeNode(5)),
                new TreeNode(3));
        System.out.println(sol.diameterOfBinaryTree(t1));   // expected: 3
        System.out.println(sol.diameterOfBinaryTree(new TreeNode(1, new TreeNode(2), null)));  // expected: 1
        System.out.println(sol.diameterOfBinaryTree(new TreeNode(1)));  // expected: 0
    }
}
