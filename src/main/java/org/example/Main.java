package org.example;

import java.util.Random;

public class Main {

    public static final int OPERATIONS = 100000;
    public static final int CAPACITY = 1000;

    public static void main(String[] args) {

        for (CacheReplacementPolicy replacementPolicy : CacheReplacementPolicy.values()) {
            Cache<Integer, Integer> cache = new CacheImpl<>(CAPACITY, replacementPolicy);

            int count = 0;
            while (count < OPERATIONS) {

                // Put 1000 integers to cache
                for (int i = 0; i < 1000; i++) {
                    int num = getRandomInt();
                    cache.put(num, num);
                }

                // Get 1000 integers from cache
                for (int i = 0; i < 1000; i++) {
                    cache.get(getRandomInt());
                    count++;
                    if (count >= OPERATIONS) {
                        break;
                    }
                }

                count++;
            }
            count--;

            // Print results
            String description = replacementPolicy.getDescription();
            System.out.println();
            System.out.println(description + " Cache");
            for (int i = 0; i < description.length(); i++) {
                System.out.print("-");
            }
            System.out.println("------");
            System.out.println("Total operations: " + count);
            System.out.println("Cache Hits: " + cache.getHitCount());
            System.out.println("Cache Misses: " + cache.getMissCount());
            System.out.println("Hit Rate: " + String.format("%.2f", (double) cache.getHitCount() / (cache.getHitCount() + cache.getMissCount()) * 100) + "%");
            System.out.println("Miss Rate: " + String.format("%.2f", (double) cache.getMissCount() / (cache.getHitCount() + cache.getMissCount()) * 100) + "%");
        }


    }

    private static int getRandomInt() {
        Random rng = new Random();

        // 80% chance num belongs to [0 - 49999]
        // 20% chance num belongs to [50000 - 99999]
        int min = 0, max = 49999;
        if (rng.nextInt(5) == 0) {
            min = 50000;
            max = 99999;
        }

        return rng.nextInt(max - min + 1) + min;
    }

}
