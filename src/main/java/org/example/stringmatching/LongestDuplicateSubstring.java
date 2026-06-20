package org.example.stringmatching;

import java.util.HashMap;
import java.util.Map;

/**
 * LC 1044. Longest Duplicate Substring.
 */
public class LongestDuplicateSubstring {

    private static final long MOD = (1L << 61) - 1; // large Mersenne prime keeps products in a long
    private static final long BASE = 131;

    /**
     * Key insight: "is there a duplicated substring of length L?" is monotonic — if length L repeats,
     * so does any shorter length — so we binary-search L over [1, n-1]. The feasibility check at a
     * fixed L is a Rabin-Karp rolling hash: slide a window of width L computing each hash in O(1) from
     * the previous one, and remember, per hash value, the start index seen. When a hash recurs we have
     * a candidate; we confirm by comparing the actual substrings (guarding against the rare modular
     * collision) before declaring a real duplicate. The longest L that passes gives the answer string.
     *
     * Using a 2^61-1 modulus plus an explicit substring verification makes false positives effectively
     * impossible; a double-hash would be an alternative way to dodge collisions without the verify.
     *
     * Time:  O(n log n) — log n binary-search steps, each an O(n) rolling-hash scan (amortized verify).
     * Space: O(n) — the hash-to-start map at the probed length.
     */
    public static String longestDupSubstring(String s) {
        int n = s.length();
        long[] h = new long[n + 1];   // prefix hashes: h[i] covers s[0..i)
        long[] pow = new long[n + 1]; // pow[k] = BASE^k mod MOD, for windowed hash extraction
        pow[0] = 1;
        for (int i = 0; i < n; i++) {
            h[i + 1] = mulmod(h[i], BASE) + s.charAt(i);
            if (h[i + 1] >= MOD) h[i + 1] -= MOD;
            pow[i + 1] = mulmod(pow[i], BASE);
        }

        int lo = 1, hi = n - 1, bestStart = -1, bestLen = 0;
        while (lo <= hi) {
            int mid = (lo + hi) >>> 1;
            int start = firstDuplicateStart(s, h, pow, mid);
            if (start >= 0) {                 // a length-mid repeat exists -> try longer
                bestStart = start;
                bestLen = mid;
                lo = mid + 1;
            } else {
                hi = mid - 1;                 // nothing repeats at this length -> shrink
            }
        }
        return bestStart < 0 ? "" : s.substring(bestStart, bestStart + bestLen);
    }

    /** Rolling-hash scan; returns the start index of some duplicated length-L window, or -1. */
    private static int firstDuplicateStart(String s, long[] h, long[] pow, int len) {
        Map<Long, Integer> seen = new HashMap<>();
        for (int i = 0; i + len <= s.length(); i++) {
            long hash = windowHash(h, pow, i, i + len);
            Integer prev = seen.get(hash);
            // Verify the actual characters to rule out a hash collision before accepting.
            if (prev != null && s.regionMatches(prev, s, i, len)) return i;
            if (prev == null) seen.put(hash, i);
        }
        return -1;
    }

    /** Hash of s[from..to) derived from prefix hashes in O(1). */
    private static long windowHash(long[] h, long[] pow, int from, int to) {
        long res = h[to] - mulmod(h[from], pow[to - from]) % MOD;
        res %= MOD;
        if (res < 0) res += MOD;
        return res;
    }

    /** Multiplies two values mod 2^61-1 without overflowing a 64-bit long, via 32-bit splitting. */
    private static long mulmod(long a, long b) {
        long aHi = a >> 31, aLo = a & 0x7fffffffL;
        long mid = aHi * b % MOD;             // (aHi << 31) * b, folded mod the Mersenne prime
        long res = ((mid << 31) % MOD) + aLo * b % MOD;
        res %= MOD;
        if (res < 0) res += MOD;
        return res;
    }

    public static void main(String[] args) {
        System.out.println(longestDupSubstring("banana"));    // expected: ana
        System.out.println(longestDupSubstring("abcd"));      // expected: (empty -> no duplicate)
        System.out.println(longestDupSubstring("aaaaa"));     // expected: aaaa
        System.out.println(longestDupSubstring("aabcaabdaab")); // expected: aab
    }
}
