package org.example.strings;

import java.util.Arrays;

public class FindAndReplaceInString {

    /**
     * All replacement indices refer to the ORIGINAL string and the problem guarantees
     * non-overlapping matches, so we can sort operations by their source index and
     * stitch the answer left to right: copy the untouched gap up to the next index,
     * then if the source substring actually matches, emit the target and jump past
     * the source; otherwise emit the original char. Sorting lets us walk both the
     * string and the operation list with a single forward cursor.
     *
     * Time:  O(m log m + n + total replacement length)  — m = #ops, n = |s|.
     * Space: O(m)                                        — index permutation plus the output builder.
     */
    public static String findReplaceString(String s, int[] indices, String[] sources, String[] targets) {
        int m = indices.length;
        Integer[] order = new Integer[m];
        for (int i = 0; i < m; i++) order[i] = i;
        Arrays.sort(order, (a, b) -> indices[a] - indices[b]);   // process replacements by position in s

        StringBuilder sb = new StringBuilder();
        int cursor = 0;   // next untouched index in s
        for (int idx : order) {
            int at = indices[idx];
            sb.append(s, cursor, at);          // copy the gap before this potential replacement
            String src = sources[idx];
            if (s.startsWith(src, at)) {       // match check against the original string
                sb.append(targets[idx]);
                cursor = at + src.length();    // skip the consumed source
            } else {
                cursor = at;                   // no match: leave s[at] to be copied by the next gap/tail
            }
        }
        sb.append(s, cursor, s.length());      // trailing remainder
        return sb.toString();
    }

    public static void main(String[] args) {
        System.out.println(findReplaceString("abcd", new int[]{0, 2}, new String[]{"a", "cd"}, new String[]{"eee", "ffff"})); // expected: eeebffff
        System.out.println(findReplaceString("abcd", new int[]{0, 2}, new String[]{"ab", "ec"}, new String[]{"eee", "ffff"})); // expected: eeecd
        System.out.println(findReplaceString("vmokgggqzp", new int[]{3, 5, 1}, new String[]{"kg", "ggq", "mo"}, new String[]{"s", "so", "bfr"})); // expected: vbfrssozp
    }
}
