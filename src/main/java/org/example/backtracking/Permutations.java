package org.example.backtracking;

import java.util.ArrayList;
import java.util.List;

public class Permutations {

    /**
     * A permutation fixes one element per position. At depth d try every element
     * not yet used, mark it used, recurse to d+1, then un-mark it so the next
     * sibling branch can reuse it. The used[] flags make each element appear
     * exactly once along any root-to-leaf path; a leaf at depth n is one full
     * permutation.
     * Time:  O(n * n!) — n! leaves, O(n) to copy each into the result.
     * Space: O(n) — recursion depth, path, and used[] (output excluded).
     */
    public static List<List<Integer>> permute(int[] nums) {
        List<List<Integer>> out = new ArrayList<>();
        backtrack(nums, new boolean[nums.length], new ArrayList<>(), out);
        return out;
    }

    private static void backtrack(int[] nums, boolean[] used, List<Integer> path, List<List<Integer>> out) {
        if (path.size() == nums.length) {        // every slot filled -> a full permutation
            out.add(new ArrayList<>(path));
            return;
        }
        for (int i = 0; i < nums.length; i++) {
            if (used[i]) continue;               // each element used at most once per path
            used[i] = true;                      // choose
            path.add(nums[i]);
            backtrack(nums, used, path, out);
            path.remove(path.size() - 1);        // un-choose
            used[i] = false;
        }
    }

    public static void main(String[] args) {
        System.out.println(permute(new int[]{1, 2, 3}));
        // expected: [[1, 2, 3], [1, 3, 2], [2, 1, 3], [2, 3, 1], [3, 1, 2], [3, 2, 1]]

        System.out.println(permute(new int[]{0, 1}));
        // expected: [[0, 1], [1, 0]]

        System.out.println(permute(new int[]{7}));
        // expected: [[7]]

        System.out.println(permute(new int[]{1, 2, 3, 4}).size());
        // expected: 24
    }
}
