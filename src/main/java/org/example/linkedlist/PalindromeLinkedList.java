package org.example.linkedlist;

/**
 * LC 234. Palindrome Linked List.
 */
public class PalindromeLinkedList {

    static class ListNode {
        int val;
        ListNode next;
        ListNode(int val) { this.val = val; }
    }

    /**
     * Key insight: a list is a palindrome iff its second half reversed equals its
     * first half. Find the middle with slow/fast, reverse the second half in place,
     * then walk inward from both ends comparing values — no array copy, so O(1) space.
     *
     * Time: O(n) — find-mid, reverse, and compare are each linear.
     * Space: O(1) — in-place reversal; only pointers.
     */
    public static boolean isPalindrome(ListNode head) {
        if (head == null || head.next == null) return true;

        // Find middle: for even length, slow lands on the first node of the 2nd half.
        ListNode slow = head, fast = head;
        while (fast != null && fast.next != null) {
            slow = slow.next;
            fast = fast.next.next;
        }

        // Reverse from the middle to the end.
        ListNode prev = null;
        while (slow != null) {
            ListNode next = slow.next;
            slow.next = prev;
            prev = slow;
            slow = next;
        }

        // Compare first half against the reversed second half.
        ListNode left = head, right = prev;
        while (right != null) { // 2nd half is the shorter side, so it bounds the loop
            if (left.val != right.val) return false;
            left = left.next;
            right = right.next;
        }
        return true;
    }

    private static ListNode build(int... vals) {
        ListNode dummy = new ListNode(0);
        ListNode tail = dummy;
        for (int v : vals) { tail.next = new ListNode(v); tail = tail.next; }
        return dummy.next;
    }

    public static void main(String[] args) {
        System.out.println(isPalindrome(build(1, 2, 2, 1))); // expected: true
        System.out.println(isPalindrome(build(1, 2, 3, 2, 1))); // expected: true
        System.out.println(isPalindrome(build(1, 2)));       // expected: false
        System.out.println(isPalindrome(build(1)));          // expected: true
    }
}
