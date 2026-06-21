package org.example.strings;

public class MinimumWindowSubsequence {

    /**
     * We need the shortest substring of S that contains T as a SUBSEQUENCE (gaps
     * allowed) — distinct from the substring-anagram window of LC 76. The trick is
     * two-phase per candidate: scan forward through S matching T's chars in order;
     * the moment we finish T we have a window ENDING at index i, but its front is
     * loose. So we immediately walk BACKWARD from i, re-matching T in reverse — this
     * snaps the start to the latest possible position, giving the tightest window that
     * still ends at i. We record it if shorter (earliest start wins ties), then resume
     * the forward scan from start+1 to discover the next window. Greedily taking the
     * rightmost valid start at each end guarantees we never miss the global minimum.
     *
     * Time:  O(|s| * |t|)  — each successful match triggers an O(|t|) back-walk.
     * Space: O(1)          — a handful of indices only.
     */
    public static String minWindow(String s, String t) {
        int n = s.length(), m = t.length();
        int bestStart = -1, bestLen = Integer.MAX_VALUE;

        int i = 0;
        while (i < n) {
            int j = 0;                                        // pointer into t for the forward match
            while (i < n) {
                if (s.charAt(i) == t.charAt(j)) {
                    j++;
                    if (j == m) break;                        // matched all of t; window ends at i
                }
                i++;
            }
            if (j < m) break;                                 // ran off s without completing t — done

            // Forward end found at i. Shrink: walk back to the tightest start.
            int end = i;
            j = m - 1;
            while (j >= 0) {
                if (s.charAt(i) == t.charAt(j)) j--;          // re-match t in reverse
                if (j < 0) break;
                i--;
            }
            int start = i;                                    // rightmost start that still spans t

            if (end - start + 1 < bestLen) {                  // strictly shorter -> keep earliest on ties
                bestLen = end - start + 1;
                bestStart = start;
            }
            i = start + 1;                                    // restart search just past this window's start
        }

        return bestStart == -1 ? "" : s.substring(bestStart, bestStart + bestLen);
    }

    public static void main(String[] args) {
        System.out.println(minWindow("abcdebdde", "bde"));   // expected: bcde   (shorter than "bdde")
        System.out.println(minWindow("fgrqsqsnodwmxzkzxwqegkndaa", "kzed")); // expected: kzxwqegknd  (k14 z15 e19 d23 — tightest in-order span)
        System.out.println(minWindow("jmeqksfrsdcmsiwvaovztaqenprpvnbstl", "u")); // expected: (empty)  — 'u' absent
        System.out.println(minWindow("cnhczmccqouqadqtmjjzl", "mtm")); // expected: mccqouqadqtm
    }
}
