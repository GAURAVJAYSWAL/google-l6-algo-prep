package org.example.dp;

public class StudentAttendanceRecordII {

    private static final int MOD = 1_000_000_007;

    /**
     * State machine over (a, l) where a = total 'A's so far (0 or 1) and l = number of
     * 'L's at the very END of the string (0, 1, or 2) — six states in all. Appending a
     * character drives the transition; a record is valid iff it never reaches a=2 or l=3.
     *   append 'P'  -> trailing lates reset to 0, absences unchanged: (a, *) -> (a, 0)
     *   append 'A'  -> only legal from a=0; goes to (1, 0), also resetting trailing lates
     *   append 'L'  -> only legal from l<2; goes to (a, l+1)
     * dp[a][l] counts records of the current length ending in that state; we roll it n
     * times and sum all six buckets. The 'A' resets lates because an 'A' breaks any run.
     * Time:  O(n) — constant work (six states) per appended character.
     * Space: O(1) — a fixed 2x3 state table.
     */
    public static int checkRecord(int n) {
        // dp[a][l]: records of current length with a absences and l trailing lates.
        long[][] dp = new long[2][3];
        dp[0][0] = 1;                               // the empty record: 0 absences, 0 trailing lates
        for (int step = 0; step < n; step++) {
            long[][] next = new long[2][3];
            for (int a = 0; a < 2; a++) {
                for (int l = 0; l < 3; l++) {
                    long ways = dp[a][l];
                    if (ways == 0) continue;
                    // append 'P': trailing lates collapse to 0, absences kept.
                    next[a][0] = (next[a][0] + ways) % MOD;
                    // append 'A': allowed only if no absence yet; lands in (1, 0).
                    if (a == 0) next[1][0] = (next[1][0] + ways) % MOD;
                    // append 'L': allowed only if fewer than 2 trailing lates.
                    if (l < 2) next[a][l + 1] = (next[a][l + 1] + ways) % MOD;
                }
            }
            dp = next;
        }
        long total = 0;
        for (int a = 0; a < 2; a++)
            for (int l = 0; l < 3; l++)
                total = (total + dp[a][l]) % MOD;
        return (int) total;
    }

    public static void main(String[] args) {
        System.out.println(checkRecord(2));
        // expected: 8 (all length-2 records except "AA")

        System.out.println(checkRecord(1));
        // expected: 3 ("P","L","A")

        System.out.println(checkRecord(10101));
        // expected: 183236316

        System.out.println(checkRecord(0));
        // expected: 1 (the empty record)
    }
}
