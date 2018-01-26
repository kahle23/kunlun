package com.apyhs.artoria.collection;

import com.apyhs.artoria.util.ObjectUtils;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.util.*;

@SuppressWarnings("unchecked")
public class SoftHashMap<K, V> extends AbstractMap<K, V> implements Map<K, V> {
    private Map hash;
    private ReferenceQueue queue = new ReferenceQueue();
    private Set entrySet = null;

    private void processQueue() {
        SoftHashMap.ValueCell cell;
        while((cell = (SoftHashMap.ValueCell)this.queue.poll()) != null) {
            if (cell.isValid()) {
                this.hash.remove(cell.key);
            }
            else {
                SoftHashMap.ValueCell.dropped--;
            }
        }

    }

    private Object fill(Object o) {
        return null;
    }

    public SoftHashMap(int initialCapacity, float loadFactor) {
        this.hash = new HashMap(initialCapacity, loadFactor);
    }

    public SoftHashMap(int initialCapacity) {
        this.hash = new HashMap(initialCapacity);
    }

    public SoftHashMap() {
        this.hash = new HashMap();
    }

    @Override
    public int size() {
        return this.entrySet().size();
    }

    @Override
    public boolean isEmpty() {
        return this.entrySet().isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return SoftHashMap.ValueCell.strip(this.hash.get(key), false) != null;
    }

    @Override
    public V get(Object key) {
        this.processQueue();
        Object cell = this.hash.get(key);
        if (cell == null) {
            cell = this.fill(key);
            if (cell != null) {
                this.hash.put(key, SoftHashMap.ValueCell.create(key, cell, this.queue));
                return (V) cell;
            }
        }

        return (V) SoftHashMap.ValueCell.strip(cell, false);
    }

    @Override
    public V put(K key, V value) {
        this.processQueue();
        SoftHashMap.ValueCell cell = SoftHashMap.ValueCell.create(key, value, this.queue);
        return (V) SoftHashMap.ValueCell.strip(this.hash.put(key, cell), true);
    }

    @Override
    public V remove(Object key) {
        this.processQueue();
        return (V) SoftHashMap.ValueCell.strip(this.hash.remove(key), true);
    }

    @Override
    public void clear() {
        this.processQueue();
        this.hash.clear();
    }

    @Override
    public Set<Map.Entry<K, V>> entrySet() {
        if (this.entrySet == null) {
            this.entrySet = new SoftHashMap.EntrySet();
        }

        return this.entrySet;
    }

    private class EntrySet extends AbstractSet {
        Set hashEntries;

        private EntrySet() {
            this.hashEntries = SoftHashMap.this.hash.entrySet();
        }

        @Override
        public Iterator iterator() {
            return new Iterator() {
                Iterator hashIterator;
                SoftHashMap.Entry next;

                {
                    this.hashIterator = SoftHashMap.EntrySet.this.hashEntries.iterator();
                    this.next = null;
                }

                @Override
                public boolean hasNext() {
                    while(true) {
                        if (this.hashIterator.hasNext()) {
                            java.util.Map.Entry entry = (java.util.Map.Entry)this.hashIterator.next();
                            SoftHashMap.ValueCell cell = (SoftHashMap.ValueCell)entry.getValue();
                            Object value = null;
                            if (cell != null && (value = cell.get()) == null) {
                                continue;
                            }

                            this.next = SoftHashMap.this.new Entry(entry, value);
                            return true;
                        }

                        return false;
                    }
                }

                @Override
                public Object next() {
                    if (this.next == null && !this.hasNext()) {
                        throw new NoSuchElementException();
                    } else {
                        SoftHashMap.Entry entry = this.next;
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

        @Override
        public boolean isEmpty() {
            return !this.iterator().hasNext();
        }

        @Override
        public int size() {
            int count = 0;
            Iterator iterator = this.iterator();
            while(iterator.hasNext()) {
                ++count;
                iterator.next();
            }
            return count;
        }

        @Override
        public boolean remove(Object entry) {
            SoftHashMap.this.processQueue();
            return entry instanceof SoftHashMap.Entry && this.hashEntries.remove(((Entry) entry).entry);
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
            return this.entry.setValue(SoftHashMap.ValueCell.create(this.entry.getKey(), value, SoftHashMap.this.queue));
        }

        @Override
        public boolean equals(Object entry) {
            if (!(entry instanceof java.util.Map.Entry)) {
                return false;
            }
            else {
                java.util.Map.Entry entryObj = (java.util.Map.Entry) entry;
                return ObjectUtils.equals(this.entry.getKey(), entryObj.getKey()) && ObjectUtils.equals(this.value, entryObj.getValue());
            }
        }

        @Override
        public int hashCode() {
            Object key;
            return ((key = this.getKey()) == null ? 0 : key.hashCode()) ^ (this.value == null ? 0 : this.value.hashCode());
        }
    }

    private static class ValueCell extends SoftReference {
        private static Object INVALID_KEY = new Object();
        private static int dropped = 0;
        private Object key;

        private ValueCell(Object key, Object value, ReferenceQueue queue) {
            super(value, queue);
            this.key = key;
        }

        private static SoftHashMap.ValueCell create(Object key, Object value, ReferenceQueue queue) {
            return value == null ? null : new SoftHashMap.ValueCell(key, value, queue);
        }

        private static Object strip(Object cell, boolean doDrop) {
            if (cell == null) {
                return null;
            }
            else {
                SoftHashMap.ValueCell cellObj = (SoftHashMap.ValueCell)cell;
                Object value = cellObj.get();
                if (doDrop) {
                    cellObj.drop();
                }
                return value;
            }
        }

        private boolean isValid() {
            return this.key != INVALID_KEY;
        }

        private void drop() {
            super.clear();
            this.key = INVALID_KEY;
            ++dropped;
        }

    }
}
