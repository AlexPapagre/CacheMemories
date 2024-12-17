package org.example;

import java.util.Random;

public class Main {

    public static final int OPERATIONS = 100000;

    public static void main(String[] args) {

        Cache<Integer, Integer> cache = new LRUCache<>();

        Random rng = new Random();
        
        for (int i = 0; i < Math.sqrt(OPERATIONS); i++) {

            // Put integers in cache
            for (int j = 0; j < Math.sqrt(OPERATIONS); j++) {

                // 80% chance num belongs to [0 - 49999]
                // 20% chance num belongs to [50000 - 99999]
                int min = 0, max = 49999;
                if (rng.nextInt(5) == 0) {
                    min = 50000;
                    max = 99999;
                }
                int num = rng.nextInt(max - min + 1) + min;
                cache.put(num, num);
            }

            // Get integers in from cache
            for (int j = 0; j < Math.sqrt(OPERATIONS); j++) {

                // 80% chance num belongs to [0 - 49999]
                // 20% chance num belongs to [50000 - 99999]
                int min = 0, max = 49999;
                if (rng.nextInt(5) == 0) {
                    min = 50000;
                    max = 99999;
                }
                int num = rng.nextInt(max - min + 1) + min;
                cache.get(num);
            }

        }
    }
}
