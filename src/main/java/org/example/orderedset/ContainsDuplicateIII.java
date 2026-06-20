package org.example.orderedset;

import java.util.HashMap;
import java.util.Map;

/**
 * LC 220. Contains Duplicate III.
 */
public class ContainsDuplicateIII {

    /**
     * Key insight: bucket values into ranges of width (valueDiff+1) so any two numbers in the SAME
     * bucket are guaranteed within valueDiff, and the only other candidates within valueDiff sit in
     * the two ADJACENT buckets (checked explicitly). Slide a window of width indexDiff over the
     * indices, keeping one representative per bucket; landing in an occupied bucket, or finding an
     * adjacent-bucket neighbor within valueDiff, is an answer. Evicting the bucket of the element
     * that fell out of the window keeps at most indexDiff buckets live.
     * (Alternative: a TreeSet over the window using floor/ceiling to find the nearest value — also
     * correct but O(n log k) versus this O(n) bucketing.)
     *
     * Time:  O(n) — each index does O(1) bucket lookups and one eviction.
     * Space: O(min(n, indexDiff)) — buckets retained for the current window.
     */
    public static boolean containsNearbyAlmostDuplicate(int[] nums, int indexDiff, int valueDiff) {
        if (indexDiff <= 0 || valueDiff < 0) return false;
        long width = (long) valueDiff + 1;                 // bucket holds a valueDiff+1 wide range
        Map<Long, Long> bucket = new HashMap<>();          // bucket id -> the value living in it

        for (int i = 0; i < nums.length; i++) {
            long id = bucketId(nums[i], width);
            if (bucket.containsKey(id)) {
                return true;                               // same bucket => gap <= valueDiff
            }
            // Adjacent buckets might still hold a value within valueDiff; verify the actual distance.
            if (bucket.containsKey(id - 1) && nums[i] - bucket.get(id - 1) <= valueDiff) {
                return true;
            }
            if (bucket.containsKey(id + 1) && bucket.get(id + 1) - nums[i] <= valueDiff) {
                return true;
            }
            bucket.put(id, (long) nums[i]);
            if (i >= indexDiff) {
                bucket.remove(bucketId(nums[i - indexDiff], width)); // drop the element leaving the window
            }
        }
        return false;
    }

    // Floor division so negatives bucket correctly (e.g. -1 and 0 with width 1 stay distinct, -1..-w group).
    private static long bucketId(long value, long width) {
        return Math.floorDiv(value, width);
    }

    public static void main(String[] args) {
        System.out.println(containsNearbyAlmostDuplicate(new int[]{1, 2, 3, 1}, 3, 0));   // expected: true
        System.out.println(containsNearbyAlmostDuplicate(new int[]{1, 5, 9, 1, 5, 9}, 2, 3)); // expected: false
        System.out.println(containsNearbyAlmostDuplicate(new int[]{-3, 3}, 2, 6));        // expected: true  (|-3-3|=6 <= 6, indices differ by 1)
        System.out.println(containsNearbyAlmostDuplicate(new int[]{1, 2, 1, 1}, 1, 0));   // expected: true
    }
}
