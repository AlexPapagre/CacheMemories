package org.example;

/**
 * A cache interface
 *
 * @param <K> the key type
 * @param <V> the value type
 */
public interface Cache<K, V> {

    /**
     * Get the value for a key. Returns null if the key is not
     * in the cache.
     *
     * @param key the key
     */
    V get(K key);

    /**
     * Put a new key value pair in the cache.
     *
     * @param key the key
     * @param value the value
     */
    void put(K key, V value);

    /**
     * Returns hit count.
     *
     * @return hitCount the hit count
     */
    int getHitCount();

    /**
     * Returns miss count.
     *
     * @return missCount the miss count
     */
    int getMissCount();

}
