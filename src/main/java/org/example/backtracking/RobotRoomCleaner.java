package org.example.backtracking;

import java.util.HashSet;
import java.util.Set;

public class RobotRoomCleaner {

    /**
     * The robot is blind: it has no coordinates, only move/turn/clean. We impose our
     * own frame by tracking the cell as (r, c) relative to the start and the current
     * facing as one of 4 directions. DFS each cell, clean it, and probe all four
     * neighbours in a fixed turn order. The key to backtracking without absolute
     * positioning is the "go back" maneuver: turn 180, move one cell, turn 180 again
     * — this restores BOTH the position and the original facing so the loop can try
     * the next direction from a known state. A visited set stops revisits.
     * Time:  O(N) directions tried over N free cells -> O(4N), each move O(1).
     * Space: O(N) — visited set plus recursion depth bounded by free cells.
     */
    public static void cleanRoom(Robot robot) {
        // Facing order: up, right, down, left. Turning right advances this index by 1 (mod 4).
        int[] dr = {-1, 0, 1, 0};
        int[] dc = {0, 1, 0, -1};
        backtrack(robot, 0, 0, 0, dr, dc, new HashSet<>());
    }

    private static void backtrack(Robot robot, int r, int c, int dir,
                                  int[] dr, int[] dc, Set<Long> visited) {
        visited.add(encode(r, c));
        robot.clean();

        for (int turn = 0; turn < 4; turn++) {
            int nd = (dir + turn) % 4;           // current facing after `turn` right-turns
            int nr = r + dr[nd], nc = c + dc[nd];
            if (!visited.contains(encode(nr, nc)) && robot.move()) {
                backtrack(robot, nr, nc, nd, dr, dc, visited);
                goBack(robot);                   // restore position AND facing for the next probe
            }
            robot.turnRight();                   // rotate to the next direction to try
        }
    }

    // Reverse one step: about-face, advance, about-face — ends facing the original direction.
    private static void goBack(Robot robot) {
        robot.turnRight();
        robot.turnRight();
        robot.move();
        robot.turnRight();
        robot.turnRight();
    }

    // Pack (r, c) into one long; offset keeps negative relative coords non-negative and distinct.
    private static long encode(int r, int c) {
        return (long) (r + 100_000) * 1_000_000L + (c + 100_000);
    }

    /** LeetCode's opaque robot API: no coordinates are ever exposed. */
    public interface Robot {
        /** Move forward one cell; returns false (without moving) if blocked by a wall. */
        boolean move();
        /** Rotate 90 degrees to the right in place. */
        void turnRight();
        /** Rotate 90 degrees to the left in place. */
        void turnLeft();
        /** Clean the current cell. */
        void clean();
    }

    /**
     * Grid-backed mock so main can actually run the algorithm. grid[i][j]: 1 = free,
     * 0 = wall. The robot starts at (row, col) facing up and counts cleaned cells.
     */
    static class MockRobot implements Robot {
        private final int[][] grid;
        private int r, c, dir;                   // absolute position + facing, hidden from solver
        private final int[] dr = {-1, 0, 1, 0};
        private final int[] dc = {0, 1, 0, -1};
        private final boolean[][] cleaned;
        int cleanedCount = 0;

        MockRobot(int[][] grid, int row, int col) {
            this.grid = grid;
            this.r = row;
            this.c = col;
            this.dir = 0;                        // facing up
            this.cleaned = new boolean[grid.length][grid[0].length];
        }

        @Override
        public boolean move() {
            int nr = r + dr[dir], nc = c + dc[dir];
            if (nr < 0 || nr >= grid.length || nc < 0 || nc >= grid[0].length || grid[nr][nc] == 0) {
                return false;                    // wall or out of bounds -> blocked
            }
            r = nr;
            c = nc;
            return true;
        }

        @Override
        public void turnRight() {
            dir = (dir + 1) % 4;
        }

        @Override
        public void turnLeft() {
            dir = (dir + 3) % 4;
        }

        @Override
        public void clean() {
            if (!cleaned[r][c]) {
                cleaned[r][c] = true;
                cleanedCount++;
            }
        }
    }

    private static int countFree(int[][] grid) {
        int free = 0;
        for (int[] row : grid) for (int cell : row) free += cell;
        return free;
    }

    public static void main(String[] args) {
        int[][] grid1 = {
                {1, 1, 1, 1, 1, 0, 1, 1},
                {1, 1, 1, 1, 1, 0, 1, 1},
                {1, 0, 1, 1, 1, 1, 1, 1},
                {0, 0, 0, 1, 0, 0, 0, 0},
                {1, 1, 1, 1, 1, 1, 1, 1}
        };
        MockRobot r1 = new MockRobot(grid1, 1, 3);
        cleanRoom(r1);
        System.out.println(r1.cleanedCount + " / " + countFree(grid1));
        // expected: 30 / 30  (every free cell reachable from start is cleaned)

        int[][] grid2 = {{1}};
        MockRobot r2 = new MockRobot(grid2, 0, 0);
        cleanRoom(r2);
        System.out.println(r2.cleanedCount + " / " + countFree(grid2));
        // expected: 1 / 1

        int[][] grid3 = {
                {1, 1, 0},
                {0, 1, 0},
                {0, 1, 1}
        };
        MockRobot r3 = new MockRobot(grid3, 0, 0);
        cleanRoom(r3);
        System.out.println(r3.cleanedCount + " / " + countFree(grid3));
        // expected: 5 / 5
    }
}
