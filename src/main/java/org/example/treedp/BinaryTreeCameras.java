package org.example.treedp;

/**
 * LC 968. Binary Tree Cameras.
 */
public class BinaryTreeCameras {

    static class TreeNode {
        int val;
        TreeNode left, right;
        TreeNode(int val) { this.val = val; }
        TreeNode(int val, TreeNode left, TreeNode right) {
            this.val = val; this.left = left; this.right = right;
        }
    }

    private static int cameras;

    /**
     * Cameras are scarcest when placed on PARENTS of leaves, so we decide greedily bottom-up
     * with three returned states: 0 = uncovered, 1 = covered but no camera here, 2 = camera here.
     * If either child is uncovered (0) the current node MUST hold a camera (count it, return 2);
     * if either child has a camera (2) this node is covered (return 1); otherwise both children
     * are merely covered, so this node is uncovered (return 0) and defers to its parent.
     * Treat null as covered (1) so leaves come back uncovered and push the camera up one level.
     * Time:  O(n) — one post-order visit per node.
     * Space: O(h) — recursion stack, h the tree height.
     */
    public static int minCameraCover(TreeNode root) {
        cameras = 0;
        if (dfs(root) == 0) cameras++;                      // root left uncovered -> needs its own camera
        return cameras;
    }

    // 0 = uncovered, 1 = covered (no camera), 2 = has a camera
    private static int dfs(TreeNode node) {
        if (node == null) return 1;                         // null is treated as already covered
        int l = dfs(node.left);
        int r = dfs(node.right);
        if (l == 0 || r == 0) {                             // a child is exposed -> cover it from here
            cameras++;
            return 2;
        }
        if (l == 2 || r == 2) return 1;                     // a child's camera already covers this node
        return 0;                                           // both children covered but watch nothing here
    }

    public static void main(String[] args) {
        // [0,0,null,0,0]
        TreeNode t1 = new TreeNode(0,
                new TreeNode(0, new TreeNode(0), new TreeNode(0)),
                null);
        System.out.println(minCameraCover(t1));   // expected: 1

        // [0,0,null,0,null,0,null,null,0]
        TreeNode t2 = new TreeNode(0,
                new TreeNode(0, null,
                        new TreeNode(0, null,
                                new TreeNode(0,
                                        new TreeNode(0), null))),
                null);
        System.out.println(minCameraCover(t2));   // expected: 2

        System.out.println(minCameraCover(new TreeNode(0)));   // expected: 1
        System.out.println(minCameraCover(null));              // expected: 0
    }
}
