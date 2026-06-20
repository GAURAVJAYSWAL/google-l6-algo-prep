package org.example.segmenttree;

/**
 * LC 307. Range Sum Query - Mutable.
 */
public class RangeSumQueryMutable {

    /**
     * Key insight: a Fenwick (Binary Indexed) tree stores partial sums indexed so that node i
     * covers the (i &amp; -i) elements ending at i — that low-bit isolates the block this node owns.
     * Walking down by i -= i&amp;-i accumulates a prefix sum, and walking up by i += i&amp;-i propagates
     * a point update, each touching only O(log n) nodes. A range sum is the difference of two
     * prefix sums.
     *
     * Time:  update O(log n), sumRange O(log n); construction O(n log n).
     * Space: O(n) — the BIT plus a copy of the values for delta computation.
     */
    static class NumArray {
        private final int[] tree; // 1-indexed Fenwick tree
        private final int[] nums; // current values, to compute deltas on update
        private final int n;

        public NumArray(int[] nums) {
            this.n = nums.length;
            this.nums = new int[n];
            this.tree = new int[n + 1];
            for (int i = 0; i < n; i++) update(i, nums[i]);
        }

        public void update(int index, int val) {
            int delta = val - nums[index];
            nums[index] = val;
            for (int i = index + 1; i <= n; i += i & -i) { // i & -i jumps to the next owner up
                tree[i] += delta;
            }
        }

        public int sumRange(int left, int right) {
            return prefix(right + 1) - prefix(left); // inclusive range via prefix difference
        }

        // Sum of the first i elements (1-indexed boundary).
        private int prefix(int i) {
            int sum = 0;
            for (; i > 0; i -= i & -i) sum += tree[i]; // drop the lowest set bit each step
            return sum;
        }
    }

    public static void main(String[] args) {
        NumArray na = new NumArray(new int[]{1, 3, 5});
        System.out.println(na.sumRange(0, 2)); // expected: 9
        na.update(1, 2);                        // [1, 2, 5]
        System.out.println(na.sumRange(0, 2)); // expected: 8
        System.out.println(na.sumRange(1, 1)); // expected: 2

        NumArray na2 = new NumArray(new int[]{-1, 0, 4, 2});
        System.out.println(na2.sumRange(1, 3)); // expected: 6
        na2.update(0, 3);                         // [3, 0, 4, 2]
        System.out.println(na2.sumRange(0, 1));  // expected: 3
    }
}
