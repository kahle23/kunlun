package artoria.cache;

import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

public interface CacheProvider {

    /**
     * Register common properties' information.
     * @param commonProperties The common properties
     */
    void registerCommonProperties(Map<?, ?> commonProperties);

    /**
     * Clears common properties' information.
     */
    void clearCommonProperties();

    /**
     * Get common properties' information.
     * @return The common properties
     */
    Map<String, Object> getCommonProperties();

    /**
     * Register the ocr handler.
     * @param cacheName The cache name
     * @param cache The cache
     */
    void registerCache(String cacheName, Cache cache);

    /**
     * Deregister the ocr handler.
     * @param cacheName The ocr handler name
     */
    void deregisterCache(String cacheName);

    /**
     * Get the ocr handler by name.
     * @param cacheName The ocr handler name
     * @return The ocr handler
     */
    Cache getCache(String cacheName);

    /**
     * Return a collection of the cache names known by this manager.
     * @return the names of all caches known by the cache manager
     */
    Collection<String> getCacheNames();

    /**
     * Return the value to which this cache maps the specified key,
     * obtaining that value from value loader if necessary.
     * @param key The key whose associated value is to be returned
     * @param callable The value loader
     * @return The value to which this cache maps the specified key
     */
    <T> T get(String cacheName, Object key, Callable<T> callable);

    /**
     * Return the value to which this cache maps the specified key,
     * generically specifying a type that return value will be cast to.
     * @param key The key whose associated value is to be returned
     * @param type The required type of the returned value
     * @return The value to which this cache maps the specified key
     */
    <T> T get(String cacheName, Object key, Class<T> type);

    /**
     * Return the value to which this cache maps the specified key.
     * @param key The key whose associated value is to be returned
     * @return The value to which this cache maps the specified key
     */
    Object get(String cacheName, Object key);

    /**
     * Determines if the cache contains a value for the specified key.
     * @param key The key whose presence in this cache is to be tested
     * @return True if this cache contains a mapping for the specified key
     */
    boolean containsKey(String cacheName, Object key);

    /**
     * Return the number of key-value mappings in this cache.
     * @return The number of key-value mappings in this cache
     */
    long size(String cacheName);

    /**
     * Associate the specified value with the specified key in this cache.
     * @param key The key with which the specified value is to be associated
     * @param value The value to be associated with the specified key
     * @return The previous value associated with key, or null if there was no mapping for key
     */
    Object put(String cacheName, Object key, Object value);

    /**
     * Associate the specified value with the specified key in this cache and set a time to live.
     * @param key The key with which the specified value is to be associated
     * @param value The value to be associated with the specified key
     * @param timeToLive The time to live of the key-value pair
     * @param timeUnit The unit of time to live
     * @return The previous value associated with key, or null if there was no mapping for key
     */
    Object put(String cacheName, Object key, Object value, long timeToLive, TimeUnit timeUnit);

    /**
     * Atomically associate the specified value with the specified key in this cache
     * if it is not set already.
     * @param key The key with which the specified value is to be associated
     * @param value The value to be associated with the specified key
     * @return The previous value associated with key, or null if there was no mapping for key
     */
    Object putIfAbsent(String cacheName, Object key, Object value);

    /**
     * Atomically associate the specified value with the specified key in this cache
     * if it is not set already and set a time to live.
     * @param key The key with which the specified value is to be associated
     * @param value The value to be associated with the specified key
     * @param timeToLive The time to live of the key-value pair
     * @param timeUnit The unit of time to live
     * @return The previous value associated with key, or null if there was no mapping for key
     */
    Object putIfAbsent(String cacheName, Object key, Object value, long timeToLive, TimeUnit timeUnit);

    /**
     * Copies all of the entries from the specified map to the cache.
     * @param map The mappings to be stored in this cache
     */
    void putAll(String cacheName, Map<?, ?> map);

    /**
     * Set time to live for given key.
     * @param key The key with which the specified value is to be associated
     * @param timeToLive The time to live of the key-value pair
     * @param timeUnit The unit of time to live
     * @return Whether the operation succeeds
     */
    boolean expire(String cacheName, Object key, long timeToLive, TimeUnit timeUnit);

    /**
     * Set the expiration for given key as the input date.
     * @param key The key with which the specified value is to be associated
     * @param date The date that will be set to expiration date
     * @return Whether the operation succeeds
     */
    boolean expireAt(String cacheName, Object key, Date date);

    /**
     * Remove the expiration from given key.
     * @param key The key with which the specified value is to be associated
     * @return Whether the operation succeeds
     */
    boolean persist(String cacheName, Object key);

    /**
     * Remove the mapping for this key from this cache if it is present.
     * @param key The key whose mapping is to be removed from the cache
     * @return The previous value associated with key
     */
    Object remove(String cacheName, Object key);

    /**
     * Removes entries for the specified keys.
     * @param keys The keys to remove
     */
    void removeAll(String cacheName, Collection<?> keys);

    /**
     * Remove all mappings from the cache.
     */
    void clear(String cacheName);

    /**
     * Prune the objects that need to be cleaned according to the pruning strategy.
     * @return The number of objects that have been cleaned out
     */
    long prune(String cacheName);

    /**
     * Return a view of all the keys for entries contained in this cache.
     * @return The view of all the keys for entries contained in this cache
     */
    Collection<Object> keys(String cacheName);

    /**
     * Return a view of the entries stored in this cache as a map.
     * @return The view of the entries stored in this cache
     */
    Map<Object, Object> entries(String cacheName);

}
