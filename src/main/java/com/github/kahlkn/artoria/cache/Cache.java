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
     * @param timeToLive The amount of time for the value to live
     *                   , in millisecond, negative number indicates unlimited
     */
    void put(Object key, Object value, long timeToLive);

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
