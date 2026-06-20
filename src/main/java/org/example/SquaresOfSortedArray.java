package org.example;

import java.util.Arrays;

public class SquaresOfSortedArray {

    // ===== YOUR CODE GOES HERE =====
    public static int[] sortedSquares(int[] nums) {
        // TODO: implement
        return new int[0];
    }
    // ================================

    public static void main(String[] args) {
        int[][] inputs = {
                {-4, -1, 0, 3, 10},     // expected: [0, 1, 9, 16, 100]
                {-7, -3, 2, 3, 11},     // expected: [4, 9, 9, 49, 121]
                {0},                    // expected: [0]
                {-5, -3, -2, -1},       // expected: [1, 4, 9, 25]   (all negative)
                {1, 2, 3, 4},           // expected: [1, 4, 9, 16]   (all positive)
                {-1, 1}                 // expected: [1, 1]
        };
        int[][] expected = {
                {0, 1, 9, 16, 100},
                {4, 9, 9, 49, 121},
                {0},
                {1, 4, 9, 25},
                {1, 4, 9, 16},
                {1, 1}
        };

        int passed = 0;
        for (int i = 0; i < inputs.length; i++) {
            int[] result = sortedSquares(inputs[i]);
            boolean ok = Arrays.equals(result, expected[i]);
            if (ok) passed++;
            System.out.printf("Test %d: input=%s | expected=%s | got=%s | %s%n",
                    i + 1,
                    Arrays.toString(inputs[i]),
                    Arrays.toString(expected[i]),
                    Arrays.toString(result),
                    ok ? "PASS" : "FAIL");
        }
        System.out.printf("%n%d/%d passed%n", passed, inputs.length);
    }
}