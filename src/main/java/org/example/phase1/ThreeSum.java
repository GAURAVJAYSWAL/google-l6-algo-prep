package org.example.phase1;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ThreeSum {

    /**
     * Sort, then fix each nums[i] and run Two-Sum-II two pointers on the rest,
     * searching for a pair that sums to -nums[i].
     * Duplicate-skipping (the crux) happens in three places: the anchor i,
     * and left/right after each match.
     *
     * Time:  O(n^2)  — outer loop n, inner two-pointer scan n.
     * Space: O(1) extra (ignoring sort + output).
     */
    public static List<List<Integer>> threeSum(int[] nums) {
        Arrays.sort(nums);
        List<List<Integer>> result = new ArrayList<>();

        for (int i = 0; i < nums.length - 2; i++) {
            if (nums[i] > 0) break;                            // 3 sorted positives can't sum to 0
            if (i > 0 && nums[i] == nums[i - 1]) continue;     // skip duplicate anchor

            int left = i + 1, right = nums.length - 1;
            while (left < right) {
                int sum = nums[i] + nums[left] + nums[right];
                if (sum == 0) {
                    result.add(Arrays.asList(nums[i], nums[left], nums[right]));
                    left++;
                    right--;
                    while (left < right && nums[left] == nums[left - 1]) left++;     // skip dup
                    while (left < right && nums[right] == nums[right + 1]) right--;   // skip dup
                } else if (sum < 0) {
                    left++;
                } else {
                    right--;
                }
            }
        }
        return result;
    }

    public static void main(String[] args) {
        System.out.println(threeSum(new int[]{-1,0,1,2,-1,-4})); // [[-1,-1,2],[-1,0,1]]
        System.out.println(threeSum(new int[]{0,1,1}));          // []
        System.out.println(threeSum(new int[]{0,0,0}));          // [[0,0,0]]
    }
}