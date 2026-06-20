package org.example.arrays;

import java.util.Arrays;

public class SortColors {

    /**
     * Dutch National Flag: maintain three regions — [0,low) all 0s, [low,mid)
     * all 1s, (high,end] all 2s — with [mid,high] unknown. A 0 swaps into the
     * low region and advances both pointers; a 2 swaps to the high region and
     * shrinks it (mid stays, since the swapped-in value is still unexamined);
     * a 1 is already in place so only mid advances.
     * Time:  O(n) — single pass, mid and high converge.
     * Space: O(1) — sorted in place.
     */
    public static void sortColors(int[] nums) {
        int low = 0, mid = 0, high = nums.length - 1;
        while (mid <= high) {
            if (nums[mid] == 0) {
                swap(nums, low++, mid++);
            } else if (nums[mid] == 2) {
                swap(nums, mid, high--);   // don't advance mid: swapped-in value is unexamined
            } else {
                mid++;
            }
        }
    }

    private static void swap(int[] nums, int a, int b) {
        int t = nums[a];
        nums[a] = nums[b];
        nums[b] = t;
    }

    public static void main(String[] args) {
        int[] a = {2, 0, 2, 1, 1, 0};
        sortColors(a);
        System.out.println(Arrays.toString(a)); // expected: [0, 0, 1, 1, 2, 2]

        int[] b = {2, 0, 1};
        sortColors(b);
        System.out.println(Arrays.toString(b)); // expected: [0, 1, 2]

        int[] c = {0};
        sortColors(c);
        System.out.println(Arrays.toString(c)); // expected: [0]

        int[] d = {1, 2, 0, 2, 1, 0, 1};
        sortColors(d);
        System.out.println(Arrays.toString(d)); // expected: [0, 0, 1, 1, 1, 2, 2]
    }
}
