package org.example.strings;

import java.util.ArrayList;
import java.util.List;

public class TextJustification {

    /**
     * Greedy packing: fit as many words on a line as possible (each gap needs >= 1
     * space). Once a line is fixed, the extra spaces beyond the single gaps are
     * spread as evenly as possible — when they don't divide evenly, the leftmost
     * gaps get one more. A single-word line and the last line are left-justified
     * (words joined by one space, padded on the right).
     *
     * Time:  O(n)  — every character is emitted once across all lines.
     * Space: O(n)  — the output strings (excluding output, only O(maxWidth) scratch).
     */
    public static List<String> fullJustify(String[] words, int maxWidth) {
        List<String> lines = new ArrayList<>();
        int n = words.length;
        int i = 0;
        while (i < n) {
            // Find the maximal run [i, j) that fits: lettersLen + (gaps) <= maxWidth.
            int j = i;
            int lettersLen = 0;
            while (j < n && lettersLen + words[j].length() + (j - i) <= maxWidth) {
                lettersLen += words[j].length();
                j++;
            }

            int wordCount = j - i;
            int gaps = wordCount - 1;
            StringBuilder sb = new StringBuilder();

            boolean lastLine = (j == n);
            if (gaps == 0 || lastLine) {
                // Left-justify: one space between words, pad the remainder on the right.
                for (int k = i; k < j; k++) {
                    if (k > i) sb.append(' ');
                    sb.append(words[k]);
                }
                while (sb.length() < maxWidth) sb.append(' ');
            } else {
                int totalSpaces = maxWidth - lettersLen;
                int base = totalSpaces / gaps;        // spaces every gap gets
                int extra = totalSpaces % gaps;       // leftmost `extra` gaps get one more
                for (int k = i; k < j; k++) {
                    sb.append(words[k]);
                    if (k < j - 1) {
                        int spaces = base + (k - i < extra ? 1 : 0);
                        for (int s = 0; s < spaces; s++) sb.append(' ');
                    }
                }
            }

            lines.add(sb.toString());
            i = j;
        }
        return lines;
    }

    public static void main(String[] args) {
        System.out.println(fullJustify(new String[]{"This", "is", "an", "example", "of", "text", "justification."}, 16));
        // expected: ["This    is    an", "example  of text", "justification.  "]

        System.out.println(fullJustify(new String[]{"What", "must", "be", "acknowledgment", "shall", "be"}, 16));
        // expected: ["What   must   be", "acknowledgment  ", "shall be        "]

        System.out.println(fullJustify(new String[]{"Science", "is", "what", "we", "understand", "well", "enough", "to", "explain", "to", "a", "computer."}, 20));
        // expected: ["Science  is  what we", "understand      well", "enough to explain to", "a computer.         "]
    }
}
