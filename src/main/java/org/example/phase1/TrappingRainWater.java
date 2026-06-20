package org.example.phase1;
/**
 * Trapping Rain Water (LeetCode 42).
 * Given an array where each element is the height of a vertical bar of width 1,
 * compute how much water is trapped between the bars after raining.
 * Run from the command line:
 *     javac TrappingRainWater.java
 *     java  TrappingRainWater
 */
public class TrappingRainWater {

    /**
     * KEY INSIGHT
     * -----------
     * The water that can rest on top of bar i is:
     *
     *     min(highestBarToTheLeft, highestBarToTheRight) - height[i]
     *
     * Computing both maxes explicitly costs O(n) extra space. The two-pointer
     * trick avoids that: we walk inward from both ends and always advance the
     * pointer on the SHORTER side.
     *
     * INVARIANT
     * ---------
     * When height[left] < height[right], we know there exists at least one bar
     * on the right (namely height[right]) that is taller than height[left]. So
     * the right boundary for the water at `left` is guaranteed to be >= leftMax.
     * That means leftMax alone fully determines the water at `left` — we never
     * need the exact right wall. The mirror case applies symmetrically.
     *
     * Time:  O(n)  — each index is visited exactly once.
     * Space: O(1)  — only a handful of scalars.
     *
     * @param height array of non-negative bar heights; may be empty or null.
     * @return total units of trapped water.
     */
    public int trap(int[] height) {
        if (height == null || height.length < 3) {
            return 0; // fewer than 3 bars can never trap water
        }

        // Two pointers closing in from the ends.
        int left = 0, right = height.length - 1;

        // Tallest wall seen so far from each side. Start at 0 since heights >= 0.
        int leftMax = 0, rightMax = 0;

        int total = 0;

        while (left < right) {
            // The shorter side is the bottleneck, so process it.
            if (height[left] < height[right]) {
                // Right side is guaranteed taller, so leftMax bounds the water.
                leftMax = Math.max(leftMax, height[left]);
                total += leftMax - height[left];   // 0 if current bar is new max
                left++;
            } else {
                // Symmetric case: left side is guaranteed >= height[right].
                rightMax = Math.max(rightMax, height[right]);
                total += rightMax - height[right];
                right--;
            }
        }

        return total;
    }

    // ---- Simple test harness ---------------------------------------------

    public static void main(String[] args) {
        TrappingRainWater solver = new TrappingRainWater();

        check(solver, new int[]{0, 1, 0, 2, 1, 0, 1, 3, 2, 1, 2, 1}, 6);
        check(solver, new int[]{5, 1, 5, 1, 4}, 7);
        check(solver, new int[]{4, 2, 0, 3, 2, 5}, 9);
        check(solver, new int[]{}, 0);          // empty
        check(solver, new int[]{3}, 0);         // single bar
        check(solver, new int[]{2, 2, 2}, 0);   // flat, no dips
    }

    private static void check(TrappingRainWater solver, int[] height, int expected) {
        int actual = solver.trap(height);
        String status = (actual == expected) ? "PASS" : "FAIL";
        System.out.printf("[%s] expected=%d actual=%d  input=%s%n",
                status, expected, actual, java.util.Arrays.toString(height));
    }
}