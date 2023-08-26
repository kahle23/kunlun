package artoria.cache;

import artoria.cache.support.SimpleCache;
import artoria.logging.Logger;
import artoria.logging.LoggerFactory;
import artoria.util.Assert;

import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import static artoria.common.Constants.DEFAULT;

/**
 * The cache tools.
 * @author Kahle
 */
public class CacheUtils {
    private static final Logger log = LoggerFactory.getLogger(CacheUtils.class);
    private static volatile CacheProvider cacheProvider;

    public static CacheProvider getCacheProvider() {
        if (cacheProvider != null) { return cacheProvider; }
        synchronized (CacheUtils.class) {
            if (cacheProvider != null) { return cacheProvider; }
            CacheUtils.setCacheProvider(new SimpleCacheProvider());
            CacheUtils.registerCache(new SimpleCache(DEFAULT));
            return cacheProvider;
        }
    }

    public static void setCacheProvider(CacheProvider cacheProvider) {
        Assert.notNull(cacheProvider, "Parameter \"cacheFactory\" must not null. ");
        log.info("Set cache factory: {}", cacheProvider.getClass().getName());
        CacheUtils.cacheProvider = cacheProvider;
    }

    public static void registerCache(Cache cache) {

        getCacheProvider().registerCache(cache);
    }

    public static void deregisterCache(String cacheName) {

        getCacheProvider().deregisterCache(cacheName);
    }

    public static Cache getCache(String cacheName) {

        return getCacheProvider().getCache(cacheName);
    }

    public static Collection<String> getCacheNames() {

        return getCacheProvider().getCacheNames();
    }

    public static <T> T get(String cacheName, Object key, Callable<T> callable) {

        return getCacheProvider().get(cacheName, key, callable);
    }

    public static <T> T get(String cacheName, Object key, Class<T> type) {

        return getCacheProvider().get(cacheName, key, type);
    }

    public static Object get(String cacheName, Object key) {

        return getCacheProvider().get(cacheName, key);
    }

    public static boolean containsKey(String cacheName, Object key) {

        return getCacheProvider().containsKey(cacheName, key);
    }

    public static long size(String cacheName) {

        return getCacheProvider().size(cacheName);
    }

    public static Object put(String cacheName, Object key, Object value) {

        return getCacheProvider().put(cacheName, key, value);
    }

    public static Object put(String cacheName, Object key, Object value, long timeToLive, TimeUnit timeUnit) {

        return getCacheProvider().put(cacheName, key, value, timeToLive, timeUnit);
    }

    public static Object putIfAbsent(String cacheName, Object key, Object value) {

        return getCacheProvider().putIfAbsent(cacheName, key, value);
    }

    public static Object putIfAbsent(String cacheName, Object key, Object value, long timeToLive, TimeUnit timeUnit) {

        return getCacheProvider().putIfAbsent(cacheName, key, value, timeToLive, timeUnit);
    }

    public static void putAll(String cacheName, Map<?, ?> map) {

        getCacheProvider().putAll(cacheName, map);
    }

    public static boolean expire(String cacheName, Object key, long timeToLive, TimeUnit timeUnit) {

        return getCacheProvider().expire(cacheName, key, timeToLive, timeUnit);
    }

    public static boolean expireAt(String cacheName, Object key, Date date) {

        return getCacheProvider().expireAt(cacheName, key, date);
    }

    public static boolean persist(String cacheName, Object key) {

        return getCacheProvider().persist(cacheName, key);
    }

    public static Object remove(String cacheName, Object key) {

        return getCacheProvider().remove(cacheName, key);
    }

    public static void removeAll(String cacheName, Collection<?> keys) {

        getCacheProvider().removeAll(cacheName, keys);
    }

    public static void clear(String cacheName) {

        getCacheProvider().clear(cacheName);
    }

    public static long prune(String cacheName) {

        return getCacheProvider().prune(cacheName);
    }

    public static Collection<Object> keys(String cacheName) {

        return getCacheProvider().keys(cacheName);
    }

    public static Map<Object, Object> entries(String cacheName) {

        return getCacheProvider().entries(cacheName);
    }

}
