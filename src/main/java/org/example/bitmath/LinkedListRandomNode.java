package org.example.bitmath;

import java.util.Random;

/**
 * LC 382. Linked List Random Node.
 */
public class LinkedListRandomNode {

    static class ListNode {
        int val;
        ListNode next;
        ListNode(int val) { this.val = val; }
        ListNode(int val, ListNode next) { this.val = val; this.next = next; }
    }

    /**
     * Key insight: reservoir sampling of size 1 — walk the list keeping one chosen value, and
     * at the k-th node (1-indexed) replace the choice with probability 1/k. By induction every
     * node ends with probability 1/n, so we never need to know n in advance or store the list.
     * This is the canonical answer when the length is unknown or the list is huge / streaming.
     *
     * Time:  getRandom O(n) — one pass per call; constructor O(1).
     * Space: O(1) — no buffering of the list regardless of its length.
     */
    static class Solution {
        private final ListNode head;
        private final Random rng;

        public Solution(ListNode head) {
            this(head, new Random());
        }

        // Seeded overload for a deterministic demo.
        public Solution(ListNode head, Random rng) {
            this.head = head;
            this.rng = rng;
        }

        public int getRandom() {
            int chosen = head.val;
            int k = 1;
            for (ListNode node = head.next; node != null; node = node.next) {
                k++;
                // Keep node with probability 1/k; nextInt(k)==0 happens exactly that often.
                if (rng.nextInt(k) == 0) chosen = node.val;
            }
            return chosen;
        }
    }

    public static void main(String[] args) {
        ListNode head = new ListNode(1, new ListNode(2, new ListNode(3)));
        Solution s = new Solution(head, new Random(1));

        int[] counts = new int[4]; // index by value 1..3
        for (int i = 0; i < 3000; i++) counts[s.getRandom()]++;
        System.out.println("counts[1..3]=[" + counts[1] + ", " + counts[2] + ", " + counts[3] + "]");
        // expected: each roughly 1000 (uniform over the 3 nodes; deterministic with seed 1)

        boolean roughlyUniform = counts[1] > 700 && counts[2] > 700 && counts[3] > 700;
        System.out.println(roughlyUniform); // expected: true

        Solution single = new Solution(new ListNode(42), new Random(0));
        System.out.println(single.getRandom()); // expected: 42 (only one node)
    }
}
