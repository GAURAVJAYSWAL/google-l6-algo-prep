package org.example.phase1;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FourSum {

    /**
     * 3Sum with one more nested layer: fix nums[a], fix nums[b], two-pointer the rest.
     * Skip duplicates at all four positions (a, b, left, right).
     * Use long for the running sum: 4 * 10^9 overflows a 32-bit int.
     * Time:  O(n^3).
     * Space: O(1) extra (ignoring sort + output).
     */
    public static List<List<Integer>> fourSum(int[] nums, int target) {
        Arrays.sort(nums);
        List<List<Integer>> result = new ArrayList<>();
        int n = nums.length;

        for (int a = 0; a < n - 3; a++) {
            if (a > 0 && nums[a] == nums[a - 1]) continue;           // skip dup a
            for (int b = a + 1; b < n - 2; b++) {
                if (b > a + 1 && nums[b] == nums[b - 1]) continue;   // skip dup b

                int left = b + 1, right = n - 1;
                while (left < right) {
                    long sum = (long) nums[a] + nums[b] + nums[left] + nums[right]; // overflow-safe
                    if (sum == target) {
                        result.add(Arrays.asList(nums[a], nums[b], nums[left], nums[right]));
                        left++;
                        right--;
                        while (left < right && nums[left] == nums[left - 1]) left++;     // skip dup
                        while (left < right && nums[right] == nums[right + 1]) right--;   // skip dup
                    } else if (sum < target) {
                        left++;
                    } else {
                        right--;
                    }
                }
            }
        }
        return result;
    }

    public static void main(String[] args) {
        System.out.println(fourSum(new int[]{1,0,-1,0,-2,2}, 0));
        // [[-2,-1,1,2],[-2,0,0,2],[-1,0,0,1]]
        System.out.println(fourSum(new int[]{2,2,2,2,2}, 8));   // [[2,2,2,2]]
        System.out.println(fourSum(new int[]{1000000000,1000000000,1000000000,1000000000}, -294967296)); // []
    }
}