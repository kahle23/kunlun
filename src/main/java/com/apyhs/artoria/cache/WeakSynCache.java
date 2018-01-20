package com.apyhs.artoria.cache;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * Weak reference synchronized cache.
 * @author Kahle
 */
public class WeakSynCache implements Cache {
    private final Map<Object, Reference<Object>> storage = new WeakHashMap<Object, Reference<Object>>();
    private Cache next;

    @Override
    public Object get(Object key, DataLoader loader) {
        synchronized (storage) {
            Reference<Object> ref = storage.get(key);
            if (ref != null) {
                Object data = ref.get();
                if (data != null) {
                    return data;
                }
                storage.remove(key);
            }
            return null;
        }
    }

    @Override
    public void put(Object key, Object value) {
        synchronized (storage) {
            if (value != null) {
                storage.put(key, new WeakReference<Object>(value));
            }
            else {
                storage.remove(key);
            }
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
