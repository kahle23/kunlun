/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.cache;

import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

/**
 * The interface that defines common cache operations.
 * @author Kahle
 */
public interface Cache {

    /**
     * Return the underlying native cache provider.
     * @return The native cache provider
     */
    Object getNative();

    /**
     * Return the value to which this cache maps the specified key,
     *  obtaining that value from value loader if necessary.
     * @param key The key whose associated value is to be returned
     * @param callable The value loader
     * @return The value to which this cache maps the specified key
     */
    <T> T get(Object key, Callable<T> callable);

    /**
     * Return the value to which this cache maps the specified key,
     *  generically specifying a type that return value will be cast to.
     * @param key The key whose associated value is to be returned
     * @param type The required type of the returned value
     * @return The value to which this cache maps the specified key
     */
    <T> T get(Object key, Class<T> type);

    /**
     * Return the value to which this cache maps the specified key.
     * @param key The key whose associated value is to be returned
     * @return The value to which this cache maps the specified key
     */
    Object get(Object key);

    /**
     * Determines if the cache contains a value for the specified key.
     * @param key The key whose presence in this cache is to be tested
     * @return True if this cache contains a mapping for the specified key
     */
    boolean containsKey(Object key);

    /**
     * Return the number of key-value mappings in this cache.
     * @return The number of key-value mappings in this cache
     */
    long size();

    /**
     * Associate the specified value with the specified key in this cache.
     * @param key The key with which the specified value is to be associated
     * @param value The value to be associated with the specified key
     * @return The previous value associated with key, or null if there was no mapping for key
     */
    Object put(Object key, Object value);

    /**
     * Associate the specified value with the specified key in this cache and set a time to live.
     * @param key The key with which the specified value is to be associated
     * @param value The value to be associated with the specified key
     * @param timeToLive The time to live of the key-value pair
     * @param timeUnit The unit of time to live
     * @return The previous value associated with key, or null if there was no mapping for key
     */
    Object put(Object key, Object value, long timeToLive, TimeUnit timeUnit);

    /**
     * Atomically associate the specified value with the specified key in this cache
     *  if it is not set already.
     * @param key The key with which the specified value is to be associated
     * @param value The value to be associated with the specified key
     * @return The previous value associated with key, or null if there was no mapping for key
     */
    Object putIfAbsent(Object key, Object value);

    /**
     * Atomically associate the specified value with the specified key in this cache
     *  if it is not set already and set a time to live.
     * @param key The key with which the specified value is to be associated
     * @param value The value to be associated with the specified key
     * @param timeToLive The time to live of the key-value pair
     * @param timeUnit The unit of time to live
     * @return The previous value associated with key, or null if there was no mapping for key
     */
    Object putIfAbsent(Object key, Object value, long timeToLive, TimeUnit timeUnit);

    /**
     * Copies all the entries from the specified map to the cache.
     * @param map The mappings to be stored in this cache
     */
    void putAll(Map<?, ?> map);

    /**
     * Set time to live for given key.
     * @param key The key with which the specified value is to be associated
     * @param timeToLive The time to live of the key-value pair
     * @param timeUnit The unit of time to live
     * @return Whether the operation succeeds
     */
    boolean expire(Object key, long timeToLive, TimeUnit timeUnit);

    /**
     * Set the expiration for given key as the input date.
     * @param key The key with which the specified value is to be associated
     * @param date The date that will be set to expiration date
     * @return Whether the operation succeeds
     */
    boolean expireAt(Object key, Date date);

    /**
     * Remove the expiration from given key.
     * @param key The key with which the specified value is to be associated
     * @return Whether the operation succeeds
     */
    boolean persist(Object key);

    /**
     * Remove the mapping for this key from this cache if it is present.
     * @param key The key whose mapping is to be removed from the cache
     * @return The previous value associated with key
     */
    Object remove(Object key);

    /**
     * Removes entries for the specified keys.
     * @param keys The keys to remove
     */
    void removeAll(Collection<?> keys);

    /**
     * Remove all mappings from the cache.
     */
    void clear();

    /**
     * Prune the objects that need to be cleaned according to the pruning strategy.
     * @return The number of objects that have been cleaned out
     */
    long prune();

    /**
     * Return a view of all the keys for entries contained in this cache.
     * @return The view of all the keys for entries contained in this cache
     */
    Collection<Object> keys();

    /**
     * Return a view of the entries stored in this cache as a map.
     * @return The view of the entries stored in this cache
     */
    Map<Object, Object> entries();

}
