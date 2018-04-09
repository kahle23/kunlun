package com.github.kahlkn.artoria.cache;

import com.github.kahlkn.artoria.collection.ReferenceHashMap;

import java.io.Serializable;
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
            Element element = (Element) storage.get(key);
            return element != null ? element.getValue() : null;
        }
    }

    @Override
    public void put(Object key, Object value, long timeToLive) {
        synchronized (storage) {
            Element element = new Element(key, value, timeToLive);
            storage.put(key, element);
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

    private static class Element implements Serializable {
        private final Object key;
        private final Object value;
        private final long createTime;
        private final long timeToLive;

        public Element(Object key, Object value, long timeToLive) {
            this.key = key;
            this.value = value;
            this.createTime = System.currentTimeMillis();
            this.timeToLive = timeToLive;
        }

        public Object getValue() {
            long differ = System.currentTimeMillis() - createTime;
            return timeToLive >= 0 ? differ <= timeToLive ? value : null : value;
        }

    }

}
