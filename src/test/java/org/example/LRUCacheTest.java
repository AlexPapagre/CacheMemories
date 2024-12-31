package org.example;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class LRUCacheTest {

    private static final int CAPACITY = 1000;

    @Test
    public void testLRUCache() {
        Cache<Integer, Integer> cache = new LRUCache<>(3);

        cache.put(1, 1);
        cache.put(2, 2);
        cache.put(3, 3);

        assertEquals(1, (int) cache.get(1));
        assertEquals(2, (int) cache.get(2));
        assertEquals(3, (int) cache.get(3));

        cache.put(4, 4);

        assertNull(cache.get(1));
        assertEquals(2, (int) cache.get(2));
        assertEquals(3, (int) cache.get(3));
        assertEquals(4, (int) cache.get(4));

        cache.put(5, 5);

        assertNull(cache.get(2));
        assertEquals(3, (int) cache.get(3));
        assertEquals(4, (int) cache.get(4));
        assertEquals(5, (int) cache.get(5));
    }

    @Test
    public void testLRUCacheEdgeCases() {
        Cache<Integer, Integer> cache = new LRUCache<>(CAPACITY);

        // Puts 'CAPACITY' integers in cache and checks if they are saved properly
        for (int i = 0; i < CAPACITY; i++) {
            assertNull(cache.get(i));
            cache.put(i, i);
            assertEquals(i, (int) cache.get(i));
        }

        // Puts 'CAPACITY' integers in cache and checks if the least recent integer got removed
        for (int i = CAPACITY; i < CAPACITY * 2; i++) {
            cache.put(i, i);
            assertNull(cache.get(i - CAPACITY));
        }
    }

    @Test
    public void stressTestLRUCache() {
        Cache<Integer, Integer> cache = new LRUCache<>(CAPACITY);

        // Puts 1000000 integers in cache and checks if they are saved properly
        for (int i = 0; i < 1000000; i++) {
            assertNull(cache.get(i));
            cache.put(i, i);
            assertEquals(i, (int) cache.get(i));
        }

        // Checks if the last 'CAPACITY' integers are saved and checks if the other integers got removed
        for (int i = 0; i < 1000000; i++) {
            if (i >= 1000000 - CAPACITY) {
                assertEquals(i, (int) cache.get(i));
            } else {
                assertNull(cache.get(i));
            }
        }
    }

}
