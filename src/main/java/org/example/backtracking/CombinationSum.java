package org.example.backtracking;

import java.util.ArrayList;
import java.util.List;

public class CombinationSum {

    /**
     * Distinct candidates, each reusable unlimited times. To avoid permuted
     * duplicates we only ever move forward or stay: recursing on the SAME index
     * allows reuse, while starting the loop at start forbids revisiting earlier
     * candidates. Prune as soon as remaining < 0 (this candidate overshoots), and
     * record a combination when remaining hits exactly 0.
     * Time:  O(n^(T/m)) — branching n, depth bounded by target/min-candidate.
     * Space: O(T/m) — recursion depth and the path (output excluded).
     */
    public static List<List<Integer>> combinationSum(int[] candidates, int target) {
        List<List<Integer>> out = new ArrayList<>();
        backtrack(candidates, 0, target, new ArrayList<>(), out);
        return out;
    }

    private static void backtrack(int[] cand, int start, int remaining, List<Integer> path, List<List<Integer>> out) {
        if (remaining == 0) {                    // exact hit -> a valid combination
            out.add(new ArrayList<>(path));
            return;
        }
        if (remaining < 0) return;               // overshoot -> dead branch, prune
        for (int i = start; i < cand.length; i++) {
            path.add(cand[i]);                   // choose cand[i]
            backtrack(cand, i, remaining - cand[i], path, out); // i (not i+1): allow reuse
            path.remove(path.size() - 1);        // un-choose
        }
    }

    public static void main(String[] args) {
        System.out.println(combinationSum(new int[]{2, 3, 6, 7}, 7));
        // expected: [[2, 2, 3], [7]]

        System.out.println(combinationSum(new int[]{2, 3, 5}, 8));
        // expected: [[2, 2, 2, 2], [2, 3, 3], [3, 5]]

        System.out.println(combinationSum(new int[]{2}, 1));
        // expected: []

        System.out.println(combinationSum(new int[]{3, 5, 8}, 11));
        // expected: [[3, 3, 5], [3, 8]]
    }
}
