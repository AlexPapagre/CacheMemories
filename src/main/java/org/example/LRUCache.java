package org.example;

import java.util.HashMap;
import java.util.Map;

public class LRUCache<K, V> implements Cache<K, V> {
    private Map<K, V> map = new HashMap<>();
    private Node<K, V> head, tail;
    private int maxSize, size;

    public LRUCache(int maxSize) {
        this.head = null;
        this.tail = null;
        this.size = 0;
        this.maxSize = maxSize;
    }

    @Override
    public V get(K key) {
        if (!map.containsKey(key)) {
            return null;
        }

        return map.get(key);
    }

    @Override
    public void put(K key, V value) {
        Node<K, V> n = new Node<>();
        n.key = key;
        n.value = value;

        if (head == null) {
            head = tail = n;
        } else {
            n.prev = tail;
            tail.next = n;
            tail = n;
        }
        size++;

        map.put(key, value);

        // Remove head if cache is full
        if (isFull()) {
            pop();
        }
    }

    private boolean isFull() {
        return size == maxSize;
    }

    private void pop() {
        head = head.next;
        head.prev = null;
        size--;
    }

    private static class Node<K, V> {
        public K key;
        public V value;
        public Node<K, V> next, prev;
    }

}
