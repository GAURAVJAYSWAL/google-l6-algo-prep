package org.example.advancedgraphs;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

/**
 * LC 913. Cat and Mouse.
 */
public class CatAndMouse {

    private static final int DRAW = 0, MOUSE_WIN = 1, CAT_WIN = 2;
    private static final int MOUSE_TURN = 0, CAT_TURN = 1;

    /**
     * Key insight: solve the game by retrograde analysis over states (mouse, cat, turn).
     * Working forward stalls because of cyclic draws, so instead we seed the known terminal
     * states (mouse at the hole -> mouse wins; mouse meets cat -> cat wins) and propagate the
     * verdict backward. A state is decided the moment we can prove it: if the player to move
     * has ANY move into an already-known winning-for-them state, that player wins immediately;
     * if EVERY one of their moves leads to a state winning for the opponent, they lose. The
     * second case is detected by a degree counter — we decrement a predecessor's remaining
     * unknown moves each time one resolves against it, and a count of zero means forced loss.
     * Any state never reached by this propagation is a draw (its outcome is a cycle). The hole
     * is special: the cat may never step onto node 0, so it is excluded from the cat's moves.
     *
     * Time:  O(V^3) — O(V^2) turn-states, each with O(V) predecessor moves examined once.
     * Space: O(V^2) — the result and degree tables over (mouse, cat, turn).
     */
    public static int catMouseGame(int[][] graph) {
        int n = graph.length;
        int[][][] result = new int[n][n][2];        // result[mouse][cat][turn], 0 = unknown/draw
        int[][][] degree = new int[n][n][2];         // remaining unresolved moves for the player to move

        // Precompute branching factor of each state; the cat can never move onto the hole (node 0).
        for (int m = 0; m < n; m++) {
            for (int c = 0; c < n; c++) {
                degree[m][c][MOUSE_TURN] = graph[m].length;
                degree[m][c][CAT_TURN] = graph[c].length;
                for (int x : graph[c]) {
                    if (x == 0) { degree[m][c][CAT_TURN]--; break; } // strip the illegal cat-into-hole move
                }
            }
        }

        Deque<int[]> queue = new ArrayDeque<>(); // entries: {mouse, cat, turn}
        // Seed terminal states.
        for (int t = 0; t < 2; t++) {
            for (int c = 1; c < n; c++) {            // cat never sits on the hole, so c starts at 1
                // Mouse reaches the hole -> mouse wins, whoever's turn it is.
                result[0][c][t] = MOUSE_WIN;
                queue.offer(new int[]{0, c, t});
            }
            for (int x = 1; x < n; x++) {            // mouse == cat (x == x), and neither is the hole here
                result[x][x][t] = CAT_WIN;
                queue.offer(new int[]{x, x, t});
            }
        }

        while (!queue.isEmpty()) {
            int[] state = queue.poll();
            int mouse = state[0], cat = state[1], turn = state[2];
            int res = result[mouse][cat][turn];

            // Visit every predecessor: a state that can move INTO (mouse, cat, turn).
            for (int[] prev : predecessors(mouse, cat, turn, graph)) {
                int pm = prev[0], pc = prev[1], pt = prev[2];
                if (result[pm][pc][pt] != DRAW) continue; // already decided

                int moverWin = (pt == MOUSE_TURN) ? MOUSE_WIN : CAT_WIN; // what a win looks like for the predecessor's mover
                if (res == moverWin) {
                    // Predecessor's mover can step into a state they win -> they take it.
                    result[pm][pc][pt] = moverWin;
                    queue.offer(new int[]{pm, pc, pt});
                } else if (--degree[pm][pc][pt] == 0) {
                    // All of the predecessor's moves lead to the opponent winning -> forced loss.
                    result[pm][pc][pt] = (pt == MOUSE_TURN) ? CAT_WIN : MOUSE_WIN;
                    queue.offer(new int[]{pm, pc, pt});
                }
            }
        }

        // Game starts: mouse at 1, cat at 2, mouse to move.
        return result[1][2][MOUSE_TURN];
    }

    /**
     * States that can move into (mouse, cat, turn). If it is the mouse's turn now, the cat
     * just moved, so we vary the cat's previous position (excluding the hole); symmetrically
     * for the cat's turn we vary the mouse's previous position.
     */
    private static List<int[]> predecessors(int mouse, int cat, int turn, int[][] graph) {
        List<int[]> preds = new ArrayList<>();
        if (turn == MOUSE_TURN) {
            // Previous mover was the cat (CAT_TURN), arriving at `cat` from an adjacent node.
            for (int prevCat : graph[cat]) {
                if (prevCat == 0) continue;          // the cat could never have stood on the hole
                preds.add(new int[]{mouse, prevCat, CAT_TURN});
            }
        } else {
            // Previous mover was the mouse (MOUSE_TURN), arriving at `mouse` from an adjacent node.
            for (int prevMouse : graph[mouse]) {
                preds.add(new int[]{prevMouse, cat, MOUSE_TURN});
            }
        }
        return preds;
    }

    public static void main(String[] args) {
        System.out.println(catMouseGame(new int[][]{
                {2, 5}, {3}, {0, 4, 5}, {1, 4, 5}, {2, 3}, {0, 2, 3}})); // expected: 0 (draw)
        System.out.println(catMouseGame(new int[][]{
                {1, 3}, {0}, {3}, {0, 2}})); // expected: 1 (mouse wins)
        System.out.println(catMouseGame(new int[][]{
                {2, 3}, {2}, {0, 1}, {0}})); // expected: 2 (mouse at 1 is forced into the cat at 2)
        System.out.println(catMouseGame(new int[][]{
                {1, 4}, {0, 2, 5}, {1, 3}, {2, 4}, {0, 3, 5}, {1, 4}})); // expected: 1 (mouse forces a path to the hole)
    }
}
