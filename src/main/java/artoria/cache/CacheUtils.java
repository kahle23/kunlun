package artoria.cache;

import artoria.logging.Logger;
import artoria.logging.LoggerFactory;
import artoria.util.Assert;

import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import static artoria.collection.ReferenceMap.Type.SOFT;
import static artoria.common.Constants.DEFAULT;
import static artoria.common.Constants.ZERO;

/**
 * Cache tools.
 * @author Kahle
 */
public class CacheUtils {
    private static final Map<String, Cache> CACHE_MAP = new ConcurrentHashMap<String, Cache>();
    private static Logger log = LoggerFactory.getLogger(CacheUtils.class);

    static {
        SimpleCache cache = new SimpleCache(DEFAULT, ZERO, SOFT);
        cache.setPrintLog(true);
        register(cache);
    }

    public static void register(Cache cache) {
        Assert.notNull(cache, "Parameter \"cache\" must not null. ");
        String cacheName = cache.getName();
        Assert.notBlank(cacheName, "Parameter \"cacheName\" must not blank. ");
        String cacheClassName = cache.getClass().getName();
        log.info("Register \"{}\" to \"{}\". ", cacheClassName, cacheName);
        CACHE_MAP.put(cacheName, cache);
    }

    public static Cache unregister(String cacheName) {
        Assert.notBlank(cacheName, "Parameter \"cacheName\" must not blank. ");
        Cache remove = CACHE_MAP.remove(cacheName);
        if (remove != null) {
            String removeClassName = remove.getClass().getName();
            log.info("Unregister \"{}\" to \"{}\". ", removeClassName, cacheName);
        }
        return remove;
    }

    public static Cache getCache(String cacheName) {
        Assert.notBlank(cacheName, "Parameter \"cacheName\" must not blank. ");
        Cache cache = CACHE_MAP.get(cacheName);
        Assert.notNull(cache, "The cache does not exist. Please register first. ");
        return cache;
    }

    public static <T> T get(String cacheName, Object key, Callable<T> callable) {

        return getCache(cacheName).get(key, callable);
    }

    public static <T> T get(String cacheName, Object key, Class<T> type) {

        return getCache(cacheName).get(key, type);
    }

    public static Object get(String cacheName, Object key) {

        return getCache(cacheName).get(key);
    }

    public static boolean containsKey(String cacheName, Object key) {

        return getCache(cacheName).containsKey(key);
    }

    public static int size(String cacheName) {

        return getCache(cacheName).size();
    }

    public static Object put(String cacheName, Object key, Object value) {

        return getCache(cacheName).put(key, value);
    }

    public static Object putIfAbsent(String cacheName, Object key, Object value) {

        return getCache(cacheName).putIfAbsent(key, value);
    }

    public static void putAll(String cacheName, Map<?, ?> map) {

        getCache(cacheName).putAll(map);
    }

    public static boolean expire(String cacheName, Object key, long timeToLive, TimeUnit timeUnit) {

        return getCache(cacheName).expire(key, timeToLive, timeUnit);
    }

    public static boolean expireAt(String cacheName, Object key, Date date) {

        return getCache(cacheName).expireAt(key, date);
    }

    public static boolean persist(String cacheName, Object key) {

        return getCache(cacheName).persist(key);
    }

    public static Object remove(String cacheName, Object key) {

        return getCache(cacheName).remove(key);
    }

    public static void removeAll(String cacheName, Collection<?> keys) {

        getCache(cacheName).removeAll(keys);
    }

    public static void clear(String cacheName) {

        getCache(cacheName).clear();
    }

    public static int prune(String cacheName) {

        return getCache(cacheName).prune();
    }

    public static Collection<Object> keys(String cacheName) {

        return getCache(cacheName).keys();
    }

    public static Map<Object, Object> entries(String cacheName) {

        return getCache(cacheName).entries();
    }

}
