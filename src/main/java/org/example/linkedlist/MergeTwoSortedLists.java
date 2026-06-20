package org.example.linkedlist;

/**
 * LC 21. Merge Two Sorted Lists.
 */
public class MergeTwoSortedLists {

    static class ListNode {
        int val;
        ListNode next;
        ListNode(int val) { this.val = val; }
    }

    /**
     * Key insight: a classic two-finger merge. A dummy head lets us append the
     * smaller front node each step without special-casing the very first append,
     * then we splice on whatever non-empty list remains in one move.
     *
     * Time: O(m + n) — each node is visited and linked exactly once.
     * Space: O(1) — we re-link existing nodes; no copies.
     */
    public static ListNode mergeTwoLists(ListNode l1, ListNode l2) {
        ListNode dummy = new ListNode(0);
        ListNode tail = dummy;
        while (l1 != null && l2 != null) {
            if (l1.val <= l2.val) { tail.next = l1; l1 = l1.next; }
            else                  { tail.next = l2; l2 = l2.next; }
            tail = tail.next;
        }
        tail.next = (l1 != null) ? l1 : l2; // attach the leftover tail
        return dummy.next;
    }

    private static ListNode build(int... vals) {
        ListNode dummy = new ListNode(0);
        ListNode tail = dummy;
        for (int v : vals) { tail.next = new ListNode(v); tail = tail.next; }
        return dummy.next;
    }

    private static String toString(ListNode head) {
        StringBuilder sb = new StringBuilder("[");
        for (ListNode n = head; n != null; n = n.next) {
            if (sb.length() > 1) sb.append(", ");
            sb.append(n.val);
        }
        return sb.append(']').toString();
    }

    public static void main(String[] args) {
        System.out.println(toString(mergeTwoLists(build(1, 2, 4), build(1, 3, 4)))); // expected: [1, 1, 2, 3, 4, 4]
        System.out.println(toString(mergeTwoLists(build(), build())));               // expected: []
        System.out.println(toString(mergeTwoLists(build(), build(0))));              // expected: [0]
        System.out.println(toString(mergeTwoLists(build(5), build(1, 2, 3))));       // expected: [1, 2, 3, 5]
    }
}
