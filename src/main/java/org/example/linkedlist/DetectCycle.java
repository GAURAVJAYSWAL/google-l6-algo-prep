package org.example.linkedlist;

/**
 * LC 141. Linked List Cycle.
 */
public class DetectCycle {

    static class ListNode {
        int val;
        ListNode next;
        ListNode(int val) { this.val = val; }
    }

    /**
     * Key insight: Floyd's tortoise and hare. Advance one pointer by 1 and another
     * by 2 each step. In a cycle the fast pointer gains one node per step on the
     * slow one, so it must eventually lap and coincide; with no cycle, fast simply
     * reaches the end.
     *
     * Time: O(n) — fast pointer traverses at most ~2n nodes before meeting or exiting.
     * Space: O(1) — two pointers, no auxiliary set.
     */
    public static boolean hasCycle(ListNode head) {
        ListNode slow = head;
        ListNode fast = head;
        while (fast != null && fast.next != null) {
            slow = slow.next;
            fast = fast.next.next;
            if (slow == fast) return true; // pointers met -> cycle
        }
        return false;
    }

    public static void main(String[] args) {
        ListNode a = new ListNode(3), b = new ListNode(2), c = new ListNode(0), d = new ListNode(-4);
        a.next = b; b.next = c; c.next = d; d.next = b; // tail links back to b
        System.out.println(hasCycle(a)); // expected: true

        ListNode x = new ListNode(1), y = new ListNode(2);
        x.next = y; y.next = x; // two-node cycle
        System.out.println(hasCycle(x)); // expected: true

        ListNode p = new ListNode(1), q = new ListNode(2);
        p.next = q; // no cycle
        System.out.println(hasCycle(p)); // expected: false

        System.out.println(hasCycle(null)); // expected: false
    }
}
