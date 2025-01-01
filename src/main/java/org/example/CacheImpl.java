package org.example;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class CacheImpl<K, V> implements Cache<K, V> {
    private final Caching<K, V> caching;
    private int freeSpace, hitCount, missCount;

    public CacheImpl(int capacity, CacheReplacementPolicy replacementPolicy) {
        freeSpace = capacity;
        caching = replacementPolicy.equals(CacheReplacementPolicy.LFU) ? new CachingLfu<>() : new CachingLruMru<>(replacementPolicy);
    }

    @Override
    public V get(K key) {
        return caching.get(key);
    }

    @Override
    public void put(K key, V value) {
        caching.put(key, value);
    }

    @Override
    public int getHitCount() {
        return hitCount;
    }

    @Override
    public int getMissCount() {
        return missCount;
    }

    private boolean isFull() {
        return freeSpace < 0;
    }

    private interface Caching<K, V> {
        V get(K key);
        void put(K key, V value);
    }

    private class CachingLruMru<K, V> implements Caching<K, V> {
        private final Map<K, Node<K, V>> cache = new HashMap<>();
        private Node<K, V> head, tail;
        private final CacheReplacementPolicy replacementPolicy;

        public CachingLruMru(CacheReplacementPolicy replacementPolicy) {
            this.replacementPolicy = replacementPolicy;
        }

        @Override
        public V get(K key) {
            if (!cache.containsKey(key)) {
                missCount++;
                return null;
            }
            hitCount++;

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
                if (replacementPolicy.equals(CacheReplacementPolicy.LRU)) {
                    removeLeastRecent();
                } else {
                    removeMostRecent();
                }
                freeSpace++;
            }

            // Put node in List
            putInList(n);

            // Put node in Map
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

        private void removeLeastRecent() {
            cache.remove(head.key);
            head = head.next;
            head.prev = null;
        }

        private void removeMostRecent() {
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
    }

    private class CachingLfu<K, V> implements Caching<K, V> {
        private final TreeMap<Integer, Map<K, V>> freqMap = new TreeMap<>();
        private final Map<K, Integer> cache = new HashMap<>();

        public CachingLfu() {
        }

        @Override
        public V get(K key) {
            if (!cache.containsKey(key)) {
                missCount++;
                return null;
            }
            hitCount++;

            int freq = cache.get(key);
            V value = freqMap.get(freq).get(key);

            increaseFrequency(key, value, freq);

            return value;
        }

        @Override
        public void put(K key, V value) {
            if (cache.containsKey(key)) {
                int freq = cache.get(key);
                if (freqMap.get(freq).get(key).equals(value)) {

                    // Increase frequency if same entry exists
                    increaseFrequency(key, value, freq);
                    return;

                } else {

                    // Remove dublicate key with different value
                    removeFromFreqMap(key, freq);
                }
            } else {
                freeSpace--;
            }

            // Remove one if cache is full
            if (isFull()) {
                removeLeastFrequent();
                freeSpace++;
            }

            putInFreqMap(key, value, 1);
        }

        private void increaseFrequency(K key, V value, int freq) {
            removeFromFreqMap(key, freq);
            putInFreqMap(key, value, freq + 1);
        }

        private void removeFromFreqMap(K key, int freq) {
            freqMap.get(freq).remove(key);
            if (freqMap.get(freq).isEmpty()) {
                freqMap.remove(freq);
            }
        }

        private void removeLeastFrequent() {
            int freq = freqMap.firstKey();
            K key = freqMap.get(freq).entrySet().iterator().next().getKey();
            removeFromFreqMap(key, freq);
            cache.remove(key);
        }

        private void putInFreqMap(K key, V value, int freq) {
            if (!freqMap.containsKey(freq)) {
                freqMap.put(freq, new HashMap<>());
            }
            freqMap.get(freq).put(key, value);
            cache.put(key, freq);
        }

    }

    private static class Node<K, V> {
        public K key;
        public V value;
        public Node<K, V> next, prev;
    }

}
