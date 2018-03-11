package com.github.kahlkn.artoria.cache;

import com.github.kahlkn.artoria.util.Assert;

/**
 * Cache tools.
 * @author Kahle
 */
public class CacheUtils {
    private Cache cache;

    public CacheUtils() {
        this.cache = new SimpleCache();
    }

    public CacheUtils(Cache cache) {
        this.setCache(cache);
    }

    public Cache getCache() {
        return cache;
    }

    public void setCache(Cache cache) {
        Assert.notNull(cache, "Parameter \"cache\" must not null. ");
        this.cache = cache;
    }

    public Object get(Object key, DataLoader loader) {
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

    public void remove(Object key) {
        Cache next = cache;
        while (next != null) {
            next.remove(key);
            next = next.next();
        }
    }

    public void clear() {
        Cache next = cache;
        while (next != null) {
            next.clear();
            next = next.next();
        }
    }

}
