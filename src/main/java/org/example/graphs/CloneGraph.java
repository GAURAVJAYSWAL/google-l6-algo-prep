package org.example.graphs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * LC 133. Clone Graph.
 */
public class CloneGraph {

    static class Node {
        int val;
        List<Node> neighbors;
        Node(int val) { this.val = val; this.neighbors = new ArrayList<>(); }
        Node(int val, List<Node> neighbors) { this.val = val; this.neighbors = neighbors; }
    }

    /**
     * Key insight: a deep copy of a cyclic graph needs a single source of truth mapping
     * each original node to its clone. DFS creates a clone the first time a node is seen
     * and records it in the map; on re-encountering a node (including via a back edge in
     * a cycle) we return the existing clone instead of recursing, which both terminates
     * and preserves shared structure.
     *
     * Time:  O(V + E) — every node is cloned once and every edge is wired once.
     * Space: O(V) — the map plus recursion stack hold up to V nodes.
     */
    public static Node cloneGraph(Node node) {
        return dfs(node, new HashMap<>());
    }

    private static Node dfs(Node node, Map<Node, Node> cloned) {
        if (node == null) return null;
        if (cloned.containsKey(node)) return cloned.get(node); // already copied -> reuse (breaks cycles)

        Node copy = new Node(node.val);
        cloned.put(node, copy);                 // register BEFORE recursing so cycles resolve
        for (Node neighbor : node.neighbors) {
            copy.neighbors.add(dfs(neighbor, cloned));
        }
        return copy;
    }

    public static void main(String[] args) {
        // Build the classic 4-node square: 1-2, 2-3, 3-4, 4-1.
        Node n1 = new Node(1), n2 = new Node(2), n3 = new Node(3), n4 = new Node(4);
        n1.neighbors.add(n2); n1.neighbors.add(n4);
        n2.neighbors.add(n1); n2.neighbors.add(n3);
        n3.neighbors.add(n2); n3.neighbors.add(n4);
        n4.neighbors.add(n1); n4.neighbors.add(n3);

        Node clone = cloneGraph(n1);
        System.out.println(clone != n1);                       // expected: true  (distinct object)
        System.out.println(clone.val);                         // expected: 1
        System.out.println(adjacency(clone));                  // expected: [2, 4]
        System.out.println(clone.neighbors.get(0) != n2);      // expected: true  (neighbors deep-copied)

        // Single node, no edges.
        Node solo = cloneGraph(new Node(1));
        System.out.println(solo.val + " " + solo.neighbors.size()); // expected: 1 0

        System.out.println(cloneGraph(null));                  // expected: null
    }

    private static List<Integer> adjacency(Node node) {
        List<Integer> vals = new ArrayList<>();
        for (Node nb : node.neighbors) vals.add(nb.val);
        return vals;
    }
}
