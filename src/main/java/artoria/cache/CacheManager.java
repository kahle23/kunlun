package artoria.cache;

/**
 * Cache manager.
 * @author Kahle
 */
public interface CacheManager {

    /**
     * Looks up a managed cache given its name.
     * @param name The name of the cache to look for
     * @param <K> The type of key
     * @param <V> The type of value
     * @return The cache or null if it does exist
     */
    <K, V> Cache<K, V> getCache(String name);

    /**
     * Looks up a managed cache given its name.
     * @param name The name of the managed cache to acquire
     * @param keyType The expected class of the key
     * @param valueType The expected class of the value
     * @param <K> The type of key
     * @param <V> The type of value
     * @return The cache or null if it does exist
     */
    <K, V> Cache<K, V> getCache(String name, Class<K> keyType, Class<V> valueType);

    /**
     * Create a named cache at runtime.
     * @param name The name of the cache
     * @param <K> The type of key
     * @param <V> The type of value
     * @return The cache object
     */
    <K, V> Cache<K, V> createCache(String name);

    /**
     * Create a named cache at runtime.
     * @param name The name of the cache
     * @param configuration A configuration for the cache
     * @param <K> The type of key
     * @param <V> The type of value
     * @return The cache object
     */
    <K, V> Cache<K, V> createCache(String name, Configuration<K, V> configuration);

    /**
     * Destroys a specifically named and managed cache.
     * @param name The cache to destroy
     */
    void destroyCache(String name);

    /**
     * Return an array of the current cache names.
     * @return An array of the current cache names
     */
    String[] getCacheNames();

}
