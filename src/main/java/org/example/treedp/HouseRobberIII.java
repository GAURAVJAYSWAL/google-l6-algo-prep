package org.example.treedp;

/**
 * LC 337. House Robber III.
 */
public class HouseRobberIII {

    static class TreeNode {
        int val;
        TreeNode left, right;
        TreeNode(int val) { this.val = val; }
        TreeNode(int val, TreeNode left, TreeNode right) {
            this.val = val; this.left = left; this.right = right;
        }
    }

    /**
     * The alarm-trips-on-adjacent rule means each node's choice depends only on whether its
     * children were robbed, so one post-order pass returns a pair {robThis, skipThis}:
     * robThis = node.val + leftSkip + rightSkip (children must be skipped), and
     * skipThis = max(leftRob, leftSkip) + max(rightRob, rightSkip) (children free to choose).
     * The answer is the better of the root's two options.
     * Time:  O(n) — each node visited once.
     * Space: O(h) — recursion stack, h the tree height.
     */
    public static int rob(TreeNode root) {
        int[] res = dfs(root);
        return Math.max(res[0], res[1]);                    // rob root vs skip root
    }

    // returns {max loot if this node is robbed, max loot if this node is skipped}
    private static int[] dfs(TreeNode node) {
        if (node == null) return new int[]{0, 0};
        int[] l = dfs(node.left);
        int[] r = dfs(node.right);
        int robThis = node.val + l[1] + r[1];               // robbing node forces skipping both children
        int skipThis = Math.max(l[0], l[1]) + Math.max(r[0], r[1]); // each child takes its own best
        return new int[]{robThis, skipThis};
    }

    public static void main(String[] args) {
        // [3,2,3,null,3,null,1]
        TreeNode t1 = new TreeNode(3,
                new TreeNode(2, null, new TreeNode(3)),
                new TreeNode(3, null, new TreeNode(1)));
        System.out.println(rob(t1));   // expected: 7

        // [3,4,5,1,3,null,1]
        TreeNode t2 = new TreeNode(3,
                new TreeNode(4, new TreeNode(1), new TreeNode(3)),
                new TreeNode(5, null, new TreeNode(1)));
        System.out.println(rob(t2));   // expected: 9

        System.out.println(rob(new TreeNode(7)));     // expected: 7
        System.out.println(rob(null));                // expected: 0
    }
}
