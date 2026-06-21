package org.example.greedy;

/**
 * LC 1007 — Minimum Domino Rotations For Equal Row (Medium).
 * Find the fewest top/bottom swaps so one whole row (tops or bottoms) holds a single value, or -1.
 */
public class MinimumDominoRotations {

    /**
     * If some value X can fill an entire row, X must appear on every domino, hence on the first domino —
     * so the only viable candidates are tops[0] and bottoms[0]. For each candidate, count rotations needed
     * to make all tops equal it vs. all bottoms equal it; a candidate fails the moment a domino has it on
     * neither face.
     * Time:  O(n) — at most two linear passes (a tiny constant per candidate).
     * Space: O(1) — only counters.
     */
    int minDominoRotations(int[] tops, int[] bottoms) {
        int n = tops.length;
        int best = Math.min(rotationsFor(tops, bottoms, tops[0]),
                            rotationsFor(tops, bottoms, bottoms[0]));
        return best > n ? -1 : best;   // sentinel n+1 means "no candidate works"
    }

    /**
     * Rotations to make ALL tops == target, or ALL bottoms == target, whichever is fewer.
     * Returns n+1 (impossible) if any domino has target on neither face.
     */
    private static int rotationsFor(int[] tops, int[] bottoms, int target) {
        int n = tops.length;
        int rotTop = 0, rotBottom = 0;   // swaps to bring target to top / to bottom
        for (int i = 0; i < n; i++) {
            if (tops[i] != target && bottoms[i] != target) return n + 1;
            if (tops[i] != target) rotTop++;        // target is on the bottom; swap up
            if (bottoms[i] != target) rotBottom++;  // target is on the top; swap down
        }
        return Math.min(rotTop, rotBottom);
    }

    public static void main(String[] args) {
        MinimumDominoRotations s = new MinimumDominoRotations();

        // tops=[2,1,2,4,2,2], bottoms=[5,2,6,2,3,2]: rotate the two non-2 tops to make all tops 2 → 2.
        System.out.println(s.minDominoRotations(
                new int[]{2, 1, 2, 4, 2, 2}, new int[]{5, 2, 6, 2, 3, 2})); // expected: 2

        // tops=[3,5,1,2,3], bottoms=[3,6,3,3,4]: no value spans every domino → -1.
        System.out.println(s.minDominoRotations(
                new int[]{3, 5, 1, 2, 3}, new int[]{3, 6, 3, 3, 4})); // expected: -1

        // tops=[1,2,1,1,1,2,2,2], bottoms=[2,1,2,2,2,2,2,2]: make all bottoms 2 by rotating index 1 → 1.
        System.out.println(s.minDominoRotations(
                new int[]{1, 2, 1, 1, 1, 2, 2, 2}, new int[]{2, 1, 2, 2, 2, 2, 2, 2})); // expected: 1
    }
}
