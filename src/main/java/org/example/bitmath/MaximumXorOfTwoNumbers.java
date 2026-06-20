package org.example.bitmath;

/**
 * LC 421. Maximum XOR of Two Numbers in an Array.
 */
public class MaximumXorOfTwoNumbers {

    /**
     * Key insight: insert every number's 32-bit binary, MSB first, into a binary trie.
     * To maximize a^x for a fixed a, greed bit-by-bit from the top: at each level prefer the
     * child holding the OPPOSITE bit (which sets that XOR bit to 1) and fall back to the same
     * bit only when the opposite branch is absent. Walking one number against the whole trie
     * finds its best partner; doing this for all numbers yields the global maximum.
     * (Alternative: a prefix-hashset method — fix the answer's bits greedily from the top and
     * check whether two stored prefixes XOR to the desired prefix, also O(32 n).)
     *
     * Time:  O(32 n) — each number costs one 32-step insert and one 32-step query.
     * Space: O(32 n) — the trie holds up to 32 nodes per inserted number.
     */
    public static int findMaximumXOR(int[] nums) {
        TrieNode root = new TrieNode();
        for (int num : nums) {
            insert(root, num);
        }
        int best = 0;
        for (int num : nums) {
            best = Math.max(best, maxXorWith(root, num));
        }
        return best;
    }

    private static void insert(TrieNode root, int num) {
        TrieNode node = root;
        for (int bit = 31; bit >= 0; bit--) {
            int b = (num >> bit) & 1;
            if (node.child[b] == null) node.child[b] = new TrieNode();
            node = node.child[b];
        }
    }

    // Walks the trie taking the opposite bit whenever possible to build the largest XOR with num.
    private static int maxXorWith(TrieNode root, int num) {
        TrieNode node = root;
        int xor = 0;
        for (int bit = 31; bit >= 0; bit--) {
            int b = (num >> bit) & 1;
            int want = b ^ 1;                       // opposite bit makes this XOR bit a 1
            if (node.child[want] != null) {
                xor |= (1 << bit);                  // we got the 1; descend the opposite branch
                node = node.child[want];
            } else {
                node = node.child[b];               // forced onto the same-bit branch (it must exist)
            }
        }
        return xor;
    }

    static class TrieNode {
        final TrieNode[] child = new TrieNode[2];   // child[0] = bit 0, child[1] = bit 1
    }

    public static void main(String[] args) {
        System.out.println(findMaximumXOR(new int[]{3, 10, 5, 25, 2, 8}));      // expected: 28
        System.out.println(findMaximumXOR(new int[]{0}));                       // expected: 0
        System.out.println(findMaximumXOR(new int[]{2, 4}));                    // expected: 6
        System.out.println(findMaximumXOR(new int[]{8, 10, 2}));                // expected: 10
        System.out.println(findMaximumXOR(new int[]{14, 70, 53, 83, 49, 91, 36, 80, 92, 51, 66, 70})); // expected: 127
    }
}
