package org.example.heap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TopKFrequentElements {

    /**
     * A frequency can never exceed n, so index buckets by frequency: bucket[f] lists every
     * value seen exactly f times. Walking buckets from high frequency down and collecting
     * until we have k yields the answer in linear time, sidestepping the O(n log k) heap
     * route entirely. (Heap alternative: push (value, freq) into a min-heap of size k,
     * evicting the least frequent — simpler to reason about but log-factor slower.)
     *
     * Time:  O(n) — count, scatter into buckets, then one descending pass over n+1 buckets.
     * Space: O(n) — the frequency map plus the bucket array.
     */
    public static int[] topKFrequent(int[] nums, int k) {
        Map<Integer, Integer> freq = new HashMap<>();
        for (int num : nums) freq.merge(num, 1, Integer::sum);

        // bucket[f] holds all values with frequency exactly f; max possible frequency is nums.length.
        List<Integer>[] buckets = new List[nums.length + 1];
        for (Map.Entry<Integer, Integer> e : freq.entrySet()) {
            int f = e.getValue();
            if (buckets[f] == null) buckets[f] = new ArrayList<>();
            buckets[f].add(e.getKey());
        }

        int[] result = new int[k];
        int idx = 0;
        // Highest frequencies first; stop the moment we have collected k values.
        for (int f = buckets.length - 1; f >= 1 && idx < k; f--) {
            if (buckets[f] == null) continue;
            for (int val : buckets[f]) {
                result[idx++] = val;
                if (idx == k) break;
            }
        }
        return result;
    }

    public static void main(String[] args) {
        System.out.println(Arrays.toString(topKFrequent(new int[]{1, 1, 1, 2, 2, 3}, 2))); // expected: [1, 2]
        System.out.println(Arrays.toString(topKFrequent(new int[]{1}, 1)));                // expected: [1]
        System.out.println(Arrays.toString(topKFrequent(new int[]{4, 4, 4, 5, 5, 6}, 1))); // expected: [4]
        System.out.println(Arrays.toString(topKFrequent(new int[]{7, 7, 8, 8, 9}, 3)));    // expected: [7, 8, 9]
    }
}
