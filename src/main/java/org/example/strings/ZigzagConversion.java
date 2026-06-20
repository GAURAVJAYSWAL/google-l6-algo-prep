package org.example.strings;

public class ZigzagConversion {

    /**
     * Simulate writing the string row by row. A "direction" toggles: we move down
     * the rows until we hit the bottom row, then bounce up to the top, and repeat.
     * Append each character to its current row's buffer; concatenating the rows
     * top-to-bottom yields the zigzag reading order. No geometry/index formulas.
     *
     * Time:  O(n)  — each character placed once, then concatenated once.
     * Space: O(n)  — the per-row buffers hold every character.
     */
    public static String convert(String s, int numRows) {
        if (numRows == 1 || numRows >= s.length()) return s;   // no zigzag to do

        StringBuilder[] rows = new StringBuilder[numRows];
        for (int r = 0; r < numRows; r++) rows[r] = new StringBuilder();

        int row = 0;
        int step = 1;   // +1 = heading down, -1 = heading up
        for (char c : s.toCharArray()) {
            rows[row].append(c);
            // Bounce off the top and bottom edges.
            if (row == 0) step = 1;
            else if (row == numRows - 1) step = -1;
            row += step;
        }

        StringBuilder result = new StringBuilder(s.length());
        for (StringBuilder r : rows) result.append(r);
        return result.toString();
    }

    public static void main(String[] args) {
        System.out.println(convert("PAYPALISHIRING", 3)); // expected: PAHNAPLSIIGYIR
        System.out.println(convert("PAYPALISHIRING", 4)); // expected: PINALSIGYAHRPI
        System.out.println(convert("A", 1));              // expected: A
        System.out.println(convert("AB", 1));             // expected: AB
    }
}
