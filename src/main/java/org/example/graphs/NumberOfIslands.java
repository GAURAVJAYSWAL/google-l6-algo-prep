package org.example.graphs;

/**
 * LC 200. Number of Islands.
 */
public class NumberOfIslands {

    /**
     * Key insight: every island is a maximal connected blob of '1's. Scan the grid;
     * the first time we hit an unvisited land cell we have found a new island, so we
     * increment the count and flood-fill (DFS) the entire blob, sinking it to '0' so
     * no cell is counted twice. Each cell is thus visited a constant number of times.
     *
     * Time:  O(m*n) — each cell is examined once and sunk at most once.
     * Space: O(m*n) — worst-case recursion depth when the grid is all land.
     */
    public static int numIslands(char[][] grid) {
        if (grid == null || grid.length == 0) return 0;
        int count = 0;
        for (int r = 0; r < grid.length; r++) {
            for (int c = 0; c < grid[0].length; c++) {
                if (grid[r][c] == '1') {
                    count++;
                    sink(grid, r, c); // erase the whole island so it is counted once
                }
            }
        }
        return count;
    }

    private static void sink(char[][] grid, int r, int c) {
        if (r < 0 || c < 0 || r >= grid.length || c >= grid[0].length || grid[r][c] != '1') return;
        grid[r][c] = '0'; // mark visited in place
        sink(grid, r + 1, c);
        sink(grid, r - 1, c);
        sink(grid, r, c + 1);
        sink(grid, r, c - 1);
    }

    public static void main(String[] args) {
        char[][] g1 = {
                {'1', '1', '1', '1', '0'},
                {'1', '1', '0', '1', '0'},
                {'1', '1', '0', '0', '0'},
                {'0', '0', '0', '0', '0'}
        };
        System.out.println(numIslands(g1)); // expected: 1

        char[][] g2 = {
                {'1', '1', '0', '0', '0'},
                {'1', '1', '0', '0', '0'},
                {'0', '0', '1', '0', '0'},
                {'0', '0', '0', '1', '1'}
        };
        System.out.println(numIslands(g2)); // expected: 3

        char[][] g3 = {{'0', '0', '0'}, {'0', '0', '0'}};
        System.out.println(numIslands(g3)); // expected: 0
    }
}
