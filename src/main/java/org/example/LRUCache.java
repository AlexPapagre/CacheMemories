package org.example;

import java.util.HashMap;
import java.util.Map;

public class LRUCache<K, V> implements Cache<K, V> {
    private Map<K, Node<K, V>> map = new HashMap<>();
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

        swapToTail(map.get(key));

        return map.get(key).value;
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

        if (!map.containsKey(key)) {
            size++;
        }

        map.put(key, n);

        // Remove head if cache is full
        if (isFull()) {
            pop();
        }
    }

    private void swapToTail(Node<K, V> n) {
        if (size <= 1 || n == tail) {
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
        return size == maxSize + 1;
    }

    private void pop() {
        map.remove(head.key);
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
