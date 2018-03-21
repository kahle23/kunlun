package com.github.kahlkn.artoria.collection;

import com.github.kahlkn.artoria.util.ObjectUtils;

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
public class ReferenceHashMap<K, V> extends AbstractMap<K, V> implements Map<K, V> {
    private final Map<K, ValueCell<V>> hash;
    private final Type type;
    private Set<Map.Entry<K, V>> entrySet = null;
    private final ReferenceQueue<V> queue = new ReferenceQueue<V>();

    public ReferenceHashMap(Type type) {
        this.hash = new HashMap<K, ValueCell<V>>();
        this.type = type;
    }

    public ReferenceHashMap(int initialCapacity, Type type) {
        this.hash = new HashMap<K, ValueCell<V>>(initialCapacity);
        this.type = type;
    }

    public ReferenceHashMap(int initialCapacity, float loadFactor, Type type) {
        this.hash = new HashMap<K, ValueCell<V>>(initialCapacity, loadFactor);
        this.type = type;
    }

    @Override
    public int size() {
        Set<Map.Entry<K, V>> entries = this.entrySet();
        return entries.size();
    }

    @Override
    public void clear() {
        this.processQueue();
        this.hash.clear();
    }

    @Override
    public boolean isEmpty() {
        Set<Map.Entry<K, V>> entries = this.entrySet();
        return entries.isEmpty();
    }

    @Override
    public V get(Object key) {
        this.processQueue();
        ValueCell<V> cell = this.hash.get(key);
        return this.stripValueCell(cell, false);
    }

    @Override
    public V remove(Object key) {
        this.processQueue();
        ValueCell<V> oldValue = this.hash.remove(key);
        return this.stripValueCell(oldValue, true);
    }

    @Override
    public V put(K key, V value) {
        this.processQueue();
        ValueCell<V> cell = this.newValueCell(key, value, this.queue);
        ValueCell<V> oldValue = this.hash.put(key, cell);
        return this.stripValueCell(oldValue, true);
    }

    @Override
    public boolean containsKey(Object key) {
        ValueCell<V> cell = this.hash.get(key);
        return this.stripValueCell(cell, false) != null;
    }

    @Override
    public Set<Map.Entry<K, V>> entrySet() {
        if (this.entrySet == null) {
            this.entrySet = new EntrySet();
        }
        return this.entrySet;
    }

    private void processQueue() {
        ValueCell cell;
        while ((cell = (ValueCell) this.queue.poll()) != null) {
            if (cell.isValid()) {
                this.hash.remove(cell.getKey());
            }
        }
    }

    private ValueCell<V> newValueCell(K key, V value, ReferenceQueue<V> queue) {
        if (value == null) {
            return null;
        }
        switch (type) {
            case WEAK: return new WeakValueCell<V>(key, value, queue);
            case SOFT: return new SoftValueCell<V>(key, value, queue);
            default: return new WeakValueCell<V>(key, value, queue);
        }
    }

    private V stripValueCell(ValueCell<V> cell, boolean doDrop) {
        if (cell == null) {
            return null;
        }
        else {
            V value = cell.get();
            if (doDrop) {
                cell.drop();
            }
            return value;
        }
    }

    public enum Type {

        /**
         * Reference type weak.
         */
        WEAK,

        /**
         * Reference type soft.
         */
        SOFT

    }

    private class EntrySet extends AbstractSet<Map.Entry<K, V>> {
        Set<Map.Entry<K, ValueCell<V>>> hashEntries;

        private EntrySet() {
            this.hashEntries = hash.entrySet();
        }

        @Override
        public int size() {
            int count = 0;
            Iterator<Map.Entry<K, V>> iterator = this.iterator();
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
            return b && this.hashEntries.remove(((ReferenceHashMap.Entry) entry).entry);
        }

        @Override
        public boolean isEmpty() {
            return !this.iterator().hasNext();
        }

        @Override
        public Iterator<Map.Entry<K, V>> iterator() {
            return new Iterator<Map.Entry<K, V>>() {
                Iterator<Map.Entry<K, ValueCell<V>>> hashIterator = hashEntries.iterator();
                Entry next = null;

                @Override
                public boolean hasNext() {
                    while (true) {
                        if (this.hashIterator.hasNext()) {
                            Map.Entry<K, ValueCell<V>> entry = this.hashIterator.next();
                            ValueCell<V> cell = entry.getValue();
                            V value = null;
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
                public Map.Entry<K, V> next() {
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

    private class Entry implements java.util.Map.Entry<K, V> {
        private java.util.Map.Entry<K, ValueCell<V>> entry;
        private V value;

        Entry(java.util.Map.Entry<K, ValueCell<V>> entry, V value) {
            this.entry = entry;
            this.value = value;
        }

        @Override
        public K getKey() {
            return this.entry.getKey();
        }

        @Override
        public V getValue() {
            return this.value;
        }

        @Override
        public V setValue(V value) {
            K key = this.entry.getKey();
            this.value = value;
            ValueCell<V> cell = ReferenceHashMap.this.newValueCell(key, value, queue);
            ValueCell<V> oldValue = this.entry.setValue(cell);
            return oldValue.get();
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

    private interface ValueCell<T> {

        /**
         * Get value from cell.
         * @return The value cell saved
         */
        T get();

        /**
         * Clear cell value.
         */
        void drop();

        /**
         * Get cell key.
         * @return The key cell saved
         */
        Object getKey();

        /**
         * Judge the cell is valid.
         * @return If true is valid, and false not valid
         */
        boolean isValid();

    }

    private static class WeakValueCell<T> extends WeakReference<T> implements ValueCell<T> {
        private static Object INVALID_KEY = new Object();
        private Object key;

        private WeakValueCell(Object key, T value, ReferenceQueue<T> queue) {
            super(value, queue);
            this.key = key;
        }

        @Override
        public void drop() {
            super.clear();
            this.key = INVALID_KEY;
        }

        @Override
        public Object getKey() {
            return key;
        }

        @Override
        public boolean isValid() {
            return this.key != INVALID_KEY;
        }

    }

    private static class SoftValueCell<T> extends SoftReference<T> implements ValueCell<T> {
        private static Object INVALID_KEY = new Object();
        private Object key;

        private SoftValueCell(Object key, T value, ReferenceQueue<T> queue) {
            super(value, queue);
            this.key = key;
        }

        @Override
        public void drop() {
            super.clear();
            this.key = INVALID_KEY;
        }

        @Override
        public Object getKey() {
            return key;
        }

        @Override
        public boolean isValid() {
            return this.key != INVALID_KEY;
        }

    }

}
