package org.example.heap;

import java.util.Arrays;

public class KthLargestElementInAnArray {

    /**
     * Quickselect: the k-th largest is the (n-k)-th smallest in sorted order. Lomuto-partition
     * around a pivot so everything left of its final index is smaller; that index IS the pivot's
     * sorted rank, so we recurse into only the side that can contain our target — average O(n)
     * because the work halves each step. The pivot is chosen median-of-three (first/mid/last)
     * deterministically — no Math.random — which dodges the sorted-input worst case while staying
     * reproducible. (Alternative: a size-k min-heap gives O(n log k) and never mutates the input,
     * preferable for streaming, but quickselect wins on a static array.)
     *
     * Time:  O(n) average (geometric sum n + n/2 + ...); O(n^2) worst case on adversarial pivots.
     * Space: O(1) — partitions in place, iterative loop (no recursion stack).
     */
    public static int findKthLargest(int[] nums, int k) {
        int target = nums.length - k; // sorted-ascending index of the answer
        int lo = 0, hi = nums.length - 1;
        while (lo < hi) {
            int p = partition(nums, lo, hi);
            if (p == target) break;
            else if (p < target) lo = p + 1; // answer lies to the right
            else hi = p - 1;                  // answer lies to the left
        }
        return nums[target];
    }

    // Lomuto partition; returns the final resting index of the chosen pivot value.
    private static int partition(int[] a, int lo, int hi) {
        int mid = lo + (hi - lo) / 2;
        int pivot = medianOfThree(a, lo, mid, hi); // index of the median of the three samples
        swap(a, pivot, hi);                         // park pivot at the end for the scan
        int pivotVal = a[hi];
        int store = lo; // boundary: a[lo..store-1] are all < pivotVal
        for (int i = lo; i < hi; i++) {
            if (a[i] < pivotVal) {
                swap(a, i, store++);
            }
        }
        swap(a, store, hi); // drop the pivot into its sorted slot
        return store;
    }

    // Return the index (among i, j, k) whose value is the median — a robust pivot pick.
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

    public static void main(String[] args) {
        System.out.println(findKthLargest(new int[]{3, 2, 1, 5, 6, 4}, 2));       // expected: 5
        System.out.println(findKthLargest(new int[]{3, 2, 3, 1, 2, 4, 5, 5, 6}, 4)); // expected: 4
        System.out.println(findKthLargest(new int[]{1}, 1));                       // expected: 1
        System.out.println(findKthLargest(new int[]{7, 7, 7, 7}, 3));              // expected: 7 (duplicates)
        // Sorted input would be the quicksort worst case; median-of-three keeps it linear.
        System.out.println(findKthLargest(new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9}, 1)); // expected: 9
    }
}
