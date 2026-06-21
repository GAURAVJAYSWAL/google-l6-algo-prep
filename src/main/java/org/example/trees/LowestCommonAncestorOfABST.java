package org.example.trees;

public class LowestCommonAncestorOfABST {

    static class TreeNode {
        int val;
        TreeNode left, right;
        TreeNode(int val) { this.val = val; }
        TreeNode(int val, TreeNode left, TreeNode right) {
            this.val = val; this.left = left; this.right = right;
        }
    }

    /**
     * In a general tree the LCA needs a full post-order search because membership can only
     * be confirmed by visiting subtrees. A BST hands us that information for free: the
     * ordering tells us which side each target lives on without looking. Walking down, if
     * both p and q sit below the current node we go left; if both sit above, right; the
     * first node where they fall on opposite sides (or one equals the node) is the split
     * point — the deepest ancestor of both — so we never recurse or revisit.
     * Time:  O(h) — a single root-to-split descent.
     * Space: O(1) — iterative, no stack.
     */
    public TreeNode lowestCommonAncestor(TreeNode root, TreeNode p, TreeNode q) {
        TreeNode node = root;
        while (node != null) {
            if (p.val < node.val && q.val < node.val) node = node.left;        // both smaller
            else if (p.val > node.val && q.val > node.val) node = node.right;  // both larger
            else return node;                             // split point (or node == p/q)
        }
        return null;
    }

    public static void main(String[] args) {
        LowestCommonAncestorOfABST sol = new LowestCommonAncestorOfABST();
        TreeNode n0 = new TreeNode(0), n3 = new TreeNode(3), n5 = new TreeNode(5), n9 = new TreeNode(9);
        TreeNode n4 = new TreeNode(4, n3, n5);
        TreeNode n2 = new TreeNode(2, n0, n4);
        TreeNode n7 = new TreeNode(7, null, n9);
        TreeNode root = new TreeNode(6, n2, n7);
        System.out.println(sol.lowestCommonAncestor(root, n2, n7).val);   // expected: 6
        System.out.println(sol.lowestCommonAncestor(root, n2, n4).val);   // expected: 2
        System.out.println(sol.lowestCommonAncestor(root, n3, n5).val);   // expected: 4
    }
}
