package artoria.cache;

import artoria.collect.ReferenceMap;
import artoria.lang.ReferenceType;
import artoria.logging.Logger;
import artoria.logging.LoggerFactory;
import artoria.util.Assert;
import artoria.util.MapUtils;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.Lock;

import static artoria.common.Constants.*;
import static artoria.lang.ReferenceType.SOFT;
import static artoria.lang.ReferenceType.WEAK;
import static java.util.Collections.emptyMap;
import static java.util.concurrent.TimeUnit.MILLISECONDS;

/**
 * The memory cache simple implement by jdk.
 * @author Kahle
 */
public class SimpleCache extends AbstractReadWriteLockCache {
    /**
     * The log object.
     */
    private static Logger log = LoggerFactory.getLogger(SimpleCache.class);
    /**
     * The number of times the cache was missed.
     */
    private final AtomicLong missCount = new AtomicLong();
    /**
     * The number of times the cache was hit.
     */
    private final AtomicLong hitCount = new AtomicLong();
    /**
     * Cached storage object.
     */
    private final Map<Object, ValueWrapper> storage;
    /**
     * The amount of time for the element to idle, in millisecond. 0 indicates unlimited.
     */
    private final Long timeToIdle;
    /**
     * The amount of time for the element to live, in millisecond. 0 indicates unlimited.
     */
    private final Long timeToLive;
    /**
     * Cache capacity. 0 indicates unlimited.
     */
    private final Long capacity;
    /**
     * The ratio that determines whether it is full, between zero and one.
     */
    private final Float fullRatio;

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

    protected Map<Object, ValueWrapper> getStorage() {

        return storage;
    }

    protected Map<Object, ValueWrapper> buildStorage(ReferenceType referenceType) {
        Assert.isTrue(SOFT.equals(referenceType) || WEAK.equals(referenceType),
                "Parameter \"referenceType\" must be soft reference or weak reference. ");
        return new ReferenceMap<Object, ValueWrapper>(referenceType
                , new HashMap<Object, ReferenceMap.ValueCell<Object, ValueWrapper>>(THIRTY));
    }

    @Override
    protected ValueWrapper getStorageValue(Object key) {
        ValueWrapper valueWrapper = storage.get(key);
        if (valueWrapper == null) { return null; }
        if (valueWrapper.isExpired()) {
            // Old: Write locks cannot be used because read locks are in use.
            // Old: Upgrading read locks to write locks is not supported.
            // The deletion should not be done here.
            // If there is a simultaneous traversal operation,
            //      an "ConcurrentModificationException" error will occur.
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
                recordEviction(key, TWO);
            }
        }
    }

    @Override
    protected boolean isFull() {

        return capacity > ZERO && size() >= (capacity * fullRatio);
    }

    @Override
    protected void recordTouch(Object key, boolean touched) {
        (touched ? hitCount : missCount).incrementAndGet();
        if (getRecordLog()) {
            String content = NEWLINE +
                    "---- Begin Cache ----" + NEWLINE +
                    "Name:        " + getName() + NEWLINE +
                    "Key:         " + key + NEWLINE +
                    "Touched:     " + touched + NEWLINE +
                    "Hit Count:   " + hitCount + NEWLINE +
                    "Miss Count:  " + missCount + NEWLINE +
                    "Provider:    " + getClass().getName() + NEWLINE +
                    "---- End Cache ----" + NEWLINE;
            log.info(content);
        }
    }

    @Override
    public Object getNativeCache() {

        return storage;
    }

    @Override
    public Object get(Object key) {
        Assert.notNull(key, "Parameter \"key\" must not null. ");
        Lock readLock = getReadWriteLock().readLock();
        readLock.lock();
        try {
            ValueWrapper valueWrapper = getStorageValue(key);
            Object value = valueWrapper != null ? valueWrapper.getValue() : null;
            recordTouch(key, value != null);
            if (value != null && timeToIdle >= ZERO) {
                valueWrapper.expire(MILLISECONDS.toMillis(timeToIdle));
            }
            return value;
        }
        finally {
            readLock.unlock();
        }
    }

    @Override
    public boolean containsKey(Object key) {
        Assert.notNull(key, "Parameter \"key\" must not null. ");
        Lock readLock = getReadWriteLock().readLock();
        readLock.lock();
        try {
            boolean containsKey = storage.containsKey(key);
            recordTouch(key, containsKey);
            if (containsKey && timeToIdle >= ZERO) {
                expire(key, timeToIdle, MILLISECONDS);
            }
            return containsKey;
        }
        finally {
            readLock.unlock();
        }
    }

    @Override
    public long size() {

        return storage.size();
    }

    @Override
    public void clear() {
        Lock writeLock = getReadWriteLock().writeLock();
        writeLock.lock();
        try {
            storage.clear();
            hitCount.set(ZERO);
            missCount.set(ZERO);
        }
        finally {
            writeLock.unlock();
        }
    }

    @Override
    public long prune() {
        if (MapUtils.isEmpty(storage)) { return ZERO; }
        Lock writeLock = getReadWriteLock().writeLock();
        writeLock.lock();
        try {
            long count = ZERO;
            for (Map.Entry<Object, ValueWrapper> entry : storage.entrySet()) {
                ValueWrapper valueWrapper = entry.getValue();
                Object key = entry.getKey();
                if (valueWrapper == null) {
                    if (key != null) {
                        storage.remove(key);
                        recordEviction(key, TWO);
                    }
                    continue;
                }
                if (valueWrapper.isExpired()) {
                    storage.remove(key);
                    recordEviction(key, TWO);
                    count++;
                }
            }
            if (isFull()) {
                log.warn("The cache named \"{}\" is still full after pruning. ", getName());
            }
            return count;
        }
        finally {
            writeLock.unlock();
        }
    }

    @Override
    public Collection<Object> keys() {
        // "Collections.unmodifiableSet" is not secure because it still reads the original data.
        // If at the same time to delete, error "ConcurrentModificationException" will happen.
        Lock readLock = getReadWriteLock().readLock();
        readLock.lock();
        try {
            Map<Object, ValueWrapper> newMap = new LinkedHashMap<Object, ValueWrapper>(storage);
            return Collections.unmodifiableSet(newMap.keySet());
        }
        finally {
            readLock.unlock();
        }
    }

    @Override
    public Map<Object, Object> entries() {
        if (MapUtils.isEmpty(storage)) { return emptyMap(); }
        Lock readLock = getReadWriteLock().readLock();
        readLock.lock();
        try {
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
        finally {
            readLock.unlock();
        }
    }

}
