package org.example.dp;

public class HouseRobber {

    /**
     * State: best[i] = max loot from houses [0..i]. At house i you either skip it
     * (keep best[i-1]) or rob it (best[i-2] + nums[i], since adjacent houses trip the
     * alarm): best[i] = max(best[i-1], best[i-2] + nums[i]). Only the previous two
     * results matter, so two rolling variables replace the array.
     * Time:  O(n)  — single pass.
     * Space: O(1)  — two scalars.
     */
    public static int rob(int[] nums) {
        int prev2 = 0, prev1 = 0;                        // best loot ending two / one houses back
        for (int v : nums) {
            int cur = Math.max(prev1, prev2 + v);        // skip this house, or rob it and add loot from i-2
            prev2 = prev1;
            prev1 = cur;
        }
        return prev1;
    }

    public static void main(String[] args) {
        System.out.println(rob(new int[]{1, 2, 3, 1}));      // expected: 4
        System.out.println(rob(new int[]{2, 7, 9, 3, 1}));   // expected: 12
        System.out.println(rob(new int[]{2, 1, 1, 2}));      // expected: 4
        System.out.println(rob(new int[]{5}));               // expected: 5
    }
}
