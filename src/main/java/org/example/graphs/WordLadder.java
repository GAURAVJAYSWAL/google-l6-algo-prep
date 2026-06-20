package org.example.graphs;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

/**
 * LC 127. Word Ladder.
 */
public class WordLadder {

    /**
     * Key insight: words are nodes and an edge joins two words differing in exactly one
     * letter; the shortest ladder is then a shortest path, which unweighted BFS finds.
     * The trick is generating neighbors cheaply: instead of comparing against the whole
     * dictionary (O(N) per word), we map each word to L wildcard patterns like "h*t" and
     * group words by pattern. Two words sharing a pattern are exactly one edit apart, so
     * neighbor lookup costs O(L) instead of O(N*L).
     *
     * Time:  O(N * L^2) — N words, each forming L patterns, each pattern of length L to build.
     * Space: O(N * L^2) — the pattern-to-words index.
     */
    public static int ladderLength(String beginWord, String endWord, List<String> wordList) {
        Set<String> dict = new HashSet<>(wordList);
        if (!dict.contains(endWord)) return 0; // target unreachable if absent from the dictionary

        // Bucket every word under each of its single-wildcard patterns.
        Map<String, List<String>> patterns = new HashMap<>();
        for (String word : dict) {
            char[] chars = word.toCharArray();
            for (int i = 0; i < chars.length; i++) {
                char original = chars[i];
                chars[i] = '*';
                patterns.computeIfAbsent(new String(chars), k -> new ArrayList<>()).add(word);
                chars[i] = original;
            }
        }

        Queue<String> queue = new ArrayDeque<>();
        Set<String> visited = new HashSet<>();
        queue.offer(beginWord);
        visited.add(beginWord);
        int level = 1; // length counts words (nodes), so the start is level 1

        while (!queue.isEmpty()) {
            int size = queue.size();
            for (int i = 0; i < size; i++) {
                String word = queue.poll();
                if (word.equals(endWord)) return level;

                char[] chars = word.toCharArray();
                for (int j = 0; j < chars.length; j++) {
                    char original = chars[j];
                    chars[j] = '*';
                    String pattern = new String(chars);
                    chars[j] = original;
                    for (String next : patterns.getOrDefault(pattern, List.of())) {
                        if (visited.add(next)) queue.offer(next); // add() returns false if already seen
                    }
                }
            }
            level++;
        }
        return 0; // exhausted the component without reaching endWord
    }

    public static void main(String[] args) {
        System.out.println(ladderLength("hit", "cog", List.of("hot", "dot", "dog", "lot", "log", "cog")));
        // expected: 5  (hit -> hot -> dot -> dog -> cog)

        System.out.println(ladderLength("hit", "cog", List.of("hot", "dot", "dog", "lot", "log")));
        // expected: 0  (endWord not in dictionary)

        System.out.println(ladderLength("a", "c", List.of("a", "b", "c")));
        // expected: 2  (a -> c)
    }
}
