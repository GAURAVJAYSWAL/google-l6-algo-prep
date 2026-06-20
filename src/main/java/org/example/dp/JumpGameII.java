package org.example.dp;

public class JumpGameII {

    /**
     * Reframe min-jumps as a BFS over "levels" of reach, but done greedily in O(1) space.
     * All indices reachable within j jumps form a contiguous prefix [0, curEnd]; while
     * scanning that level we track `farthest`, the boundary of the NEXT level. When the
     * scan index hits curEnd we have exhausted the current level, so we spend one jump
     * and advance curEnd to farthest. The number of such boundary crossings to cover the
     * last index is the minimum jump count.
     * Time:  O(n) — one pass; each index updates farthest once.
     * Space: O(1) — three scalar pointers, no queue needed.
     */
    public static int jump(int[] nums) {
        int jumps = 0, curEnd = 0, farthest = 0;
        // Stop at n-1: reaching the last index inside the current level needs no extra jump.
        for (int i = 0; i < nums.length - 1; i++) {
            farthest = Math.max(farthest, i + nums[i]);
            if (i == curEnd) {                      // consumed everything this level can reach
                jumps++;
                curEnd = farthest;                  // commit to the next level boundary
            }
        }
        return jumps;
    }

    public static void main(String[] args) {
        System.out.println(jump(new int[]{2, 3, 1, 1, 4}));
        // expected: 2 (0->1->4)

        System.out.println(jump(new int[]{2, 3, 0, 1, 4}));
        // expected: 2

        System.out.println(jump(new int[]{0}));
        // expected: 0

        System.out.println(jump(new int[]{1, 2, 3}));
        // expected: 2
    }
}
