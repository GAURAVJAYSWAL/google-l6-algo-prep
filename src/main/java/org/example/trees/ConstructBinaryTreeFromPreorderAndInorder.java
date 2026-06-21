package org.example.trees;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConstructBinaryTreeFromPreorderAndInorder {

    static class TreeNode {
        int val;
        TreeNode left, right;
        TreeNode(int val) { this.val = val; }
        TreeNode(int val, TreeNode left, TreeNode right) {
            this.val = val; this.left = left; this.right = right;
        }
    }

    private int preIdx;
    private Map<Integer, Integer> inPos;

    /**
     * Preorder lists roots in the exact order we want to create them (root before either
     * subtree), so a single left-to-right pointer over preorder always names the next root.
     * That root's position in inorder splits the remaining inorder window into the left
     * subtree (left of it) and right subtree (right of it). A value→inorder-index map makes
     * each split O(1); recursing left-first keeps the preorder pointer synced with the
     * roots it is about to consume.
     * Time:  O(n) — each node created once, splits are O(1) via the map.
     * Space: O(n) — the index map plus O(h) recursion.
     */
    public TreeNode buildTree(int[] preorder, int[] inorder) {
        preIdx = 0;
        inPos = new HashMap<>();
        for (int i = 0; i < inorder.length; i++) inPos.put(inorder[i], i);
        return build(preorder, 0, inorder.length - 1);
    }

    private TreeNode build(int[] preorder, int inLo, int inHi) {
        if (inLo > inHi) return null;                     // empty inorder window
        int rootVal = preorder[preIdx++];                 // next root in preorder
        TreeNode root = new TreeNode(rootVal);
        int mid = inPos.get(rootVal);                     // split inorder at the root
        root.left = build(preorder, inLo, mid - 1);       // left first — consumes its preorder block
        root.right = build(preorder, mid + 1, inHi);
        return root;
    }

    private static List<Integer> inorderTraversal(TreeNode root) {
        List<Integer> out = new ArrayList<>();
        inorderTraversal(root, out);
        return out;
    }

    private static void inorderTraversal(TreeNode node, List<Integer> out) {
        if (node == null) return;
        inorderTraversal(node.left, out);
        out.add(node.val);
        inorderTraversal(node.right, out);
    }

    public static void main(String[] args) {
        ConstructBinaryTreeFromPreorderAndInorder sol = new ConstructBinaryTreeFromPreorderAndInorder();
        TreeNode t1 = sol.buildTree(new int[]{3, 9, 20, 15, 7}, new int[]{9, 3, 15, 20, 7});
        System.out.println(inorderTraversal(t1));         // expected: [9, 3, 15, 20, 7]
        TreeNode t2 = sol.buildTree(new int[]{-1}, new int[]{-1});
        System.out.println(inorderTraversal(t2));         // expected: [-1]
        TreeNode t3 = sol.buildTree(new int[]{1, 2, 4, 5, 3, 6}, new int[]{4, 2, 5, 1, 3, 6});
        System.out.println(inorderTraversal(t3));         // expected: [4, 2, 5, 1, 3, 6]
    }
}
