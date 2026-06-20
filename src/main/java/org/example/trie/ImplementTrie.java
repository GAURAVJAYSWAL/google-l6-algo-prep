package org.example.trie;

/**
 * LC 208. Implement Trie (Prefix Tree).
 */
public class ImplementTrie {

    /**
     * Key insight: a prefix tree where every node owns 26 child slots, one per
     * lowercase letter, so a word is a root-to-node path. {@code isWord} marks the
     * node where a complete inserted word ends, which is what distinguishes a real
     * word from a mere prefix that happens to be a path through the tree.
     *
     * Time:  O(L) per op — one step per character of the key/prefix of length L.
     * Space: O(total characters inserted) — at most 26 children allocated per node.
     */
    static class Trie {
        private final Trie[] children = new Trie[26];
        private boolean isWord;

        public Trie() {
        }

        public void insert(String word) {
            Trie node = this;
            for (int i = 0; i < word.length(); i++) {
                int c = word.charAt(i) - 'a';
                if (node.children[c] == null) node.children[c] = new Trie();
                node = node.children[c];
            }
            node.isWord = true;   // mark the terminal node as a complete word
        }

        public boolean search(String word) {
            Trie node = walk(word);
            return node != null && node.isWord;   // path exists AND ends a word
        }

        public boolean startsWith(String prefix) {
            return walk(prefix) != null;           // path existing is enough
        }

        // Follows the path for s, returning the final node or null if it breaks.
        private Trie walk(String s) {
            Trie node = this;
            for (int i = 0; i < s.length(); i++) {
                int c = s.charAt(i) - 'a';
                if (node.children[c] == null) return null;
                node = node.children[c];
            }
            return node;
        }
    }

    public static void main(String[] args) {
        Trie trie = new Trie();
        trie.insert("apple");
        System.out.println(trie.search("apple"));     // expected: true
        System.out.println(trie.search("app"));       // expected: false (prefix only)
        System.out.println(trie.startsWith("app"));   // expected: true
        trie.insert("app");
        System.out.println(trie.search("app"));       // expected: true (now inserted)

        Trie t2 = new Trie();
        t2.insert("banana");
        System.out.println(t2.startsWith("ban"));     // expected: true
        System.out.println(t2.search("ban"));         // expected: false
        System.out.println(t2.startsWith("xyz"));     // expected: false
    }
}
