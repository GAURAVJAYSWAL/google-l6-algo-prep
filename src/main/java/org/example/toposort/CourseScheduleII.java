package org.example.toposort;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.List;

/**
 * LC 210. Course Schedule II.
 */
public class CourseScheduleII {

    /**
     * Key insight: prerequisite [a, b] is a directed edge b -> a, and any valid study plan is a
     * topological order of the resulting graph. Kahn's BFS emits a course exactly when its last
     * remaining prerequisite has been taken (indegree hits 0), so appending courses in dequeue
     * order yields a correct ordering. If a cycle exists, those nodes never reach indegree 0 and
     * the order ends up shorter than numCourses, signalling an impossible schedule.
     *
     * Time:  O(V + E) — every course and prerequisite edge is touched once.
     * Space: O(V + E) — adjacency lists plus the indegree array, queue, and output.
     */
    public static int[] findOrder(int numCourses, int[][] prerequisites) {
        List<List<Integer>> adj = new ArrayList<>();
        for (int i = 0; i < numCourses; i++) adj.add(new ArrayList<>());
        int[] indegree = new int[numCourses];
        for (int[] p : prerequisites) {
            adj.get(p[1]).add(p[0]); // must take p[1] before p[0]
            indegree[p[0]]++;
        }

        Deque<Integer> queue = new ArrayDeque<>();
        for (int i = 0; i < numCourses; i++) {
            if (indegree[i] == 0) queue.offer(i); // no remaining prerequisites
        }

        int[] order = new int[numCourses];
        int idx = 0;
        while (!queue.isEmpty()) {
            int course = queue.poll();
            order[idx++] = course;                       // safe to take now -> next in the plan
            for (int next : adj.get(course)) {
                if (--indegree[next] == 0) queue.offer(next); // unlocked once last prereq is taken
            }
        }

        return idx == numCourses ? order : new int[0]; // shortfall means a cycle blocked some courses
    }

    public static void main(String[] args) {
        System.out.println(Arrays.toString(findOrder(2, new int[][]{{1, 0}})));
        // expected: [0, 1]
        System.out.println(Arrays.toString(findOrder(4, new int[][]{{1, 0}, {2, 0}, {3, 1}, {3, 2}})));
        // expected: [0, 1, 2, 3] (0 first, 3 last; any valid topo order is accepted)
        System.out.println(Arrays.toString(findOrder(2, new int[][]{{1, 0}, {0, 1}})));
        // expected: [] (cycle 0 <-> 1)
        System.out.println(Arrays.toString(findOrder(1, new int[][]{})));
        // expected: [0]
    }
}
