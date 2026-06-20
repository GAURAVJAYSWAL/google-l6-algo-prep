package org.example.trees;

public class BinaryTreeMaximumPathSum {

    static class TreeNode {
        int val;
        TreeNode left, right;
        TreeNode(int val) { this.val = val; }
        TreeNode(int val, TreeNode left, TreeNode right) {
            this.val = val; this.left = left; this.right = right;
        }
    }

    private int best = Integer.MIN_VALUE;

    /**
     * Any maximal path has a single highest node where it "turns"; at that apex the
     * path is node.val + best-downward-left + best-downward-right. Post-order returns
     * the best *straight* downward path a parent can extend, while we separately fold
     * the turning sum into a global max. Clamping a child's contribution to 0 means
     * "skip this child" whenever including it would only hurt.
     * Time:  O(n) — one visit per node.
     * Space: O(h) — recursion stack, h the tree height.
     */
    public int maxPathSum(TreeNode root) {
        best = Integer.MIN_VALUE;
        gain(root);
        return best;
    }

    private int gain(TreeNode node) {
        if (node == null) return 0;
        int left  = Math.max(gain(node.left), 0);        // drop negative branches
        int right = Math.max(gain(node.right), 0);
        best = Math.max(best, node.val + left + right);   // path that turns here
        return node.val + Math.max(left, right);          // a parent can only take one side
    }

    public static void main(String[] args) {
        BinaryTreeMaximumPathSum sol = new BinaryTreeMaximumPathSum();

        // [1,2,3]
        TreeNode t1 = new TreeNode(1, new TreeNode(2), new TreeNode(3));
        System.out.println(sol.maxPathSum(t1));        // expected: 6

        // [-10,9,20,null,null,15,7]
        TreeNode t2 = new TreeNode(-10,
                new TreeNode(9),
                new TreeNode(20, new TreeNode(15), new TreeNode(7)));
        System.out.println(sol.maxPathSum(t2));        // expected: 42

        System.out.println(sol.maxPathSum(new TreeNode(-3)));  // expected: -3
    }
}
