package org.example.dp;

public class ClimbingStairs {

    /**
     * Ways to reach step i is exactly the Fibonacci recurrence: you arrive either
     * from step i-1 (a 1-step) or step i-2 (a 2-step), so dp[i] = dp[i-1] + dp[i-2].
     * Only the last two values are ever needed, so we carry two rolling variables
     * instead of a full table.
     * Time:  O(n)  — one pass advancing the pair of variables.
     * Space: O(1)  — two scalars regardless of n.
     */
    public static int climbStairs(int n) {
        int prev2 = 1, prev1 = 1;                        // ways(0) = ways(1) = 1
        for (int i = 2; i <= n; i++) {
            int cur = prev1 + prev2;                     // ways(i) = ways(i-1) + ways(i-2)
            prev2 = prev1;
            prev1 = cur;
        }
        return prev1;
    }

    public static void main(String[] args) {
        System.out.println(climbStairs(2));   // expected: 2
        System.out.println(climbStairs(3));   // expected: 3
        System.out.println(climbStairs(5));   // expected: 8
        System.out.println(climbStairs(45));  // expected: 1836311903
    }
}
