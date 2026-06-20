package org.example.arrays;

import java.util.Arrays;

public class RemoveDuplicatesFromSortedArray {

    /**
     * Because the array is sorted, duplicates are adjacent. A slow write pointer
     * marks the last unique value written; the fast pointer scans ahead and, on
     * finding a value different from that last unique, writes it just past the
     * write pointer. The new length is write + 1.
     * Time:  O(n) — one pass. Space: O(1) — in place.
     */
    public static int removeDuplicates(int[] nums) {
        if (nums.length == 0) return 0;
        int write = 0;                               // nums[0..write] are the unique prefix
        for (int read = 1; read < nums.length; read++) {
            if (nums[read] != nums[write]) {
                nums[++write] = nums[read];          // new distinct value -> extend prefix
            }
        }
        return write + 1;
    }

    public static void main(String[] args) {
        int[] a = {1, 1, 2};
        int la = removeDuplicates(a);
        System.out.println(la + " " + Arrays.toString(Arrays.copyOf(a, la))); // expected: 2 [1, 2]

        int[] b = {0, 0, 1, 1, 1, 2, 2, 3, 3, 4};
        int lb = removeDuplicates(b);
        System.out.println(lb + " " + Arrays.toString(Arrays.copyOf(b, lb))); // expected: 5 [0, 1, 2, 3, 4]

        int[] c = {1};
        int lc = removeDuplicates(c);
        System.out.println(lc + " " + Arrays.toString(Arrays.copyOf(c, lc))); // expected: 1 [1]

        int[] d = {2, 2, 2, 2};
        int ld = removeDuplicates(d);
        System.out.println(ld + " " + Arrays.toString(Arrays.copyOf(d, ld))); // expected: 1 [2]
    }
}
