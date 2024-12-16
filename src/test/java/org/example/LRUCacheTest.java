package org.example;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class LRUCacheTest {

    private static final int SIZE = 3;

    @Test
    public void testLRUCache() {
        Cache<Integer, Integer> cache = new LRUCache<>(SIZE);

        for (int i = 0; i < SIZE; i++) {
            assertNull(cache.get(i));
            cache.put(i, i);
            assertEquals((int) cache.get(i), i);
        }

        for (int i = SIZE; i < SIZE * 2; i++) {
            assertEquals((int) cache.get(i - SIZE), i - SIZE);
            cache.put(i, i);
            assertNull(cache.get(i - SIZE));
        }
    }

    @Test
    public void testEdgeCases() {
        Cache<Integer, Integer> cache = new LRUCache<>(SIZE);

        for (int i = 0; i < SIZE; i++) {
            assertNull(cache.get(i));
            cache.put(i, i);
            assertEquals((int) cache.get(i), i);
        }

        for (int i = SIZE; i < SIZE * 2; i++) {
            assertEquals((int) cache.get(i - SIZE), i - SIZE);
            cache.put(i, i);
            assertNull(cache.get(i - SIZE));
        }
    }

    @Test
    public void stressTest() {
        Cache<Integer, Integer> cache = new LRUCache<>(SIZE);

        for (int i = 0; i < 1000000; i++) {
            assertNull(cache.get(i));
            cache.put(i, i);
            assertEquals((int) cache.get(i), i);
        }

        for (int i = 0; i < 1000000; i++) {
            if (i >= 1000000 - SIZE) {
                assertEquals((int) cache.get(i), i);
            } else {
                assertNull(cache.get(i));
            }
        }
    }

}
