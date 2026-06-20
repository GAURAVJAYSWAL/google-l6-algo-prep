package org.example.dp;

public class DecodeWays {

    /**
     * State: dp[i] = number of decodings of the prefix s[0..i). Two transitions feed
     * dp[i]: take s[i-1] as a ONE-digit letter (valid for '1'..'9') contributing
     * dp[i-1], and take s[i-2..i) as a TWO-digit letter (valid for "10".."26")
     * contributing dp[i-2]. A leading '0' decodes nothing on its own, so it only
     * survives as the second digit of 10 or 20. dp[0] = 1 (empty string). Carry just
     * the last two counts as rolling variables.
     * Time:  O(n)  — one pass over the string.
     * Space: O(1)  — two rolling counters.
     */
    public static int numDecodings(String s) {
        if (s.charAt(0) == '0') return 0;                // a string starting with 0 has no valid first letter
        int prev2 = 1, prev1 = 1;                        // dp[0] = 1, dp[1] = 1 (first char already known non-zero)
        for (int i = 2; i <= s.length(); i++) {
            int cur = 0;
            if (s.charAt(i - 1) != '0') {
                cur += prev1;                            // s[i-1] stands alone as 1..9
            }
            int two = (s.charAt(i - 2) - '0') * 10 + (s.charAt(i - 1) - '0');
            if (two >= 10 && two <= 26) {
                cur += prev2;                            // s[i-2..i) pairs into 10..26
            }
            prev2 = prev1;
            prev1 = cur;
        }
        return prev1;
    }

    public static void main(String[] args) {
        System.out.println(numDecodings("12"));    // expected: 2
        System.out.println(numDecodings("226"));   // expected: 3
        System.out.println(numDecodings("06"));    // expected: 0
        System.out.println(numDecodings("2101"));  // expected: 1
    }
}
