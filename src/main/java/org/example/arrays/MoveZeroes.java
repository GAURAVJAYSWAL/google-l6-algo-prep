package org.example.arrays;

import java.util.Arrays;

public class MoveZeroes {

    /**
     * Two pointers: `insert` marks where the next non-zero belongs. Scanning with
     * a read pointer, every non-zero is swapped down to `insert` (which only ever
     * trails or equals the reader), preserving relative order while the zeros bubble
     * to the tail. Swapping (vs. overwrite-then-pad) keeps it a single clean pass.
     * Time:  O(n) — one pass. Space: O(1) — in place.
     */
    public static void moveZeroes(int[] nums) {
        int insert = 0;
        for (int read = 0; read < nums.length; read++) {
            if (nums[read] != 0) {
                swap(nums, insert++, read);          // pull non-zero forward, advance write slot
            }
        }
    }

    private static void swap(int[] nums, int a, int b) {
        if (a == b) return;
        int t = nums[a];
        nums[a] = nums[b];
        nums[b] = t;
    }

    public static void main(String[] args) {
        int[] a = {0, 1, 0, 3, 12};
        moveZeroes(a);
        System.out.println(Arrays.toString(a)); // expected: [1, 3, 12, 0, 0]

        int[] b = {0};
        moveZeroes(b);
        System.out.println(Arrays.toString(b)); // expected: [0]

        int[] c = {1, 2, 3};
        moveZeroes(c);
        System.out.println(Arrays.toString(c)); // expected: [1, 2, 3]

        int[] d = {0, 0, 1};
        moveZeroes(d);
        System.out.println(Arrays.toString(d)); // expected: [1, 0, 0]
    }
}
