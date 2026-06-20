package org.example.graphs;

import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.Queue;
import java.util.Set;

/**
 * LC 818. Race Car.
 */
public class RaceCar {

    /**
     * Key insight: the car's full state is (position, speed); 'A' moves to (pos + speed, speed * 2)
     * and 'R' resets speed to +/-1 facing the other way. Each instruction is one unweighted edge, so
     * the fewest instructions to reach target is a shortest path found by BFS over states. We bound
     * the search by only exploring positions within [-target, 2*target]: any optimal solution stays
     * in this band (overshooting past 2*target is never on a shortest path), keeping the state space
     * finite. A 'visited' set on (pos, speed) prevents revisiting.
     *
     * Alternative: bottom-up DP on dp[target] — for each target either accelerate just past it then
     * reverse, or stop short, reverse, and recurse — giving O(target log target) without BFS.
     *
     * Time:  O(target log target) — reachable states are bounded by the band and the doubling speeds.
     * Space: O(target log target) — the visited set and BFS queue over those states.
     */
    public static int racecar(int target) {
        Queue<int[]> queue = new ArrayDeque<>();
        Set<String> visited = new HashSet<>();
        queue.offer(new int[]{0, 1}); // start at position 0, speed +1
        visited.add(0 + "," + 1);
        int steps = 0;

        while (!queue.isEmpty()) {
            for (int sz = queue.size(); sz > 0; sz--) {
                int[] state = queue.poll();
                int pos = state[0], speed = state[1];
                if (pos == target) return steps;

                // Option A: accelerate — position advances, speed doubles.
                int aPos = pos + speed, aSpeed = speed * 2;
                if (Math.abs(aPos) <= 2 * target) { // stay within the band that can hold an optimal path
                    String key = aPos + "," + aSpeed;
                    if (visited.add(key)) queue.offer(new int[]{aPos, aSpeed});
                }

                // Option R: reverse — speed resets to +/-1 facing the opposite way.
                int rSpeed = speed > 0 ? -1 : 1;
                String key = pos + "," + rSpeed;
                if (visited.add(key)) queue.offer(new int[]{pos, rSpeed});
            }
            steps++;
        }
        return -1; // unreachable; defensive only since every target is reachable
    }

    public static void main(String[] args) {
        System.out.println(racecar(3)); // expected: 2   (AA)
        System.out.println(racecar(6)); // expected: 5   (AAARA)
        System.out.println(racecar(1)); // expected: 1   (A)
    }
}
