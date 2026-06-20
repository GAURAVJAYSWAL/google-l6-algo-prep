package org.example.design;

import java.util.HashMap;
import java.util.Map;

/**
 * LC 460. LFU Cache.
 */
public class LFUCache {

    /**
     * Key insight: bucket keys by frequency. Each frequency owns a doubly-linked list
     * holding exactly the keys at that count, ordered by recency (most-recent at head,
     * least-recent at tail). Touching a key splices its node out of bucket f and into
     * bucket f+1 in O(1); the LRU tie-break within a frequency is free because the list
     * is already recency-ordered. A {@code minFreq} pointer names the bucket to evict
     * from — its tail is globally the least-frequent, least-recent key. {@code minFreq}
     * only ever resets to 1 on insert, or increments when the old min bucket empties
     * after a promotion, so it stays correct without scanning.
     *
     * Time:  O(1) for get and put — all splices and lookups are constant work.
     * Space: O(capacity) — node map plus frequency buckets, each holding live keys.
     */
    static class LFU {

        private static class Node {
            int key, value, freq = 1;
            Node prev, next;
            Node(int key, int value) { this.key = key; this.value = value; }
        }

        // A doubly-linked list with sentinel head/tail; head.next = MRU, tail.prev = LRU.
        private static class DLList {
            final Node head = new Node(0, 0);
            final Node tail = new Node(0, 0);
            int size = 0;

            DLList() {
                head.next = tail;
                tail.prev = head;
            }

            void addFirst(Node node) {                  // insert as most-recent
                node.prev = head;
                node.next = head.next;
                head.next.prev = node;
                head.next = node;
                size++;
            }

            void remove(Node node) {
                node.prev.next = node.next;
                node.next.prev = node.prev;
                size--;
            }

            Node removeLast() {                          // evict least-recent
                Node lru = tail.prev;
                remove(lru);
                return lru;
            }
        }

        private final int capacity;
        private int minFreq = 0;
        private final Map<Integer, Node> nodes = new HashMap<>();
        private final Map<Integer, DLList> freqBuckets = new HashMap<>();

        public LFU(int capacity) {
            this.capacity = capacity;
        }

        public int get(int key) {
            Node node = nodes.get(key);
            if (node == null) return -1;
            touch(node);                                 // an access bumps its frequency
            return node.value;
        }

        public void put(int key, int value) {
            if (capacity == 0) return;
            Node node = nodes.get(key);
            if (node != null) {
                node.value = value;
                touch(node);
                return;
            }
            if (nodes.size() == capacity) {              // full -> evict LFU (LRU on ties)
                DLList minList = freqBuckets.get(minFreq);
                Node evicted = minList.removeLast();
                nodes.remove(evicted.key);
            }
            Node fresh = new Node(key, value);           // new keys always start at freq 1
            nodes.put(key, fresh);
            freqBuckets.computeIfAbsent(1, k -> new DLList()).addFirst(fresh);
            minFreq = 1;                                 // a freq-1 key now exists
        }

        // Promotes node from bucket f to bucket f+1, refreshing recency and minFreq.
        private void touch(Node node) {
            int f = node.freq;
            DLList list = freqBuckets.get(f);
            list.remove(node);
            // If we just emptied the minimum bucket, the new minimum is f+1.
            if (f == minFreq && list.size == 0) minFreq++;
            node.freq = f + 1;
            freqBuckets.computeIfAbsent(node.freq, k -> new DLList()).addFirst(node);
        }
    }

    public static void main(String[] args) {
        LFU cache = new LFU(2);
        cache.put(1, 1);
        cache.put(2, 2);
        System.out.println(cache.get(1));   // expected: 1   (freq of 1 -> 2)
        cache.put(3, 3);                     // evicts key 2 (least frequent)
        System.out.println(cache.get(2));   // expected: -1
        System.out.println(cache.get(3));   // expected: 3
        cache.put(4, 4);                     // 1 and 3 both freq 2; 1 is LRU -> evict 1
        System.out.println(cache.get(1));   // expected: -1
        System.out.println(cache.get(3));   // expected: 3
        System.out.println(cache.get(4));   // expected: 4

        LFU c2 = new LFU(0);                 // zero capacity never stores anything
        c2.put(0, 0);
        System.out.println(c2.get(0));      // expected: -1

        LFU c3 = new LFU(2);
        c3.put(2, 1);
        c3.put(3, 2);
        System.out.println(c3.get(3));      // expected: 2
        System.out.println(c3.get(2));      // expected: 1
        c3.put(4, 3);                        // 2 and 3 freq 2; 3 is LRU -> evict 3
        System.out.println(c3.get(2));      // expected: 1
        System.out.println(c3.get(3));      // expected: -1
        System.out.println(c3.get(4));      // expected: 3
    }
}
