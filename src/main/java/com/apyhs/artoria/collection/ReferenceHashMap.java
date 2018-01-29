package com.apyhs.artoria.collection;

import com.apyhs.artoria.util.ObjectUtils;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.util.*;

/**
 * Reference hash map, has weak and soft reference.
 * @author Kahle
 * @param <K> The key type
 * @param <V> The value type
 */
@SuppressWarnings("unchecked")
public class ReferenceHashMap<K, V> extends AbstractMap<K, V> implements Map<K, V> {
    private final Map hash;
    private final Type type;
    private Set entrySet = null;
    private final ReferenceQueue queue = new ReferenceQueue();

    private void processQueue() {
        ValueCell cell;
        while((cell = (ValueCell) this.queue.poll()) != null) {
            if (cell.isValid()) {
                this.hash.remove(cell.getKey());
            }
        }
    }

    public ReferenceHashMap(Type type) {
        this.hash = new HashMap();
        this.type = type;
    }

    public ReferenceHashMap(int initialCapacity, Type type) {
        this.hash = new HashMap(initialCapacity);
        this.type = type;
    }

    public ReferenceHashMap(int initialCapacity, float loadFactor, Type type) {
        this.hash = new HashMap(initialCapacity, loadFactor);
        this.type = type;
    }

    @Override
    public int size() {
        Set<Map.Entry<K, V>> set = this.entrySet();
        return set.size();
    }

    @Override
    public void clear() {
        this.processQueue();
        this.hash.clear();
    }

    @Override
    public boolean isEmpty() {
        Set<Map.Entry<K, V>> set = this.entrySet();
        return set.isEmpty();
    }

    @Override
    public V get(Object key) {
        this.processQueue();
        Object cell = this.hash.get(key);
        return (V) this.stripValueCell(cell, false);
    }

    @Override
    public V remove(Object key) {
        this.processQueue();
        Object oldValue = this.hash.remove(key);
        return (V) this.stripValueCell(oldValue, true);
    }

    @Override
    public V put(K key, V value) {
        this.processQueue();
        ValueCell cell = this.newValueCell(key, value, this.queue);
        Object oldValue = this.hash.put(key, cell);
        return (V) this.stripValueCell(oldValue, true);
    }

    @Override
    public boolean containsKey(Object key) {
        Object cell = this.hash.get(key);
        return this.stripValueCell(cell, false) != null;
    }

    @Override
    public Set<Map.Entry<K, V>> entrySet() {
        if (this.entrySet == null) {
            this.entrySet = new EntrySet();
        }
        return this.entrySet;
    }

    private ValueCell newValueCell(Object key, Object value, ReferenceQueue queue) {
        if (value == null) {
            return null;
        }
        switch (type) {
            case WEAK: return new WeakValueCell(key, value, queue);
            case SOFT: return new SoftValueCell(key, value, queue);
            default: return new WeakValueCell(key, value, queue);
        }
    }

    private Object stripValueCell(Object cell, boolean doDrop) {
        if (cell == null) {
            return null;
        }
        else {
            ValueCell cellObj = (ValueCell) cell;
            Object value = cellObj.get();
            if (doDrop) {
                cellObj.drop();
            }
            return value;
        }
    }

    public enum Type {
        WEAK,
        SOFT
    }

    private class EntrySet extends AbstractSet {
        Set hashEntries;

        private EntrySet() {
            this.hashEntries = hash.entrySet();
        }

        @Override
        public int size() {
            int count = 0;
            Iterator iterator = this.iterator();
            while (iterator.hasNext()) {
                ++count;
                iterator.next();
            }
            return count;
        }

        @Override
        public boolean remove(Object entry) {
            ReferenceHashMap.this.processQueue();
            boolean b = entry instanceof ReferenceHashMap.Entry;
            return b && this.hashEntries.remove(((Entry) entry).entry);
        }

        @Override
        public boolean isEmpty() {
            return !this.iterator().hasNext();
        }

        @Override
        public Iterator iterator() {
            return new Iterator() {
                Iterator hashIterator = hashEntries.iterator();
                Entry next = null;

                @Override
                public boolean hasNext() {
                    while (true) {
                        if (this.hashIterator.hasNext()) {
                            java.util.Map.Entry entry = (java.util.Map.Entry) this.hashIterator.next();
                            ValueCell cell = (ValueCell) entry.getValue();
                            Object value = null;
                            if (cell != null && (value = cell.get()) == null) {
                                continue;
                            }
                            this.next = new Entry(entry, value);
                            return true;
                        }
                        return false;
                    }
                }

                @Override
                public Object next() {
                    if (this.next == null && !this.hasNext()) {
                        throw new NoSuchElementException();
                    }
                    else {
                        Entry entry = this.next;
                        this.next = null;
                        return entry;
                    }
                }

                @Override
                public void remove() {
                    this.hashIterator.remove();
                }

            };
        }

    }

    private class Entry implements java.util.Map.Entry {
        private java.util.Map.Entry entry;
        private Object value;

        Entry(java.util.Map.Entry entry, Object value) {
            this.entry = entry;
            this.value = value;
        }

        @Override
        public Object getKey() {
            return this.entry.getKey();
        }

        @Override
        public Object getValue() {
            return this.value;
        }

        @Override
        public Object setValue(Object value) {
            Object key = this.entry.getKey();
            this.value = value;
            ValueCell cell = ReferenceHashMap.this.newValueCell(key, value, queue);
            return this.entry.setValue(cell);
        }

        @Override
        public boolean equals(Object entry) {
            if (!(entry instanceof java.util.Map.Entry)) {
                return false;
            }
            else {
                java.util.Map.Entry entryObj = (java.util.Map.Entry) entry;
                boolean keyEqu = ObjectUtils.equals(this.entry.getKey(), entryObj.getKey());
                return keyEqu && ObjectUtils.equals(this.value, entryObj.getValue());
            }
        }

        @Override
        public int hashCode() {
            Object key = this.getKey();
            int keyHc = key == null ? 0 : key.hashCode();
            Object val = this.value;
            int valHc = val == null ? 0 : val.hashCode();
            return keyHc ^ valHc;
        }

    }

    private interface ValueCell {
        void drop();
        Object get();
        Object getKey();
        boolean isValid();
    }

    private static class WeakValueCell extends WeakReference implements ValueCell {
        private static Object INVALID_KEY = new Object();
        private Object key;

        private WeakValueCell(Object key, Object value, ReferenceQueue queue) {
            super(value, queue);
            this.key = key;
        }

        public void drop() {
            super.clear();
            this.key = INVALID_KEY;
        }

        public Object getKey() {
            return key;
        }

        public boolean isValid() {
            return this.key != INVALID_KEY;
        }

    }

    private static class SoftValueCell extends SoftReference implements ValueCell {
        private static Object INVALID_KEY = new Object();
        private Object key;

        private SoftValueCell(Object key, Object value, ReferenceQueue queue) {
            super(value, queue);
            this.key = key;
        }

        public void drop() {
            super.clear();
            this.key = INVALID_KEY;
        }

        public Object getKey() {
            return key;
        }

        public boolean isValid() {
            return this.key != INVALID_KEY;
        }

    }

}
