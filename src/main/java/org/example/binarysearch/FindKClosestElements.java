package org.example.binarysearch;

import java.util.ArrayList;
import java.util.List;

public class FindKClosestElements {

    /**
     * The answer is always a contiguous length-k window, so we binary search its
     * left edge over [0, n-k]. For a candidate left index mid, compare the window's
     * two outer rivals: x - arr[mid] (gap to the element just left of the window)
     * versus arr[mid + k] - x (gap to the element just right of it). If the left
     * gap is strictly larger, dropping arr[mid] and sliding right is at least as
     * good, so move lo up; otherwise the window is good enough, so move hi down.
     * Ties favor the smaller element, which the < comparison preserves.
     * Time:  O(log(n - k) + k) — search the edge, then copy k elements out.
     * Space: O(k)              — the output list.
     */
    public static List<Integer> findClosestElements(int[] arr, int k, int x) {
        int lo = 0, hi = arr.length - k;                 // left edge ranges over [0, n-k]
        while (lo < hi) {
            int mid = lo + (hi - lo) / 2;
            if (x - arr[mid] > arr[mid + k] - x) {       // element leaving on the right is closer than the one on the left
                lo = mid + 1;                            // slide window right
            } else {
                hi = mid;                                // current left edge is good enough; keep it as candidate
            }
        }
        List<Integer> result = new ArrayList<>(k);
        for (int i = lo; i < lo + k; i++) {
            result.add(arr[i]);
        }
        return result;
    }

    public static void main(String[] args) {
        System.out.println(findClosestElements(new int[]{1, 2, 3, 4, 5}, 4, 3));   // expected: [1, 2, 3, 4]
        System.out.println(findClosestElements(new int[]{1, 2, 3, 4, 5}, 4, -1));  // expected: [1, 2, 3, 4]
        System.out.println(findClosestElements(new int[]{1, 1, 2, 2, 2, 2, 2, 3, 3}, 3, 3)); // expected: [2, 3, 3]
        System.out.println(findClosestElements(new int[]{1, 5, 10, 11, 13}, 2, 8)); // expected: [5, 10] (tie at dist 3 favors smaller 5)
    }
}
