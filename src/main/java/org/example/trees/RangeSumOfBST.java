package org.example.trees;

public class RangeSumOfBST {

    static class TreeNode {
        int val;
        TreeNode left, right;
        TreeNode(int val) { this.val = val; }
        TreeNode(int val, TreeNode left, TreeNode right) {
            this.val = val; this.left = left; this.right = right;
        }
    }

    /**
     * The BST order lets us prune whole subtrees instead of visiting them. If a node's
     * value is below `low`, every value in its left subtree is also below low, so the
     * entire left side cannot contribute — recurse right only (and symmetrically above
     * `high`). Only when the node lies inside [low, high] must both children be explored.
     * Time:  O(n) worst case, but only nodes on the boundary paths / in-range are touched.
     * Space: O(h) — recursion stack, h the tree height.
     */
    public int rangeSumBST(TreeNode root, int low, int high) {
        if (root == null) return 0;
        if (root.val < low) return rangeSumBST(root.right, low, high);   // skip left subtree
        if (root.val > high) return rangeSumBST(root.left, low, high);   // skip right subtree
        return root.val + rangeSumBST(root.left, low, high) + rangeSumBST(root.right, low, high);
    }

    public static void main(String[] args) {
        RangeSumOfBST sol = new RangeSumOfBST();
        TreeNode t1 = new TreeNode(10,
                new TreeNode(5, new TreeNode(3), new TreeNode(7)),
                new TreeNode(15, null, new TreeNode(18)));
        System.out.println(sol.rangeSumBST(t1, 7, 15));   // expected: 32
        TreeNode t2 = new TreeNode(10,
                new TreeNode(5, new TreeNode(3, new TreeNode(1), null), new TreeNode(7, new TreeNode(6), null)),
                new TreeNode(15, new TreeNode(13), new TreeNode(18)));
        System.out.println(sol.rangeSumBST(t2, 6, 10));   // expected: 23
        System.out.println(sol.rangeSumBST(new TreeNode(1), 1, 1));  // expected: 1
    }
}
