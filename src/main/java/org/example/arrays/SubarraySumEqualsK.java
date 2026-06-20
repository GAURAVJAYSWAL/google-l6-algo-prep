package org.example.arrays;

import java.util.HashMap;
import java.util.Map;

public class SubarraySumEqualsK {

    /**
     * A subarray (j, i] sums to k exactly when prefix[i] - prefix[j] == k, i.e.
     * prefix[j] == prefix[i] - k. Keep a count of every prefix sum seen so far;
     * at each i, the number of valid starts is how many times (prefix - k) has
     * occurred. Seeding the map with {0:1} lets prefixes that themselves equal k count.
     *
     * Time:  O(n)  — one pass, O(1) map ops.
     * Space: O(n)  — distinct prefix sums.
     */
    public static int subarraySum(int[] nums, int k) {
        Map<Integer, Integer> prefixCount = new HashMap<>();
        prefixCount.put(0, 1);   // empty prefix: enables subarrays starting at index 0

        int prefix = 0;
        int count = 0;
        for (int num : nums) {
            prefix += num;
            count += prefixCount.getOrDefault(prefix - k, 0);   // starts j where prefix[j] == prefix - k
            prefixCount.merge(prefix, 1, Integer::sum);
        }
        return count;
    }

    public static void main(String[] args) {
        System.out.println(subarraySum(new int[]{1, 1, 1}, 2));        // expected: 2
        System.out.println(subarraySum(new int[]{1, 2, 3}, 3));        // expected: 2  ([1,2] and [3])
        System.out.println(subarraySum(new int[]{1, -1, 0}, 0));      // expected: 3
        System.out.println(subarraySum(new int[]{-1, -1, 1}, 0));     // expected: 1
    }
}
