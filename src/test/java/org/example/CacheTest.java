package org.example;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CacheTest {

    private static final int CAPACITY = 1000;

    @Test
    public void testLRUCache() {
        Cache<Integer, Integer> cache = new CacheImpl<>(CAPACITY, CacheReplacementPolicy.LRU);

        // Puts 'CAPACITY * 2' integers in cache and checks if they are saved properly
        for (int i = 0; i < CAPACITY * 2; i++) {
            assertNull(cache.get(i));
            cache.put(i, i);
            assertEquals((int) cache.get(i), i);
        }
    }

    @Test
    public void testLRUCacheEdgeCases() {
        Cache<Integer, Integer> cache = new CacheImpl<>(CAPACITY, CacheReplacementPolicy.LRU);

        // Puts 'CAPACITY' integers in cache and checks if they are saved properly
        for (int i = 0; i < CAPACITY; i++) {
            assertNull(cache.get(i));
            cache.put(i, i);
            assertEquals((int) cache.get(i), i);
        }

        // Puts 'CAPACITY' integers in cache and checks if the least recent integer got removed
        for (int i = CAPACITY; i < CAPACITY * 2; i++) {
            cache.put(i, i);
            assertNull(cache.get(i - CAPACITY));
        }
    }

    @Test
    public void stressTestLRUCache() {
        Cache<Integer, Integer> cache = new CacheImpl<>(CAPACITY, CacheReplacementPolicy.LRU);

        // Puts 1000000 integers in cache and checks if they are saved properly
        for (int i = 0; i < 1000000; i++) {
            assertNull(cache.get(i));
            cache.put(i, i);
            assertEquals((int) cache.get(i), i);
        }

        // Checks if the last 'CAPACITY' integers are saved and checks if the other integers got removed
        for (int i = 0; i < 1000000; i++) {
            if (i >= 1000000 - CAPACITY) {
                assertEquals((int) cache.get(i), i);
            } else {
                assertNull(cache.get(i));
            }
        }
    }

    @Test
    public void testMRUCache() {
        Cache<Integer, Integer> cache = new CacheImpl<>(CAPACITY, CacheReplacementPolicy.MRU);

        // Puts 'CAPACITY * 2' integers in cache and checks if they are saved properly
        for (int i = 0; i < CAPACITY * 2; i++) {
            assertNull(cache.get(i));
            cache.put(i, i);
            assertEquals((int) cache.get(i), i);
        }
    }

    @Test
    public void testMRUCacheEdgeCases() {
        Cache<Integer, Integer> cache = new CacheImpl<>(CAPACITY, CacheReplacementPolicy.MRU);

        // Puts 'CAPACITY' integers in cache and checks if they are saved properly
        for (int i = 0; i < CAPACITY; i++) {
            assertNull(cache.get(i));
            cache.put(i, i);
            assertEquals((int) cache.get(i), i);
        }

        // Puts 'CAPACITY' integers in cache and checks if the most recent integer got removed
        for (int i = CAPACITY; i < CAPACITY * 2; i++) {
            assertEquals((int) cache.get(i - CAPACITY), i - CAPACITY);
            cache.put(i, i);
            assertNull(cache.get(i - CAPACITY));
        }
    }

    @Test
    public void stressTestMRUCache() {
        Cache<Integer, Integer> cache = new CacheImpl<>(CAPACITY, CacheReplacementPolicy.MRU);

        // Puts 1000000 integers in cache and checks if they are saved properly
        for (int i = 0; i < 1000000; i++) {
            assertNull(cache.get(i));
            cache.put(i, i);
            assertEquals((int) cache.get(i), i);
        }

        // Checks if the first 'CAPACITY - 1' integers and the last integer (99999) are saved and checks if the other integers got removed
        for (int i = 0; i < 1000000; i++) {
            if (i < CAPACITY - 1 || i == 999999) {
                assertEquals((int) cache.get(i), i);
            } else {
                assertNull(cache.get(i));
            }
        }
    }

}
