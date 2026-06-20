package org.example.bitmath;

import java.util.Arrays;

/**
 * LC 338. Counting Bits.
 */
public class CountingBits {

    /**
     * Key insight: i shares all but its lowest bit with i&gt;&gt;1 (i with the last bit dropped),
     * so its popcount is that of i&gt;&gt;1 plus whether i itself is odd. This turns each value
     * into an O(1) lookup of an already-computed smaller index — pure bottom-up DP.
     *
     * Time:  O(n) — one constant-work transition per index.
     * Space: O(n) — the answer array (excluding output, O(1) auxiliary).
     */
    public static int[] countBits(int n) {
        int[] dp = new int[n + 1];
        for (int i = 1; i <= n; i++) {
            dp[i] = dp[i >> 1] + (i & 1); // reuse the count for i with its last bit removed
        }
        return dp;
    }

    public static void main(String[] args) {
        System.out.println(Arrays.toString(countBits(2)));  // expected: [0, 1, 1]
        System.out.println(Arrays.toString(countBits(5)));  // expected: [0, 1, 1, 2, 1, 2]
        System.out.println(Arrays.toString(countBits(0)));  // expected: [0]
        System.out.println(Arrays.toString(countBits(8)));  // expected: [0, 1, 1, 2, 1, 2, 2, 3, 1]
    }
}
