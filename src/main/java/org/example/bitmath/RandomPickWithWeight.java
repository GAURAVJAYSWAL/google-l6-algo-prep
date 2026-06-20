package org.example.bitmath;

import java.util.Random;

/**
 * LC 528. Random Pick with Weight.
 */
public class RandomPickWithWeight {

    /**
     * Key insight: laying the weights end-to-end on a number line, index i owns the interval
     * (prefix[i-1], prefix[i]]. A uniform integer target in [1, total] therefore lands in
     * index i with probability proportional to w[i]; the first prefix sum &gt;= target — found by
     * binary search for the lower bound — is that index. Prefix sums make pick O(log n).
     *
     * Time:  constructor O(n); pickIndex O(log n) per call via binary search.
     * Space: O(n) — the prefix-sum array.
     */
    static class Solution {
        private final int[] prefix;
        private final int total;
        private final Random rng;

        public Solution(int[] w) {
            this(w, new Random());
        }

        // Seeded overload so the demo is reproducible.
        public Solution(int[] w, Random rng) {
            prefix = new int[w.length];
            int running = 0;
            for (int i = 0; i < w.length; i++) {
                running += w[i];
                prefix[i] = running;
            }
            this.total = running;
            this.rng = rng;
        }

        public int pickIndex() {
            int target = rng.nextInt(total) + 1; // uniform in [1, total]
            // Lower-bound binary search: smallest index whose prefix sum >= target.
            int lo = 0, hi = prefix.length - 1;
            while (lo < hi) {
                int mid = (lo + hi) >>> 1;
                if (prefix[mid] < target) lo = mid + 1;
                else hi = mid;
            }
            return lo;
        }
    }

    public static void main(String[] args) {
        // Single weight -> always index 0.
        Solution single = new Solution(new int[]{1}, new Random(42));
        System.out.println(single.pickIndex()); // expected: 0

        // Weights [1, 3] -> index 1 roughly 3x as often as index 0.
        Solution s = new Solution(new int[]{1, 3}, new Random(7));
        int[] counts = new int[2];
        for (int i = 0; i < 4000; i++) counts[s.pickIndex()]++;
        System.out.println("counts=[" + counts[0] + ", " + counts[1] + "]");
        // expected: counts ~[1000, 3000] (index 1 about 3x index 0; deterministic with seed 7)
        System.out.println(counts[1] > counts[0]); // expected: true
    }
}
