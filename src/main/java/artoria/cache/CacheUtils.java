package artoria.cache;

import artoria.lang.ReferenceType;
import artoria.logging.Logger;
import artoria.logging.LoggerFactory;
import artoria.util.Assert;

import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import static artoria.common.Constants.*;

/**
 * Cache tools.
 * @author Kahle
 */
public class CacheUtils {
    private static final Map<String, Cache> STORAGE = new ConcurrentHashMap<String, Cache>();
    private static Logger log = LoggerFactory.getLogger(CacheUtils.class);

    public static void register(Cache cache) {
        Assert.notNull(cache, "Parameter \"cache\" must not null. ");
        String cacheName = cache.getName();
        Assert.notBlank(cacheName, "Parameter \"cacheName\" must not blank. ");
        String cacheClassName = cache.getClass().getName();
        log.info("Register \"{}\" to \"{}\". ", cacheClassName, cacheName);
        STORAGE.put(cacheName, cache);
    }

    public static Cache unregister(String cacheName) {
        Assert.notBlank(cacheName, "Parameter \"cacheName\" must not blank. ");
        Cache remove = STORAGE.remove(cacheName);
        if (remove != null) {
            String removeClassName = remove.getClass().getName();
            log.info("Unregister \"{}\" to \"{}\". ", removeClassName, cacheName);
        }
        return remove;
    }

    public static Map<String, Cache> getStorage() {

        return STORAGE;
    }

    public static Cache getCache(String cacheName) {
        Assert.notBlank(cacheName, "Parameter \"cacheName\" must not blank. ");
        Cache cache = STORAGE.get(cacheName);
        if (cache != null) { return cache; }
        if (DEFAULT.equals(cacheName)) {
            SimpleCache simpleCache = new SimpleCache(DEFAULT,
                    ZERO, TimeUnit.HOURS.toMillis(TWO), MINUS_ONE, ReferenceType.SOFT);
            simpleCache.setPrintLog(true);
            register(cache = simpleCache);
        }
        else { register(cache = new UndefinedCache(cacheName)); }
        //Assert.notNull(cache, "The cache does not exist. Please register first. ");
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

    public static long size(String cacheName) {

        return getCache(cacheName).size();
    }

    public static Object put(String cacheName, Object key, Object value) {

        return getCache(cacheName).put(key, value);
    }

    public static Object put(String cacheName, Object key, Object value, long timeToLive, TimeUnit timeUnit) {

        return getCache(cacheName).put(key, value, timeToLive, timeUnit);
    }

    public static Object putIfAbsent(String cacheName, Object key, Object value) {

        return getCache(cacheName).putIfAbsent(key, value);
    }

    public static Object putIfAbsent(String cacheName, Object key, Object value, long timeToLive, TimeUnit timeUnit) {

        return getCache(cacheName).putIfAbsent(key, value, timeToLive, timeUnit);
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

    public static long prune(String cacheName) {

        return getCache(cacheName).prune();
    }

    public static Collection<Object> keys(String cacheName) {

        return getCache(cacheName).keys();
    }

    public static Map<Object, Object> entries(String cacheName) {

        return getCache(cacheName).entries();
    }

}
