package org.example.graphs;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * LC 269. Alien Dictionary.
 */
public class AlienDictionary {

    /**
     * Key insight: the dictionary being sorted means each adjacent pair of words reveals
     * one ordering constraint — the first position where they differ gives "earlier char
     * precedes later char". Those constraints form a precedence graph over the distinct
     * letters; a valid alphabet is any topological order of it. Kahn's BFS produces such
     * an order and detects a cycle (contradiction) when fewer than all letters emerge.
     * Special invalid case: if a word is a prefix of the previous, longer word (e.g.
     * "abc" before "ab"), no ordering can justify it, so we return "".
     *
     * Time:  O(C) — C is the total length of all words; building edges and the toposort are linear in C.
     * Space: O(1) — at most 26 nodes and 26*26 edges, i.e. constant.
     */
    public static String alienOrder(String[] words) {
        // Seed every distinct character so isolated letters still appear in the output.
        Map<Character, Set<Character>> adj = new HashMap<>();
        Map<Character, Integer> indegree = new HashMap<>();
        for (String w : words) {
            for (char ch : w.toCharArray()) {
                adj.putIfAbsent(ch, new HashSet<>());
                indegree.putIfAbsent(ch, 0);
            }
        }

        // Derive one edge per adjacent word pair from their first differing character.
        for (int i = 0; i + 1 < words.length; i++) {
            String a = words[i], b = words[i + 1];
            int len = Math.min(a.length(), b.length());
            int j = 0;
            while (j < len && a.charAt(j) == b.charAt(j)) j++;
            if (j == len) {
                // No difference found within the shared prefix.
                if (a.length() > b.length()) return ""; // longer word ranked first -> invalid
                continue;                               // b extends a, which is fine
            }
            char from = a.charAt(j), to = b.charAt(j);
            if (adj.get(from).add(to)) {                // count each distinct edge exactly once
                indegree.merge(to, 1, Integer::sum);
            }
        }

        Deque<Character> queue = new ArrayDeque<>();
        for (Map.Entry<Character, Integer> e : indegree.entrySet()) {
            if (e.getValue() == 0) queue.offer(e.getKey());
        }

        StringBuilder order = new StringBuilder();
        while (!queue.isEmpty()) {
            char ch = queue.poll();
            order.append(ch);
            for (char next : adj.get(ch)) {
                if (indegree.merge(next, -1, Integer::sum) == 0) queue.offer(next);
            }
        }

        // If not every letter was emitted, a precedence cycle exists.
        return order.length() == indegree.size() ? order.toString() : "";
    }

    public static void main(String[] args) {
        System.out.println(alienOrder(new String[]{"wrt", "wrf", "er", "ett", "rftt"})); // expected: wertf
        System.out.println(alienOrder(new String[]{"z", "x"}));                          // expected: zx
        System.out.println(alienOrder(new String[]{"z", "x", "z"}));                     // expected: (empty -> cycle)
        System.out.println(alienOrder(new String[]{"abc", "ab"}));                       // expected: (empty -> invalid prefix)
    }
}
