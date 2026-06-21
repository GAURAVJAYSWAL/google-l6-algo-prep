package org.example.dp;

import java.util.Arrays;

/**
 * LC 1235 — Maximum Profit in Job Scheduling (Hard).
 * Weighted interval scheduling: choose a subset of non-overlapping jobs maximizing total profit.
 */
public class MaximumProfitInJobScheduling {

    /** Immutable job triple, sorted by end time so earlier-finishing schedules form a searchable prefix. */
    private static final class Job {
        final int start, end, profit;
        Job(int start, int end, int profit) {
            this.start = start;
            this.end = end;
            this.profit = profit;
        }
    }

    /**
     * Sorting by end time turns "best profit using only jobs that finish early enough" into a prefix:
     * dp[i] = best profit considering the first i jobs (by end) and is monotonic non-decreasing, so for a
     * new job we binary-search the latest job whose end <= this job's start and add its profit.
     * Time:  O(n log n) — sort dominates; each job does one O(log n) binary search.
     * Space: O(n) — the jobs array and parallel dp/ends arrays.
     */
    int jobScheduling(int[] startTime, int[] endTime, int[] profit) {
        int n = startTime.length;
        Job[] jobs = new Job[n];
        for (int i = 0; i < n; i++) jobs[i] = new Job(startTime[i], endTime[i], profit[i]);
        Arrays.sort(jobs, (a, b) -> Integer.compare(a.end, b.end));

        int[] ends = new int[n];   // ends[i] = end time of the i-th job (sorted)
        int[] dp = new int[n];     // dp[i] = best total profit achievable using jobs[0..i]
        for (int i = 0; i < n; i++) {
            ends[i] = jobs[i].end;
            // Latest job index whose end <= current start; its dp is the best compatible prefix.
            int idx = latestNonConflicting(ends, i, jobs[i].start);
            int take = jobs[i].profit + (idx == -1 ? 0 : dp[idx]);
            int skip = (i == 0) ? 0 : dp[i - 1];
            dp[i] = Math.max(skip, take);
        }
        return n == 0 ? 0 : dp[n - 1];
    }

    /** Binary search in ends[0..hi-1] for the largest index with end <= target; -1 if none. */
    private static int latestNonConflicting(int[] ends, int hi, int target) {
        int lo = 0, ans = -1, right = hi - 1;
        while (lo <= right) {
            int mid = (lo + right) >>> 1;
            if (ends[mid] <= target) { ans = mid; lo = mid + 1; }
            else right = mid - 1;
        }
        return ans;
    }

    public static void main(String[] args) {
        MaximumProfitInJobScheduling s = new MaximumProfitInJobScheduling();

        // Jobs (1,3,50),(2,4,10),(3,5,40),(3,6,70): best is (1,3,50)+(3,6,70)=120.
        System.out.println(s.jobScheduling(
                new int[]{1, 2, 3, 3}, new int[]{3, 4, 5, 6}, new int[]{50, 10, 40, 70})); // expected: 120

        // (1,2,50),(3,5,20),(6,19,100),(2,100,200): (2,100,200) alone beats the chain (50+20+100=170).
        System.out.println(s.jobScheduling(
                new int[]{1, 2, 3, 4, 6}, new int[]{3, 5, 10, 6, 9}, new int[]{20, 20, 100, 70, 60})); // expected: 150

        // Single job.
        System.out.println(s.jobScheduling(
                new int[]{1}, new int[]{2}, new int[]{5})); // expected: 5
    }
}
