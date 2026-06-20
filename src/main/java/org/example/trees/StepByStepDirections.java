package org.example.trees;

public class StepByStepDirections {

    static class TreeNode {
        int val;
        TreeNode left, right;
        TreeNode(int val) { this.val = val; }
        TreeNode(int val, TreeNode left, TreeNode right) {
            this.val = val; this.left = left; this.right = right;
        }
    }

    /**
     * The shortest start->dest route runs up to the lowest common ancestor, then down.
     * Both root-paths share a prefix down to that LCA, so after stripping the common
     * prefix the leftover of the start-path (each step replaced by 'U') walks up to the
     * LCA and the leftover of the dest-path walks down — no explicit LCA node needed,
     * the shared prefix length *is* the LCA.
     * Time:  O(n) — two root-to-target DFS passes over the tree.
     * Space: O(h) — path buffers and recursion stack, h the tree height.
     */
    public String getDirections(TreeNode root, int startValue, int destValue) {
        StringBuilder sp = new StringBuilder(), dp = new StringBuilder();
        findPath(root, startValue, sp);
        findPath(root, destValue, dp);

        int common = 0;                                  // longest shared prefix = path down to the LCA
        int min = Math.min(sp.length(), dp.length());
        while (common < min && sp.charAt(common) == dp.charAt(common)) common++;

        StringBuilder ans = new StringBuilder();
        ans.append("U".repeat(sp.length() - common));    // climb from start up to the LCA
        ans.append(dp, common, dp.length());             // then descend from the LCA to dest
        return ans.toString();
    }

    // Builds the root->target direction string; returns true once target is reached so
    // the recursion stops extending and unwinds, leaving exactly that path in `path`.
    private boolean findPath(TreeNode node, int target, StringBuilder path) {
        if (node == null) return false;
        if (node.val == target) return true;
        path.append('L');
        if (findPath(node.left, target, path)) return true;
        path.setCharAt(path.length() - 1, 'R');          // reuse the slot: not left, try right
        if (findPath(node.right, target, path)) return true;
        path.deleteCharAt(path.length() - 1);            // dead end: backtrack
        return false;
    }

    public static void main(String[] args) {
        StepByStepDirections sol = new StepByStepDirections();

        // [5,1,2,3,null,6,4]
        TreeNode t1 = new TreeNode(5,
                new TreeNode(1, new TreeNode(3), null),
                new TreeNode(2, new TreeNode(6), new TreeNode(4)));
        System.out.println(sol.getDirections(t1, 3, 6));   // expected: UURL

        // [2,1] start=2 dest=1
        TreeNode t2 = new TreeNode(2, new TreeNode(1), null);
        System.out.println(sol.getDirections(t2, 2, 1));   // expected: L

        System.out.println(sol.getDirections(t1, 6, 4));   // expected: UR
    }
}
