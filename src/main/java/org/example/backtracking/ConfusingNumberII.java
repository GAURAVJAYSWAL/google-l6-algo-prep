package org.example.backtracking;

public class ConfusingNumberII {

    // Digits that survive a 180-degree rotation, paired with what they become.
    private static final int[] DIGITS = {0, 1, 6, 8, 9};
    private static final int[] ROTATED = {0, 1, 9, 8, 6}; // 0->0,1->1,6->9,8->8,9->6

    /**
     * Only numbers built solely from {0,1,6,8,9} can be rotated at all, so generate
     * exactly those by DFS (never prepend 0 as a leading digit). A number is
     * "confusing" iff rotating it 180 degrees gives a DIFFERENT valid number, so we
     * carry the rotation alongside each built value: appending digit d makes the new
     * value v*10+d and pushes rot(d) to the FRONT of the rotation (rotated = rot(d)
     * * 10^len + rotated). Count when value != rotated.
     * Time:  O(5^L) — L = number of digits in n; at most five choices per position.
     * Space: O(L) — recursion depth.
     */
    public static int confusingNumberII(int n) {
        return dfs(0, 0, 1, n);                   // value, rotated, place-value of next digit
    }

    private static int dfs(long value, long rotated, long place, int n) {
        int count = (value != 0 && value != rotated) ? 1 : 0; // skip 0 itself; need a real flip
        for (int i = 0; i < DIGITS.length; i++) {
            if (value == 0 && DIGITS[i] == 0) continue;       // no leading zero
            long nextValue = value * 10 + DIGITS[i];
            if (nextValue > n) continue;                      // pruning: would exceed n
            long nextRotated = ROTATED[i] * place + rotated;  // new rotated digit lands in front
            count += dfs(nextValue, nextRotated, place * 10, n);
        }
        return count;
    }

    public static void main(String[] args) {
        System.out.println(confusingNumberII(20));
        // expected: 6   (6, 9, 10, 16, 18, 19)

        System.out.println(confusingNumberII(100));
        // expected: 19

        System.out.println(confusingNumberII(9));
        // expected: 2   (6, 9)

        System.out.println(confusingNumberII(1000000000));
        // expected: 1950627
    }
}
