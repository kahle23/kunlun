package artoria.cache.support;

import artoria.cache.AbstractCache;
import artoria.exception.ExceptionUtils;
import artoria.util.Assert;
import artoria.util.ObjectUtils;

import java.util.Date;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import static artoria.common.Constants.MINUS_ONE;
import static artoria.common.Constants.ZERO;

/**
 * The abstract value wrapper cache.
 * @author Kahle
 */
public abstract class AbstractValueWrapperCache extends AbstractCache {

    /**
     * The default constructor.
     * @param name The cache name
     */
    public AbstractValueWrapperCache(String name) {

        super(name);
    }

    /**
     * Get the value stored in the cache.
     * @param key The specified key
     * @return The stored value
     */
    protected abstract ValueWrapper getStorageValue(Object key);

    /**
     * Put the specified key and value into the cache.
     * @param key The specified key
     * @param valueWrapper The specified value
     * @return The old value for this key, if present
     */
    protected abstract ValueWrapper putStorageValue(Object key, ValueWrapper valueWrapper);

    /**
     * Remove the value of the specified key.
     * @param key The specified key
     * @return The removed value
     */
    protected abstract ValueWrapper removeStorageValue(Object key);

    /**
     * Clear the expired elements.
     */
    protected abstract void clearExpired();

    /**
     * Calculate time to live.
     * @param timeToLive The amount of time for the element to live
     * @param timeToIdle The amount of time for the element to idle
     * @return The time to live
     */
    protected long calcTimeToLive(long timeToLive, long timeToIdle) {
        long result;
        if (timeToLive >= ZERO && timeToIdle >= ZERO) {
            result = Math.min(timeToLive, timeToIdle);
        }
        else if (timeToLive >= ZERO) {
            result = timeToLive;
        }
        else if (timeToIdle >= ZERO) {
            result = timeToIdle;
        }
        else { result = MINUS_ONE; }
        return result;
    }

    @Override
    public <T> T get(Object key, Callable<T> callable) {
        Assert.notNull(callable, "Parameter \"callable\" must not null. ");
        Assert.notNull(key, "Parameter \"key\" must not null. ");
        Object value = get(key);
        if (value != null) { return ObjectUtils.cast(value); }
        synchronized (String.valueOf(key).intern()) {
            // Try to get again.
            ValueWrapper valueWrapper = getStorageValue(key);
            if (valueWrapper != null) {
                value = valueWrapper.getValue();
                return ObjectUtils.cast(value);
            }
            // Try to call.
            try {
                value = callable.call();
            }
            catch (Exception e) {
                throw ExceptionUtils.wrap(e);
            }
            // Cache the result.
            if (value != null) {
                put(key, value);
            }
        }
        return ObjectUtils.cast(value);
    }

    @Override
    public Object get(Object key) {
        Assert.notNull(key, "Parameter \"key\" must not null. ");
        ValueWrapper valueWrapper = getStorageValue(key);
        return valueWrapper != null ? valueWrapper.getValue() : null;
    }

    @Override
    public Object put(Object key, Object value) {
        Assert.notNull(value, "Parameter \"value\" must not null. ");
        Assert.notNull(key, "Parameter \"key\" must not null. ");
        // Querying and reusing old values requires resetting the expiration time.
        // So it's a good idea to construct a new value and set it.
        ValueWrapper valueWrapper = putStorageValue(key, new ValueWrapper(key, value));
        Object preValue = valueWrapper != null ? valueWrapper.getValue() : null;
        if (isFull()) { prune(); }
        else { clearExpired(); }
        return preValue;
    }

    @Override
    public Object put(Object key, Object value, long timeToLive, TimeUnit timeUnit) {
        Assert.notNull(timeUnit, "Parameter \"timeUnit\" must not null. ");
        Assert.notNull(value, "Parameter \"value\" must not null. ");
        Assert.notNull(key, "Parameter \"key\" must not null. ");
        Assert.isTrue(timeToLive >= ZERO,
                "Parameter \"timeToLive\" must greater than or equal to 0. ");
        Object preValue = put(key, value);
        expire(key, timeToLive, timeUnit);
        return preValue;
    }

    @Override
    public Object putIfAbsent(Object key, Object value) {
        Assert.notNull(value, "Parameter \"value\" must not null. ");
        Assert.notNull(key, "Parameter \"key\" must not null. ");
        ValueWrapper nowValue = getStorageValue(key);
        if (nowValue == null) {
            return put(key, value);
        }
        return null;
    }

    @Override
    public Object putIfAbsent(Object key, Object value, long timeToLive, TimeUnit timeUnit) {
        Assert.notNull(timeUnit, "Parameter \"timeUnit\" must not null. ");
        Assert.notNull(value, "Parameter \"value\" must not null. ");
        Assert.notNull(key, "Parameter \"key\" must not null. ");
        Assert.isTrue(timeToLive >= ZERO,
                "Parameter \"timeToLive\" must greater than or equal to 0. ");
        ValueWrapper nowValue = getStorageValue(key);
        if (nowValue == null) {
            return put(key, value, timeToLive, timeUnit);
        }
        return null;
    }

    @Override
    public boolean expire(Object key, long timeToLive, TimeUnit timeUnit) {
        Assert.notNull(timeUnit, "Parameter \"timeUnit\" must not null. ");
        Assert.notNull(key, "Parameter \"key\" must not null. ");
        Assert.isTrue(timeToLive >= ZERO,
                "Parameter \"timeToLive\" must greater than or equal to 0. ");
        ValueWrapper valueWrapper = getStorageValue(key);
        if (valueWrapper == null) { return false; }
        valueWrapper.expire(timeUnit.toMillis(timeToLive));
        return true;
    }

    @Override
    public boolean expireAt(Object key, Date date) {
        Assert.notNull(date, "Parameter \"date\" must not null. ");
        Assert.notNull(key, "Parameter \"key\" must not null. ");
        long timeToLive = date.getTime() - currentTimeMillis();
        if (timeToLive <= ZERO) { timeToLive = ZERO; }
        return expire(key, timeToLive, TimeUnit.MILLISECONDS);
    }

    @Override
    public boolean persist(Object key) {
        Assert.notNull(key, "Parameter \"key\" must not null. ");
        ValueWrapper valueWrapper = getStorageValue(key);
        if (valueWrapper == null) { return false; }
        valueWrapper.expire(MINUS_ONE);
        return true;
    }

    @Override
    public Object remove(Object key) {
        Assert.notNull(key, "Parameter \"key\" must not null. ");
        ValueWrapper remove = removeStorageValue(key);
        clearExpired();
        return remove;
    }

    protected class ValueWrapper {
        /**
         * The cache key.
         */
        private final Object key;
        /**
         * The value.
         */
        private Object value;
        /**
         * The expiration time.
         */
        private volatile long expirationTime;
        /**
         * The last access time.
         */
        private volatile long lastAccessTime;
        /**
         * The number of times the entry was accessed.
         */
        private final AtomicLong accessCount = new AtomicLong();

        protected ValueWrapper(Object key, Object value) {
            Assert.notNull(key, "Parameter \"key\" must not null. ");
            this.lastAccessTime = currentTimeMillis();
            this.expirationTime = MINUS_ONE;
            this.value = value;
            this.key = key;
        }

        public long expirationTime() {

            return expirationTime;
        }

        public long lastAccessTime() {

            return lastAccessTime;
        }

        public long accessCount() {

            return accessCount.get();
        }

        public boolean isExpired() {
            if (expirationTime < ZERO) { return false; }
            return (currentTimeMillis() - expirationTime) >= ZERO;
        }

        public void expire(long timeToLive) {
            if (timeToLive < ZERO) { expirationTime = MINUS_ONE; }
            else { expirationTime = currentTimeMillis() + timeToLive; }
        }

        public Object getKey() {

            return key;
        }

        public Object getValue() {
            lastAccessTime = currentTimeMillis();
            accessCount.incrementAndGet();
            return value;
        }

        public void setValue(Object value) {

            this.value = value;
        }

    }

}
