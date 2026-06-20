package org.example.strings;

public class SentenceScreenFitting {

    /**
     * Lay the sentence out as one cyclic stream "word1 word2 ... wordk " (note the
     * trailing space). For a row of given width, fitting is just: advance a cursor
     * by `cols` characters, then if the landing char is a space skip past it, else
     * back up to the previous space. The number of full sentences shown equals
     * cursor / streamLength after processing all rows. We memoize, per starting
     * word, the next starting word and how many sentences a single row consumes,
     * so repeated row shapes are answered in O(1).
     *
     * Time:  O(rows + k * L)  — each distinct start word built once into the row cache, k = #words, L = max word length.
     * Space: O(k)             — two memo arrays keyed by starting word index.
     */
    public static int wordsTyping(String[] sentence, int rows, int cols) {
        int k = sentence.length;
        int[] nextStart = new int[k];      // starting word of the following row, given this row starts at i
        int[] sentencesAdded = new int[k]; // full sentences completed while filling one row that starts at word i

        for (int i = 0; i < k; i++) {
            int start = i;
            int used = 0;          // columns consumed so far in this row
            int completed = 0;     // sentences finished within this row
            while (used + sentence[start].length() <= cols) {
                used += sentence[start].length() + 1;   // +1 for the trailing space after the word
                start++;
                if (start == k) {                        // wrapped past the last word => one full sentence placed
                    start = 0;
                    completed++;
                }
            }
            nextStart[i] = start;
            sentencesAdded[i] = completed;
        }

        int count = 0;
        int word = 0;
        for (int r = 0; r < rows; r++) {
            count += sentencesAdded[word];   // O(1) per row thanks to the cache
            word = nextStart[word];
        }
        return count;
    }

    public static void main(String[] args) {
        System.out.println(wordsTyping(new String[]{"hello", "world"}, 2, 8));               // expected: 1
        System.out.println(wordsTyping(new String[]{"a", "bcd", "e"}, 3, 6));                // expected: 2
        System.out.println(wordsTyping(new String[]{"i", "had", "apple", "pie"}, 4, 5));     // expected: 1
        System.out.println(wordsTyping(new String[]{"f", "p", "a"}, 8, 7));                  // expected: 10
    }
}
