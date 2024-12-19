package org.example;

import java.util.HashMap;
import java.util.Map;

public class CacheImpl<K, V> implements Cache<K, V> {
    private final Map<K, Node<K, V>> cache = new HashMap<>();
    private Node<K, V> head, tail;
    private int size, hitCount, missCount;
    private final int capacity;
    private final CacheReplacementPolicy replacementPolicy;

    public CacheImpl(int capacity, CacheReplacementPolicy replacementPolicy) {
        head = null;
        tail = null;
        size = 0;
        hitCount = 0;
        missCount = 0;
        this.capacity = capacity;
        this.replacementPolicy = replacementPolicy;
    }

    @Override
    public V get(K key) {
        if (!cache.containsKey(key)) {
            missCount++;
            return null;
        }
        hitCount++;

        swapToTail(cache.get(key));

        return cache.get(key).value;
    }

    @Override
    public void put(K key, V value) {
        Node<K, V> n = new Node<>();
        n.key = key;
        n.value = value;

        if (!cache.containsKey(key)) {
            size++;
        }

        // Remove one if cache is full
        if (isFull()) {
            if (replacementPolicy == CacheReplacementPolicy.LRU) {
                popLeastRecent();
            } else {
                popMostRecent();
            }
            size--;
        }

        // Put node in list
        putInList(n);

        // Put node in map
        cache.put(key, n);

    }

    @Override
    public int getHitCount() {
        return hitCount;
    }

    @Override
    public int getMissCount() {
        return missCount;
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
        return size == capacity + 1;
    }

    private void popLeastRecent() {
        cache.remove(head.key);
        head = head.next;
        head.prev = null;
    }

    private void popMostRecent() {
        cache.remove(tail.key);
        tail = tail.prev;
        tail.next = null;
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
