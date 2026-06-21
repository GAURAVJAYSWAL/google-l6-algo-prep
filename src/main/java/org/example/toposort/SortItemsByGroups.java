package org.example.toposort;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.List;

/**
 * LC 1203. Sort Items by Group.
 */
public class SortItemsByGroups {

    /**
     * Key insight: items must be grouped contiguously, so the problem decomposes into two nested
     * topological sorts. First give every ungrouped item its own private group, making "every item
     * belongs to exactly one group" hold. Each dependency i -> j (j must precede i) then induces an
     * edge between j's group and i's group when they differ (ordering of groups), and an edge inside
     * the shared group otherwise (ordering of items). Topologically sort the groups, topologically
     * sort the items within each group, and emit items group-by-group in group order. Any cycle in
     * either graph makes a valid arrangement impossible, so we return an empty array.
     *
     * Time:  O(n + m + E) — n items, m original groups, E dependency edges; each is processed once.
     * Space: O(n + m + E) — adjacency lists, indegree arrays, and the per-group item buckets.
     */
    public static int[] sortItems(int n, int m, int[] group, List<List<Integer>> beforeItems) {
        // Assign each ungrouped item (-1) a fresh, unique group id so grouping is total.
        int groupCount = m;
        for (int i = 0; i < n; i++) {
            if (group[i] == -1) group[i] = groupCount++;
        }

        List<List<Integer>> groupAdj = new ArrayList<>();
        List<List<Integer>> itemAdj = new ArrayList<>();
        for (int i = 0; i < groupCount; i++) groupAdj.add(new ArrayList<>());
        for (int i = 0; i < n; i++) itemAdj.add(new ArrayList<>());
        int[] groupIndegree = new int[groupCount];
        int[] itemIndegree = new int[n];

        // beforeItems.get(i) lists items that must come BEFORE i, i.e. edges before -> i.
        for (int i = 0; i < n; i++) {
            for (int before : beforeItems.get(i)) {
                if (group[before] != group[i]) {                 // cross-group dependency
                    groupAdj.get(group[before]).add(group[i]);
                    groupIndegree[group[i]]++;
                } else {                                         // same-group dependency
                    itemAdj.get(before).add(i);
                    itemIndegree[i]++;
                }
            }
        }

        List<Integer> groupOrder = topoSort(groupAdj, groupIndegree, groupCount);
        List<Integer> itemOrder = topoSort(itemAdj, itemIndegree, n);
        if (groupOrder == null || itemOrder == null) return new int[0]; // cycle in either level

        // Bucket the globally item-sorted list into each group, preserving intra-group order.
        List<List<Integer>> itemsByGroup = new ArrayList<>();
        for (int i = 0; i < groupCount; i++) itemsByGroup.add(new ArrayList<>());
        for (int item : itemOrder) itemsByGroup.get(group[item]).add(item);

        int[] result = new int[n];
        int idx = 0;
        for (int g : groupOrder) {                               // groups in topo order...
            for (int item : itemsByGroup.get(g)) {               // ...then items within each group
                result[idx++] = item;
            }
        }
        return result;
    }

    /** Kahn's BFS over `count` nodes; returns a valid order, or null if a cycle is detected. */
    private static List<Integer> topoSort(List<List<Integer>> adj, int[] indegree, int count) {
        Deque<Integer> queue = new ArrayDeque<>();
        for (int i = 0; i < count; i++) {
            if (indegree[i] == 0) queue.offer(i);
        }

        List<Integer> order = new ArrayList<>(count);
        while (!queue.isEmpty()) {
            int node = queue.poll();
            order.add(node);
            for (int next : adj.get(node)) {
                if (--indegree[next] == 0) queue.offer(next);
            }
        }

        return order.size() == count ? order : null; // shortfall means a cycle
    }

    /** Convenience for tests: wraps each int[] of "before" items as a List<Integer>. */
    private static List<List<Integer>> toBeforeItems(int[][] rows) {
        List<List<Integer>> list = new ArrayList<>();
        for (int[] row : rows) {
            List<Integer> r = new ArrayList<>();
            for (int v : row) r.add(v);
            list.add(r);
        }
        return list;
    }

    public static void main(String[] args) {
        int[][] before1 = {{}, {6}, {5}, {6}, {3, 6}, {}, {}, {}};
        System.out.println(Arrays.toString(sortItems(
                8, 2, new int[]{-1, -1, 1, 0, 0, 1, 0, -1}, toBeforeItems(before1))));
        // expected: [6, 3, 4, 5, 2, 0, 7, 1] (a valid two-level ordering; items stay grouped contiguously)

        int[][] before2 = {{}, {6}, {5}, {6}, {3}, {}, {4}, {}};
        System.out.println(Arrays.toString(sortItems(
                8, 2, new int[]{-1, -1, 1, 0, 0, 1, 0, -1}, toBeforeItems(before2))));
        // expected: [] (4 -> 6 -> ... -> 4 forms a same-group cycle)

        int[][] before3 = {{}, {}, {}};
        System.out.println(Arrays.toString(sortItems(
                3, 0, new int[]{-1, -1, -1}, toBeforeItems(before3))));
        // expected: [0, 1, 2] (all ungrouped, no deps -> any order; here index order)

        int[][] before4 = {{1}, {2}, {}};
        System.out.println(Arrays.toString(sortItems(
                3, 1, new int[]{0, 0, 0}, toBeforeItems(before4))));
        // expected: [2, 1, 0] (single group, chain 2 -> 1 -> 0)
    }
}
