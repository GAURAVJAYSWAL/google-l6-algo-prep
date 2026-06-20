package org.example.linkedlist;

/**
 * LC 2. Add Two Numbers.
 *
 * <p>Digits are stored in reverse order, so the list heads are the least-significant
 * digits — exactly the order grade-school addition processes them.
 */
public class AddTwoNumbers {

    static class ListNode {
        int val;
        ListNode next;
        ListNode(int val) { this.val = val; }
    }

    /**
     * Key insight: because the least-significant digit comes first, we can add column
     * by column from the heads, propagating a carry forward. Treating a missing node
     * as 0 lets unequal lengths and a trailing carry fall out of the same loop.
     *
     * Time: O(max(m, n)) — one column per iteration.
     * Space: O(max(m, n)) — the result list, plus O(1) working state.
     */
    public static ListNode addTwoNumbers(ListNode l1, ListNode l2) {
        ListNode dummy = new ListNode(0);
        ListNode tail = dummy;
        int carry = 0;
        while (l1 != null || l2 != null || carry != 0) { // carry keeps us going past both lists
            int sum = carry;
            if (l1 != null) { sum += l1.val; l1 = l1.next; }
            if (l2 != null) { sum += l2.val; l2 = l2.next; }
            carry = sum / 10;
            tail.next = new ListNode(sum % 10);
            tail = tail.next;
        }
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
        // 342 + 465 = 807
        System.out.println(toString(addTwoNumbers(build(2, 4, 3), build(5, 6, 4)))); // expected: [7, 0, 8]
        // 0 + 0 = 0
        System.out.println(toString(addTwoNumbers(build(0), build(0))));             // expected: [0]
        // 9999999 + 9999 = 10009998
        System.out.println(toString(addTwoNumbers(
                build(9, 9, 9, 9, 9, 9, 9), build(9, 9, 9, 9))));                    // expected: [8, 9, 9, 9, 0, 0, 0, 1]
    }
}
