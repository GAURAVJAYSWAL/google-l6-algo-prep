package org.example.strings;

public class RansomNote {

    /**
     * The note is buildable iff, letter by letter, the magazine supplies at least as
     * many of each character as the note demands — order is irrelevant, only counts
     * matter. We tally the magazine's 26 lowercase letters once, then spend them down
     * as we read the note; the instant any letter's remaining supply dips below zero
     * we know the note out-demands the magazine and can stop early.
     *
     * Time:  O(|magazine| + |ransomNote|)  — one pass to count, one pass to spend.
     * Space: O(1)                          — a fixed 26-slot frequency array.
     */
    public static boolean canConstruct(String ransomNote, String magazine) {
        int[] freq = new int[26];
        for (int k = 0; k < magazine.length(); k++) {
            freq[magazine.charAt(k) - 'a']++;
        }
        for (int k = 0; k < ransomNote.length(); k++) {
            if (--freq[ransomNote.charAt(k) - 'a'] < 0) {     // demanded a letter the magazine ran out of
                return false;
            }
        }
        return true;
    }

    public static void main(String[] args) {
        System.out.println(canConstruct("a", "b"));        // expected: false
        System.out.println(canConstruct("aa", "ab"));      // expected: false  (only one 'a' available)
        System.out.println(canConstruct("aa", "aab"));     // expected: true
        System.out.println(canConstruct("", "anything"));  // expected: true   (empty note needs nothing)
    }
}
