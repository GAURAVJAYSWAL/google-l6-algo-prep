package org.example.stack;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * LeetCode 232 — Implement Queue using Stacks (Easy).
 *
 * A FIFO queue backed by two LIFO stacks. This class IS the queue: push/pop/peek/empty.
 */
public class ImplementQueueUsingStacks {

    private final Deque<Integer> in = new ArrayDeque<>();   // newest elements land here
    private final Deque<Integer> out = new ArrayDeque<>();  // oldest elements surface here

    /**
     * Two stacks simulate FIFO by reversing order exactly once. Pushes always go to {@code in};
     * pops/peeks read from {@code out}, refilling it from {@code in} only when it runs dry.
     * Because each element is moved from {@code in} to {@code out} at most once over its lifetime,
     * the cost of every transfer is charged to a single element — giving amortized O(1) per op
     * even though an individual pop can be O(n).
     */
    public void push(int x) {
        in.push(x);
    }

    /** Removes and returns the front element. Amortized O(1). */
    public int pop() {
        shiftIfNeeded();
        return out.pop();
    }

    /** Returns the front element without removing it. Amortized O(1). */
    public int peek() {
        shiftIfNeeded();
        return out.peek();
    }

    /** True when no elements remain in either stack. O(1). */
    public boolean empty() {
        return in.isEmpty() && out.isEmpty();
    }

    /** Pour {@code in} into {@code out} only when {@code out} is empty, reversing LIFO into FIFO. */
    private void shiftIfNeeded() {
        if (out.isEmpty()) {
            while (!in.isEmpty()) {
                out.push(in.pop());
            }
        }
    }

    public static void main(String[] args) {
        ImplementQueueUsingStacks q = new ImplementQueueUsingStacks();
        q.push(1);
        q.push(2);
        System.out.println(q.peek());   // expected: 1  (front is the oldest element)
        System.out.println(q.pop());    // expected: 1
        System.out.println(q.empty());  // expected: false  (2 still queued)

        q.push(3);                       // queue is now [2, 3]
        System.out.println(q.pop());    // expected: 2
        System.out.println(q.pop());    // expected: 3
        System.out.println(q.empty());  // expected: true
    }
}
