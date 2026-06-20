package org.example.trees;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class BinaryTreeLevelOrderTraversal {

    static class TreeNode {
        int val;
        TreeNode left, right;
        TreeNode(int val) { this.val = val; }
        TreeNode(int val, TreeNode left, TreeNode right) {
            this.val = val; this.left = left; this.right = right;
        }
    }

    /**
     * A queue holds exactly the nodes of the frontier; snapshotting its size before
     * draining gives the count of nodes on the current level, so we can group output
     * by level without storing per-node depth.
     * Time:  O(n) — each node is enqueued and dequeued once.
     * Space: O(n) — the queue holds up to one full level (~n/2 leaves) at peak.
     */
    public static List<List<Integer>> levelOrder(TreeNode root) {
        List<List<Integer>> levels = new ArrayList<>();
        if (root == null) return levels;

        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);
        while (!queue.isEmpty()) {
            int size = queue.size();                     // freeze count before this level mutates the queue
            List<Integer> level = new ArrayList<>(size);
            for (int i = 0; i < size; i++) {
                TreeNode node = queue.poll();
                level.add(node.val);
                if (node.left != null)  queue.offer(node.left);
                if (node.right != null) queue.offer(node.right);
            }
            levels.add(level);
        }
        return levels;
    }

    public static void main(String[] args) {
        // [3,9,20,null,null,15,7]
        TreeNode t1 = new TreeNode(3,
                new TreeNode(9),
                new TreeNode(20, new TreeNode(15), new TreeNode(7)));
        System.out.println(levelOrder(t1));            // expected: [[3], [9, 20], [15, 7]]

        System.out.println(levelOrder(new TreeNode(1)));  // expected: [[1]]

        System.out.println(levelOrder(null));          // expected: []
    }
}
