package artoria.cache;

import artoria.collect.ReferenceMap;
import artoria.lang.ReferenceType;
import artoria.logging.Logger;
import artoria.logging.LoggerFactory;
import artoria.util.Assert;
import artoria.util.MapUtils;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static artoria.common.Constants.*;
import static artoria.lang.ReferenceType.SOFT;
import static artoria.lang.ReferenceType.WEAK;
import static java.util.Collections.emptyMap;
import static java.util.concurrent.TimeUnit.MILLISECONDS;

/**
 * The memory cache simple implement by jdk.
 * @author Kahle
 */
public class SimpleCache extends AbstractValueWrapperCache {
    /**
     * The log object.
     */
    private static Logger log = LoggerFactory.getLogger(SimpleCache.class);
    /**
     * The cached storage object.
     */
    protected final Map<Object, ValueWrapper> storage;
    /**
     * The amount of time for the element to idle, in millisecond. 0 indicates unlimited.
     */
    protected final Long timeToIdle;
    /**
     * The amount of time for the element to live, in millisecond. 0 indicates unlimited.
     */
    protected final Long timeToLive;
    /**
     * The cache capacity. 0 indicates unlimited.
     */
    protected final Long capacity;
    /**
     * The ratio that determines whether it is full, between zero and one.
     */
    protected final Float fullRatio;


    public SimpleCache(String name) {

        this(name, ZERO, ONE, MINUS_ONE, MINUS_ONE, ReferenceType.WEAK);
    }

    public SimpleCache(String name, ReferenceType referenceType) {

        this(name, ZERO, ONE, MINUS_ONE, MINUS_ONE, referenceType);
    }

    public SimpleCache(String name, long capacity, ReferenceType referenceType) {

        this(name, capacity, 0.8f, MINUS_ONE, MINUS_ONE, referenceType);
    }

    public SimpleCache(String name, long capacity, long timeToLive, ReferenceType referenceType) {

        this(name, capacity, 0.8f, timeToLive, MINUS_ONE, referenceType);
    }

    public SimpleCache(String name, long capacity, float fullRatio, long timeToLive, long timeToIdle, ReferenceType referenceType) {
        super(name);
        Assert.isFalse(timeToLive == ZERO, "Parameter \"timeToLive\" must not be equal to zero. ");
        Assert.isFalse(timeToIdle == ZERO, "Parameter \"timeToIdle\" must not be equal to zero. ");
        Assert.notNull(referenceType, "Parameter \"referenceType\" must not null. ");
        this.timeToLive = timeToLive < ZERO ? MINUS_ONE : timeToLive;
        this.timeToIdle = timeToIdle < ZERO ? MINUS_ONE : timeToIdle;
        this.fullRatio = fullRatio < ZERO || fullRatio > ONE ? 0.8f : fullRatio;
        this.capacity = capacity < ZERO ? ZERO : capacity;
        this.storage = buildStorage(referenceType);
    }

    protected Map<Object, ValueWrapper> buildStorage(ReferenceType referenceType) {
        Assert.isTrue(SOFT.equals(referenceType) || WEAK.equals(referenceType),
                "Parameter \"referenceType\" must be soft reference or weak reference. ");
        return new ReferenceMap<Object, ValueWrapper>(referenceType
                , new ConcurrentHashMap<Object, ReferenceMap.ValueCell<Object, ValueWrapper>>(THIRTY));
    }

    @Override
    protected boolean isFull() {

        return capacity > ZERO && size() >= (capacity * fullRatio);
    }

    @Override
    protected ValueWrapper getStorageValue(Object key) {
        ValueWrapper valueWrapper = storage.get(key);
        if (valueWrapper == null) { return null; }
        if (valueWrapper.isExpired()) {
            return storage.remove(key);
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
            log.warn("The cache named \"{}\" is still full after pruning. ", getName());
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
