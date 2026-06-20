package org.example.trees;

import java.util.ArrayList;
import java.util.List;

public class BinaryTreeRightSideView {

    static class TreeNode {
        int val;
        TreeNode left, right;
        TreeNode(int val) { this.val = val; }
        TreeNode(int val, TreeNode left, TreeNode right) {
            this.val = val; this.left = left; this.right = right;
        }
    }

    /**
     * From the right you see the first node encountered at each depth. A DFS that visits
     * the right child before the left reaches every depth via its rightmost node first,
     * so the first time the recursion arrives at a new depth (depth == result.size())
     * that node is exactly the one visible — record it and let deeper-left nodes fill in
     * only depths the right side never reached.
     * Time:  O(n) — each node visited once.
     * Space: O(h) — recursion stack, h the tree height.
     */
    public List<Integer> rightSideView(TreeNode root) {
        List<Integer> view = new ArrayList<>();
        dfs(root, 0, view);
        return view;
    }

    private void dfs(TreeNode node, int depth, List<Integer> view) {
        if (node == null) return;
        if (depth == view.size()) view.add(node.val);    // first node reaching this depth is rightmost
        dfs(node.right, depth + 1, view);                // right first so it wins each depth
        dfs(node.left, depth + 1, view);
    }

    public static void main(String[] args) {
        BinaryTreeRightSideView sol = new BinaryTreeRightSideView();

        // [1,2,3,null,5,null,4]
        TreeNode t1 = new TreeNode(1,
                new TreeNode(2, null, new TreeNode(5)),
                new TreeNode(3, null, new TreeNode(4)));
        System.out.println(sol.rightSideView(t1));     // expected: [1, 3, 4]

        // [1,null,3] — right-only chain
        System.out.println(sol.rightSideView(
                new TreeNode(1, null, new TreeNode(3))));  // expected: [1, 3]

        System.out.println(sol.rightSideView(null));   // expected: []

        // [1,2,3,4] — deepest level visible node comes from the left side
        TreeNode t4 = new TreeNode(1,
                new TreeNode(2, new TreeNode(4), null),
                new TreeNode(3));
        System.out.println(sol.rightSideView(t4));     // expected: [1, 3, 4]
    }
}
