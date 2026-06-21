package org.example.sorting;

import java.util.Arrays;

/**
 * LC 912. Sort an Array.
 *
 * A reference implementation showing three classic O(n log n) sorts from scratch,
 * each self-contained so it can be lifted into an interview verbatim.
 */
public class SortAnArray {

    // ---------------------------------------------------------------------
    // 1) Merge sort — stable, top-down.
    // ---------------------------------------------------------------------

    /**
     * Divide the array in half, sort each half recursively, then merge the two
     * sorted runs by always taking the smaller front element. Taking the LEFT
     * element on ties is exactly what makes the merge — and thus the whole sort —
     * stable: equal keys keep their original relative order.
     *
     * Time:  O(n log n) — log n levels of halving, each level merges all n elements.
     * Space: O(n) — a scratch buffer for the merge (plus O(log n) recursion stack).
     */
    public static int[] mergeSort(int[] nums) {
        int[] arr = nums.clone();          // don't mutate the caller's array
        int[] buffer = new int[arr.length];
        mergeSortInto(arr, buffer, 0, arr.length - 1);
        return arr;
    }

    private static void mergeSortInto(int[] a, int[] buf, int lo, int hi) {
        if (lo >= hi) return;              // 0 or 1 element is already sorted
        int mid = lo + (hi - lo) / 2;
        mergeSortInto(a, buf, lo, mid);
        mergeSortInto(a, buf, mid + 1, hi);
        merge(a, buf, lo, mid, hi);
    }

    private static void merge(int[] a, int[] buf, int lo, int mid, int hi) {
        int i = lo, j = mid + 1, k = lo;
        while (i <= mid && j <= hi) {
            // '<=' takes the left run first on ties -> stable.
            if (a[i] <= a[j]) buf[k++] = a[i++];
            else buf[k++] = a[j++];
        }
        while (i <= mid) buf[k++] = a[i++];
        while (j <= hi) buf[k++] = a[j++];
        System.arraycopy(buf, lo, a, lo, hi - lo + 1);
    }

    // ---------------------------------------------------------------------
    // 2) Quicksort — in-place, deterministic median-of-three pivot.
    // ---------------------------------------------------------------------

    /**
     * Partition around a pivot so all smaller elements precede it and all larger
     * follow, then recurse on each side. The pivot is chosen median-of-three
     * (first/mid/last) DETERMINISTICALLY — no Math.random — which dodges the
     * already-sorted-input O(n^2) trap a naive first/last pivot would hit, while
     * staying perfectly reproducible. Not stable: long-range swaps reorder equal keys.
     *
     * Time:  O(n log n) average; O(n^2) worst case only on adversarial pivots.
     * Space: O(log n) — in-place partition, recursion bounded by smaller-side depth.
     */
    public static int[] quickSort(int[] nums) {
        int[] arr = nums.clone();
        quickSortRange(arr, 0, arr.length - 1);
        return arr;
    }

    private static void quickSortRange(int[] a, int lo, int hi) {
        while (lo < hi) {
            int p = partition(a, lo, hi);
            // Recurse into the smaller half, loop on the larger -> O(log n) stack.
            if (p - lo < hi - p) {
                quickSortRange(a, lo, p - 1);
                lo = p + 1;
            } else {
                quickSortRange(a, p + 1, hi);
                hi = p - 1;
            }
        }
    }

    // Lomuto partition; returns the pivot's final (sorted) index.
    private static int partition(int[] a, int lo, int hi) {
        int mid = lo + (hi - lo) / 2;
        int pivotIdx = medianOfThree(a, lo, mid, hi);
        swap(a, pivotIdx, hi);             // park the pivot at the end for the scan
        int pivotVal = a[hi];
        int store = lo;                    // a[lo..store-1] are all < pivotVal
        for (int i = lo; i < hi; i++) {
            if (a[i] < pivotVal) swap(a, i, store++);
        }
        swap(a, store, hi);                // drop the pivot into its sorted slot
        return store;
    }

    // Index (among i, j, k) whose value is the median — a robust pivot pick.
    private static int medianOfThree(int[] a, int i, int j, int k) {
        if (a[i] < a[j]) {
            if (a[j] < a[k]) return j;
            return a[i] < a[k] ? k : i;
        } else {
            if (a[i] < a[k]) return i;
            return a[j] < a[k] ? k : j;
        }
    }

    // ---------------------------------------------------------------------
    // 3) Heap sort — in-place, sift-down.
    // ---------------------------------------------------------------------

    /**
     * Build a max-heap in place (sift down from the last internal node up), then
     * repeatedly swap the root (current maximum) to the end of the heap region and
     * sift the new root down to restore the heap. Each extraction grows a sorted
     * suffix, so the array ends up ascending. Not stable: swaps move equal keys past
     * each other.
     *
     * Time:  O(n log n) — O(n) to build, then n extractions each O(log n) to sift.
     * Space: O(1) — the heap lives in the array itself, no recursion.
     */
    public static int[] heapSort(int[] nums) {
        int[] a = nums.clone();
        int n = a.length;
        // Heapify: every node from the last parent down to the root.
        for (int i = n / 2 - 1; i >= 0; i--) siftDown(a, i, n);
        // Extract max to the back, shrink the heap, restore.
        for (int end = n - 1; end > 0; end--) {
            swap(a, 0, end);
            siftDown(a, 0, end);           // 'end' = new (smaller) heap size
        }
        return a;
    }

    // Push a[i] down within heap region [0, size) until the max-heap property holds.
    private static void siftDown(int[] a, int i, int size) {
        while (true) {
            int left = 2 * i + 1, right = 2 * i + 2, largest = i;
            if (left < size && a[left] > a[largest]) largest = left;
            if (right < size && a[right] > a[largest]) largest = right;
            if (largest == i) break;       // heap property restored
            swap(a, i, largest);
            i = largest;
        }
    }

    private static void swap(int[] a, int i, int j) {
        int t = a[i];
        a[i] = a[j];
        a[j] = t;
    }

    public static void main(String[] args) {
        int[][] inputs = {
                {5, 2, 3, 1},
                {5, 1, 1, 2, 0, 0},
                {1, 2, 3, 4, 5, 6, 7},     // already sorted: median-of-three keeps quicksort fast
                {3},
                {-3, 0, -1, 7, -3, 2}
        };
        for (int[] in : inputs) {
            int[] expected = in.clone();
            Arrays.sort(expected);
            int[] m = mergeSort(in);
            int[] q = quickSort(in);
            int[] h = heapSort(in);
            boolean ok = Arrays.equals(m, expected)
                    && Arrays.equals(q, expected)
                    && Arrays.equals(h, expected);
            System.out.printf("input=%s | sorted=%s | merge=%s quick=%s heap=%s | %s%n",
                    Arrays.toString(in), Arrays.toString(expected),
                    Arrays.toString(m), Arrays.toString(q), Arrays.toString(h),
                    ok ? "PASS" : "FAIL");
        }
        // expected: every line PASS, each of merge/quick/heap equal to the sorted array, e.g.
        // input=[5, 2, 3, 1] -> sorted=[1, 2, 3, 5]
        // input=[5, 1, 1, 2, 0, 0] -> sorted=[0, 0, 1, 1, 2, 5]
        // input=[3] -> sorted=[3]
    }
}
