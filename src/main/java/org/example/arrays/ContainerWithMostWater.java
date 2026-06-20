package org.example.arrays;

public class ContainerWithMostWater {

    /**
     * Area = width * min(left wall, right wall). Start at the widest pair and move
     * inward. The shorter wall caps the area, so moving the taller one can only
     * shrink width without lifting the cap — moving the shorter wall is the only
     * move that could ever improve things, so we always advance it.
     *
     * Time:  O(n)  — pointers meet after n steps total.
     * Space: O(1).
     */
    public static int maxArea(int[] height) {
        int left = 0, right = height.length - 1;
        int best = 0;
        while (left < right) {
            int area = (right - left) * Math.min(height[left], height[right]);
            best = Math.max(best, area);
            if (height[left] < height[right]) {   // discard the shorter wall, its best is found
                left++;
            } else {
                right--;
            }
        }
        return best;
    }

    public static void main(String[] args) {
        System.out.println(maxArea(new int[]{1, 8, 6, 2, 5, 4, 8, 3, 7})); // expected: 49
        System.out.println(maxArea(new int[]{1, 1}));                     // expected: 1
        System.out.println(maxArea(new int[]{4, 3, 2, 1, 4}));           // expected: 16
    }
}
