package org.example.stringmatching;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * LC 187. Repeated DNA Sequences.
 */
public class RepeatedDNASequences {

    /**
     * Key insight: there are only four bases, so each fits in 2 bits and a 10-letter window packs into
     * a 20-bit integer — a perfect rolling hash with NO collisions. Slide the window one base at a
     * time: shift the 20-bit code left by 2, OR in the new base's code, and mask to 20 bits, which
     * drops the base that fell off the left. A "seen" set records every window's code; the first time
     * a code reappears we add its substring to an "added" set (so each repeated sequence is reported
     * once). The 2-bit encoding turns substring hashing into pure integer arithmetic, far cheaper than
     * hashing 10-char strings every step.
     *
     * Time:  O(n) — one constant-work step per starting index (windows shorter than 10 are skipped).
     * Space: O(n) — the seen and added sets hold up to O(n) distinct 20-bit codes.
     */
    public static List<String> findRepeatedDnaSequences(String s) {
        List<String> result = new ArrayList<>();
        int n = s.length();
        if (n < 10) return result; // no window of length 10 fits

        int[] code = new int[128];
        code['A'] = 0; code['C'] = 1; code['G'] = 2; code['T'] = 3; // 2 bits per base
        int mask = (1 << 20) - 1; // keep only the low 20 bits (10 bases * 2)

        Set<Integer> seen = new HashSet<>();
        Set<Integer> added = new HashSet<>(); // guards against emitting the same sequence twice
        int hash = 0;

        for (int i = 0; i < n; i++) {
            hash = ((hash << 2) | code[s.charAt(i)]) & mask; // append new base, evict the oldest
            if (i >= 9) { // a full 10-base window now ends at i
                if (!seen.add(hash) && added.add(hash)) {
                    result.add(s.substring(i - 9, i + 1));
                }
            }
        }
        return result;
    }

    public static void main(String[] args) {
        System.out.println(findRepeatedDnaSequences("AAAAACCCCCAAAAACCCCCCAAAAAGGGTTT"));
        // expected: [AAAAACCCCC, CCCCCAAAAA]
        System.out.println(findRepeatedDnaSequences("AAAAAAAAAAAAA")); // expected: [AAAAAAAAAA]
        System.out.println(findRepeatedDnaSequences("AAAAAAAAAA"));    // expected: [] (appears only once)
        System.out.println(findRepeatedDnaSequences("ACGT"));          // expected: [] (shorter than 10)
    }
}
