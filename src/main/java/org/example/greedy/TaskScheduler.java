package org.example.greedy;

/**
 * LC 621. Task Scheduler.
 */
public class TaskScheduler {

    /**
     * Key insight: the busiest task dictates the skeleton. Place its maxFreq copies n+1 apart,
     * forming (maxFreq-1) full frames of width (n+1) plus a final partial frame holding the
     * countOfMax tasks tied for the maximum. That gives (maxFreq-1)*(n+1)+countOfMax slots.
     * When there are enough distinct tasks to backfill every gap, no idling is needed and the
     * answer is simply the task count — hence the max with the array length.
     *
     * Time:  O(t) — one pass to tally frequencies (alphabet is fixed at 26).
     * Space: O(1) — a 26-bucket frequency array.
     */
    public static int leastInterval(char[] tasks, int n) {
        int[] freq = new int[26];
        for (char c : tasks) freq[c - 'A']++;

        int maxFreq = 0;
        for (int f : freq) maxFreq = Math.max(maxFreq, f);

        int countOfMax = 0; // how many tasks share that peak frequency
        for (int f : freq) if (f == maxFreq) countOfMax++;

        int framed = (maxFreq - 1) * (n + 1) + countOfMax;
        return Math.max(framed, tasks.length); // dense input may leave no idle gaps
    }

    public static void main(String[] args) {
        System.out.println(leastInterval(new char[]{'A', 'A', 'A', 'B', 'B', 'B'}, 2)); // expected: 8
        System.out.println(leastInterval(new char[]{'A', 'A', 'A', 'B', 'B', 'B'}, 0)); // expected: 6
        System.out.println(leastInterval(new char[]{'A', 'A', 'A', 'A', 'A', 'A', 'B', 'C', 'D', 'E', 'F', 'G'}, 2)); // expected: 16
        System.out.println(leastInterval(new char[]{'A', 'B', 'C', 'D'}, 3)); // expected: 4 (all distinct, no idling)
    }
}
