package org.example.arrays;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class TwoSum {

    /**
     * One pass: for each number, check if its complement (target - num) was
     * already seen. The map stores value -> index, so the answer for the current
     * number is available the moment its partner appears — no second loop needed.
     *
     * Time:  O(n)  — single scan, O(1) map lookups.
     * Space: O(n)  — up to n entries in the map.
     */
    public static int[] twoSum(int[] nums, int target) {
        Map<Integer, Integer> seen = new HashMap<>();
        for (int i = 0; i < nums.length; i++) {
            int complement = target - nums[i];
            if (seen.containsKey(complement)) {
                return new int[]{seen.get(complement), i};   // earlier index first
            }
            seen.put(nums[i], i);
        }
        return new int[]{-1, -1};   // problem guarantees a solution; defensive default
    }

    public static void main(String[] args) {
        System.out.println(Arrays.toString(twoSum(new int[]{2, 7, 11, 15}, 9)));  // expected: [0, 1]
        System.out.println(Arrays.toString(twoSum(new int[]{3, 2, 4}, 6)));       // expected: [1, 2]
        System.out.println(Arrays.toString(twoSum(new int[]{3, 3}, 6)));          // expected: [0, 1]
    }
}
