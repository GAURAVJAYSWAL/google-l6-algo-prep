package org.example.strings;

public class RomanToInteger {

    /**
     * Scan left to right adding each numeral's value, EXCEPT when a smaller numeral
     * immediately precedes a larger one (e.g. IV, IX, CM) — that pairing means
     * subtraction. Detect it by comparing each value to its right neighbour: if the
     * current is smaller, subtract it; otherwise add it.
     *
     * Time:  O(n)  — single pass over the string.
     * Space: O(1)  — fixed lookup, a couple of scalars.
     */
    public static int romanToInt(String s) {
        int total = 0;
        int prev = 0;   // value of the numeral to the right (processed already)
        // Walk right-to-left so each numeral knows what follows it.
        for (int i = s.length() - 1; i >= 0; i--) {
            int val = value(s.charAt(i));
            if (val < prev) {
                total -= val;   // smaller before larger => subtractive form
            } else {
                total += val;
            }
            prev = val;
        }
        return total;
    }

    private static int value(char c) {
        return switch (c) {
            case 'I' -> 1;
            case 'V' -> 5;
            case 'X' -> 10;
            case 'L' -> 50;
            case 'C' -> 100;
            case 'D' -> 500;
            case 'M' -> 1000;
            default -> 0;
        };
    }

    public static void main(String[] args) {
        System.out.println(romanToInt("III"));     // expected: 3
        System.out.println(romanToInt("LVIII"));   // expected: 58
        System.out.println(romanToInt("MCMXCIV")); // expected: 1994
        System.out.println(romanToInt("IV"));      // expected: 4
    }
}
