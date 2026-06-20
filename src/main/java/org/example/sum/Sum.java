package org.example.sum;

import java.util.*;

public class Sum {
    static void main() {
        int[] numbers = {2, 7, 11, 15};
        int target = 9;//   →  Output: [1,2]
        //Input:  numbers = [2,3,4], target = 6        →  Output: [1,3]
        //Input:  numbers = [-1,0], target = -1        →  Output: [1,2]
        System.out.println(Arrays.toString(getSumArrayV2(numbers, 9)));
    }

    public static int[] getSumArray(int[] numbers, int target) {
        int[] sumArray = new int[2];
        Map<Integer, Integer> map = new HashMap<>();
        for (int number : numbers) {
            map.put(number, map.getOrDefault(number, 0) + 1);
        }
        for (int number : numbers) {
            int diff = target - number;
            map.put(number, map.getOrDefault(number, 0) + 1);
            if (diff == target && map.containsKey(diff) && map.get(diff) > 1) {
                sumArray[0] = diff;
                sumArray[1] = diff;
                return sumArray;
            } else if (diff != target && map.containsKey(diff)) {
                sumArray[0] = diff;
                sumArray[1] = number;
                return sumArray;
            }

        }
        return sumArray;
    }


    public static int[] getSumArrayV2(int[] numbers, int target) {
        int start = 0;
        int end = numbers.length - 1;
        while (start < end) {
            int sum = numbers[start] + numbers[end];
            if (sum == target) {
                return new int[]{numbers[start], numbers[end]};
            } else if (sum < target) {
                start++;
            } else {
                --end;
            }
        }
        return null;
    }

    public static List<List<Integer>> threeSum(int[] numbers) {
        Arrays.sort(numbers);
        List<List<Integer>> results = new ArrayList<>();
        for (int i = 0; i < numbers.length - 2; i++) {
            if (numbers[i] > 0) break;                              // fixed: break on positive
            if (i > 0 && numbers[i] == numbers[i - 1]) continue;    // skip duplicate anchor
            int left = i + 1, right = numbers.length - 1;
            while (left < right) {
                int sum = numbers[i] + numbers[left] + numbers[right];
                if (sum == 0) {
                    results.add(Arrays.asList(numbers[i], numbers[left], numbers[right]));
                    left++;
                    right--;
                    while (left < right && numbers[left] == numbers[left - 1]) left++;   // skip dup
                    while (left < right && numbers[right] == numbers[right + 1]) right--; // skip dup
                } else if (sum > 0) {
                    right--;
                } else {
                    left++;
                }
            }
        }
        return results;
    }

    public static int getMaxWater(int[] numbers) {
        int sum = 0;
        if (numbers.length <= 1) return 0;
        int left = 0;
        int right = numbers.length - 1;
        while (left < right) {
            sum = Math.max(sum, (Math.min(numbers[left], numbers[right]) * (right - left)));
            if (numbers[left] < numbers[right]) {
                ++left;
            } else {
                --right;
            }
        }
        return sum;
    }

    /**
     * Container With Most Water
     * Approach: greedy two pointers.
     * Area between two lines = min(height[left], height[right]) * (right - left).
     * Start at the widest possible container and move inward. The shorter line is
     * always the bottleneck, so we move that pointer — moving the taller one can
     * only shrink width without lifting the height cap, so it can never help.
     * Time:  O(n) — each element visited at most once.
     * Space: O(1).
     * Assumptions (worth confirming with the interviewer):
     *  - n >= 2 is guaranteed by constraints; if not, an empty/size-1 array holds 0 water.
     *  - heights are non-negative.
     */
    public static int maxArea(int[] height) {
        int left = 0;
        int right = height.length - 1;
        int maxWater = 0;

        while (left < right) {
            int width = right - left;
            int containedHeight = Math.min(height[left], height[right]);
            maxWater = Math.max(maxWater, width * containedHeight);

            // Move the pointer at the shorter line — the only move that can improve the result.
            if (height[left] < height[right]) {
                left++;
            } else {
                right--;
            }
        }

        return maxWater;
    }
}
