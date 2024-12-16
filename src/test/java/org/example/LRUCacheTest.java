package org.example;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class LRUCacheTest {

    private static final int MAX_SIZE = 3000;

    @Test
    public void testLRUCache() {
        Cache<Integer, Integer> cache = new LRUCache<>(MAX_SIZE);

        for (int i = 0; i < MAX_SIZE * 2; i++) {
            assertNull(cache.get(i));
            cache.put(i, i);
            assertEquals((int) cache.get(i), i);
        }
    }

    @Test
    public void testEdgeCases() {
        Cache<Integer, Integer> cache = new LRUCache<>(MAX_SIZE);

        for (int i = 0; i < MAX_SIZE; i++) {
            assertNull(cache.get(i));
            cache.put(i, i);
            assertEquals((int) cache.get(i), i);
        }

        for (int i = MAX_SIZE; i < MAX_SIZE * 2; i++) {
            cache.put(i, i);
            assertNull(cache.get(i - MAX_SIZE));
        }
    }

    @Test
    public void stressTest() {
        Cache<Integer, Integer> cache = new LRUCache<>(MAX_SIZE);

        for (int i = 0; i < 1000000; i++) {
            assertNull(cache.get(i));
            cache.put(i, i);
            assertEquals((int) cache.get(i), i);
        }

        for (int i = 0; i < 1000000; i++) {
            if (i >= 1000000 - MAX_SIZE) {
                assertEquals((int) cache.get(i), i);
            } else {
                assertNull(cache.get(i));
            }
        }
    }

}
