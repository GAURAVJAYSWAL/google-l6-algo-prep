package org.example.heap;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;

public class SlidingWindowMaximum {

    /**
     * Maintain a deque of indices whose values are strictly decreasing front-to-back; the
     * front is always the current window's maximum. A new element evicts every smaller value
     * from the back (they can never be the max while this larger, newer element is in
     * window), and indices that fall out of the left edge are dropped from the front. Each
     * index enters and leaves the deque at most once, so the whole sweep is linear.
     *
     * Time:  O(n) — every index is pushed and popped at most once.
     * Space: O(k) — the deque holds at most one window's worth of indices.
     */
    public static int[] maxSlidingWindow(int[] nums, int k) {
        int n = nums.length;
        int[] result = new int[n - k + 1];
        Deque<Integer> dq = new ArrayDeque<>(); // indices, values strictly decreasing front->back

        for (int i = 0; i < n; i++) {
            // Drop indices that have slid past the left edge of the window.
            if (!dq.isEmpty() && dq.peekFirst() <= i - k) {
                dq.pollFirst();
            }
            // Smaller trailing values are dominated by nums[i] for the rest of their lifetime.
            while (!dq.isEmpty() && nums[dq.peekLast()] < nums[i]) {
                dq.pollLast();
            }
            dq.offerLast(i);
            if (i >= k - 1) {
                result[i - k + 1] = nums[dq.peekFirst()]; // front is the window max
            }
        }
        return result;
    }

    public static void main(String[] args) {
        System.out.println(Arrays.toString(maxSlidingWindow(new int[]{1, 3, -1, -3, 5, 3, 6, 7}, 3)));
        // expected: [3, 3, 5, 5, 6, 7]
        System.out.println(Arrays.toString(maxSlidingWindow(new int[]{1}, 1)));
        // expected: [1]
        System.out.println(Arrays.toString(maxSlidingWindow(new int[]{9, 8, 7, 6}, 2)));
        // expected: [9, 8, 7]
        System.out.println(Arrays.toString(maxSlidingWindow(new int[]{1, 2, 3, 4}, 4)));
        // expected: [4]
    }
}
