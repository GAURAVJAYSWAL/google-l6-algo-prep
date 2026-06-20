package org.example.graphs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

/**
 * LC 721. Accounts Merge.
 */
public class AccountsMerge {

    /**
     * Key insight: two accounts belong to the same person iff they share at least one email, and
     * "same person" is the transitive closure of that relation — precisely connected components.
     * We union accounts by their indices: map each email to the first account index that owns it,
     * and whenever an email recurs, union the current account with that owner. After unioning, we
     * regroup all emails under their component's representative, sort them (a TreeSet keeps them
     * ordered and deduped), and prefix the owner's name to produce each merged account.
     *
     * Time:  O(N log N) — N total emails; the per-component sort dominates over near-constant unions.
     * Space: O(N) — the union-find arrays plus the email-to-owner and component maps.
     */
    public static List<List<String>> accountsMerge(List<List<String>> accounts) {
        int n = accounts.size();
        int[] parent = new int[n];
        int[] rank = new int[n];
        for (int i = 0; i < n; i++) parent[i] = i;

        // First account index seen owning a given email; a repeat triggers a union.
        Map<String, Integer> emailToAccount = new HashMap<>();
        for (int i = 0; i < n; i++) {
            List<String> account = accounts.get(i);
            for (int j = 1; j < account.size(); j++) { // index 0 is the name, skip it
                String email = account.get(j);
                Integer owner = emailToAccount.get(email);
                if (owner == null) emailToAccount.put(email, i);
                else union(parent, rank, i, owner); // shared email links the two accounts
            }
        }

        // Gather each component's emails under its representative root, sorted and deduped.
        Map<Integer, TreeSet<String>> components = new HashMap<>();
        for (Map.Entry<String, Integer> e : emailToAccount.entrySet()) {
            int root = find(parent, e.getValue());
            components.computeIfAbsent(root, k -> new TreeSet<>()).add(e.getKey());
        }

        List<List<String>> result = new ArrayList<>();
        for (Map.Entry<Integer, TreeSet<String>> e : components.entrySet()) {
            List<String> merged = new ArrayList<>();
            merged.add(accounts.get(e.getKey()).get(0)); // name from the representative account
            merged.addAll(e.getValue());                 // already sorted ascending
            result.add(merged);
        }
        return result;
    }

    private static int find(int[] parent, int x) {
        while (parent[x] != x) {
            parent[x] = parent[parent[x]]; // path compression (halving)
            x = parent[x];
        }
        return x;
    }

    private static void union(int[] parent, int[] rank, int a, int b) {
        int ra = find(parent, a), rb = find(parent, b);
        if (ra == rb) return;
        if (rank[ra] < rank[rb]) { int t = ra; ra = rb; rb = t; }
        parent[rb] = ra;
        if (rank[ra] == rank[rb]) rank[ra]++;
    }

    public static void main(String[] args) {
        List<List<String>> accounts = new ArrayList<>();
        accounts.add(List.of("John", "johnsmith@mail.com", "john_newyork@mail.com"));
        accounts.add(List.of("John", "johnsmith@mail.com", "john00@mail.com"));
        accounts.add(List.of("Mary", "mary@mail.com"));
        accounts.add(List.of("John", "johnnybravo@mail.com"));

        List<List<String>> merged = accountsMerge(accounts);
        merged.sort((x, y) -> x.toString().compareTo(y.toString())); // stable order for printing
        System.out.println(merged);
        // expected: [[John, john00@mail.com, john_newyork@mail.com, johnsmith@mail.com],
        //            [John, johnnybravo@mail.com], [Mary, mary@mail.com]]

        List<List<String>> single = new ArrayList<>();
        single.add(List.of("Alex", "alex@mail.com"));
        System.out.println(accountsMerge(single)); // expected: [[Alex, alex@mail.com]]
    }
}
