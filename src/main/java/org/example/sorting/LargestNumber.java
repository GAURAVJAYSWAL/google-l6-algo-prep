package org.example.sorting;

import java.util.Arrays;

public class LargestNumber {

    /**
     * Sort the numbers by a custom order: a should come before b iff the
     * concatenation (a+b) is numerically larger than (b+a). This pairwise rule is a
     * valid total order (it's transitive on the digit strings), and greedily placing
     * the "more valuable when in front" number first yields the globally largest
     * concatenation. The one trap is all zeros — "30","0","0" would join to "3000",
     * but [0,0] joins to "00", so if the leading char is '0' the whole thing is "0".
     *
     * Time:  O(n log n * k) — comparison sort, each compare concatenates k-digit strings.
     * Space: O(n) — boxed string array for the comparator.
     */
    public static String largestNumber(int[] nums) {
        String[] strs = new String[nums.length];
        for (int i = 0; i < nums.length; i++) strs[i] = Integer.toString(nums[i]);

        // Descending: whichever ordering of the pair makes the bigger number wins.
        Arrays.sort(strs, (a, b) -> (b + a).compareTo(a + b));

        // Largest is "0" only when the biggest piece is itself 0 (all zeros).
        if (strs[0].equals("0")) return "0";

        StringBuilder sb = new StringBuilder();
        for (String s : strs) sb.append(s);
        return sb.toString();
    }

    public static void main(String[] args) {
        System.out.println(largestNumber(new int[]{10, 2}));            // expected: "210"
        System.out.println(largestNumber(new int[]{3, 30, 34, 5, 9}));  // expected: "9534330"
        System.out.println(largestNumber(new int[]{0, 0}));            // expected: "0" (all-zeros edge case)
        System.out.println(largestNumber(new int[]{1}));               // expected: "1"
        System.out.println(largestNumber(new int[]{432, 43, 43}));     // expected: "4343432"
    }
}
