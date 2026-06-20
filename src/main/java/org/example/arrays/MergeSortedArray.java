package org.example.arrays;

import java.util.Arrays;

public class MergeSortedArray {

    /**
     * Merge from the back. nums1 has exactly the free tail (indices m..m+n-1) we
     * need, so writing the largest remaining element into nums1[m+n-1] downward
     * never clobbers an element we still have to read. If nums2 drains first the
     * nums1 prefix is already in place; if nums1 drains first, the rest of nums2
     * is copied over.
     * Time:  O(m + n) — each element placed once. Space: O(1) — in place into nums1.
     */
    public static void merge(int[] nums1, int m, int[] nums2, int n) {
        int i = m - 1, j = n - 1, write = m + n - 1;
        while (j >= 0) {                             // once nums2 is exhausted, nums1 prefix is done
            if (i >= 0 && nums1[i] > nums2[j]) {
                nums1[write--] = nums1[i--];
            } else {
                nums1[write--] = nums2[j--];
            }
        }
    }

    public static void main(String[] args) {
        int[] a = {1, 2, 3, 0, 0, 0};
        merge(a, 3, new int[]{2, 5, 6}, 3);
        System.out.println(Arrays.toString(a)); // expected: [1, 2, 2, 3, 5, 6]

        int[] b = {1};
        merge(b, 1, new int[]{}, 0);
        System.out.println(Arrays.toString(b)); // expected: [1]

        int[] c = {0};
        merge(c, 0, new int[]{1}, 1);
        System.out.println(Arrays.toString(c)); // expected: [1]

        int[] d = {4, 5, 6, 0, 0, 0};
        merge(d, 3, new int[]{1, 2, 3}, 3);
        System.out.println(Arrays.toString(d)); // expected: [1, 2, 3, 4, 5, 6]
    }
}
