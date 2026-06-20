package org.example.trees;

import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

public class SerializeAndDeserializeBinaryTree {

    static class TreeNode {
        int val;
        TreeNode left, right;
        TreeNode(int val) { this.val = val; }
        TreeNode(int val, TreeNode left, TreeNode right) {
            this.val = val; this.left = left; this.right = right;
        }
    }

    /**
     * A pre-order walk that emits an explicit marker for every null pin-points the
     * shape uniquely: the first token is always the root, and each subtree is a
     * self-delimiting prefix, so deserialize can rebuild by consuming tokens in the
     * same root-left-right order — no separate inorder traversal needed.
     */
    static class Codec {
        private static final String NULL = "#";
        private static final String SEP  = ",";

        // Time: O(n) — one append per node + null slot.  Space: O(n) — output + stack.
        public String serialize(TreeNode root) {
            StringBuilder sb = new StringBuilder();
            buildString(root, sb);
            return sb.toString();
        }

        private void buildString(TreeNode node, StringBuilder sb) {
            if (node == null) {
                sb.append(NULL).append(SEP);
                return;
            }
            sb.append(node.val).append(SEP);
            buildString(node.left, sb);
            buildString(node.right, sb);
        }

        // Time: O(n) — consume each token once.  Space: O(n) — token list + stack.
        public TreeNode deserialize(String data) {
            Iterator<String> tokens = Arrays.asList(data.split(SEP)).iterator();
            return buildTree(tokens);
        }

        private TreeNode buildTree(Iterator<String> tokens) {
            String token = tokens.next();
            if (token.equals(NULL)) return null;
            TreeNode node = new TreeNode(Integer.parseInt(token));
            node.left  = buildTree(tokens);              // pre-order: left subtree comes first
            node.right = buildTree(tokens);
            return node;
        }
    }

    // Level-order render (LeetCode convention: nulls shown, but trailing nulls trimmed)
    // just to confirm the round-trip rebuilt an identical tree.
    private static String levelOrderString(TreeNode root) {
        StringBuilder sb = new StringBuilder("[");
        Queue<TreeNode> q = new LinkedList<>();
        if (root != null) q.offer(root);
        while (!q.isEmpty()) {
            TreeNode n = q.poll();
            if (sb.length() > 1) sb.append(", ");
            if (n == null) { sb.append("null"); continue; }  // null marks a missing child; don't expand it
            sb.append(n.val);
            if (n.left != null || n.right != null) {          // only descend when a real child exists,
                q.offer(n.left);                              //   so leaves don't emit trailing nulls
                q.offer(n.right);
            }
        }
        return sb.append("]").toString();
    }

    public static void main(String[] args) {
        Codec codec = new Codec();

        // [1,2,3,null,null,4,5]
        TreeNode t1 = new TreeNode(1,
                new TreeNode(2),
                new TreeNode(3, new TreeNode(4), new TreeNode(5)));
        String s1 = codec.serialize(t1);
        System.out.println(s1);                                       // expected: 1,2,#,#,3,4,#,#,5,#,#,
        System.out.println(levelOrderString(codec.deserialize(s1)));  // expected: [1, 2, 3, 4, 5]

        System.out.println(codec.serialize(null));                    // expected: #,
        System.out.println(levelOrderString(codec.deserialize(codec.serialize(null)))); // expected: []
    }
}
