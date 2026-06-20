package org.example.bitmaskdp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SmallestSufficientTeam {

    /**
     * Key insight: with at most 16 required skills, the set of covered skills fits in an int
     * bitmask, so we DP over coverage rather than over people. State: dp[mask] = the smallest
     * team (as a person-bitmask) whose combined skills cover exactly the skills in `mask`.
     * Transition: from a reachable coverage `mask`, adding person p produces coverage
     * mask | skillOf[p]; we keep whichever team for the new coverage has fewer people. We store
     * the team itself as a long person-bitmask (people <= 60) so we can compare sizes and
     * reconstruct directly via bit positions — no separate parent array needed.
     * Time:  O(2^m * P) — m skills give 2^m coverage states, each tried against P people.
     * Space: O(2^m)     — one team bitmask per coverage state.
     */
    public static int[] smallestSufficientTeam(String[] reqSkills, List<List<String>> people) {
        int m = reqSkills.length;
        Map<String, Integer> skillBit = new HashMap<>();
        for (int i = 0; i < m; i++) skillBit.put(reqSkills[i], i); // map each skill to its bit index

        // Precompute each person's skill mask (only skills that are actually required).
        int[] skillOf = new int[people.size()];
        for (int p = 0; p < people.size(); p++) {
            int mask = 0;
            for (String s : people.get(p)) {
                Integer bit = skillBit.get(s);
                if (bit != null) mask |= (1 << bit);
            }
            skillOf[p] = mask;
        }

        int full = (1 << m) - 1;
        long[] dp = new long[1 << m];   // dp[mask] = person-bitmask of the best team covering `mask`
        boolean[] seen = new boolean[1 << m];
        dp[0] = 0L;                     // covering nothing needs nobody
        seen[0] = true;

        for (int mask = 0; mask <= full; mask++) {
            if (!seen[mask]) continue;  // unreachable coverage state
            for (int p = 0; p < people.size(); p++) {
                int nextMask = mask | skillOf[p];
                if (nextMask == mask) continue;               // person adds no new skill here
                long candidate = dp[mask] | (1L << p);        // tentative team = old team + person p
                if (!seen[nextMask] || Long.bitCount(candidate) < Long.bitCount(dp[nextMask])) {
                    seen[nextMask] = true;
                    dp[nextMask] = candidate;                 // keep the smaller team for this coverage
                }
            }
        }

        // Reconstruct: the set bits of dp[full] are the chosen people.
        long team = dp[full];
        List<Integer> result = new ArrayList<>();
        for (int p = 0; team != 0; p++, team >>= 1) {
            if ((team & 1) == 1) result.add(p);
        }
        int[] out = new int[result.size()];
        for (int i = 0; i < out.length; i++) out[i] = result.get(i);
        return out;
    }

    public static void main(String[] args) {
        System.out.println(java.util.Arrays.toString(smallestSufficientTeam(
                new String[]{"java", "nodejs", "reactjs"},
                List.of(List.of("java"), List.of("nodejs"), List.of("nodejs", "reactjs")))));
        // expected: [0, 2]

        System.out.println(java.util.Arrays.toString(smallestSufficientTeam(
                new String[]{"algorithms", "math", "java", "reactjs", "css", "aws"},
                List.of(List.of("algorithms", "math", "java"),
                        List.of("algorithms", "math", "reactjs"),
                        List.of("java", "css", "aws"),
                        List.of("reactjs", "css"),
                        List.of("css", "math", "aws")))));
        // expected: [1, 2]

        System.out.println(java.util.Arrays.toString(smallestSufficientTeam(
                new String[]{"a"},
                List.of(List.of("a"), List.of("a")))));
        // expected: [0]
    }
}
