package org.example.linkedlist;

import java.util.PriorityQueue;

/**
 * LC 23. Merge k Sorted Lists.
 */
public class MergeKSortedLists {

    static class ListNode {
        int val;
        ListNode next;
        ListNode(int val) { this.val = val; }
    }

    /**
     * Key insight: the global minimum across all lists is always one of the k current
     * heads. A min-heap keyed on node value surfaces that minimum in O(log k); pop it,
     * append it, and push its successor. The heap never holds more than k nodes.
     *
     * Time: O(N log k) — N total nodes, each pushed/popped once at O(log k).
     * Space: O(k) — heap holds at most one node per list.
     */
    public static ListNode mergeKLists(ListNode[] lists) {
        PriorityQueue<ListNode> heap = new PriorityQueue<>((a, b) -> Integer.compare(a.val, b.val));
        for (ListNode head : lists) {
            if (head != null) heap.offer(head); // seed with each non-empty head
        }
        ListNode dummy = new ListNode(0);
        ListNode tail = dummy;
        while (!heap.isEmpty()) {
            ListNode node = heap.poll();
            tail.next = node;
            tail = node;
            if (node.next != null) heap.offer(node.next); // refill from the list we drew from
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
        ListNode[] a = { build(1, 4, 5), build(1, 3, 4), build(2, 6) };
        System.out.println(toString(mergeKLists(a)));               // expected: [1, 1, 2, 3, 4, 4, 5, 6]
        System.out.println(toString(mergeKLists(new ListNode[]{}))); // expected: []
        System.out.println(toString(mergeKLists(new ListNode[]{null}))); // expected: []
        ListNode[] b = { build(), build(1), build(0, 2) };
        System.out.println(toString(mergeKLists(b)));               // expected: [0, 1, 2]
    }
}
