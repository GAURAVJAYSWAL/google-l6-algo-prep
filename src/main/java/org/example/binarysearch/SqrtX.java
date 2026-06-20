package org.example.binarysearch;

public class SqrtX {

    /**
     * floor(sqrt(x)) is the largest k with k*k <= x, and k*k is monotonic, so the
     * valid k form a prefix [0 .. answer]. We binary search the last k that
     * satisfies the predicate. mid*mid can exceed int range, so we compute it as
     * a long.
     * Time:  O(log x) — halve the candidate range each step.
     * Space: O(1)     — a few scalars.
     */
    public static int mySqrt(int x) {
        int lo = 0, hi = x;                              // sqrt(x) <= x for all x >= 0
        int ans = 0;
        while (lo <= hi) {
            int mid = lo + (hi - lo) / 2;
            if ((long) mid * mid <= x) {                 // cast guards against int overflow
                ans = mid;                               // mid is a valid floor; try larger
                lo = mid + 1;
            } else {
                hi = mid - 1;
            }
        }
        return ans;
    }

    public static void main(String[] args) {
        System.out.println(mySqrt(4));          // expected: 2
        System.out.println(mySqrt(8));          // expected: 2
        System.out.println(mySqrt(0));          // expected: 0
        System.out.println(mySqrt(2147483647)); // expected: 46340
    }
}
