package org.example.greedy;

import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

/**
 * Huffman Coding (greedy prefix-code construction). Off-LeetCode, reported in L6 loops.
 */
public class HuffmanCoding {

    static class Node {
        char ch;          // meaningful only at leaves
        int freq;
        Node left, right;

        Node(char ch, int freq) { this.ch = ch; this.freq = freq; }
        Node(int freq, Node left, Node right) { this.freq = freq; this.left = left; this.right = right; }

        boolean isLeaf() { return left == null && right == null; }
    }

    /**
     * Key insight: an optimal prefix code is built bottom-up by greedily merging the two
     * least-frequent nodes into a parent until one tree remains. The exchange argument
     * guarantees the two rarest symbols sit deepest as siblings, so repeatedly combining the
     * minima (via a min-heap keyed on frequency) minimizes the total weighted code length.
     * Assigning bit 0 to left edges and 1 to right edges yields a prefix-free code: no code is
     * an ancestor of another, so the bitstream decodes unambiguously.
     *
     * Time:  O(k log k) — k distinct symbols, each heap push/pop is logarithmic.
     * Space: O(k) — the heap, the tree, and the code table.
     */
    public static Map<Character, String> buildCodes(String text) {
        Map<Character, Integer> freq = new HashMap<>();
        for (char c : text.toCharArray()) freq.merge(c, 1, Integer::sum);

        PriorityQueue<Node> heap = new PriorityQueue<>((a, b) -> a.freq - b.freq);
        for (Map.Entry<Character, Integer> e : freq.entrySet()) {
            heap.add(new Node(e.getKey(), e.getValue()));
        }

        // Merge the two rarest nodes until a single root remains.
        while (heap.size() > 1) {
            Node a = heap.poll();
            Node b = heap.poll();
            heap.add(new Node(a.freq + b.freq, a, b));
        }

        Map<Character, String> codes = new HashMap<>();
        Node root = heap.poll();
        if (root != null) {
            // Single distinct character: tree is one leaf, so assign it the code "0".
            if (root.isLeaf()) codes.put(root.ch, "0");
            else assignCodes(root, new StringBuilder(), codes);
        }
        return codes;
    }

    private static void assignCodes(Node node, StringBuilder path, Map<Character, String> codes) {
        if (node.isLeaf()) {
            codes.put(node.ch, path.toString());
            return;
        }
        path.append('0');                         // left edge contributes a 0 bit
        assignCodes(node.left, path, codes);
        path.deleteCharAt(path.length() - 1);
        path.append('1');                         // right edge contributes a 1 bit
        assignCodes(node.right, path, codes);
        path.deleteCharAt(path.length() - 1);
    }

    public static String encode(String text, Map<Character, String> codes) {
        StringBuilder sb = new StringBuilder();
        for (char c : text.toCharArray()) sb.append(codes.get(c));
        return sb.toString();
    }

    /**
     * Decode by walking the code table inverted: since the code is prefix-free, the first
     * accumulated bit-prefix that matches a symbol is unambiguous.
     */
    public static String decode(String bits, Map<Character, String> codes) {
        Map<String, Character> inverse = new HashMap<>();
        for (Map.Entry<Character, String> e : codes.entrySet()) inverse.put(e.getValue(), e.getKey());

        StringBuilder out = new StringBuilder();
        StringBuilder cur = new StringBuilder();
        for (int i = 0; i < bits.length(); i++) {
            cur.append(bits.charAt(i));
            Character ch = inverse.get(cur.toString());
            if (ch != null) {            // prefix-free => first match is the intended symbol
                out.append(ch);
                cur.setLength(0);
            }
        }
        return out.toString();
    }

    public static void main(String[] args) {
        String text = "abracadabra";
        Map<Character, String> codes = buildCodes(text);
        String encoded = encode(text, codes);
        String decoded = decode(encoded, codes);

        System.out.println(decoded.equals(text));        // expected: true (round-trip is lossless)
        System.out.println(encoded.length() <= text.length() * 8); // expected: true (compresses vs 8-bit chars)

        String text2 = "mississippi";
        Map<Character, String> codes2 = buildCodes(text2);
        System.out.println(decode(encode(text2, codes2), codes2).equals(text2)); // expected: true

        // Single distinct symbol: tree is one leaf, code defaults to "0".
        Map<Character, String> single = buildCodes("aaaa");
        System.out.println(decode(encode("aaaa", single), single)); // expected: aaaa
    }
}
