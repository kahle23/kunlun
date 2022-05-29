package artoria.cache;

/**
 * The cache factory.
 * @author Kahle
 */
public interface CacheFactory {

    /**
     * Register the configuration information according to the matching pattern of the cache names.
     * @param pattern The matching pattern of cache names
     *                      (Probably a regular expression, but most likely a cache name)
     * @param cacheConfig The cache configuration
     */
    void register(String pattern, CacheConfig cacheConfig);

    /**
     * Deregister the configuration information according to the matching pattern of the cache names.
     * @param pattern The matching pattern of cache names
     * @return The cache configuration
     */
    CacheConfig deregister(String pattern);

    /**
     * Get the cache object instance based on the cache name.
     * If the cache object is not generated, it will be created according to certain rules.
     * If the cache object has been created, it is returned directly.
     * @param cacheName The cache name
     * @return The cache object instance
     */
    Cache getInstance(String cacheName);

}
