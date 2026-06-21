package org.example.trees;

import java.util.ArrayDeque;
import java.util.Deque;

public class KthSmallestElementInABST {

    static class TreeNode {
        int val;
        TreeNode left, right;
        TreeNode(int val) { this.val = val; }
        TreeNode(int val, TreeNode left, TreeNode right) {
            this.val = val; this.left = left; this.right = right;
        }
    }

    /**
     * In-order traversal of a BST visits values in ascending order, so the k-th node
     * popped in an in-order walk is exactly the k-th smallest. Doing it iteratively with
     * an explicit stack lets us stop the instant we pop the k-th node, touching only
     * O(h + k) nodes rather than materializing the whole sorted sequence.
     * Time:  O(h + k) — descend to the smallest, then pop k nodes.
     * Space: O(h) — the stack holds at most one root-to-leaf spine.
     */
    public int kthSmallest(TreeNode root, int k) {
        Deque<TreeNode> stack = new ArrayDeque<>();
        TreeNode cur = root;
        while (cur != null || !stack.isEmpty()) {
            while (cur != null) {                         // dive to the leftmost (smallest)
                stack.push(cur);
                cur = cur.left;
            }
            cur = stack.pop();                            // next-smallest in order
            if (--k == 0) return cur.val;
            cur = cur.right;                              // then its right subtree
        }
        return -1;                                        // unreachable for valid k
    }

    public static void main(String[] args) {
        KthSmallestElementInABST sol = new KthSmallestElementInABST();
        TreeNode t1 = new TreeNode(3,
                new TreeNode(1, null, new TreeNode(2)),
                new TreeNode(4));
        System.out.println(sol.kthSmallest(t1, 1));      // expected: 1
        TreeNode t2 = new TreeNode(5,
                new TreeNode(3, new TreeNode(2, new TreeNode(1), null), new TreeNode(4)),
                new TreeNode(6));
        System.out.println(sol.kthSmallest(t2, 3));      // expected: 3
        System.out.println(sol.kthSmallest(t2, 6));      // expected: 6
    }
}
