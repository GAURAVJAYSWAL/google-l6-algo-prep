package org.example.trie;

import java.util.ArrayList;
import java.util.List;

/**
 * LC 212. Word Search II.
 */
public class WordSearchII {

    static class TrieNode {
        TrieNode[] children = new TrieNode[26];
        String word;   // non-null only at a node terminating an inserted word
    }

    /**
     * Key insight: searching each word independently re-walks the same grid prefixes
     * over and over. Instead, build ONE trie of all words and DFS the grid once: the
     * trie's edges tell us which characters are still worth pursuing, so a path dies
     * the moment it stops being a prefix of any word. Storing the full word at its
     * terminal node makes collection O(1), and nulling that word after capture
     * deduplicates the same word reachable by multiple paths.
     *
     * Time:  O(M*N * 4 * 3^(L-1)) — every cell seeds a DFS bounded by trie depth L,
     *        branching to 3 unused neighbours after the first step.
     * Space: O(total characters in words) for the trie, plus O(L) recursion depth.
     */
    public static List<String> findWords(char[][] board, String[] words) {
        TrieNode root = buildTrie(words);
        List<String> result = new ArrayList<>();
        for (int r = 0; r < board.length; r++) {
            for (int c = 0; c < board[0].length; c++) {
                dfs(board, r, c, root, result);
            }
        }
        return result;
    }

    private static void dfs(char[][] board, int r, int c, TrieNode node, List<String> result) {
        if (r < 0 || r >= board.length || c < 0 || c >= board[0].length) return;
        char ch = board[r][c];
        if (ch == '#') return;                        // already on the current path
        TrieNode next = node.children[ch - 'a'];
        if (next == null) return;                     // prefix prune: no word continues here

        if (next.word != null) {
            result.add(next.word);
            next.word = null;                         // dedupe: capture each word once
        }

        board[r][c] = '#';                            // mark visited for this path
        dfs(board, r + 1, c, next, result);
        dfs(board, r - 1, c, next, result);
        dfs(board, r, c + 1, next, result);
        dfs(board, r, c - 1, next, result);
        board[r][c] = ch;                             // restore for sibling paths
    }

    private static TrieNode buildTrie(String[] words) {
        TrieNode root = new TrieNode();
        for (String w : words) {
            TrieNode node = root;
            for (int i = 0; i < w.length(); i++) {
                int idx = w.charAt(i) - 'a';
                if (node.children[idx] == null) node.children[idx] = new TrieNode();
                node = node.children[idx];
            }
            node.word = w;
        }
        return root;
    }

    public static void main(String[] args) {
        char[][] board1 = {
                {'o', 'a', 'a', 'n'},
                {'e', 't', 'a', 'e'},
                {'i', 'h', 'k', 'r'},
                {'i', 'f', 'l', 'v'}
        };
        System.out.println(findWords(board1, new String[]{"oath", "pea", "eat", "rain"}));
        // expected: [oath, eat]

        char[][] board2 = {
                {'a', 'b'},
                {'c', 'd'}
        };
        System.out.println(findWords(board2, new String[]{"abcb"}));
        // expected: [] (cannot reuse the same cell)

        char[][] board3 = {{'a', 'a'}};
        System.out.println(findWords(board3, new String[]{"aaa"}));
        // expected: [] (only two cells, no path of length 3)

        char[][] board4 = {
                {'a', 'b', 'c'},
                {'a', 'e', 'd'},
                {'a', 'f', 'g'}
        };
        System.out.println(findWords(board4, new String[]{"abcdefg", "gfedcbaaa", "eaabcdgfa", "befa", "dgc", "ade"}));
        // expected: [abcdefg, befa, eaabcdgfa, gfedcbaaa]
    }
}
