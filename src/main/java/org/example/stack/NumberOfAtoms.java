package org.example.stack;

import java.util.Map;
import java.util.TreeMap;
import java.util.ArrayDeque;
import java.util.Deque;

public class NumberOfAtoms {

    /**
     * Parentheses define nested scopes whose multipliers stack, so we keep a stack of
     * count-maps — one per open group. An element name adds its (optionally suffixed)
     * count to the current top map; "(" pushes a fresh scope; ")" pops the scope,
     * reads the trailing multiplier, scales every count in the popped map, and merges
     * it into the now-current scope. A TreeMap keeps names sorted for free, so the
     * final formula prints in lexicographic order with counts > 1 appended.
     *
     * Time:  O(n log k)  — n = formula length, k = distinct elements (TreeMap merges).
     * Space: O(n)        — the scope stack plus accumulated counts.
     */
    public static String countOfAtoms(String formula) {
        Deque<Map<String, Integer>> stack = new ArrayDeque<>();
        stack.push(new TreeMap<>());
        int i = 0, n = formula.length();

        while (i < n) {
            char c = formula.charAt(i);
            if (c == '(') {
                stack.push(new TreeMap<>());   // open a new multiplier scope
                i++;
            } else if (c == ')') {
                i++;
                int mult = readNumber(formula, i);  // multiplier that applies to the whole group
                i = advancePastNumber(formula, i);
                Map<String, Integer> group = stack.pop();
                Map<String, Integer> parent = stack.peek();
                for (Map.Entry<String, Integer> e : group.entrySet()) {
                    parent.merge(e.getKey(), e.getValue() * mult, Integer::sum); // scale then fold up
                }
            } else {
                // Element name: an uppercase letter followed by zero or more lowercase.
                int start = i++;
                while (i < n && Character.isLowerCase(formula.charAt(i))) i++;
                String name = formula.substring(start, i);
                int count = readNumber(formula, i);
                i = advancePastNumber(formula, i);
                stack.peek().merge(name, count, Integer::sum);
            }
        }

        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, Integer> e : stack.peek().entrySet()) {
            sb.append(e.getKey());
            if (e.getValue() > 1) sb.append(e.getValue());   // omit explicit count of 1
        }
        return sb.toString();
    }

    // Reads the integer starting at i; an absent number defaults to 1.
    private static int readNumber(String s, int i) {
        if (i >= s.length() || !Character.isDigit(s.charAt(i))) return 1;
        int num = 0;
        while (i < s.length() && Character.isDigit(s.charAt(i))) {
            num = num * 10 + (s.charAt(i) - '0');
            i++;
        }
        return num;
    }

    private static int advancePastNumber(String s, int i) {
        while (i < s.length() && Character.isDigit(s.charAt(i))) i++;
        return i;
    }

    public static void main(String[] args) {
        System.out.println(countOfAtoms("H2O"));                 // expected: H2O
        System.out.println(countOfAtoms("Mg(OH)2"));             // expected: H2MgO2
        System.out.println(countOfAtoms("K4(ON(SO3)2)2"));       // expected: K4N2O14S4
        System.out.println(countOfAtoms("(NH4)2"));              // expected: H8N2
    }
}
