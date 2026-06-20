package org.example.design;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * LC 380. Insert Delete GetRandom O(1).
 */
public class InsertDeleteGetRandomO1 {

    /**
     * Key insight: getRandom() needs a contiguous, gap-free array to index into, but
     * deleting a middle element of an array is O(n) due to shifting. The trick is that
     * order does not matter, so to remove a value we look up its slot via a HashMap,
     * overwrite that slot with the LAST element (patching the moved element's index in
     * the map), and pop the tail — a true O(1) deletion that keeps the array dense.
     * getRandom() then just samples a uniform index over the live array.
     *
     * Time:  insert / remove / getRandom all O(1) average — array tail ops plus map lookups.
     * Space: O(n) — the value list plus a parallel value->index map.
     */
    static class RandomizedSet {
        private final List<Integer> values = new ArrayList<>();
        private final Map<Integer, Integer> indexOf = new HashMap<>();   // value -> its slot in `values`
        private final Random rng;

        public RandomizedSet() {
            this.rng = new Random();
        }

        // Overload lets the demo inject a seeded Random for deterministic getRandom output.
        public RandomizedSet(Random rng) {
            this.rng = rng;
        }

        public boolean insert(int val) {
            if (indexOf.containsKey(val)) return false;
            indexOf.put(val, values.size());
            values.add(val);
            return true;
        }

        public boolean remove(int val) {
            Integer idx = indexOf.get(val);
            if (idx == null) return false;
            int lastIdx = values.size() - 1;
            int lastVal = values.get(lastIdx);
            // Move the tail element into the hole, then fix its recorded index.
            values.set(idx, lastVal);
            indexOf.put(lastVal, idx);
            values.remove(lastIdx);           // O(1): removing the final slot shifts nothing
            indexOf.remove(val);
            return true;
        }

        public int getRandom() {
            return values.get(rng.nextInt(values.size()));
        }
    }

    public static void main(String[] args) {
        RandomizedSet s = new RandomizedSet();
        System.out.println(s.insert(1));      // expected: true  (1 newly added)
        System.out.println(s.remove(2));      // expected: false (2 not present)
        System.out.println(s.insert(2));      // expected: true
        System.out.println(s.insert(2));      // expected: false (already present)
        System.out.println(s.remove(1));      // expected: true
        System.out.println(s.insert(2));      // expected: false (still present)

        // Fixed seed makes getRandom reproducible for a demo of uniform sampling.
        RandomizedSet det = new RandomizedSet(new Random(42));
        det.insert(10);
        det.insert(20);
        det.insert(30);
        System.out.println(det.getRandom());  // expected: 30 (deterministic under seed 42)
        System.out.println(det.getRandom());  // expected: 10 (deterministic under seed 42)
    }
}
