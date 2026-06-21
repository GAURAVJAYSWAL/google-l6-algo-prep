package org.example.strings;

public class LongPressedName {

    /**
     * A long press only ever DUPLICATES a character that was genuinely typed, so every
     * extra char in `typed` must equal the char immediately before it. We walk `name`
     * with i and `typed` with j: on a match we consume both; on a mismatch the only
     * legal explanation is that typed[j] repeats the previous typed char (a held key),
     * so we skip j alone. Any other mismatch is a real typo. The name is accepted iff
     * we manage to consume all of it this way (j may still have trailing repeats left).
     *
     * Time:  O(|typed|)  — j advances every step and never rewinds.
     * Space: O(1)        — two indices only.
     */
    public static boolean isLongPressedName(String name, String typed) {
        int i = 0, j = 0;
        while (j < typed.length()) {
            if (i < name.length() && name.charAt(i) == typed.charAt(j)) {
                i++;                                          // matched a real keystroke
                j++;
            } else if (j > 0 && typed.charAt(j) == typed.charAt(j - 1)) {
                j++;                                          // a long-press repeat of the prior char
            } else {
                return false;                                 // unexplained extra/wrong char
            }
        }
        return i == name.length();                            // all of name consumed
    }

    public static void main(String[] args) {
        System.out.println(isLongPressedName("alex", "aaleex"));  // expected: true
        System.out.println(isLongPressedName("saeed", "ssaaedd")); // expected: false  ('e' never doubled to cover the second e)
        System.out.println(isLongPressedName("leelee", "lleeelee")); // expected: true
        System.out.println(isLongPressedName("pyplrz", "ppyypllr")); // expected: false  ('z' missing in typed)
    }
}
