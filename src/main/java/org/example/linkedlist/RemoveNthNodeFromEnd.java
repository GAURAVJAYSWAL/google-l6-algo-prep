package org.example.linkedlist;

/**
 * LC 19. Remove Nth Node From End of List.
 */
public class RemoveNthNodeFromEnd {

    static class ListNode {
        int val;
        ListNode next;
        ListNode(int val) { this.val = val; }
    }

    /**
     * Key insight: open a gap of exactly n nodes between two pointers, then advance
     * both until the lead hits the end — the trailing pointer now sits on the node
     * just before the target. Starting the trailing pointer at a dummy head makes
     * removing the actual head node need no special case.
     *
     * Time: O(L) — a single pass; the lead pointer crosses the list once.
     * Space: O(1) — two pointers.
     */
    public static ListNode removeNthFromEnd(ListNode head, int n) {
        ListNode dummy = new ListNode(0);
        dummy.next = head;
        ListNode lead = dummy, trail = dummy;
        for (int i = 0; i < n; i++) lead = lead.next; // open an n-node gap
        while (lead.next != null) { // stop with lead on the last node
            lead = lead.next;
            trail = trail.next;
        }
        trail.next = trail.next.next; // splice out the target
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
        System.out.println(toString(removeNthFromEnd(build(1, 2, 3, 4, 5), 2))); // expected: [1, 2, 3, 5]
        System.out.println(toString(removeNthFromEnd(build(1), 1)));             // expected: []
        System.out.println(toString(removeNthFromEnd(build(1, 2), 1)));          // expected: [1]
        System.out.println(toString(removeNthFromEnd(build(1, 2), 2)));          // expected: [2]
    }
}
