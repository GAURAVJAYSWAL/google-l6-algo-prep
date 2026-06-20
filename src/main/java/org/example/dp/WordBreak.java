package org.example.dp;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class WordBreak {

    /**
     * State: dp[i] = can the prefix s[0..i) be fully segmented into dictionary words.
     * Transition: dp[i] is true iff there is a split point j < i where dp[j] is true
     * AND s[j..i) is a dictionary word — i.e. a valid segmentation of the first j
     * chars followed by one word ending at i. dp[0] = true (the empty prefix). The
     * answer is dp[n].
     * Time:  O(n^2 * L)  — for each end i scan all starts j; substring/lookup costs L.
     * Space: O(n)        — the dp array (plus the word set).
     */
    public static boolean wordBreak(String s, List<String> wordDict) {
        Set<String> dict = new HashSet<>(wordDict);
        int n = s.length();
        boolean[] dp = new boolean[n + 1];
        dp[0] = true;                                    // empty prefix is trivially segmentable
        for (int i = 1; i <= n; i++) {
            for (int j = 0; j < i; j++) {
                if (dp[j] && dict.contains(s.substring(j, i))) {
                    dp[i] = true;                        // [0..j) splits and s[j..i) is a word ending here
                    break;                               // one valid split is enough
                }
            }
        }
        return dp[n];
    }

    public static void main(String[] args) {
        System.out.println(wordBreak("leetcode", List.of("leet", "code")));             // expected: true
        System.out.println(wordBreak("applepenapple", List.of("apple", "pen")));        // expected: true
        System.out.println(wordBreak("catsandog", List.of("cats", "dog", "sand", "and", "cat"))); // expected: false
        System.out.println(wordBreak("a", List.of("a")));                               // expected: true
    }
}
