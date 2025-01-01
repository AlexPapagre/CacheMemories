package org.example;

import java.util.HashMap;
import java.util.Map;

public class LRUCache<K, V> implements Cache<K, V> {
    private final Map<K, Node<K, V>> cache = new HashMap<>();
    private Node<K, V> head, tail;
    private int freeSpace;

    public LRUCache(int capacity) {
        freeSpace = capacity;
    }

    @Override
    public V get(K key) {
        if (!cache.containsKey(key)) {
            return null;
        }

        makeMostRecent(cache.get(key));

        return cache.get(key).value;
    }

    @Override
    public void put(K key, V value) {
        Node<K, V> n = new Node<>();
        n.key = key;
        n.value = value;

        if (!cache.containsKey(key)) {
            freeSpace--;
        }

        // Remove one if cache is full
        if (isFull()) {
            removeLeastRecent();
        }

        // Put node in list
        putInList(n);

        // Put node in map
        cache.put(key, n);
    }

    private void makeMostRecent(Node<K, V> n) {
        if (head == tail || n == tail) {
            return;
        }

        if (n == head) {
            n.next.prev = null;
            head = n.next;
        } else {
            n.next.prev = n.prev;
            n.prev.next = n.next;
        }

        n.next = null;
        n.prev = tail;
        tail.next = n;
        tail = n;
    }

    private boolean isFull() {
        return freeSpace < 0;
    }

    private void removeLeastRecent() {
        cache.remove(head.key);
        head = head.next;
        head.prev = null;
        freeSpace++;
    }

    private void putInList(Node<K, V> n) {
        if (head == null) {
            head = tail = n;
        } else {
            n.prev = tail;
            tail.next = n;
            tail = n;
        }
    }

    private static class Node<K, V> {
        public K key;
        public V value;
        public Node<K, V> next, prev;
    }

}
