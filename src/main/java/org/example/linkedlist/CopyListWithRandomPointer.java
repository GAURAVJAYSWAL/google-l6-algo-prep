package org.example.linkedlist;

/**
 * LC 138. Copy List with Random Pointer.
 */
public class CopyListWithRandomPointer {

    static class Node {
        int val;
        Node next;
        Node random;
        Node(int val) { this.val = val; }
    }

    /**
     * Key insight: avoid a hash map by weaving each copy directly after its original
     * (A -> A' -> B -> B' -> ...). Then a copy's random is simply its original's
     * random's {@code next}, resolvable in place. A final pass unzips the two lists,
     * restoring the original and extracting the clone.
     *
     * Time: O(n) — three linear passes (interleave, wire randoms, split).
     * Space: O(1) — no map; only the cloned nodes themselves.
     */
    public static Node copyRandomList(Node head) {
        if (head == null) return null;

        // 1) Interleave clones: cur -> clone -> cur.next.
        for (Node cur = head; cur != null; cur = cur.next.next) {
            Node clone = new Node(cur.val);
            clone.next = cur.next;
            cur.next = clone;
        }

        // 2) Wire up random pointers using the interleaving.
        for (Node cur = head; cur != null; cur = cur.next.next) {
            if (cur.random != null) {
                cur.next.random = cur.random.next; // clone's random = original-random's clone
            }
        }

        // 3) Unzip: restore the original list and pull out the clone list.
        Node dummy = new Node(0);
        Node copyTail = dummy;
        for (Node cur = head; cur != null; cur = cur.next) {
            Node clone = cur.next;
            cur.next = clone.next; // restore original link
            copyTail.next = clone;
            copyTail = clone;
        }
        return dummy.next;
    }

    public static void main(String[] args) {
        // Build [7,null],[13,->7],[11,->1],[10,->11],[1,->7]
        Node n7 = new Node(7), n13 = new Node(13), n11 = new Node(11), n10 = new Node(10), n1 = new Node(1);
        n7.next = n13; n13.next = n11; n11.next = n10; n10.next = n1;
        n7.random = null; n13.random = n7; n11.random = n1; n10.random = n11; n1.random = n7;

        Node copy = copyRandomList(n7);

        // Verify the clone is a deep copy: distinct objects, same structure.
        System.out.println(copy != n7);                        // expected: true
        System.out.println(copy.val);                          // expected: 7
        System.out.println(copy.random == null);               // expected: true
        System.out.println(copy.next.val);                     // expected: 13
        System.out.println(copy.next.random == copy);          // expected: true  (13's random -> cloned 7)
        System.out.println(copy.next.next.random.val);         // expected: 1     (11's random -> cloned 1)
        System.out.println(n7.next == n13);                    // expected: true  (original restored)

        System.out.println(copyRandomList(null) == null);      // expected: true
    }
}
