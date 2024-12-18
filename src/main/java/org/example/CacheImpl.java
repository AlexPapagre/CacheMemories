package org.example;

import java.util.HashMap;
import java.util.Map;

public class CacheImpl<K, V> implements Cache<K, V> {
    public static final int DEFAULT_CAPACITY = 1000;

    private final Map<K, Node<K, V>> map = new HashMap<>();
    private Node<K, V> head, tail;
    private int size, hitCount, missCount;
    private final int capacity;
    private final CacheReplacementPolicy replacementPolicy;

    public CacheImpl(CacheReplacementPolicy replacementPolicy) {
        this(DEFAULT_CAPACITY, replacementPolicy);
    }

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
        if (!map.containsKey(key)) {
            missCount++;
            return null;
        }
        hitCount++;

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

        map.put(key, n);

        if (!map.containsKey(key)) {
            size++;

            // Remove one if cache is full
            if (isFull()) {
                if (replacementPolicy == CacheReplacementPolicy.LRU) {
                    popFromHead();
                } else {
                    popFromTail();
                }
                size--;
            }
        }

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

    private void popFromHead() {
        map.remove(head.key);
        head = head.next;
        head.prev = null;
    }

    private void popFromTail() {
        map.remove(head.key);
        tail = tail.prev;
        tail.next = null;
    }

    private static class Node<K, V> {
        public K key;
        public V value;
        public Node<K, V> next, prev;
    }

}
