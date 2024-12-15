package org.example;

public class LRUCache<K, V> implements Cache<K, V> {
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
        return null;
    }

    @Override
    public void put(K key, V value) {

    }

    private static class Node<K, V> {
        public K key;
        public V value;
        public Node<K, V> next, prev;
    }

}
