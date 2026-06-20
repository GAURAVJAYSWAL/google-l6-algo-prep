package org.example.segmenttree;

/**
 * LC 327. Count of Range Sum.
 */
public class CountOfRangeSum {

    /**
     * Key insight: the sum of subarray (i, j) equals prefix[j+1] - prefix[i], so a range sum in
     * [lower, upper] becomes: for each right prefix P, count left prefixes L with
     * P - upper &lt;= L &lt;= P - lower. Embedding this count inside merge sort lets us exploit that
     * each half is already sorted — a two-pointer window over the right half tallies the valid
     * left prefixes in linear time per merge, while the merge itself restores sorted order.
     * Prefix sums use long to dodge int overflow on adversarial inputs.
     *
     * Time:  O(n log n) — merge sort with O(n) counting per level.
     * Space: O(n) — prefix array and the merge scratch buffer.
     */
    public static int countRangeSum(int[] nums, int lower, int upper) {
        long[] prefix = new long[nums.length + 1];
        for (int i = 0; i < nums.length; i++) prefix[i + 1] = prefix[i] + nums[i];
        return sortAndCount(prefix, 0, prefix.length - 1, lower, upper, new long[prefix.length]);
    }

    private static int sortAndCount(long[] p, int lo, int hi, int lower, int upper, long[] buf) {
        if (lo >= hi) return 0;
        int mid = (lo + hi) >>> 1;
        int count = sortAndCount(p, lo, mid, lower, upper, buf)
                + sortAndCount(p, mid + 1, hi, lower, upper, buf);

        // Both halves are sorted; slide a [start, end) window over the right half. Because the
        // left index i only increases, both window bounds advance monotonically -> O(n).
        int start = mid + 1, end = mid + 1;
        for (int i = lo; i <= mid; i++) {
            while (start <= hi && p[start] - p[i] < lower) start++;   // first L meeting lower bound
            while (end <= hi && p[end] - p[i] <= upper) end++;        // first L exceeding upper bound
            count += end - start;
        }

        merge(p, lo, mid, hi, buf);
        return count;
    }

    private static void merge(long[] p, int lo, int mid, int hi, long[] buf) {
        int i = lo, j = mid + 1, k = lo;
        while (i <= mid && j <= hi) buf[k++] = p[i] <= p[j] ? p[i++] : p[j++];
        while (i <= mid) buf[k++] = p[i++];
        while (j <= hi) buf[k++] = p[j++];
        System.arraycopy(buf, lo, p, lo, hi - lo + 1);
    }

    public static void main(String[] args) {
        System.out.println(countRangeSum(new int[]{-2, 5, -1}, -2, 2));        // expected: 3
        System.out.println(countRangeSum(new int[]{0}, 0, 0));                 // expected: 1
        System.out.println(countRangeSum(new int[]{0, 0}, 0, 0));              // expected: 3
        System.out.println(countRangeSum(new int[]{Integer.MIN_VALUE, Integer.MAX_VALUE}, -1, 1)); // expected: 1 (only the full-array sum -1 qualifies; uses long to avoid overflow)
    }
}
