package artoria.cache;

import artoria.lifecycle.Destroyable;
import artoria.lifecycle.LifecycleUtils;
import artoria.util.Assert;
import artoria.util.MapUtils;
import artoria.util.StringUtils;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Cache manager simple implement by jdk.
 * @author Kahle
 */
public class SimpleCacheManager implements CacheManager, Destroyable {
    protected final Map<String, Cache> manager = new ConcurrentHashMap<String, Cache>();

    @Override
    public <K, V> Cache<K, V> getCache(String name) {

        return this.getCache(name, null, null);
    }

    @Override
    public <K, V> Cache<K, V> getCache(String name, Class<K> keyType, Class<V> valueType) {
        Assert.notBlank(name, "Parameter \"name\" must not blank. ");
        @SuppressWarnings("unchecked")
        Cache<K, V> cache = manager.get(name);
        return cache;
    }

    @Override
    public <K, V> Cache<K, V> createCache(String name) {

        return this.createCache(name, null);
    }

    @Override
    public <K, V> Cache<K, V> createCache(String name, Configuration<K, V> configuration) {
        Assert.notBlank(name, "Parameter \"name\" must not blank. ");
        @SuppressWarnings("unchecked")
        Cache<K, V> cache = manager.get(name);
        Assert.isNull(cache, "Parameter \"name\" already exist in this cache manager. ");
        cache = new SimpleCache<K, V>(name, configuration);
        LifecycleUtils.initialize(cache);
        manager.put(name, cache);
        return cache;
    }

    @Override
    public void destroyCache(String name) {
        if (StringUtils.isBlank(name)) {
            return;
        }
        Cache cache = manager.remove(name);
        if (cache == null) { return; }
        LifecycleUtils.destroy(cache);
    }

    @Override
    public String[] getCacheNames() {
        Set<String> keySet = manager.keySet();
        return keySet.toArray(new String[0]);
    }

    @Override
    public void destroy() throws Exception {
        if (MapUtils.isEmpty(manager)) {
            return;
        }
        for (Map.Entry<String, Cache> entry : manager.entrySet()) {
            Cache value = entry.getValue();
            LifecycleUtils.destroy(value);
        }
        manager.clear();
    }

}
