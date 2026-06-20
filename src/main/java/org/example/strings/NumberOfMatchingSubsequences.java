package org.example.strings;

import java.util.ArrayDeque;
import java.util.Deque;

public class NumberOfMatchingSubsequences {

    /**
     * Instead of scanning s once per word (O(|words| * |s|)), we scan s ONCE and let
     * the characters of s pull every word forward together. Bucket each word by the
     * char it currently waits for. As we read char c from s, every word parked in
     * bucket[c] has its next char satisfied: advance each one and re-file it under
     * its new waiting char — or count it as matched if it ran out of chars. A word
     * touches a bucket only when its current char matches, so total work is bounded
     * by |s| plus the sum of word lengths.
     *
     * Time:  O(|s| + sum of word lengths)  — each char of each word causes at most one re-bucket.
     * Space: O(sum of word lengths)        — pointers held across the 26 buckets.
     */
    public static int numMatchingSubseq(String s, String[] words) {
        // Each entry tracks a word and how many of its chars are already matched.
        Deque<int[]>[] buckets = new ArrayDeque[26];
        for (int i = 0; i < 26; i++) buckets[i] = new ArrayDeque<>();

        for (int w = 0; w < words.length; w++) {
            buckets[words[w].charAt(0) - 'a'].add(new int[]{w, 0}); // park on the first needed char
        }

        int matched = 0;
        for (int i = 0; i < s.length(); i++) {
            Deque<int[]> waiting = buckets[s.charAt(i) - 'a'];
            int size = waiting.size();                 // snapshot: only advance words waiting BEFORE this char
            for (int t = 0; t < size; t++) {
                int[] cur = waiting.poll();
                int word = cur[0], pos = cur[1] + 1;   // this char satisfied position cur[1]
                if (pos == words[word].length()) {
                    matched++;                         // consumed the whole word
                } else {
                    buckets[words[word].charAt(pos) - 'a'].add(new int[]{word, pos}); // re-file under next char
                }
            }
        }
        return matched;
    }

    public static void main(String[] args) {
        System.out.println(numMatchingSubseq("abcde", new String[]{"a", "bb", "acd", "ace"})); // expected: 3
        System.out.println(numMatchingSubseq("dsahjpjauf", new String[]{"ahjpjau", "ja", "ahbwzgqnuk", "tnmlanowax"})); // expected: 2
        System.out.println(numMatchingSubseq("abcabc", new String[]{"abc", "abca", "ac", "x"})); // expected: 3
    }
}
