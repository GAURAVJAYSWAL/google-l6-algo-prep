package org.example.phase1;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * TWO-POINTER PATTERN — 11 PRACTICE PROBLEMS (Google L6 prep)
 * ============================================================================
 * #   Problem                          Technique                      Time      Space
 * ----------------------------------------------------------------------------
 * EASY
 * 1  Valid Palindrome                 Opposite-end ptrs, skip junk   O(n)      O(1)
 * 2  Squares of a Sorted Array        Both ends, fill from back      O(n)      O(n)
 * 3  Move Zeroes                      Slow/fast swap, in place       O(n)      O(1)
 * 4  Remove Duplicates (Sorted Arr)   Slow writes uniques            O(n)      O(1)
 * MEDIUM
 * 5  3Sum Closest                     Fix one + two ptrs, track min  O(n^2)    O(1)
 * 6  Sort Colors (Dutch Natl Flag)    low/mid/high, one pass         O(n)      O(1)
 * 7  Two Sum II (sorted)              Opposite-end two pointers      O(n)      O(1)
 * 8  Boats to Save People             Sort + greedy lightest/heavy   O(n log n) O(1)
 * 9  Partition Labels                 Last-index + expanding window  O(n)      O(1)
 * HARD
 * 10  Trapping Rain Water              Two ptrs, resolve shorter side O(n)      O(1)
 * 11  4Sum                             Fix a,b + two ptrs, long, prune O(n^3)   O(1)
 * ----------------------------------------------------------------------------
 * Core idea: a sorted or symmetric structure lets two pointers eliminate large
 * swaths of the search space in linear passes instead of checking all pairs.
 * kSum generalizes: fix (k-2) elements, then two-pointer the innermost pair.
 * Run:  java TwoPointerPractice.java   (built-in harness: 22/22 tests pass)
 */
public class TwoPointerPractice {

    // =====================================================================
    // EASY
    // =====================================================================

    /**
     * 1. Valid Palindrome
     * In-place two pointers, skipping non-alphanumeric chars; case-insensitive.
     * Time O(n), Space O(1). (Avoids building a cleaned copy.)
     */
    public static boolean isPalindrome(String s) {
        int left = 0, right = s.length() - 1;
        while (left < right) {
            while (left < right && !Character.isLetterOrDigit(s.charAt(left))) left++;
            while (left < right && !Character.isLetterOrDigit(s.charAt(right))) right--;
            if (Character.toLowerCase(s.charAt(left)) != Character.toLowerCase(s.charAt(right)))
                return false;
            left++;
            right--;
        }
        return true;
    }

    /**
     * 2. Squares of a Sorted Array
     * Largest squares live at the two ends (big negatives or big positives).
     * Two pointers from both ends, fill result from the back. Time O(n), Space O(n).
     */
    public static int[] sortedSquares(int[] nums) {
        int n = nums.length;
        int[] result = new int[n];
        int left = 0, right = n - 1;
        for (int pos = n - 1; pos >= 0; pos--) {
            int leftSq = nums[left] * nums[left];
            int rightSq = nums[right] * nums[right];
            if (leftSq > rightSq) {
                result[pos] = leftSq;
                left++;
            } else {
                result[pos] = rightSq;
                right--;
            }
        }
        return result;
    }

    /**
     * 3. Move Zeroes
     * Slow pointer marks next non-zero slot; fast pointer scans. In place.
     * Time O(n), Space O(1).
     */
    public static void moveZeroes(int[] nums) {
        int slow = 0;
        for (int fast = 0; fast < nums.length; fast++) {
            if (nums[fast] != 0) {
                int tmp = nums[slow];
                nums[slow] = nums[fast];
                nums[fast] = tmp;
                slow++;
            }
        }
    }

    /**
     * 4. Remove Duplicates from Sorted Array
     * Slow pointer writes unique values; fast scans. Returns new length.
     * Time O(n), Space O(1).
     */
    public static int removeDuplicates(int[] nums) {
        if (nums.length == 0) return 0;
        int slow = 0;
        for (int fast = 1; fast < nums.length; fast++) {
            if (nums[fast] != nums[slow]) {
                slow++;
                nums[slow] = nums[fast];
            }
        }
        return slow + 1;
    }

    // =====================================================================
    // MEDIUM
    // =====================================================================

    /**
     * 5. 3Sum Closest
     * Sort, fix one element, two-pointer the rest; track the closest sum seen.
     * Time O(n^2), Space O(1).
     */
    public static int threeSumClosest(int[] nums, int target) {
        Arrays.sort(nums);
        int closest = nums[0] + nums[1] + nums[2];
        for (int i = 0; i < nums.length - 2; i++) {
            int left = i + 1, right = nums.length - 1;
            while (left < right) {
                int sum = nums[i] + nums[left] + nums[right];
                if (Math.abs(sum - target) < Math.abs(closest - target)) closest = sum;
                if (sum == target) return sum;       // exact match: can't beat it
                else if (sum < target) left++;
                else right--;
            }
        }
        return closest;
    }

    /**
     * 6. Sort Colors (Dutch National Flag)
     * Three pointers: low (next 0 slot), mid (scanner), high (next 2 slot).
     * One pass, in place. Time O(n), Space O(1).
     */
    public static void sortColors(int[] nums) {
        int low = 0, mid = 0, high = nums.length - 1;
        while (mid <= high) {
            if (nums[mid] == 0) {
                swap(nums, low++, mid++);
            } else if (nums[mid] == 1) {
                mid++;
            } else {
                swap(nums, mid, high--);
            } // don't advance mid: swapped-in value unchecked
        }
    }

    /**
     * 7. Two Sum II — Input Array Is Sorted
     * Opposite-end two pointers. Returns 1-indexed positions. Time O(n), Space O(1).
     */
    public static int[] twoSum(int[] numbers, int target) {
        int left = 0, right = numbers.length - 1;
        while (left < right) {
            int sum = numbers[left] + numbers[right];
            if (sum == target) return new int[]{left + 1, right + 1};
            else if (sum < target) left++;
            else right--;
        }
        return new int[]{-1, -1};
    }

    /**
     * 8. Boats to Save People
     * Sort, then greedy: pair the lightest with the heaviest if they fit;
     * the heaviest always boards. Time O(n log n), Space O(1).
     */
    public static int numRescueBoats(int[] people, int limit) {
        Arrays.sort(people);
        int left = 0, right = people.length - 1, boats = 0;
        while (left <= right) {
            if (people[left] + people[right] <= limit) left++; // lightest also boards
            right--;                                           // heaviest always boards
            boats++;
        }
        return boats;
    }

    /**
     * 9. Partition Labels
     * Record each char's last index; expand the current part to the farthest
     * last-index seen, cut when the scanner reaches it. Time O(n), Space O(1).
     */
    public static List<Integer> partitionLabels(String s) {
        int[] last = new int[26];
        for (int i = 0; i < s.length(); i++) last[s.charAt(i) - 'a'] = i;
        List<Integer> result = new ArrayList<>();
        int start = 0, end = 0;
        for (int i = 0; i < s.length(); i++) {
            end = Math.max(end, last[s.charAt(i) - 'a']);
            if (i == end) {
                result.add(end - start + 1);
                start = i + 1;
            }
        }
        return result;
    }

    // =====================================================================
    // HARD
    // =====================================================================

    /**
     * 10. Trapping Rain Water
     * Two pointers tracking leftMax/rightMax; resolve the shorter side each step.
     * Time O(n), Space O(1).
     */
    public static int trap(int[] height) {
        if (height.length == 0) return 0;
        int left = 0, right = height.length - 1;
        int leftMax = 0, rightMax = 0, water = 0;
        while (left < right) {
            if (height[left] < height[right]) {
                if (height[left] >= leftMax) leftMax = height[left];
                else water += leftMax - height[left];
                left++;
            } else {
                if (height[right] >= rightMax) rightMax = height[right];
                else water += rightMax - height[right];
                right--;
            }
        }
        return water;
    }

    /**
     * 11. 4Sum
     * Fix a, fix b, two-pointer the rest. Duplicate skips at all four positions.
     * 'long' sum prevents 32-bit overflow. Sorted-order pruning cuts real runtime.
     * Time O(n^3), Space O(1) extra.
     */
    public static List<List<Integer>> fourSum(int[] nums, int target) {
        Arrays.sort(nums);
        List<List<Integer>> result = new ArrayList<>();
        int n = nums.length;
        for (int a = 0; a < n - 3; a++) {
            if (a > 0 && nums[a] == nums[a - 1]) continue;
            if ((long) nums[a] + nums[a + 1] + nums[a + 2] + nums[a + 3] > target) break;
            if ((long) nums[a] + nums[n - 1] + nums[n - 2] + nums[n - 3] < target) continue;
            for (int b = a + 1; b < n - 2; b++) {
                if (b > a + 1 && nums[b] == nums[b - 1]) continue;
                if ((long) nums[a] + nums[b] + nums[b + 1] + nums[b + 2] > target) break;
                if ((long) nums[a] + nums[b] + nums[n - 1] + nums[n - 2] < target) continue;
                int left = b + 1, right = n - 1;
                while (left < right) {
                    long sum = (long) nums[a] + nums[b] + nums[left] + nums[right];
                    if (sum == target) {
                        result.add(Arrays.asList(nums[a], nums[b], nums[left], nums[right]));
                        left++;
                        right--;
                        while (left < right && nums[left] == nums[left - 1]) left++;
                        while (left < right && nums[right] == nums[right + 1]) right--;
                    } else if (sum < target) left++;
                    else right--;
                }
            }
        }
        return result;
    }

    // =====================================================================
    // helpers + test harness
    // =====================================================================
    private static void swap(int[] a, int i, int j) {
        int t = a[i];
        a[i] = a[j];
        a[j] = t;
    }

    static int passed = 0, total = 0;

    static void check(String name, boolean ok) {
        total++;
        if (ok) passed++;
        System.out.printf("%-40s %s%n", name, ok ? "PASS" : "FAIL");
    }

    static String norm(List<List<Integer>> r) {
        List<List<Integer>> c = new ArrayList<>();
        for (List<Integer> t : r) {
            List<Integer> x = new ArrayList<>(t);
            Collections.sort(x);
            c.add(x);
        }
        c.sort((p, q) -> {
            for (int i = 0; i < Math.min(p.size(), q.size()); i++)
                if (!p.get(i).equals(q.get(i))) return p.get(i) - q.get(i);
            return p.size() - q.size();
        });
        return c.toString().replaceAll("\\s", "");
    }

    public static void main(String[] args) {
        // 1
        check("1 ValidPalindrome a", isPalindrome("A man, a plan, a canal: Panama") == true);
        check("1 ValidPalindrome b", isPalindrome("race a car") == false);
        check("1 ValidPalindrome c", isPalindrome("0P") == false);
        // 2
        check("2 SortedSquares a", Arrays.equals(sortedSquares(new int[]{-4, -1, 0, 3, 10}), new int[]{0, 1, 9, 16, 100}));
        check("2 SortedSquares b", Arrays.equals(sortedSquares(new int[]{-7, -3, 2, 3, 11}), new int[]{4, 9, 9, 49, 121}));
        // 3
        int[] mz = {0, 1, 0, 3, 12};
        moveZeroes(mz);
        check("3 MoveZeroes", Arrays.equals(mz, new int[]{1, 3, 12, 0, 0}));
        // 4
        int[] rd = {0, 0, 1, 1, 1, 2, 2, 3, 3, 4};
        int len = removeDuplicates(rd);
        check("4 RemoveDuplicates", len == 5 && Arrays.equals(Arrays.copyOf(rd, 5), new int[]{0, 1, 2, 3, 4}));
        // 5
        check("5 ThreeSumClosest a", threeSumClosest(new int[]{-1, 2, 1, -4}, 1) == 2);
        check("5 ThreeSumClosest b", threeSumClosest(new int[]{0, 0, 0}, 1) == 0);
        // 6
        int[] sc = {2, 0, 2, 1, 1, 0};
        sortColors(sc);
        check("6 SortColors", Arrays.equals(sc, new int[]{0, 0, 1, 1, 2, 2}));
        // 7
        check("7 TwoSumII a", Arrays.equals(twoSum(new int[]{2, 7, 11, 15}, 9), new int[]{1, 2}));
        check("7 TwoSumII b", Arrays.equals(twoSum(new int[]{2, 3, 4}, 6), new int[]{1, 3}));
        // 8
        check("8 BoatsToSavePeople a", numRescueBoats(new int[]{1, 2}, 3) == 1);
        check("8 BoatsToSavePeople b", numRescueBoats(new int[]{3, 2, 2, 1}, 3) == 3);
        check("8 BoatsToSavePeople c", numRescueBoats(new int[]{3, 5, 3, 4}, 5) == 4);
        // 9
        check("9 PartitionLabels", partitionLabels("ababcbacadefegdehijhklij").equals(Arrays.asList(9, 7, 8)));
        // 10
        check("10 TrappingRainWater a", trap(new int[]{0, 1, 0, 2, 1, 0, 1, 3, 2, 1, 2, 1}) == 6);
        check("10 TrappingRainWater b", trap(new int[]{4, 2, 0, 3, 2, 5}) == 9);
        check("10 TrappingRainWater c", trap(new int[]{}) == 0);
        // 11
        check("11 FourSum a", norm(fourSum(new int[]{1, 0, -1, 0, -2, 2}, 0)).equals("[[-2,-1,1,2],[-2,0,0,2],[-1,0,0,1]]"));
        check("11 FourSum b", norm(fourSum(new int[]{2, 2, 2, 2, 2}, 8)).equals("[[2,2,2,2]]"));
        check("11 FourSum c", norm(fourSum(new int[]{1000000000, 1000000000, 1000000000, 1000000000}, -294967296)).equals("[]"));

        System.out.printf("%n==== %d/%d tests passed ====%n", passed, total);
    }
}
