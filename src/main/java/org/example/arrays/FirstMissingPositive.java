package org.example.arrays;

public class FirstMissingPositive {

    /**
     * Use the array itself as a hash: the answer must lie in [1, n+1], so place
     * each value v in [1, n] at its home slot index v-1 via cyclic swaps. After
     * placement, the first index i whose nums[i] != i+1 reveals the missing
     * positive i+1; if all match, every value 1..n is present so the answer is n+1.
     * The while-swap runs amortized O(n) because each swap parks one value home.
     * Time:  O(n) — amortized constant work per element. Space: O(1) — in place.
     */
    public static int firstMissingPositive(int[] nums) {
        int n = nums.length;
        for (int i = 0; i < n; i++) {
            // swap nums[i] into its home slot until it's out of range or already correct
            while (nums[i] > 0 && nums[i] <= n && nums[nums[i] - 1] != nums[i]) {
                int home = nums[i] - 1;
                int t = nums[home];
                nums[home] = nums[i];
                nums[i] = t;
            }
        }
        for (int i = 0; i < n; i++) {
            if (nums[i] != i + 1) return i + 1;      // first gap in the 1..n sequence
        }
        return n + 1;                                // 1..n all present
    }

    public static void main(String[] args) {
        System.out.println(firstMissingPositive(new int[]{1, 2, 0}));        // expected: 3
        System.out.println(firstMissingPositive(new int[]{3, 4, -1, 1}));     // expected: 2
        System.out.println(firstMissingPositive(new int[]{7, 8, 9, 11, 12})); // expected: 1
        System.out.println(firstMissingPositive(new int[]{1, 2, 3}));        // expected: 4
    }
}
