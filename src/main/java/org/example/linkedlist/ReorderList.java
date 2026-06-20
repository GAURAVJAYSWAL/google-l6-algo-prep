package org.example.linkedlist;

/**
 * LC 143. Reorder List.
 *
 * <p>Reorder L0 -> L1 -> ... -> Ln-1 -> Ln into L0 -> Ln -> L1 -> Ln-1 -> ...
 */
public class ReorderList {

    static class ListNode {
        int val;
        ListNode next;
        ListNode(int val) { this.val = val; }
    }

    /**
     * Key insight: the target order interleaves the list with its own reverse. So
     * split at the middle, reverse the second half in place, then zip the two halves
     * together one node at a time — all without copying or extra storage.
     *
     * Time: O(n) — find-mid, reverse, and merge are each linear passes.
     * Space: O(1) — pointer surgery only.
     */
    public static void reorderList(ListNode head) {
        if (head == null || head.next == null) return;

        // 1) Find the middle: slow lands on the end of the first half.
        ListNode slow = head, fast = head;
        while (fast.next != null && fast.next.next != null) {
            slow = slow.next;
            fast = fast.next.next;
        }

        // 2) Reverse the second half (everything after slow).
        ListNode second = slow.next;
        slow.next = null; // sever the two halves
        ListNode prev = null;
        while (second != null) {
            ListNode next = second.next;
            second.next = prev;
            prev = second;
            second = next;
        }

        // 3) Merge the two halves alternately. first is no longer than second.
        ListNode first = head;
        second = prev;
        while (second != null) {
            ListNode fNext = first.next, sNext = second.next;
            first.next = second;
            second.next = fNext;
            first = fNext;
            second = sNext;
        }
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
        ListNode a = build(1, 2, 3, 4);
        reorderList(a);
        System.out.println(toString(a)); // expected: [1, 4, 2, 3]

        ListNode b = build(1, 2, 3, 4, 5);
        reorderList(b);
        System.out.println(toString(b)); // expected: [1, 5, 2, 4, 3]

        ListNode c = build(1);
        reorderList(c);
        System.out.println(toString(c)); // expected: [1]

        ListNode d = build(1, 2);
        reorderList(d);
        System.out.println(toString(d)); // expected: [1, 2]
    }
}
