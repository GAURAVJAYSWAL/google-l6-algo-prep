package org.example.sorting;

import java.util.Arrays;

public class WiggleSortII {

    /**
     * Goal: nums[0] < nums[1] > nums[2] < nums[3] ... If we place the larger half on
     * the odd indices and the smaller half on the even indices, every neighbor pair
     * straddles the median and the wiggle holds. To keep EQUAL medians from touching,
     * we fill via VIRTUAL INDEXING: walking k = 0,1,2,... we write to real index
     * (2*k+1) % (n|1), which visits odd slots first (1,3,5,...) then wraps to even
     * slots (0,2,4,...). Combined with a 3-way (Dutch flag) partition around the median
     * that pushes values > median to the front-most virtual slots and values < median
     * to the back-most, the two copies of any median value land maximally far apart.
     * The median itself is found in O(n) average by quickselect.
     *
     * Time:  O(n) average — quickselect O(n) plus a single Dutch-flag pass.
     * Space: O(1) — partitions in place over the virtual-index mapping.
     */
    public static void wiggleSort(int[] nums) {
        int n = nums.length;
        if (n < 2) return;

        // Median = the (n/2)-th smallest; quickselect leaves it at index n/2.
        int median = quickselect(nums, n / 2);

        // 3-way partition over virtual indices: greater half -> low virtual slots
        // (odds), smaller half -> high virtual slots (evens), medians in the middle.
        int left = 0, i = 0, right = n - 1;
        while (i <= right) {
            int vi = mapIndex(i, n);
            if (nums[vi] > median) {
                swap(nums, vi, mapIndex(left++, n));
                i++;
            } else if (nums[vi] < median) {
                swap(nums, vi, mapIndex(right--, n));
                // don't advance i: the swapped-in value is still unexamined
            } else {
                i++;
            }
        }
    }

    // Virtual -> real index: odds 1,3,5,... first, then evens 0,2,4,... (n|1 makes odd n safe).
    private static int mapIndex(int i, int n) {
        return (2 * i + 1) % (n | 1);
    }

    // Quickselect: rearrange so the k-th smallest sits at index k; returns its value.
    private static int quickselect(int[] a, int k) {
        int lo = 0, hi = a.length - 1;
        while (lo < hi) {
            int p = partition(a, lo, hi);
            if (p == k) break;
            else if (p < k) lo = p + 1;
            else hi = p - 1;
        }
        return a[k];
    }

    private static int partition(int[] a, int lo, int hi) {
        int mid = lo + (hi - lo) / 2;
        int pivotIdx = medianOfThree(a, lo, mid, hi);   // deterministic pivot, no Math.random
        swap(a, pivotIdx, hi);
        int pivotVal = a[hi];
        int store = lo;
        for (int j = lo; j < hi; j++) {
            if (a[j] < pivotVal) swap(a, j, store++);
        }
        swap(a, store, hi);
        return store;
    }

    private static int medianOfThree(int[] a, int i, int j, int k) {
        if (a[i] < a[j]) {
            if (a[j] < a[k]) return j;
            return a[i] < a[k] ? k : i;
        } else {
            if (a[i] < a[k]) return i;
            return a[j] < a[k] ? k : j;
        }
    }

    private static void swap(int[] a, int i, int j) {
        int t = a[i];
        a[i] = a[j];
        a[j] = t;
    }

    // True if nums strictly wiggles: every interior element is a local max or min.
    private static boolean isWiggle(int[] nums) {
        for (int i = 1; i < nums.length; i++) {
            if (i % 2 == 1 && !(nums[i] > nums[i - 1])) return false;  // odd peaks
            if (i % 2 == 0 && !(nums[i] < nums[i - 1])) return false;  // even valleys
        }
        return true;
    }

    public static void main(String[] args) {
        int[] a = {1, 5, 1, 1, 6, 4};
        wiggleSort(a);
        System.out.println(Arrays.toString(a) + " wiggle? " + isWiggle(a));
        // expected: wiggle? true  (e.g. [1, 6, 1, 5, 1, 4])

        int[] b = {1, 3, 2, 2, 3, 1};
        wiggleSort(b);
        System.out.println(Arrays.toString(b) + " wiggle? " + isWiggle(b));
        // expected: wiggle? true  (equal medians kept apart by virtual indexing)

        int[] c = {4, 5, 5, 6};
        wiggleSort(c);
        System.out.println(Arrays.toString(c) + " wiggle? " + isWiggle(c));
        // expected: wiggle? true

        int[] d = {1, 2};
        wiggleSort(d);
        System.out.println(Arrays.toString(d) + " wiggle? " + isWiggle(d));
        // expected: [1, 2] wiggle? true
    }
}
