package org.example.dp;

public class JumpGame {

    /**
     * DP framing: let reach[i] = "the farthest index reachable having processed indices
     * 0..i". The transition is reach = max(reach, i + nums[i]) for every i we can still
     * stand on. We collapse the whole table to a single scalar `farthest`: scan left to
     * right, and the moment an index i exceeds the current farthest reach we are stuck —
     * otherwise we extend the reach. We succeed iff the last index ends up reachable.
     * Time:  O(n) — one linear pass, each index relaxes the reach once.
     * Space: O(1) — only the running farthest-reachable index.
     */
    public static boolean canJump(int[] nums) {
        int farthest = 0;                          // best index reachable so far
        for (int i = 0; i < nums.length; i++) {
            if (i > farthest) return false;        // a gap we can never bridge
            farthest = Math.max(farthest, i + nums[i]);
            if (farthest >= nums.length - 1) return true;  // last index already in range
        }
        return true;                               // single-element array trivially reachable
    }

    public static void main(String[] args) {
        System.out.println(canJump(new int[]{2, 3, 1, 1, 4}));
        // expected: true (0->1->4)

        System.out.println(canJump(new int[]{3, 2, 1, 0, 4}));
        // expected: false (stuck at index 3)

        System.out.println(canJump(new int[]{0}));
        // expected: true

        System.out.println(canJump(new int[]{2, 0, 0}));
        // expected: true
    }
}
