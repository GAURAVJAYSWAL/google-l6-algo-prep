package org.example.binarysearch;

/**
 * LC 278 — First Bad Version (Easy).
 *
 * Binary search over a monotonic predicate rather than over stored values: versions
 * read false…false,true…true (good…good,bad…bad), and we want the first {@code true}.
 */
public class FirstBadVersion {

    // Field-configurable mock of the hidden API: every version >= firstBad is "bad".
    private int firstBad;

    boolean isBadVersion(int version) {
        return version >= firstBad;
    }

    /**
     * The predicate is a step function, so we search for its boundary, not a key. Keeping
     * the answer inside {@code [lo, hi]}: when {@code mid} is bad the first bad is at mid or
     * left ({@code hi = mid}); when good it is strictly right ({@code lo = mid + 1}). The
     * window shrinks to a single index, which is the boundary. The overflow-safe midpoint
     * matters because {@code n} can be near {@link Integer#MAX_VALUE}.
     * Time:  O(log n) — the candidate range halves each step.
     * Space: O(1) — two indices only.
     */
    public int firstBadVersion(int n) {
        int lo = 1, hi = n;
        while (lo < hi) {                   // stop when the range collapses to one version
            int mid = lo + (hi - lo) / 2;   // never overflows even at n == Integer.MAX_VALUE
            if (isBadVersion(mid)) {
                hi = mid;                   // mid could itself be the first bad — keep it
            } else {
                lo = mid + 1;               // mid is good, so the boundary is to its right
            }
        }
        return lo;
    }

    public static void main(String[] args) {
        FirstBadVersion f = new FirstBadVersion();

        f.firstBad = 4;
        System.out.println(f.firstBadVersion(5));                   // expected: 4

        f.firstBad = 1;
        System.out.println(f.firstBadVersion(1));                   // expected: 1

        f.firstBad = Integer.MAX_VALUE;
        System.out.println(f.firstBadVersion(Integer.MAX_VALUE));   // expected: 2147483647
    }
}
