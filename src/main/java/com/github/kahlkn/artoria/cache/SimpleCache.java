package com.github.kahlkn.artoria.cache;

import com.github.kahlkn.artoria.collection.ReferenceHashMap;

import java.util.Map;

/**
 * Simple synchronized cache.
 * @see ReferenceHashMap
 * @author Kahle
 */
public class SimpleCache implements Cache {
    private final Map<Object, Object> storage;
    private Cache next;

    public SimpleCache() {
        this.storage = new ReferenceHashMap<Object, Object>(ReferenceHashMap.Type.WEAK);
    }

    public SimpleCache(ReferenceHashMap.Type type) {
        this.storage = new ReferenceHashMap<Object, Object>(type);
    }

    @Override
    public Object get(Object key, DataLoader loader) {
        synchronized (storage) {
            return storage.get(key);
        }
    }

    @Override
    public void put(Object key, Object value) {
        synchronized (storage) {
            storage.put(key, value);
        }
    }

    @Override
    public void remove(Object key) {
        synchronized (storage) {
            storage.remove(key);
        }
    }

    @Override
    public void clear() {
        synchronized (storage) {
            storage.clear();
        }
    }

    @Override
    public Cache next() {
        return next;
    }

    @Override
    public void next(Cache next) {
        this.next = next;
    }

}
