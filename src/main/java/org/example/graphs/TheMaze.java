package org.example.graphs;

import java.util.ArrayDeque;
import java.util.Queue;

/**
 * LC 490. The Maze.
 */
public class TheMaze {

    /**
     * Key insight: the ball does not stop on every cell — once pushed it rolls until a wall (or
     * the border) stops it, so the graph's nodes are only the stopping positions and an edge is a
     * full roll in one of the four directions. We therefore generate a neighbor by rolling all the
     * way in a direction, then BFS over those stop points. Marking stop points visited prevents
     * re-rolling, and reaching the destination as a stop point answers the query.
     *
     * Time:  O(m * n * (m + n)) — up to m*n stop states, each rolling O(m + n) to the next wall.
     * Space: O(m * n) — the visited grid and BFS queue.
     */
    public static boolean hasPath(int[][] maze, int[] start, int[] destination) {
        int m = maze.length, n = maze[0].length;
        boolean[][] visited = new boolean[m][n];
        int[][] dirs = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};

        Queue<int[]> queue = new ArrayDeque<>();
        queue.offer(start);
        visited[start[0]][start[1]] = true;

        while (!queue.isEmpty()) {
            int[] cur = queue.poll();
            if (cur[0] == destination[0] && cur[1] == destination[1]) return true;

            for (int[] d : dirs) {
                int r = cur[0], c = cur[1];
                // Roll until the next cell would be a wall or out of bounds.
                while (r + d[0] >= 0 && r + d[0] < m && c + d[1] >= 0 && c + d[1] < n
                        && maze[r + d[0]][c + d[1]] == 0) {
                    r += d[0];
                    c += d[1];
                }
                if (!visited[r][c]) { // only enqueue genuinely new stop points
                    visited[r][c] = true;
                    queue.offer(new int[]{r, c});
                }
            }
        }
        return false; // exhausted all reachable stops without landing on the destination
    }

    public static void main(String[] args) {
        int[][] maze = {
                {0, 0, 1, 0, 0},
                {0, 0, 0, 0, 0},
                {0, 0, 0, 1, 0},
                {1, 1, 0, 1, 1},
                {0, 0, 0, 0, 0}
        };
        System.out.println(hasPath(maze, new int[]{0, 4}, new int[]{4, 4})); // expected: true
        System.out.println(hasPath(maze, new int[]{0, 4}, new int[]{3, 2})); // expected: false (cannot stop there)

        int[][] line = {{0, 0, 0, 0, 0}};
        System.out.println(hasPath(line, new int[]{0, 0}, new int[]{0, 4})); // expected: true
    }
}
