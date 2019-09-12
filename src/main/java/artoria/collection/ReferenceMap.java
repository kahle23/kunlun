package artoria.collection;

import artoria.util.CollectionUtils;
import artoria.util.MapUtils;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.util.*;

/**
 * Reference map can be wrapped as weak and soft references.
 * @author Kahle
 */
public class ReferenceMap<K, V> implements Map<K, V> {
    private final Map<K, ValueCell<K, V>> internalMap;
    private final ReferenceQueue<? super V> queue;
    private final Type type;

    public ReferenceMap(Type type) {

        this(new HashMap<K, ValueCell<K, V>>(), type);
    }

    public ReferenceMap(Map<K, ValueCell<K, V>> internalMap, Type type) {
        this.queue = new ReferenceQueue<V>();
        this.internalMap = internalMap;
        this.type = type;
    }

    private ValueCell<K, V> newValueCell(K key, V value, ReferenceQueue<? super V> queue) {
        switch (type) {
            case WEAK: return new WeakValueCell<K, V>(key, value, queue);
            case SOFT: return new SoftValueCell<K, V>(key, value, queue);
            default: return new WeakValueCell<K, V>(key, value, queue);
        }
    }

    @SuppressWarnings("unchecked")
    private void processQueue() {
        ValueCell<K, V> valueCell;
        while ((valueCell = (ValueCell) queue.poll()) != null) {
            internalMap.remove(valueCell.getKey());
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public V get(Object key) {
        this.processQueue();
        ValueCell<K, V> value = internalMap.get(key);
        if (value == null) { return null; }
        // Unwrap the 'real' value from the Reference.
        V result = value.get();
        if (result == null) {
            // The wrapped value was garbage collected,
            // So remove this entry from the backing internalMap.
            internalMap.remove((K) key);
        }
        return result;
    }

    @Override
    public boolean isEmpty() {
        processQueue();
        return internalMap.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        processQueue();
        return internalMap.containsKey(key);
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean containsValue(Object value) {
        processQueue();
        Collection<V> values = values();
        boolean notEmpty = CollectionUtils.isNotEmpty(values);
        return notEmpty && values.contains((V) value);
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> map) {
        if (MapUtils.isEmpty(map)) {
            processQueue();
            return;
        }
        for (Map.Entry<? extends K, ? extends V> entry : map.entrySet()) {
            put(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public Set<K> keySet() {
        // Throw out garbage collected values first.
        processQueue();
        return internalMap.keySet();
    }

    @Override
    public Collection<V> values() {
        // Throw out garbage collected values first.
        processQueue();
        Collection<K> keys = internalMap.keySet();
        if (CollectionUtils.isEmpty(keys)) {
            return Collections.emptyList();
        }
        Collection<V> values = new ArrayList<V>(keys.size());
        for (K key : keys) {
            V val = get(key);
            if (val != null) {
                values.add(val);
            }
        }
        return values;
    }

    @Override
    public V put(K key, V value) {
        // Throw out garbage collected values first.
        processQueue();
        ValueCell<K, V> newValue = newValueCell(key, value, queue);
        ValueCell<K, V> oldValue = internalMap.put(key, newValue);
        return oldValue != null ? oldValue.get() : null;
    }

    @Override
    public V remove(Object key) {
        // Throw out garbage collected values first.
        processQueue();
        ValueCell<K, V> raw = internalMap.remove(key);
        return raw != null ? raw.get() : null;
    }

    @Override
    public void clear() {
        // Throw out garbage collected values.
        processQueue();
        internalMap.clear();
    }

    @Override
    public int size() {
        // Throw out garbage collected values first.
        processQueue();
        return internalMap.size();
    }

    @Override
    public Set<Map.Entry<K, V>> entrySet() {
        // Throw out garbage collected values first.
        processQueue();
        Collection<K> keys = internalMap.keySet();
        if (CollectionUtils.isEmpty(keys)) {
            return Collections.emptySet();
        }
        Map<K, V> kvPairs = new HashMap<K, V>(keys.size());
        for (K key : keys) {
            V val = get(key);
            if (val != null) {
                kvPairs.put(key, val);
            }
        }
        return kvPairs.entrySet();
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

    public interface ValueCell<K, V> {

        /**
         * Get value from cell.
         * @return The value cell saved
         */
        V get();

        /**
         * Get cell key.
         * @return The key cell saved
         */
        K getKey();

    }

    private static class WeakValueCell<K, V> extends WeakReference<V> implements ValueCell<K, V> {
        private final K key;

        private WeakValueCell(K key, V value, ReferenceQueue<? super V> queue) {
            super(value, queue);
            this.key = key;
        }

        @Override
        public K getKey() {

            return key;
        }

    }

    private static class SoftValueCell<K, V> extends SoftReference<V> implements ValueCell<K, V> {
        private final K key;

        private SoftValueCell(K key, V value, ReferenceQueue<? super V> queue) {
            super(value, queue);
            this.key = key;
        }

        @Override
        public K getKey() {

            return key;
        }

    }

}
