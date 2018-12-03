package artoria.cache;

import artoria.collection.ReferenceMap;
import artoria.util.CollectionUtils;
import artoria.util.MapUtils;
import artoria.util.ObjectUtils;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * Cache simple implement by jdk.
 * @param <K> The type of key
 * @param <V> The type of value
 * @author Kahle
 */
public class SimpleCache<K, V> implements Cache<K, V> {
    private final Map<K, V> cache;
    private final String name;
    private CacheLoader<K, V> cacheLoader;

    <C extends Configuration<K, V>> SimpleCache(String name, C configuration) {
        ReferenceMap.Type type = ReferenceMap.Type.SOFT;
        this.cache = new ReferenceMap<K, V>(type, true);
        this.name = name;
        if (configuration != null) {
            this.cacheLoader = configuration.getCacheLoader();
        }
    }

    @Override
    public String getName() {

        return name;
    }

    @Override
    public V get(K key) {
        V val;
        if ((val = cache.get(key)) == null) {
            this.load(key, false);
            val = cache.get(key);
        }
        return val;
    }

    @Override
    public void put(K key, V value) {

        cache.put(key, value);
    }

    @Override
    public V putAndGet(K key, V value) {

        return cache.put(key, value);
    }

    @Override
    public V putIfAbsent(K key, V value) {
        V val;
        if ((val = cache.get(key)) == null) {
            val = cache.put(key, value);
        }
        return val;
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> map) {
        if (MapUtils.isEmpty(map)) {
            return;
        }
        cache.putAll(map);
    }

    @Override
    public boolean remove(K key) {
        if (key == null) { return false; }
        cache.remove(key);
        return true;
    }

    @Override
    public boolean remove(K key, V oldValue) {
        if (key == null) { return false; }
        Object curValue = cache.get(key);
        boolean flag = !ObjectUtils.equals(curValue, oldValue);
        flag = flag || (curValue == null && !cache.containsKey(key));
        if (flag) { return false; }
        cache.remove(key);
        return true;
    }

    @Override
    public void remove(Collection<? extends K> keys) {
        if (CollectionUtils.isEmpty(keys)) {
            return;
        }
        for (K key : keys) {
            cache.remove(key);
        }
    }

    @Override
    public V removeAndGet(K key) {

        return cache.remove(key);
    }

    @Override
    public boolean containsKey(K key) {

        return cache.containsKey(key);
    }

    @Override
    public void clear() {

        cache.clear();
    }

    @Override
    public int size() {

        return cache.size();
    }

    @Override
    public void load(K key, boolean coverExisted) {
        if (cacheLoader == null) { return; }
        if (cache.containsKey(key) && !coverExisted) {
            return;
        }
        V val = cacheLoader.load(key);
        this.put(key, val);
    }

    @Override
    public void load(Iterable<? extends K> keys, boolean coverExisted) {
        if (cacheLoader == null) { return; }
        Map<K, V> loadMap = cacheLoader.loadAll(keys);
        for (Map.Entry<K, V> entry : loadMap.entrySet()) {
            if (entry == null) { continue; }
            K key = entry.getKey();
            V val = entry.getValue();
            if (cache.containsKey(key) && !coverExisted) {
                continue;
            }
            this.put(key, val);
        }
    }

    @Override
    public void refresh() {
        if (cacheLoader == null) {
            return;
        }
        if (MapUtils.isEmpty(cache)) {
            return;
        }
        Set<K> keySet = cache.keySet();
        Map<K, V> loadMap = cacheLoader.loadAll(keySet);
        cache.putAll(loadMap);
    }

    @Override
    public Object getOriginal() {

        return cache;
    }

}
