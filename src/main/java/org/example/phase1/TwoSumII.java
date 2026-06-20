package org.example.phase1;

import java.util.Arrays;

public class TwoSumII {

    /**
     * Sorted array + find a pair = opposite-end two pointers.
     * Sum too small -> move left up; too big -> move right down.
     * Time:  O(n)  — each element visited at most once.
     * Space: O(1).
     * Assumes exactly one solution exists (per problem statement).
     */
    public static int[] twoSum(int[] numbers, int target) {
        int left = 0;
        int right = numbers.length - 1;
        while (left < right) {
            int sum = numbers[left] + numbers[right];
            if (sum == target) {
                return new int[]{left + 1, right + 1};   // 1-indexed
            } else if (sum < target) {
                left++;
            } else {
                right--;
            }
        }
        return new int[]{-1, -1};
    }

    public static void main(String[] args) {
        System.out.println(Arrays.toString(twoSum(new int[]{2,7,11,15}, 9))); // [1, 2]
        System.out.println(Arrays.toString(twoSum(new int[]{2,3,4}, 6)));     // [1, 3]
        System.out.println(Arrays.toString(twoSum(new int[]{-1,0}, -1)));     // [1, 2]
    }
}
