package org.example.greedy;

/**
 * LC 134. Gas Station.
 */
public class GasStation {

    /**
     * Key insight: a solution exists iff total gas &gt;= total cost (conservation of fuel). And if
     * the running tank dips below zero at station i, no start in [start..i] can work — each such
     * start has even less fuel reaching i — so the only viable candidate jumps to i+1. One pass
     * yields both the feasibility check and the unique answer.
     *
     * Time:  O(n) — single sweep over the stations.
     * Space: O(1) — two running totals.
     */
    public static int canCompleteCircuit(int[] gas, int[] cost) {
        int total = 0;   // global surplus; decides feasibility
        int tank = 0;    // fuel since the current candidate start
        int start = 0;
        for (int i = 0; i < gas.length; i++) {
            int diff = gas[i] - cost[i];
            total += diff;
            tank += diff;
            if (tank < 0) {  // cannot reach i+1 from current start, so restart there
                start = i + 1;
                tank = 0;
            }
        }
        return total >= 0 ? start : -1;
    }

    public static void main(String[] args) {
        System.out.println(canCompleteCircuit(new int[]{1, 2, 3, 4, 5}, new int[]{3, 4, 5, 1, 2})); // expected: 3
        System.out.println(canCompleteCircuit(new int[]{2, 3, 4}, new int[]{3, 4, 3}));             // expected: -1
        System.out.println(canCompleteCircuit(new int[]{5, 1, 2, 3, 4}, new int[]{4, 4, 1, 5, 1})); // expected: 4
        System.out.println(canCompleteCircuit(new int[]{3, 3, 4}, new int[]{3, 4, 4}));             // expected: -1
    }
}
