package org.example.trees;

public class ValidateBinarySearchTree {

    static class TreeNode {
        int val;
        TreeNode left, right;
        TreeNode(int val) { this.val = val; }
        TreeNode(int val, TreeNode left, TreeNode right) {
            this.val = val; this.left = left; this.right = right;
        }
    }

    /**
     * BST validity is global, not local: a node must exceed every ancestor it sits to
     * the right of and stay below every ancestor it sits to the left of. We thread an
     * open interval (low, high) down the recursion — going left tightens the upper
     * bound to the parent, going right tightens the lower bound — so each node is
     * checked against the full window inherited from its path, not just its children.
     * Time:  O(n) — one comparison per node.
     * Space: O(h) — recursion stack, h the tree height.
     */
    public boolean isValidBST(TreeNode root) {
        return validate(root, null, null);               // null bounds = unbounded (handles Integer extremes)
    }

    private boolean validate(TreeNode node, Integer low, Integer high) {
        if (node == null) return true;
        if (low != null && node.val <= low)  return false;
        if (high != null && node.val >= high) return false;
        return validate(node.left, low, node.val)        // left subtree must stay below node
            && validate(node.right, node.val, high);     // right subtree must stay above node
    }

    public static void main(String[] args) {
        ValidateBinarySearchTree sol = new ValidateBinarySearchTree();

        // [2,1,3]
        System.out.println(sol.isValidBST(
                new TreeNode(2, new TreeNode(1), new TreeNode(3))));   // expected: true

        // [5,1,4,null,null,3,6] — 3 is in the right subtree but < root 5
        System.out.println(sol.isValidBST(
                new TreeNode(5, new TreeNode(1),
                        new TreeNode(4, new TreeNode(3), new TreeNode(6)))));  // expected: false

        // [5,4,6,null,null,3,7] — 3 is a deep right descendant yet < root 5
        System.out.println(sol.isValidBST(
                new TreeNode(5, new TreeNode(4),
                        new TreeNode(6, new TreeNode(3), new TreeNode(7)))));  // expected: false

        System.out.println(sol.isValidBST(new TreeNode(Integer.MIN_VALUE)));  // expected: true
    }
}
