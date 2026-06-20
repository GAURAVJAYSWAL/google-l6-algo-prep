package org.example.stack;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;

public class DailyTemperatures {

    /**
     * Monotonic decreasing stack of indices (temperatures strictly decreasing from
     * bottom to top). When today's temperature exceeds the day on top of the stack,
     * today is that day's next-warmer day — pop it and record the day gap. Each
     * index is pushed and popped at most once, giving linear time instead of O(n^2).
     *
     * Time:  O(n)  — every index pushed/popped once.
     * Space: O(n)  — stack holds a strictly decreasing run in the worst case.
     */
    public static int[] dailyTemperatures(int[] temperatures) {
        int n = temperatures.length;
        int[] answer = new int[n];   // defaults to 0 = no warmer day ahead
        Deque<Integer> stack = new ArrayDeque<>();   // indices, temps decreasing

        for (int i = 0; i < n; i++) {
            // Today resolves every colder day still waiting on the stack.
            while (!stack.isEmpty() && temperatures[i] > temperatures[stack.peek()]) {
                int prev = stack.pop();
                answer[prev] = i - prev;   // days until a warmer temperature
            }
            stack.push(i);
        }
        return answer;
    }

    public static void main(String[] args) {
        System.out.println(Arrays.toString(dailyTemperatures(new int[]{73, 74, 75, 71, 69, 72, 76, 73})));
        // expected: [1, 1, 4, 2, 1, 1, 0, 0]
        System.out.println(Arrays.toString(dailyTemperatures(new int[]{30, 40, 50, 60})));
        // expected: [1, 1, 1, 0]
        System.out.println(Arrays.toString(dailyTemperatures(new int[]{30, 60, 90})));
        // expected: [1, 1, 0]
        System.out.println(Arrays.toString(dailyTemperatures(new int[]{90, 80, 70})));
        // expected: [0, 0, 0]
    }
}
