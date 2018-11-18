package artoria.cache;

import java.util.Map;

/**
 * Cache loader.
 * @param <K> The type of key
 * @param <V> The type of value
 * @author Kahle
 */
public interface CacheLoader<K, V> {

    /**
     * Loads an object.
     * @param key The key identifying the object being loaded
     * @return The value that is to be stored in the cache
     *          or null if the object can't be loaded
     * @throws CacheException If there is problem executing the loader
     */
    V load(K key) throws CacheException;

    /**
     * Loads multiple objects.
     * @param keys Keys identifying the values to be loaded
     * @return A map of key, values to be stored in the cache
     * @throws CacheException If there is problem executing the loader
     */
    Map<K, V> loadAll(Iterable<? extends K> keys) throws CacheException;

}
