package org.example.dp;

public class LongestIncreasingSubsequence {

    /**
     * Patience sorting: keep `tails`, where tails[k] is the smallest possible tail of
     * any increasing subsequence of length k+1. For each x, binary search the first
     * tail >= x and overwrite it (x extends a shorter run more cheaply); if none is
     * >= x, x extends the longest run so we append. tails stays sorted, so the search
     * is valid, and its length is the LIS length. (The classic O(n^2) DP — dp[i] =
     * 1 + max dp[j] over j<i with nums[j]<nums[i] — is simpler but quadratic.)
     * Time:  O(n log n)  — one binary search per element.
     * Space: O(n)        — the tails array.
     */
    public static int lengthOfLIS(int[] nums) {
        int[] tails = new int[nums.length];
        int size = 0;                                    // current LIS length == number of active tails
        for (int x : nums) {
            int lo = 0, hi = size;                       // find leftmost tail >= x (lower bound)
            while (lo < hi) {
                int mid = lo + (hi - lo) / 2;
                if (tails[mid] < x) lo = mid + 1;
                else hi = mid;
            }
            tails[lo] = x;                               // overwrite that tail, or append when lo == size
            if (lo == size) size++;                      // x started a strictly longer subsequence
        }
        return size;
    }

    public static void main(String[] args) {
        System.out.println(lengthOfLIS(new int[]{10, 9, 2, 5, 3, 7, 101, 18})); // expected: 4
        System.out.println(lengthOfLIS(new int[]{0, 1, 0, 3, 2, 3}));           // expected: 4
        System.out.println(lengthOfLIS(new int[]{7, 7, 7, 7, 7, 7, 7}));        // expected: 1
        System.out.println(lengthOfLIS(new int[]{4, 10, 4, 3, 8, 9}));          // expected: 3
    }
}
