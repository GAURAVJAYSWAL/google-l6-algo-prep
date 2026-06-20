package org.example.arrays;

import java.util.Arrays;

public class ProductOfArrayExceptSelf {

    /**
     * answer[i] = (product of everything left of i) * (product of everything right of i).
     * First pass fills answer with prefix products; second pass walks right-to-left
     * carrying a running suffix product and multiplies it in. No division, so zeros
     * are handled naturally.
     *
     * Time:  O(n)  — two linear passes.
     * Space: O(1)  extra — output array aside, only a scalar suffix accumulator.
     */
    public static int[] productExceptSelf(int[] nums) {
        int n = nums.length;
        int[] answer = new int[n];

        answer[0] = 1;
        for (int i = 1; i < n; i++) {
            answer[i] = answer[i - 1] * nums[i - 1];   // prefix product of all elements left of i
        }

        int suffix = 1;
        for (int i = n - 1; i >= 0; i--) {
            answer[i] *= suffix;                       // fold in product of all elements right of i
            suffix *= nums[i];
        }
        return answer;
    }

    public static void main(String[] args) {
        System.out.println(Arrays.toString(productExceptSelf(new int[]{1, 2, 3, 4})));    // expected: [24, 12, 8, 6]
        System.out.println(Arrays.toString(productExceptSelf(new int[]{-1, 1, 0, -3, 3}))); // expected: [0, 0, 9, 0, 0]
        System.out.println(Arrays.toString(productExceptSelf(new int[]{2, 3})));          // expected: [3, 2]
    }
}
