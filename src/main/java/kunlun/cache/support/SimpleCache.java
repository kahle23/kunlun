/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.cache.support;

import kunlun.data.ReferenceType;
import kunlun.data.collect.ReferenceMap;
import kunlun.logging.Logger;
import kunlun.logging.LoggerFactory;
import kunlun.util.Assert;
import kunlun.util.MapUtils;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import static java.util.Collections.emptyMap;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static kunlun.common.constant.Numbers.*;
import static kunlun.data.ReferenceType.SOFT;
import static kunlun.data.ReferenceType.WEAK;

/**
 * The memory cache simple implement by jdk.
 * @author Kahle
 */
public class SimpleCache extends AbstractValueWrapperCache {
    /**
     * The log object.
     */
    private static final Logger log = LoggerFactory.getLogger(SimpleCache.class);
    /**
     * The cached storage object.
     */
    protected final Map<Object, ValueWrapper> storage;
    /**
     * The amount of time for the element to idle, in millisecond. -1 indicates unlimited.
     */
    protected final Long timeToIdle;
    /**
     * The amount of time for the element to live, in millisecond. -1 indicates unlimited.
     */
    protected final Long timeToLive;
    /**
     * The cache capacity. -1 indicates unlimited.
     */
    protected final Long capacity;
    /**
     * The ratio that determines whether it is full, between zero and one.
     */
    protected final Float fullRatio;

//    public SimpleCache(String name) {
//
//        this(name, MINUS_ONE, ONE, MINUS_ONE, MINUS_ONE, ReferenceType.WEAK);
//    }

    @Deprecated
    public SimpleCache(String name, ReferenceType referenceType) {

        this(name, MINUS_ONE, ONE, MINUS_ONE, MINUS_ONE, referenceType);
    }

    @Deprecated
    public SimpleCache(String name, long capacity, ReferenceType referenceType) {

        this(name, capacity, 0.8f, MINUS_ONE, MINUS_ONE, referenceType);
    }

    @Deprecated
    public SimpleCache(String name, long capacity, long timeToLive, ReferenceType referenceType) {

        this(name, capacity, 0.8f, timeToLive, MINUS_ONE, referenceType);
    }

    @Deprecated
    public SimpleCache(String name, long capacity, float fullRatio, long timeToLive, long timeToIdle, ReferenceType referenceType) {
//        super(name);
        Assert.isFalse(timeToLive == ZERO, "Parameter \"timeToLive\" must not be equal to zero. ");
        Assert.isFalse(timeToIdle == ZERO, "Parameter \"timeToIdle\" must not be equal to zero. ");
        Assert.notNull(referenceType, "Parameter \"referenceType\" must not null. ");
        this.timeToLive = timeToLive < ZERO ? MINUS_ONE : timeToLive;
        this.timeToIdle = timeToIdle < ZERO ? MINUS_ONE : timeToIdle;
        this.fullRatio = fullRatio < ZERO || fullRatio > ONE ? 0.8f : fullRatio;
        this.capacity = capacity < ZERO ? MINUS_ONE : capacity;
        this.storage = buildStorage(referenceType);
    }

    public SimpleCache() {

        this(new SimpleCacheConfig());
    }

    public SimpleCache(SimpleCacheConfig cacheConfig) {
        // Validate the cache config.
        Assert.notNull(cacheConfig, "Parameter \"cacheConfig\" must not null. ");
        // Process the capacity and the full ratio.
        Float fullRatio = cacheConfig.getFullRatio();
        Long capacity = cacheConfig.getCapacity();
        this.fullRatio = fullRatio == null || fullRatio < ZERO || fullRatio > ONE ? 0.95f : fullRatio;
        this.capacity = capacity == null || capacity < ZERO ? -1L : capacity;
        // Process the timeToLive and the timeToLiveUnit.
        TimeUnit timeToLiveUnit = cacheConfig.getTimeToLiveUnit();
        Long timeToLive = cacheConfig.getTimeToLive();
        if (timeToLive != null) {
            Assert.notNull(timeToLiveUnit, "Parameter \"timeToLiveUnit\" must not null. ");
            Assert.isFalse(timeToLive == ZERO
                    , "Parameter \"timeToLive\" must not be equal to zero. ");
            this.timeToLive = timeToLive < ZERO ? -1L : timeToLiveUnit.toMillis(timeToLive);
        }
        else { this.timeToLive = -1L; }
        // Process the timeToIdle and the timeToIdleUnit.
        TimeUnit timeToIdleUnit = cacheConfig.getTimeToIdleUnit();
        Long timeToIdle = cacheConfig.getTimeToIdle();
        if (timeToIdle != null) {
            Assert.notNull(timeToIdleUnit, "Parameter \"timeToIdleUnit\" must not null. ");
            Assert.isFalse(timeToIdle == ZERO
                    , "Parameter \"timeToIdle\" must not be equal to zero. ");
            this.timeToIdle = timeToIdle < ZERO ? -1L : timeToIdleUnit.toMillis(timeToIdle);
        }
        else { this.timeToIdle = -1L; }
        // Process the reference type (default weak).
        ReferenceType referenceType = cacheConfig.getReferenceType();
        if (referenceType == null) { referenceType = ReferenceType.WEAK; }
        this.storage = buildStorage(referenceType);
    }

    protected Map<Object, ValueWrapper> buildStorage(ReferenceType referenceType) {
        Assert.isTrue(SOFT.equals(referenceType) || WEAK.equals(referenceType),
            "Parameter \"referenceType\" must be only soft reference or weak reference. ");
        return new ReferenceMap<Object, ValueWrapper>(referenceType
                , new ConcurrentHashMap<Object, ReferenceMap.ValueCell<Object, ValueWrapper>>(THIRTY));
    }

    @Override
    protected boolean isFull() {
        // If the fullRatio is 0, it is always full.
        // If the capacity is 0, it is always full.
        return capacity >= ZERO && size() >= (capacity * fullRatio);
    }

    @Override
    protected ValueWrapper getStorageValue(Object key) {
        ValueWrapper valueWrapper = storage.get(key);
        if (valueWrapper == null) { return null; }
        if (valueWrapper.isExpired()) {
            storage.remove(key);
            return null;
        }
        return valueWrapper;
    }

    @Override
    protected ValueWrapper putStorageValue(Object key, ValueWrapper valueWrapper) {
        long timeToLive = calcTimeToLive(this.timeToLive, this.timeToIdle);
        // Since "put()" is called first, then "expire()" is called.
        // There is no need to compare the old expiration time.
        // And logically avoid setting the expiration time before saving the value.
        if (timeToLive >= ZERO) { valueWrapper.expire(timeToLive); }
        return storage.put(key, valueWrapper);
    }

    @Override
    protected ValueWrapper removeStorageValue(Object key) {

        return storage.remove(key);
    }

    @Override
    protected void clearExpired() {
        for (Map.Entry<Object, ValueWrapper> entry : storage.entrySet()) {
            ValueWrapper valueWrapper = entry.getValue();
            Object key = entry.getKey();
            if (valueWrapper == null) {
                if (key != null) {
                    storage.remove(key);
                }
                continue;
            }
            if (valueWrapper.isExpired()) {
                storage.remove(key);
            }
        }
    }

    @Override
    public Map<Object, ValueWrapper> getNative() {

        return storage;
    }

    @Override
    public Object get(Object key) {
        Assert.notNull(key, "Parameter \"key\" must not null. ");
        ValueWrapper valueWrapper = getStorageValue(key);
        Object value = valueWrapper != null ? valueWrapper.getValue() : null;
        if (value != null && timeToIdle >= ZERO) {
            valueWrapper.expire(MILLISECONDS.toMillis(timeToIdle));
        }
        return value;
    }

    @Override
    public boolean containsKey(Object key) {
        Assert.notNull(key, "Parameter \"key\" must not null. ");
        boolean containsKey = storage.containsKey(key);
        if (containsKey && timeToIdle >= ZERO) {
            expire(key, timeToIdle, MILLISECONDS);
        }
        return containsKey;
    }

    @Override
    public long size() {

        return storage.size();
    }

    @Override
    public void clear() {

        storage.clear();
    }

    @Override
    public long prune() {
        if (MapUtils.isEmpty(storage)) { return ZERO; }
        long count = ZERO;
        for (Map.Entry<Object, ValueWrapper> entry : storage.entrySet()) {
            ValueWrapper valueWrapper = entry.getValue();
            Object key = entry.getKey();
            if (valueWrapper == null) {
                if (key != null) {
                    storage.remove(key);
                }
                continue;
            }
            if (valueWrapper.isExpired()) {
                storage.remove(key);
                count++;
            }
        }
        if (isFull()) {
            log.warn("The cache \"{}\" is still full after pruning. ", this.toString());
        }
        return count;
    }

    @Override
    public Collection<Object> keys() {

        return Collections.unmodifiableSet(storage.keySet());
    }

    @Override
    public Map<Object, Object> entries() {
        if (MapUtils.isEmpty(storage)) { return emptyMap(); }
        Map<Object, Object> result = new HashMap<Object, Object>(storage.size());
        for (Map.Entry<Object, ValueWrapper> entry : storage.entrySet()) {
            ValueWrapper val = entry.getValue();
            Object key = entry.getKey();
            if (key == null || val == null) { continue; }
            if (val.isExpired()) { continue; }
            result.put(key, val.getValue());
        }
        return Collections.unmodifiableMap(result);
    }

}
