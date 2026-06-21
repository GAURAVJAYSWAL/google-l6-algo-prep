package org.example.strings;

public class ExpressiveWords {

    /**
     * Stretching only ever lengthens a run of equal letters, and only to size >= 3
     * (you can't stretch "ll" to "lll" without already having had >=3, per the rules).
     * So compare S and each word run-by-run: for matching letters a word group of
     * length w can produce an S group of length s ONLY when w == s (untouched), or when
     * s >= 3 and s >= w (the group was extended past the size-3 threshold). A group
     * with s < 3 cannot have been stretched, so it must match w exactly. Both strings
     * must also end together — leftover groups on either side mean a mismatch.
     *
     * Time:  O(|s| * |words|)  — each word is scanned once alongside S.
     * Space: O(1)              — only run-length pointers, no extra structures.
     */
    public static int expressiveWords(String s, String[] words) {
        int count = 0;
        for (String w : words) {
            if (stretchy(s, w)) count++;
        }
        return count;
    }

    private static boolean stretchy(String s, String w) {
        int i = 0, j = 0;                                     // i over s, j over w
        while (i < s.length() && j < w.length()) {
            if (s.charAt(i) != w.charAt(j)) return false;     // group letters must agree

            char c = s.charAt(i);
            int si = i, sj = j;
            while (i < s.length() && s.charAt(i) == c) i++;   // measure S's run length
            while (j < w.length() && w.charAt(j) == c) j++;   // measure w's run length
            int sLen = i - si, wLen = j - sj;

            // Either already equal, or S's run was stretched to >=3 and is at least as long as w's.
            if (sLen == wLen) continue;
            if (sLen >= 3 && sLen >= wLen) continue;
            return false;
        }
        return i == s.length() && j == w.length();            // both fully consumed
    }

    public static void main(String[] args) {
        System.out.println(expressiveWords("heeellooo", new String[]{"hello", "hi", "helo"})); // expected: 1  (only "hello": e 1->3 ok, o 1->3 ok; "helo" has single l vs ll)
        System.out.println(expressiveWords("zzzzzyyyyy", new String[]{"zzyy", "zy", "zyy"}));   // expected: 3  (every group stretches z 2->5, y 2->5)
        System.out.println(expressiveWords("dddiiiinnssssssoooo", new String[]{"dinnssoo", "ddinso", "ddiinnso"})); // expected: 2  ("dinnssoo" and "ddiinnso" stretch; "ddinso" has single 'n' vs run of 2)
        System.out.println(expressiveWords("aaa", new String[]{"aaaa"}));                       // expected: 0  (cannot shrink: target 3 < word 4)
    }
}
