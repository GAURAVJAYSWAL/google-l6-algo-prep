package org.example.strings;

import java.util.ArrayList;
import java.util.List;

public class EncodeAndDecodeStrings {

    /**
     * Length-prefix framing: emit each string as "<len>#<payload>". The decoder reads
     * digits up to the '#', learns exactly how many chars follow, and slices that many
     * verbatim — so any delimiter inside the payload is irrelevant. This is what makes
     * the scheme robust to arbitrary content (including '#' itself or empty strings).
     *
     * Time:  O(total chars)  — encode and decode each touch every character once.
     * Space: O(total chars)  — the encoded string / decoded list.
     */
    public static String encode(List<String> strs) {
        StringBuilder sb = new StringBuilder();
        for (String s : strs) {
            sb.append(s.length()).append('#').append(s);   // header tells decoder the exact span
        }
        return sb.toString();
    }

    public static List<String> decode(String s) {
        List<String> result = new ArrayList<>();
        int i = 0;
        while (i < s.length()) {
            int hash = s.indexOf('#', i);                  // delimiter ends the length header
            int len = Integer.parseInt(s.substring(i, hash));
            int start = hash + 1;
            result.add(s.substring(start, start + len));   // take exactly len chars, content-agnostic
            i = start + len;
        }
        return result;
    }

    public static void main(String[] args) {
        List<String> a = List.of("neet", "code", "love", "you");
        System.out.println(decode(encode(a)).equals(a)); // expected: true

        List<String> b = List.of("we", "say", ":", "yes");
        System.out.println(decode(encode(b)).equals(b)); // expected: true

        // Tricky payloads: embedded delimiter and an empty string survive the round-trip.
        List<String> c = List.of("", "a#b", "12#34", "");
        System.out.println(decode(encode(c)).equals(c)); // expected: true
        System.out.println(encode(c));                   // expected: 0#3#a#b5#12#340#
    }
}
