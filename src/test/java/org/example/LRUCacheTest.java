package org.example;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class LRUCacheTest {

    private static final int CAPACITY = 1000;

    @Test
    public void testLRUCache() {
        Cache<Integer, Integer> cache = new CacheImpl<>(CAPACITY, CacheReplacementPolicy.LRU);

        for (int i = 0; i < CAPACITY * 2; i++) {
            assertNull(cache.get(i));
            cache.put(i, i);
            assertEquals((int) cache.get(i), i);
        }
    }

    @Test
    public void testLRUCacheEdgeCases() {
        Cache<Integer, Integer> cache = new CacheImpl<>(CAPACITY, CacheReplacementPolicy.LRU);

        for (int i = 0; i < CAPACITY; i++) {
            assertNull(cache.get(i));
            cache.put(i, i);
            assertEquals((int) cache.get(i), i);
        }

        for (int i = CAPACITY; i < CAPACITY * 2; i++) {
            cache.put(i, i);
            assertNull(cache.get(i - CAPACITY));
        }
    }

    @Test
    public void stressTestLRUCache() {
        Cache<Integer, Integer> cache = new CacheImpl<>(CAPACITY, CacheReplacementPolicy.LRU);

        for (int i = 0; i < 1000000; i++) {
            assertNull(cache.get(i));
            cache.put(i, i);
            assertEquals((int) cache.get(i), i);
        }

        for (int i = 0; i < 1000000; i++) {
            if (i >= 1000000 - CAPACITY) {
                assertEquals((int) cache.get(i), i);
            } else {
                assertNull(cache.get(i));
            }
        }
    }

}
