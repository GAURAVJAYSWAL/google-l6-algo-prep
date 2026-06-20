package org.example.strings;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GroupAnagrams {

    /**
     * Words are anagrams iff they share the same letter-count signature, so build a
     * canonical key from each word's 26 counts and bucket by it. Using the count
     * vector (not a sorted string) keys in O(word length) instead of O(len log len) —
     * the optimal way to fingerprint when the alphabet is fixed.
     *
     * Time:  O(n * k)  — n words, k = max word length, to build each key.
     * Space: O(n * k)  — keys plus the grouped words stored in the map.
     */
    public static List<List<String>> groupAnagrams(String[] strs) {
        Map<String, List<String>> groups = new HashMap<>();

        for (String word : strs) {
            int[] count = new int[26];
            for (char c : word.toCharArray()) count[c - 'a']++;

            // Encode counts into a stable string key; '#' delimits to avoid e.g. "1,11" collisions.
            StringBuilder key = new StringBuilder();
            for (int c : count) key.append(c).append('#');

            groups.computeIfAbsent(key.toString(), k -> new ArrayList<>()).add(word);
        }
        return new ArrayList<>(groups.values());
    }

    public static void main(String[] args) {
        System.out.println(groupAnagrams(new String[]{"eat", "tea", "tan", "ate", "nat", "bat"}));
        // expected: [[eat, tea, ate], [tan, nat], [bat]] (group order may vary)
        System.out.println(groupAnagrams(new String[]{""}));    // expected: [[]]
        System.out.println(groupAnagrams(new String[]{"a"}));   // expected: [[a]]
    }
}
