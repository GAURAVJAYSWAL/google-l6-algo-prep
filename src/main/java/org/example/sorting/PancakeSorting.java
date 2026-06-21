package org.example.sorting;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PancakeSorting {

    /**
     * Selection sort using only prefix reversals ("flips"). Place the largest unsorted
     * value last, then the next, and so on: for the window arr[0..i], find the current
     * max, flip it to the FRONT (index 0), then flip the whole prefix arr[0..i] to send
     * it to position i where it belongs. Each value is fixed with at most two flips, so
     * the answer has at most 2n flips. We record 1-indexed flip sizes k (reverse the
     * first k elements), as the problem requires.
     *
     * Time:  O(n^2) — n iterations, each scans and flips a prefix of length up to n.
     * Space: O(1) extra beyond the output list of flip sizes.
     */
    public static List<Integer> pancakeSort(int[] arr) {
        List<Integer> flips = new ArrayList<>();
        for (int i = arr.length - 1; i > 0; i--) {
            // Index of the maximum within the still-unsorted prefix arr[0..i].
            int maxIdx = 0;
            for (int j = 1; j <= i; j++) {
                if (arr[j] > arr[maxIdx]) maxIdx = j;
            }
            if (maxIdx == i) continue;     // already in its final spot, no flips needed
            if (maxIdx != 0) {
                flip(arr, maxIdx);         // bring the max to the front
                flips.add(maxIdx + 1);     // k is 1-indexed
            }
            flip(arr, i);                  // send the max from the front to position i
            flips.add(i + 1);
        }
        return flips;
    }

    // Reverse the prefix arr[0..k] (inclusive) — one "pancake flip" of the top k+1.
    private static void flip(int[] arr, int k) {
        int lo = 0, hi = k;
        while (lo < hi) {
            int t = arr[lo];
            arr[lo] = arr[hi];
            arr[hi] = t;
            lo++;
            hi--;
        }
    }

    // Apply a flip sequence and check the array ends up sorted ascending.
    private static boolean verify(int[] original, List<Integer> flips) {
        int[] a = original.clone();
        for (int k : flips) flip(a, k - 1);   // convert 1-indexed size back to a prefix end
        for (int i = 1; i < a.length; i++) if (a[i - 1] > a[i]) return false;
        return true;
    }

    public static void main(String[] args) {
        int[] a = {3, 2, 4, 1};
        System.out.println(pancakeSort(a.clone()) + " sorts? " + verify(a, pancakeSort(a.clone())));
        // expected: e.g. [3, 4, 2, 3, 1, 2] sorts? true  (any valid sequence ending sorted)

        int[] b = {1, 2, 3};
        System.out.println(pancakeSort(b.clone()) + " sorts? " + verify(b, pancakeSort(b.clone())));
        // expected: [] sorts? true  (already sorted -> no flips)

        int[] c = {3, 1, 2};
        System.out.println(pancakeSort(c.clone()) + " sorts? " + verify(c, pancakeSort(c.clone())));
        // expected: e.g. [1, 3, 1, 2] sorts? true

        int[] d = {1};
        System.out.println(pancakeSort(d.clone()) + " sorts? " + verify(d, pancakeSort(d.clone())));
        // expected: [] sorts? true
    }
}
