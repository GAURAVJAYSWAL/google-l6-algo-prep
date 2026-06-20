package org.example.trees;

public class LowestCommonAncestor {

    static class TreeNode {
        int val;
        TreeNode left, right;
        TreeNode(int val) { this.val = val; }
        TreeNode(int val, TreeNode left, TreeNode right) {
            this.val = val; this.left = left; this.right = right;
        }
    }

    /**
     * The LCA is the deepest node from which p and q descend into different subtrees
     * (or which is itself one of them, with the other below). A post-order search
     * returns "I found p or q (or the LCA) in my subtree"; the node where the left and
     * right searches both come back non-null is exactly that split point — the answer.
     * Time:  O(n) — each node visited once.
     * Space: O(h) — recursion stack, h the tree height.
     */
    public TreeNode lowestCommonAncestor(TreeNode root, TreeNode p, TreeNode q) {
        if (root == null || root == p || root == q) return root;  // found a target or bottomed out
        TreeNode left  = lowestCommonAncestor(root.left, p, q);
        TreeNode right = lowestCommonAncestor(root.right, p, q);
        if (left != null && right != null) return root;           // p and q split here -> this is the LCA
        return left != null ? left : right;                       // both (or neither) lie on one side
    }

    public static void main(String[] args) {
        LowestCommonAncestor sol = new LowestCommonAncestor();

        // [3,5,1,6,2,0,8,null,null,7,4]
        TreeNode n7 = new TreeNode(7), n4 = new TreeNode(4);
        TreeNode n6 = new TreeNode(6), n2 = new TreeNode(2, n7, n4);
        TreeNode n0 = new TreeNode(0), n8 = new TreeNode(8);
        TreeNode n5 = new TreeNode(5, n6, n2);
        TreeNode n1 = new TreeNode(1, n0, n8);
        TreeNode root = new TreeNode(3, n5, n1);

        System.out.println(sol.lowestCommonAncestor(root, n5, n1).val);  // expected: 3
        System.out.println(sol.lowestCommonAncestor(root, n5, n4).val);  // expected: 5 (a node can be its own ancestor)
        System.out.println(sol.lowestCommonAncestor(root, n7, n8).val);  // expected: 3
    }
}
