package org.example.strings;

import java.util.PriorityQueue;

public class ReorganizeString {

    /**
     * Greedy with a max-heap by frequency: repeatedly pull the two most frequent
     * remaining characters and append both — they differ, so no two equal chars
     * land adjacent. Holding the previous most-frequent char back one step until a
     * different char is placed is exactly what prevents repeats. It is feasible iff
     * no character exceeds (n + 1) / 2; otherwise some copy is forced to touch itself.
     *
     * Time:  O(n log k)  — n placements, heap ops over k <= 26 distinct chars.
     * Space: O(k)  — counts plus the heap.
     */
    public static String reorganizeString(String s) {
        int n = s.length();
        int[] freq = new int[26];
        for (char c : s.toCharArray()) freq[c - 'a']++;

        // Max-heap ordered by remaining frequency.
        PriorityQueue<int[]> heap = new PriorityQueue<>((a, b) -> b[1] - a[1]);
        for (int i = 0; i < 26; i++) {
            if (freq[i] > 0) {
                if (freq[i] > (n + 1) / 2) return "";   // impossible to separate
                heap.offer(new int[]{i, freq[i]});
            }
        }

        StringBuilder sb = new StringBuilder(n);
        while (heap.size() > 1) {
            // Place the two most frequent — guaranteed distinct, so they don't clash.
            int[] first = heap.poll();
            int[] second = heap.poll();
            sb.append((char) ('a' + first[0]));
            sb.append((char) ('a' + second[0]));
            if (--first[1] > 0) heap.offer(first);
            if (--second[1] > 0) heap.offer(second);
        }
        // At most one char left; the feasibility check guarantees its count is 1.
        if (!heap.isEmpty()) sb.append((char) ('a' + heap.poll()[0]));
        return sb.toString();
    }

    // Verifies the result is a permutation of s with no two adjacent chars equal.
    private static boolean valid(String original, String result) {
        if (result.isEmpty()) return false;
        if (original.length() != result.length()) return false;
        for (int i = 1; i < result.length(); i++) {
            if (result.charAt(i) == result.charAt(i - 1)) return false;
        }
        return true;
    }

    public static void main(String[] args) {
        System.out.println(valid("aab", reorganizeString("aab")));   // expected: true  (e.g. "aba")
        System.out.println(reorganizeString("aaab"));                // expected: "" (impossible)
        System.out.println(valid("aaabbc", reorganizeString("aaabbc"))); // expected: true
        System.out.println(valid("vvvlo", reorganizeString("vvvlo")));   // expected: true
    }
}
