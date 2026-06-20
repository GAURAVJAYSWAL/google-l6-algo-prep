package org.example.graphs;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

/**
 * LC 207. Course Schedule.
 */
public class CourseSchedule {

    /**
     * Key insight: prerequisite [a, b] is a directed edge b -> a; the schedule is
     * feasible iff this graph is a DAG (no cycle). Kahn's algorithm repeatedly removes
     * nodes with indegree 0; a course becomes takeable exactly when all its prerequisites
     * have been taken. If a true cycle exists, those nodes never reach indegree 0, so the
     * number we manage to process falls short of numCourses.
     *
     * Time:  O(V + E) — each node and edge is processed once.
     * Space: O(V + E) — adjacency lists plus the indegree array and queue.
     */
    public static boolean canFinish(int numCourses, int[][] prerequisites) {
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

        int taken = 0;
        while (!queue.isEmpty()) {
            int course = queue.poll();
            taken++;
            for (int next : adj.get(course)) {
                if (--indegree[next] == 0) queue.offer(next); // unlocked once last prereq is taken
            }
        }
        return taken == numCourses; // shortfall means a cycle blocked some courses
    }

    public static void main(String[] args) {
        System.out.println(canFinish(2, new int[][]{{1, 0}}));            // expected: true
        System.out.println(canFinish(2, new int[][]{{1, 0}, {0, 1}}));   // expected: false
        System.out.println(canFinish(4, new int[][]{{1, 0}, {2, 0}, {3, 1}, {3, 2}})); // expected: true
        System.out.println(canFinish(3, new int[][]{{0, 1}, {1, 2}, {2, 0}}));         // expected: false
    }
}
