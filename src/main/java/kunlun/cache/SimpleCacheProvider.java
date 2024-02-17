/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.cache;

import kunlun.cache.support.SimpleCache;
import kunlun.logging.Logger;
import kunlun.logging.LoggerFactory;
import kunlun.util.Assert;
import kunlun.util.MapUtils;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import static kunlun.common.constant.Words.DEFAULT;

/**
 * The simple cache provider.
 * @author Kahle
 */
public class SimpleCacheProvider implements CacheProvider {
    private static final Logger log = LoggerFactory.getLogger(SimpleCacheProvider.class);
    protected final Map<String, Object> commonProperties;
    protected final Map<String, Cache> caches;

    protected SimpleCacheProvider(Map<String, Object> commonProperties,
                                  Map<String, Cache> caches) {
        Assert.notNull(commonProperties, "Parameter \"commonProperties\" must not null. ");
        Assert.notNull(caches, "Parameter \"caches\" must not null. ");
        this.commonProperties = commonProperties;
        this.caches = caches;
        // Register the default cache.
        registerCache(DEFAULT, new SimpleCache());
    }

    public SimpleCacheProvider() {
        this(new ConcurrentHashMap<String, Object>(),
                new ConcurrentHashMap<String, Cache>());
    }

    @Override
    public void registerCommonProperties(Map<?, ?> commonProperties) {
        if (MapUtils.isEmpty(commonProperties)) { return; }
        for (Map.Entry<?, ?> entry : commonProperties.entrySet()) {
            String keyStr = String.valueOf(entry.getKey());
            this.commonProperties.put(keyStr, entry.getValue());
        }
    }

    @Override
    public void clearCommonProperties() {

        this.commonProperties.clear();
    }

    @Override
    public Map<String, Object> getCommonProperties() {

        return Collections.unmodifiableMap(commonProperties);
    }

    @Override
    public void registerCache(String cacheName, Cache cache) {
        Assert.notBlank(cacheName, "Parameter \"cacheName\" must not blank. ");
        Assert.notNull(cache, "Parameter \"cache\" must not null. ");
        String className = cache.getClass().getName();
        caches.put(cacheName, cache);
        log.info("Register the cache \"{}\" to \"{}\". ", className, cacheName);
    }

    @Override
    public void deregisterCache(String cacheName) {
        Assert.notBlank(cacheName, "Parameter \"cacheName\" must not blank. ");
        Cache remove = caches.remove(cacheName);
        if (remove != null) {
            String className = remove.getClass().getName();
            log.info("Deregister the cache \"{}\" from \"{}\". ", className, cacheName);
        }
    }

    @Override
    public Cache getCache(String cacheName) {
        Assert.notBlank(cacheName, "Parameter \"cacheName\" must not blank. ");
        Cache cache = caches.get(cacheName);
        Assert.notNull(cache
                , "The corresponding cache could not be found by name. ");
        return cache;
    }

    @Override
    public Collection<String> getCacheNames() {

        return Collections.unmodifiableSet(caches.keySet());
    }

    @Override
    public <T> T get(String cacheName, Object key, Callable<T> callable) {

        return getCache(cacheName).get(key, callable);
    }

    @Override
    public <T> T get(String cacheName, Object key, Class<T> type) {

        return getCache(cacheName).get(key, type);
    }

    @Override
    public Object get(String cacheName, Object key) {

        return getCache(cacheName).get(key);
    }

    @Override
    public boolean containsKey(String cacheName, Object key) {

        return getCache(cacheName).containsKey(key);
    }

    @Override
    public long size(String cacheName) {

        return getCache(cacheName).size();
    }

    @Override
    public Object put(String cacheName, Object key, Object value) {

        return getCache(cacheName).put(key, value);
    }

    @Override
    public Object put(String cacheName, Object key, Object value, long timeToLive, TimeUnit timeUnit) {

        return getCache(cacheName).put(key, value, timeToLive, timeUnit);
    }

    @Override
    public Object putIfAbsent(String cacheName, Object key, Object value) {

        return getCache(cacheName).putIfAbsent(key, value);
    }

    @Override
    public Object putIfAbsent(String cacheName, Object key, Object value, long timeToLive, TimeUnit timeUnit) {

        return getCache(cacheName).putIfAbsent(key, value, timeToLive, timeUnit);
    }

    @Override
    public void putAll(String cacheName, Map<?, ?> map) {

        getCache(cacheName).putAll(map);
    }

    @Override
    public boolean expire(String cacheName, Object key, long timeToLive, TimeUnit timeUnit) {

        return getCache(cacheName).expire(key, timeToLive, timeUnit);
    }

    @Override
    public boolean expireAt(String cacheName, Object key, Date date) {

        return getCache(cacheName).expireAt(key, date);
    }

    @Override
    public boolean persist(String cacheName, Object key) {

        return getCache(cacheName).persist(key);
    }

    @Override
    public Object remove(String cacheName, Object key) {

        return getCache(cacheName).remove(key);
    }

    @Override
    public void removeAll(String cacheName, Collection<?> keys) {

        getCache(cacheName).removeAll(keys);
    }

    @Override
    public void clear(String cacheName) {

        getCache(cacheName).clear();
    }

    @Override
    public long prune(String cacheName) {

        return getCache(cacheName).prune();
    }

    @Override
    public Collection<Object> keys(String cacheName) {

        return getCache(cacheName).keys();
    }

    @Override
    public Map<Object, Object> entries(String cacheName) {

        return getCache(cacheName).entries();
    }

}
