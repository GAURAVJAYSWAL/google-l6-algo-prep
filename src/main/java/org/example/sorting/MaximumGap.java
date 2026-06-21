package org.example.sorting;

public class MaximumGap {

    /**
     * Pigeonhole / bucket sort in linear time. With n numbers the smallest possible
     * maximum gap is ceil((max-min)/(n-1)); size each bucket to that span, so any two
     * numbers inside ONE bucket differ by less than the gap. The maximum gap must
     * therefore span a bucket boundary — compare each non-empty bucket's min to the
     * PREVIOUS non-empty bucket's max. We only need each bucket's min and max, never
     * its contents, so a single pass suffices. (Alternative: LSD radix sort, O(d*n)
     * over d digits, then one scan of adjacent differences.)
     *
     * Time:  O(n) — fill buckets in one pass, sweep buckets in another.
     * Space: O(n) — n+1 buckets, each holding just a running min and max.
     */
    public static int maximumGap(int[] nums) {
        int n = nums.length;
        if (n < 2) return 0;               // fewer than two numbers -> no gap

        int min = nums[0], max = nums[0];
        for (int x : nums) {
            min = Math.min(min, x);
            max = Math.max(max, x);
        }
        if (min == max) return 0;          // all equal -> gap of 0

        // Bucket span >= 1 so any in-bucket pair differs by less than the answer.
        int bucketSize = Math.max(1, (max - min) / (n - 1));
        int bucketCount = (max - min) / bucketSize + 1;
        int[] bucketMin = new int[bucketCount];
        int[] bucketMax = new int[bucketCount];
        boolean[] used = new boolean[bucketCount];
        java.util.Arrays.fill(bucketMin, Integer.MAX_VALUE);
        java.util.Arrays.fill(bucketMax, Integer.MIN_VALUE);

        for (int x : nums) {
            int b = (x - min) / bucketSize;
            bucketMin[b] = Math.min(bucketMin[b], x);
            bucketMax[b] = Math.max(bucketMax[b], x);
            used[b] = true;
        }

        // The gap lives across empty buckets: this bucket's min minus the last seen max.
        int maxGap = 0;
        int prevMax = min;                 // min always occupies the first bucket
        for (int b = 0; b < bucketCount; b++) {
            if (!used[b]) continue;
            maxGap = Math.max(maxGap, bucketMin[b] - prevMax);
            prevMax = bucketMax[b];
        }
        return maxGap;
    }

    public static void main(String[] args) {
        System.out.println(maximumGap(new int[]{3, 6, 9, 1}));   // expected: 3
        System.out.println(maximumGap(new int[]{10}));            // expected: 0 (length < 2)
        System.out.println(maximumGap(new int[]{1, 1, 1, 1}));    // expected: 0 (all equal)
        System.out.println(maximumGap(new int[]{1, 10000000}));   // expected: 9999999
        System.out.println(maximumGap(new int[]{100, 3, 2, 1}));  // expected: 97
    }
}
