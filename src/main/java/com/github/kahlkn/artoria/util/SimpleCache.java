package com.github.kahlkn.artoria.util;

import com.github.kahlkn.artoria.collection.ReferenceHashMap;

import java.util.Map;

/**
 * Simple synchronized cache block.
 * @see ReferenceHashMap
 * @author Kahle
 */
public class SimpleCache {
    private final Map<Object, Object> storage;

    public SimpleCache() {
        // Default reference map is weak.
        this(true);
    }

    public SimpleCache(boolean isWeak) {
        ReferenceHashMap.Type type =
                isWeak ? ReferenceHashMap.Type.WEAK : ReferenceHashMap.Type.SOFT;
        this.storage = new ReferenceHashMap<Object, Object>(type);
    }

    public Object get(Object key) {
        synchronized (storage) {
            return storage.get(key);
        }
    }

    public Object get(Object key, DataLoader loader) {
        synchronized (storage) {
            Object res = storage.get(key);
            if (res == null) {
                res = loader.load();
                storage.put(key, res);
            }
            return res;
        }
    }

    public void put(Object key, Object value) {
        synchronized (storage) {
            storage.put(key, value);
        }
    }

    public void put(Object key, DataLoader loader) {
        synchronized (storage) {
            storage.put(key, loader.load());
        }
    }

    public void remove(Object key) {
        synchronized (storage) {
            storage.remove(key);
        }
    }

    public void clear() {
        synchronized (storage) {
            storage.clear();
        }
    }

}
