package com.apyhs.artoria.cache;

import com.apyhs.artoria.logging.Logger;
import com.apyhs.artoria.logging.LoggerFactory;
import com.apyhs.artoria.util.Assert;

/**
 * Cache tools.
 * @author Kahle
 */
public class CacheUtils {
    private static Logger log = LoggerFactory.getLogger(CacheUtils.class);
    private static Cache cache;

    static {
        CacheUtils.setCache(new WeakSynCache());
    }

    public static Cache getCache() {
        return cache;
    }

    public static void setCache(Cache cache) {
        Assert.notNull(cache, "Parameter \"cache\" must not null. ");
        log.info("Set cache: " + cache.getClass().getName());
        CacheUtils.cache = cache;
    }

    public static Object get(Object key, DataLoader loader) {
        Object data;
        Cache current = null;
        Cache next = cache;
        while (next != null) {
            data = next.get(key, loader);
            if (data == null) {
                current = next;
                next = next.next();
            }
            else {
                if (current != null) {
                    current.put(key, data);
                }
                return data;
            }
        }
        data = loader.load();
        if (data == null) {
            return null;
        }
        if (current != null) {
            current.put(key, data);
        }
        return data;
    }

    public static void remove(Object key) {
        Cache next = cache;
        while (next != null) {
            next.remove(key);
            next = next.next();
        }
    }

    public static void clear() {
        Cache next = cache;
        while (next != null) {
            next.clear();
            next = next.next();
        }
    }

}
