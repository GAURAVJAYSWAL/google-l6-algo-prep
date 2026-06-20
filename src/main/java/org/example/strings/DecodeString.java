package org.example.strings;

import java.util.ArrayDeque;
import java.util.Deque;

public class DecodeString {

    /**
     * Two stacks track the nested k[...] context. On '[' we push the repeat count
     * and the string built so far, then reset the builder for the inner segment.
     * On ']' we pop the count and the saved prefix, repeat the just-finished inner
     * string k times, and splice it back onto the prefix. Digits accumulate into
     * the current count (multi-digit safe); letters extend the current builder.
     *
     * Time:  O(output)  — total length of the decoded string; each char emitted once.
     * Space: O(output)  — stacks plus the result buffer.
     */
    public static String decodeString(String s) {
        Deque<Integer> countStack = new ArrayDeque<>();
        Deque<StringBuilder> stringStack = new ArrayDeque<>();
        StringBuilder current = new StringBuilder();
        int count = 0;

        for (char c : s.toCharArray()) {
            if (Character.isDigit(c)) {
                count = count * 10 + (c - '0');   // build multi-digit k, e.g. "12["
            } else if (c == '[') {
                countStack.push(count);
                stringStack.push(current);        // stash everything before this bracket
                count = 0;
                current = new StringBuilder();
            } else if (c == ']') {
                int k = countStack.pop();
                StringBuilder prefix = stringStack.pop();
                prefix.append(current.toString().repeat(k));   // expand the inner segment
                current = prefix;
            } else {
                current.append(c);
            }
        }
        return current.toString();
    }

    public static void main(String[] args) {
        System.out.println(decodeString("3[a]2[bc]"));     // expected: aaabcbc
        System.out.println(decodeString("3[a2[c]]"));      // expected: accaccacc
        System.out.println(decodeString("2[abc]3[cd]ef")); // expected: abcabccdcdcdef
        System.out.println(decodeString("10[a]"));         // expected: aaaaaaaaaa
    }
}
