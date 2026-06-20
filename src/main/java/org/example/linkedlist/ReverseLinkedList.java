package org.example.linkedlist;

/**
 * LC 206. Reverse Linked List.
 *
 * <p>Two equivalent formulations are shown: the iterative pointer-walk and the
 * recursive unwind.
 */
public class ReverseLinkedList {

    static class ListNode {
        int val;
        ListNode next;
        ListNode(int val) { this.val = val; }
        ListNode(int val, ListNode next) { this.val = val; this.next = next; }
    }

    /**
     * Key insight: walk the list once, flipping each node's {@code next} to point
     * at the node we just came from. {@code prev} accumulates the reversed prefix
     * and becomes the new head when {@code cur} runs off the end.
     *
     * Time: O(n) — single pass over the list.
     * Space: O(1) — only three pointers, no extra structures.
     */
    public static ListNode reverseIterative(ListNode head) {
        ListNode prev = null;
        ListNode cur = head;
        while (cur != null) {
            ListNode next = cur.next; // stash before we overwrite the link
            cur.next = prev;
            prev = cur;
            cur = next;
        }
        return prev;
    }

    /**
     * Key insight: recurse to the tail, which is the new head; on the way back up,
     * each node makes its successor point back at itself. {@code head.next.next = head}
     * is the link-flip; {@code head.next = null} keeps the old head from cycling.
     *
     * Time: O(n) — one stack frame per node.
     * Space: O(n) — recursion depth equals the list length.
     */
    public static ListNode reverseRecursive(ListNode head) {
        if (head == null || head.next == null) return head;
        ListNode newHead = reverseRecursive(head.next);
        head.next.next = head; // successor now points back at us
        head.next = null;
        return newHead;
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
        System.out.println(toString(reverseIterative(build(1, 2, 3, 4, 5)))); // expected: [5, 4, 3, 2, 1]
        System.out.println(toString(reverseRecursive(build(1, 2, 3, 4, 5)))); // expected: [5, 4, 3, 2, 1]
        System.out.println(toString(reverseIterative(build(1, 2))));          // expected: [2, 1]
        System.out.println(toString(reverseRecursive(build())));              // expected: []
    }
}
