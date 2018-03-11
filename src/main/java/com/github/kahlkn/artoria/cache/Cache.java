package com.github.kahlkn.artoria.cache;

/**
 * Cache block.
 * @author Kahle
 */
public interface Cache {

    /**
     * Get data from cache.
     * @param key The data's key
     * @param loader Data loader
     * @return Data object
     */
    Object get(Object key, DataLoader loader);

    /**
     * Put data in cache.
     * @param key The data's key
     * @param value The data
     */
    void put(Object key, Object value);

    /**
     * Remove data by key.
     * @param key The data's key
     */
    void remove(Object key);

    /**
     * Clear cache.
     */
    void clear();

    /**
     * Get next cache object.
     * @return Cache object
     */
    Cache next();

    /**
     * Set next cache object.
     * @param next Next cache
     */
    void next(Cache next);

}
