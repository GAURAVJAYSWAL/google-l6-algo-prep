package org.example.linkedlist;

/**
 * LC 876 — Middle of the Linked List (Easy).
 *
 * Find the middle node of a singly linked list in one pass, returning the second
 * middle when the length is even.
 */
public class MiddleOfTheLinkedList {

    static class ListNode {
        int val;
        ListNode next;
        ListNode(int val) { this.val = val; }
        ListNode(int val, ListNode next) { this.val = val; this.next = next; }
    }

    /**
     * Two-speed invariant: {@code slow} advances one node while {@code fast} advances two,
     * so {@code slow} has always covered exactly half of what {@code fast} has. When
     * {@code fast} runs off the end, {@code slow} sits at the middle — and the loop
     * condition naturally yields the SECOND middle for even length (fast lands on null
     * after the last step, leaving slow one past center).
     * Time:  O(n) — fast traverses the whole list once.
     * Space: O(1) — two pointers, no extra structures.
     */
    public ListNode middleNode(ListNode head) {
        ListNode slow = head, fast = head;
        while (fast != null && fast.next != null) {
            slow = slow.next;       // +1
            fast = fast.next.next;  // +2, so slow stays at the halfway mark
        }
        return slow;
    }

    private static ListNode build(int... vals) {
        ListNode dummy = new ListNode(0);
        ListNode tail = dummy;
        for (int v : vals) {
            tail.next = new ListNode(v);
            tail = tail.next;
        }
        return dummy.next;
    }

    private static String fromHere(ListNode node) {
        StringBuilder sb = new StringBuilder("[");
        for (ListNode cur = node; cur != null; cur = cur.next) {
            if (cur != node) sb.append(", ");
            sb.append(cur.val);
        }
        return sb.append(']').toString();
    }

    public static void main(String[] args) {
        // Odd length: single middle.
        ListNode odd = build(1, 2, 3, 4, 5);
        ListNode mOdd = new MiddleOfTheLinkedList().middleNode(odd);
        System.out.println(mOdd.val + " " + fromHere(mOdd)); // expected: 3 [3, 4, 5]

        // Even length: second of the two middles.
        ListNode even = build(1, 2, 3, 4, 5, 6);
        ListNode mEven = new MiddleOfTheLinkedList().middleNode(even);
        System.out.println(mEven.val + " " + fromHere(mEven)); // expected: 4 [4, 5, 6]

        // Single node.
        ListNode one = build(7);
        System.out.println(new MiddleOfTheLinkedList().middleNode(one).val); // expected: 7
    }
}
