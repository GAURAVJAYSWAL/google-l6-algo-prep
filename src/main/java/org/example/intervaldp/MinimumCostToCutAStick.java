package org.example.intervaldp;

import java.util.Arrays;

/**
 * LC 1547. Minimum Cost to Cut a Stick.
 */
public class MinimumCostToCutAStick {

    /**
     * State: after sorting the cut positions with sentinels 0 and n added, dp[i][j] = min
     * cost to perform every cut strictly between positions cuts[i] and cuts[j].
     * Transition: the first cut k in (i, j) splits the current stick [cuts[i], cuts[j]],
     * costing its full length (cuts[j] - cuts[i]) regardless of which cut comes first, then
     * the two pieces are solved independently:
     * dp[i][j] = (cuts[j] - cuts[i]) + min over i<k<j of dp[i][k] + dp[k][j], and 0 when no
     * cut lies between i and j.
     * Time:  O(m^3) — m sorted cut boundaries, O(m^2) intervals each scanning O(m) splits.
     * Space: O(m^2) — the dp table.
     */
    public static int minCost(int n, int[] cuts) {
        int m = cuts.length;
        int[] c = Arrays.copyOf(cuts, m + 2);
        c[m] = 0;                                           // left sentinel (stick start)
        c[m + 1] = n;                                       // right sentinel (stick end)
        Arrays.sort(c);

        int[][] dp = new int[m + 2][m + 2];
        for (int len = 2; len < m + 2; len++) {             // span >= 2 boundaries contains >=1 cut
            for (int i = 0; i + len < m + 2; i++) {
                int j = i + len;
                int best = Integer.MAX_VALUE;
                for (int k = i + 1; k < j; k++) {           // k = first cut made in (cuts[i], cuts[j])
                    best = Math.min(best, dp[i][k] + dp[k][j]);
                }
                dp[i][j] = (c[j] - c[i]) + best;            // length of this piece + the two subproblems
            }
        }
        return dp[0][m + 1];
    }

    public static void main(String[] args) {
        System.out.println(minCost(7, new int[]{1, 3, 4, 5}));        // expected: 16
        System.out.println(minCost(9, new int[]{5, 6, 1, 4, 2}));     // expected: 22
        System.out.println(minCost(10, new int[]{4}));               // expected: 10
        System.out.println(minCost(5, new int[]{1, 2, 3, 4}));       // expected: 12
    }
}
