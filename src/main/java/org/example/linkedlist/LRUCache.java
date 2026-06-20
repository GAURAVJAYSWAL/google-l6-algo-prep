package org.example.linkedlist;

import java.util.HashMap;
import java.util.Map;

/**
 * LC 146. LRU Cache.
 */
public class LRUCache {

    /** Doubly linked node so we can unlink in O(1) given only the node itself. */
    static class Node {
        int key, val;
        Node prev, next;
        Node(int key, int val) { this.key = key; this.val = val; }
    }

    private final int capacity;
    private final Map<Integer, Node> map = new HashMap<>();
    private final Node head; // sentinel: head.next is most-recently-used
    private final Node tail; // sentinel: tail.prev is least-recently-used

    /**
     * Key insight: a hash map gives O(1) lookup by key; a doubly linked list gives
     * O(1) reordering by recency. Pairing them — map value is the list node — lets
     * both get and put touch only a constant number of links. Sentinel head/tail
     * nodes remove all null-edge handling.
     *
     * Time: O(1) — get and put each do a map op plus a fixed number of relinks.
     * Space: O(capacity) — at most capacity entries across map and list.
     */
    public LRUCache(int capacity) {
        this.capacity = capacity;
        head = new Node(0, 0);
        tail = new Node(0, 0);
        head.next = tail;
        tail.prev = head;
    }

    public int get(int key) {
        Node node = map.get(key);
        if (node == null) return -1;
        moveToFront(node); // touched -> now most recently used
        return node.val;
    }

    public void put(int key, int value) {
        Node node = map.get(key);
        if (node != null) {
            node.val = value;
            moveToFront(node);
            return;
        }
        if (map.size() == capacity) {
            Node lru = tail.prev; // evict least-recently-used
            unlink(lru);
            map.remove(lru.key);
        }
        Node fresh = new Node(key, value);
        map.put(key, fresh);
        addToFront(fresh);
    }

    private void addToFront(Node node) {
        node.prev = head;
        node.next = head.next;
        head.next.prev = node;
        head.next = node;
    }

    private void unlink(Node node) {
        node.prev.next = node.next;
        node.next.prev = node.prev;
    }

    private void moveToFront(Node node) {
        unlink(node);
        addToFront(node);
    }

    public static void main(String[] args) {
        LRUCache cache = new LRUCache(2);
        cache.put(1, 1);
        cache.put(2, 2);
        System.out.println(cache.get(1)); // expected: 1
        cache.put(3, 3);                   // evicts key 2
        System.out.println(cache.get(2)); // expected: -1
        cache.put(4, 4);                   // evicts key 1
        System.out.println(cache.get(1)); // expected: -1
        System.out.println(cache.get(3)); // expected: 3
        System.out.println(cache.get(4)); // expected: 4
    }
}
