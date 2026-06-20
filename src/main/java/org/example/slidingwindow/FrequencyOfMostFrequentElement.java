package org.example.slidingwindow;

import java.util.Arrays;

public class FrequencyOfMostFrequentElement {

    /**
     * Sort, then for each right we try to raise every element in window [left, right]
     * up to nums[right] (the window's max). The cost to do so is
     * nums[right]*windowLen - sum(window); while that exceeds budget k, shrink from
     * the left. The largest valid window size is the answer. Sorting is what makes
     * "raise everyone to the rightmost value" the cheapest possible leveling.
     *
     * Time:  O(n log n)  — sort dominates; the two-pointer sweep is O(n).
     * Space: O(1) extra  — only a running window sum (ignoring the sort).
     */
    public static int maxFrequency(int[] nums, long k) {
        Arrays.sort(nums);
        long windowSum = 0;
        int left = 0, best = 1;

        for (int right = 0; right < nums.length; right++) {
            windowSum += nums[right];
            // Cost to lift all windowLen elements to nums[right]; shrink until it fits in k.
            while ((long) nums[right] * (right - left + 1) - windowSum > k) {
                windowSum -= nums[left];
                left++;
            }
            best = Math.max(best, right - left + 1);
        }
        return best;
    }

    public static void main(String[] args) {
        System.out.println(maxFrequency(new int[]{1, 2, 4}, 5));       // expected: 3
        System.out.println(maxFrequency(new int[]{1, 4, 8, 13}, 5));   // expected: 2
        System.out.println(maxFrequency(new int[]{3, 9, 6}, 2));       // expected: 1
        System.out.println(maxFrequency(new int[]{1, 1, 1, 2, 2, 4}, 0)); // expected: 3
    }
}
