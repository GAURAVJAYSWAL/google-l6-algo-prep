package org.example.trees;

public class LongestUnivaluePath {

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
     * Like the diameter, the longest same-value path turns at one apex. The trick is what
     * each child returns: the longest same-value "arrow" that can be *extended through the
     * parent*, which is only non-zero when the child holds the same value as the parent —
     * otherwise the chain breaks and the child contributes 0. At each node we join the two
     * qualifying arrows (left + right) for the turning length, but pass only one arrow up,
     * since a parent can continue down just one side.
     * Time:  O(n) — one visit per node.
     * Space: O(h) — recursion stack, h the tree height.
     */
    public int longestUnivaluePath(TreeNode root) {
        best = 0;
        arrow(root);
        return best;
    }

    private int arrow(TreeNode node) {
        if (node == null) return 0;
        int left = arrow(node.left);
        int right = arrow(node.right);
        int leftArrow = 0, rightArrow = 0;
        if (node.left != null && node.left.val == node.val) leftArrow = left + 1;    // extend through equal child
        if (node.right != null && node.right.val == node.val) rightArrow = right + 1;
        best = Math.max(best, leftArrow + rightArrow);   // join both arrows here (edge count)
        return Math.max(leftArrow, rightArrow);          // a parent can take only one side
    }

    public static void main(String[] args) {
        LongestUnivaluePath sol = new LongestUnivaluePath();
        TreeNode t1 = new TreeNode(5,
                new TreeNode(4, new TreeNode(1), new TreeNode(1)),
                new TreeNode(5, null, new TreeNode(5)));
        System.out.println(sol.longestUnivaluePath(t1));   // expected: 2
        TreeNode t2 = new TreeNode(1,
                new TreeNode(4, new TreeNode(4), new TreeNode(4)),
                new TreeNode(5, null, new TreeNode(5)));
        System.out.println(sol.longestUnivaluePath(t2));   // expected: 2
        System.out.println(sol.longestUnivaluePath(new TreeNode(1)));  // expected: 0
    }
}
