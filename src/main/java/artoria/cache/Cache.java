package artoria.cache;

import java.util.Collection;
import java.util.Map;

/**
 * A cache is a Map-like data structure that provides
 * temporary storage of application data.
 * @param <K> The type of key
 * @param <V> The type of value
 * @author Kahle
 */
public interface Cache<K, V> {

    /**
     * Return the name of the cache.
     * @return The name of the cache
     */
    String getName();

    /**
     * Gets a value from the cache.
     * @param key The key whose associated value is to be returned
     * @return The element, or null, if it does not exist
     */
    V get(K key);

    /**
     * Associates the specified value with the specified key in the cache.
     * @param key Key with which the specified value is to be associated
     * @param value Value to be associated with the specified key
     */
    void put(K key, V value);

    /**
     * Associates the specified value with the specified key in this cache,
     * returning an existing value if one existed.
     * @param key Key with which the specified value is to be associated
     * @param value Value to be associated with the specified key
     * @return The value associated with the key
     *          at the start of the operation or null if none was associated
     */
    V putAndGet(K key, V value);

    /**
     * Atomically associates the specified key with the given value
     * if it is not already associated with a value.
     * @param key Key with which the specified value is to be associated
     * @param value Value to be associated with the specified key
     * @return True if a value was set
     */
    V putIfAbsent(K key, V value);

    /**
     * Copies all of the entries from the specified map to the cache.
     * @param map Mappings to be stored in this cache
     */
    void putAll(Map<? extends K, ? extends V> map);

    /**
     * Removes the mapping for a key from this cache if it is present.
     * @param key Key whose mapping is to be removed from the cache
     * @return False if there was no matching key
     */
    boolean remove(K key);

    /**
     * Atomically removes the mapping for a key only
     * if currently mapped to the given value.
     * @param key Key whose mapping is to be removed from the cache
     * @param oldValue Value expected to be associated with the specified key
     * @return False if there was no matching key
     */
    boolean remove(K key, V oldValue);

    /**
     * Removes values for the specified keys.
     * @param keys the keys to remove
     */
    void remove(Collection<? extends K> keys);

    /**
     * Atomically removes the value for a key only
     * if currently mapped to some value.
     * @param key Key with which the specified value is associated
     * @return The value if one existed
     *          or null if no mapping existed for this key
     */
    V removeAndGet(K key);

    /**
     * Determines if the cache contains a value for the specified key.
     * @param key Key whose presence in this cache is to be tested
     * @return True if this map contains a mapping for the specified key
     */
    boolean containsKey(K key);

    /**
     * Clears the contents of the cache.
     */
    void clear();

    /**
     * Return the number of key-value mappings in this cache.
     * @return The number of key-value mappings in this cache
     */
    int size();

    /**
     * Loads the specified value into the cache using
     * the CacheLoader for the given key.
     * @param key The keys to load
     * @param coverExisted Cover if key existed
     */
    void load(K key, boolean coverExisted);

    /**
     * Loads the specified values into the cache using
     * the CacheLoader for the given keys.
     * @param keys The keys to load
     * @param coverExisted Cover if key existed
     */
    void load(Iterable<? extends K> keys, boolean coverExisted);

    /**
     * Refresh this cache all keys.
     */
    void refresh();

    /**
     * Gets original operation object.
     * @return Original operation object
     */
    Object getOriginal();

}
