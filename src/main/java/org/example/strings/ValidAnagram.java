package org.example.strings;

public class ValidAnagram {

    /**
     * Anagrams have identical character multisets. Tally s with +1 and t with -1 in a
     * single 26-slot array; if every slot returns to zero the multisets matched. A
     * length mismatch is an instant no.
     *
     * Time:  O(n)  — one pass over each string.
     * Space: O(1)  — fixed 26-element counter (lowercase letters only).
     */
    public static boolean isAnagram(String s, String t) {
        if (s.length() != t.length()) return false;

        int[] count = new int[26];
        for (int i = 0; i < s.length(); i++) {
            count[s.charAt(i) - 'a']++;
            count[t.charAt(i) - 'a']--;   // net zero everywhere iff same letters in same amounts
        }
        for (int c : count) {
            if (c != 0) return false;
        }
        return true;
    }

    public static void main(String[] args) {
        System.out.println(isAnagram("anagram", "nagaram")); // expected: true
        System.out.println(isAnagram("rat", "car"));         // expected: false
        System.out.println(isAnagram("a", "ab"));            // expected: false
        System.out.println(isAnagram("", ""));               // expected: true
    }
}
