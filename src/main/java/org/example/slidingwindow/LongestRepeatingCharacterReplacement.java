package org.example.slidingwindow;

public class LongestRepeatingCharacterReplacement {

    /**
     * A window is "fixable" with k replacements when (windowLen - countOfMostFrequentChar) <= k,
     * i.e. the minority chars can all be rewritten. Grow right always; when the window
     * stops being fixable, slide left by one to keep length non-decreasing. maxFreq is
     * never decreased — a stale-but-too-large maxFreq only makes the window over-confident
     * by exactly the steps left moved, so the best answer is still captured.
     *
     * Time:  O(n)  — both pointers move forward at most n times; freq update is O(1).
     * Space: O(1)  — fixed 26-slot frequency array.
     */
    public static int characterReplacement(String s, int k) {
        int[] freq = new int[26];
        int left = 0, maxFreq = 0, best = 0;

        for (int right = 0; right < s.length(); right++) {
            maxFreq = Math.max(maxFreq, ++freq[s.charAt(right) - 'A']);
            // Replacements needed = window size minus its dominant char; if it exceeds k, slide.
            if ((right - left + 1) - maxFreq > k) {
                freq[s.charAt(left) - 'A']--;
                left++;
            }
            best = Math.max(best, right - left + 1);
        }
        return best;
    }

    public static void main(String[] args) {
        System.out.println(characterReplacement("ABAB", 2));     // expected: 4
        System.out.println(characterReplacement("AABABBA", 1));  // expected: 4
        System.out.println(characterReplacement("AAAA", 0));     // expected: 4
        System.out.println(characterReplacement("ABCDE", 1));    // expected: 2
    }
}
