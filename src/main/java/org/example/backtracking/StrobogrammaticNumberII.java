package org.example.backtracking;

import java.util.ArrayList;
import java.util.List;

/**
 * LeetCode 247 — Strobogrammatic Number II (Medium).
 *
 * A strobogrammatic number reads the same after a 180° rotation. Return every such number
 * of length n (as strings, no leading zeros except the number "0" itself).
 */
public class StrobogrammaticNumberII {

    // The only digits that survive a 180° rotation, paired as (top, its rotated bottom).
    private static final char[][] PAIRS = {
            {'0', '0'}, {'1', '1'}, {'6', '9'}, {'8', '8'}, {'9', '6'}
    };

    /**
     * Every strobogrammatic number is symmetric: rotating it swaps position i with position
     * (len-1-i) AND maps each digit to its rotational partner. Since only five mirror-pairs
     * survive rotation, we can construct numbers from the outside in — fixing a valid (left,right)
     * pair at each step and recursing toward the centre — so the recursion enumerates exactly the
     * valid numbers and nothing else. Two guards keep it correct: the outermost pair skips (0,0)
     * to avoid leading zeros (unless n==1), and an odd centre digit must be self-rotatable {0,1,8}.
     * Time:  O(5^(n/2)) — five choices per symmetric pair, ~n/2 pairs.
     * Space: O(n) — recursion depth plus the char buffer being filled.
     */
    public List<String> findStrobogrammatic(int n) {
        List<String> result = new ArrayList<>();
        build(new char[n], 0, n - 1, result);
        return result;
    }

    private void build(char[] buf, int left, int right, List<String> result) {
        if (left > right) {                          // crossed the middle: a complete number
            result.add(new String(buf));
            return;
        }
        if (left == right) {                         // exact centre of an odd length
            for (char[] pair : PAIRS) {
                if (pair[0] == pair[1]) {            // only self-rotatable digits: 0, 1, 8
                    buf[left] = pair[0];
                    result.add(new String(buf));
                }
            }
            return;
        }
        for (char[] pair : PAIRS) {
            // No leading zero: the outermost pair (left == 0) may not start with '0' for n > 1.
            if (left == 0 && pair[0] == '0') continue;
            buf[left] = pair[0];
            buf[right] = pair[1];
            build(buf, left + 1, right - 1, result);
        }
    }

    public static void main(String[] args) {
        StrobogrammaticNumberII s = new StrobogrammaticNumberII();

        System.out.println(s.findStrobogrammatic(1)); // expected: [0, 1, 8]
        System.out.println(s.findStrobogrammatic(2)); // expected: [11, 69, 88, 96]
        // n=3: outer pair in {(1,1),(6,9),(8,8),(9,6)}, centre in {0,1,8}.
        System.out.println(s.findStrobogrammatic(3)); // expected: [101, 111, 181, 609, 619, 689, 808, 818, 888, 906, 916, 986]
    }
}
