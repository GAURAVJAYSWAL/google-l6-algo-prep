package org.example.backtracking;

import java.util.ArrayList;
import java.util.List;

public class Subsets {

    /**
     * Each element is independently either in or out of a subset, so the powerset
     * is a binary decision tree of height n. At index i branch twice: exclude
     * nums[i] (recurse without it) and include nums[i] (add, recurse, then remove).
     * Every leaf at depth n is one subset; there are exactly 2^n of them.
     * Time:  O(n * 2^n) — 2^n subsets, O(n) to copy each into the result.
     * Space: O(n) — recursion depth and the path buffer (output excluded).
     */
    public static List<List<Integer>> subsets(int[] nums) {
        List<List<Integer>> out = new ArrayList<>();
        backtrack(nums, 0, new ArrayList<>(), out);
        return out;
    }

    private static void backtrack(int[] nums, int idx, List<Integer> path, List<List<Integer>> out) {
        if (idx == nums.length) {                // decided every element -> one subset
            out.add(new ArrayList<>(path));
            return;
        }
        backtrack(nums, idx + 1, path, out);     // exclude nums[idx]
        path.add(nums[idx]);                     // include nums[idx]
        backtrack(nums, idx + 1, path, out);
        path.remove(path.size() - 1);            // un-choose
    }

    public static void main(String[] args) {
        System.out.println(subsets(new int[]{1, 2, 3}));
        // expected: [[], [3], [2], [2, 3], [1], [1, 3], [1, 2], [1, 2, 3]]

        System.out.println(subsets(new int[]{0}));
        // expected: [[], [0]]

        System.out.println(subsets(new int[]{1, 2, 3, 4}).size());
        // expected: 16

        System.out.println(subsets(new int[]{}));
        // expected: [[]]
    }
}
