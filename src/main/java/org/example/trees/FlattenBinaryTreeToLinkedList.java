package org.example.trees;

public class FlattenBinaryTreeToLinkedList {

    static class TreeNode {
        int val;
        TreeNode left, right;
        TreeNode(int val) { this.val = val; }
        TreeNode(int val, TreeNode left, TreeNode right) {
            this.val = val; this.left = left; this.right = right;
        }
    }

    /**
     * In the flattened order a node is immediately followed by its left subtree, then by
     * what used to follow it (its right subtree). The Morris trick splices that in with
     * O(1) extra space: for each node with a left child, find the left subtree's
     * rightmost node — its in-order successor's predecessor — and hang the current right
     * subtree off it, then move the whole left subtree to the right and clear left.
     * Time:  O(n) — each edge is traversed a constant number of times.
     * Space: O(1) — pointer rewiring only, no recursion or stack.
     */
    public void flatten(TreeNode root) {
        TreeNode curr = root;
        while (curr != null) {
            if (curr.left != null) {
                TreeNode rightmost = curr.left;          // rightmost of left subtree = where the
                while (rightmost.right != null) {        //   detached right subtree must reattach
                    rightmost = rightmost.right;
                }
                rightmost.right = curr.right;            // graft old right tail after the left subtree
                curr.right = curr.left;                  // move left subtree onto the right spine
                curr.left = null;
            }
            curr = curr.right;                           // advance down the now-linear right chain
        }
    }

    // Renders the flattened list as right-pointer chain to confirm preorder ordering.
    private static String asList(TreeNode root) {
        StringBuilder sb = new StringBuilder("[");
        for (TreeNode n = root; n != null; n = n.right) {
            if (sb.length() > 1) sb.append(", ");
            sb.append(n.val);
        }
        return sb.append("]").toString();
    }

    public static void main(String[] args) {
        FlattenBinaryTreeToLinkedList sol = new FlattenBinaryTreeToLinkedList();

        // [1,2,5,3,4,null,6]
        TreeNode t1 = new TreeNode(1,
                new TreeNode(2, new TreeNode(3), new TreeNode(4)),
                new TreeNode(5, null, new TreeNode(6)));
        sol.flatten(t1);
        System.out.println(asList(t1));                // expected: [1, 2, 3, 4, 5, 6]

        TreeNode t2 = null;
        sol.flatten(t2);
        System.out.println(asList(t2));                // expected: []

        TreeNode t3 = new TreeNode(0);
        sol.flatten(t3);
        System.out.println(asList(t3));                // expected: [0]
    }
}
