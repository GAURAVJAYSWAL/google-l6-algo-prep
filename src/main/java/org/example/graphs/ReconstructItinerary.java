package org.example.graphs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

/**
 * LC 332. Reconstruct Itinerary.
 */
public class ReconstructItinerary {

    /**
     * Key insight: every ticket must be used exactly once, so a valid itinerary is an Eulerian path
     * over the directed multigraph of flights, and Hierholzer's algorithm builds one in linear time.
     * Storing each airport's outgoing destinations in a min-heap makes the walk always take the
     * smallest available next airport, which yields the lexicographically smallest itinerary for free.
     * We greedily follow edges (consuming them) until we hit a dead end; that airport is appended to
     * the route post-order, because a stuck vertex can only be the END of the remaining tour. Earlier
     * vertices keep exploring their leftover edges, splicing those sub-tours in ahead of the dead end.
     * Reversing the post-order list at the finish gives the path from "JFK" forward.
     *
     * Time:  O(E log E) — each of the E tickets is pushed/popped once across the airports' heaps.
     * Space: O(E) — the adjacency heaps plus the recursion stack and the result list.
     */
    public static List<String> findItinerary(List<List<String>> tickets) {
        Map<String, PriorityQueue<String>> adj = new HashMap<>();
        for (List<String> t : tickets) {
            adj.computeIfAbsent(t.get(0), k -> new PriorityQueue<>()).offer(t.get(1)); // lexical order
        }

        List<String> route = new ArrayList<>();
        dfs("JFK", adj, route);
        java.util.Collections.reverse(route); // post-order was built backwards
        return route;
    }

    /** Hierholzer walk: drain every outgoing edge, then append this airport once it dead-ends. */
    private static void dfs(String airport, Map<String, PriorityQueue<String>> adj, List<String> route) {
        PriorityQueue<String> next = adj.get(airport);
        while (next != null && !next.isEmpty()) {
            dfs(next.poll(), adj, route); // consume the smallest unused ticket from here
        }
        route.add(airport); // no edges left -> this is a tail of the Eulerian path
    }

    public static void main(String[] args) {
        List<List<String>> t1 = List.of(
                List.of("MUC", "LHR"), List.of("JFK", "MUC"),
                List.of("SFO", "SJC"), List.of("LHR", "SFO"));
        System.out.println(findItinerary(t1)); // expected: [JFK, MUC, LHR, SFO, SJC]

        List<List<String>> t2 = List.of(
                List.of("JFK", "SFO"), List.of("JFK", "ATL"), List.of("SFO", "ATL"),
                List.of("ATL", "JFK"), List.of("ATL", "SFO"));
        System.out.println(findItinerary(t2)); // expected: [JFK, ATL, JFK, SFO, ATL, SFO]

        List<List<String>> t3 = List.of(List.of("JFK", "KUL"), List.of("JFK", "NRT"), List.of("NRT", "JFK"));
        System.out.println(findItinerary(t3)); // expected: [JFK, NRT, JFK, KUL]
    }
}
