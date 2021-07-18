package artoria.cache;

import artoria.exception.ExceptionUtils;
import artoria.logging.Logger;
import artoria.logging.LoggerFactory;
import artoria.util.Assert;
import artoria.util.CollectionUtils;
import artoria.util.ObjectUtils;

import java.util.Collection;
import java.util.Date;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import static artoria.common.Constants.*;

/**
 * Abstract cache.
 * @author Kahle
 */
public abstract class AbstractValueWrapperCache extends AbstractCache {
    /**
     * The log object.
     */
    private static Logger log = LoggerFactory.getLogger(AbstractValueWrapperCache.class);

    public AbstractValueWrapperCache(String name, Boolean printLog) {

        super(name, printLog);
    }

    @Override
    public <T> T get(Object key, Callable<T> callable) {
        Assert.notNull(callable, "Parameter \"callable\" must not null. ");
        Assert.notNull(key, "Parameter \"key\" must not null. ");
        Object object = get(key);
        if (object != null) {
            return ObjectUtils.cast(object);
        }
        synchronized (String.valueOf(key).intern()) {
            if ((object = get(key)) != null) {
                return ObjectUtils.cast(object);
            }
            try {
                object = callable.call();
            }
            catch (Exception e) {
                throw ExceptionUtils.wrap(e);
            }
            if (object != null) {
                put(key, object);
            }
        }
        return ObjectUtils.cast(object);
    }

    @Override
    public Object get(Object key) {
        Assert.notNull(key, "Parameter \"key\" must not null. ");
        ValueWrapper valueWrapper = (ValueWrapper) getStorageValue(key);
        boolean notNull = valueWrapper != null;
        if (notNull && valueWrapper.isExpired()) {
            removeStorageValue(key);
            recordEviction(key, TWO);
            notNull = false;
        }
        Object value = notNull ? valueWrapper.getValue() : null;
        recordTouch(key, value != null);
        return value;
    }

    @Override
    public Object put(Object key, Object value) {

        return put(key, value, MINUS_ONE, null);
    }

    @Override
    public Object put(Object key, Object value, long timeToLive, TimeUnit timeUnit) {
        Assert.notNull(value, "Parameter \"value\" must not null. ");
        Assert.notNull(key, "Parameter \"key\" must not null. ");
        if (timeToLive >= ZERO && timeUnit == null) {
            throw new IllegalArgumentException("Parameter \"timeUnit\" must not null. ");
        }
        ValueWrapper valueWrapper = (ValueWrapper) getStorageValue(key);
        Object preValue = null;
        if (valueWrapper != null && valueWrapper.isExpired()) {
            preValue = valueWrapper.getValue();
            removeStorageValue(key);
            recordEviction(key, TWO);
            valueWrapper = null;
        }
        if (valueWrapper != null) {
            valueWrapper.setValue(value);
        }
        else {
            valueWrapper = new ValueWrapper(key, value);
        }
        putStorageValue(key, valueWrapper);
        if (timeToLive >= ZERO) {
            valueWrapper.expire(timeUnit.toMillis(timeToLive));
        }
        else { valueWrapper.expire(MINUS_ONE); }
        if (isFull()) { prune(); }
        return preValue;
    }

    @Override
    public Object putIfAbsent(Object key, Object value) {
        ValueWrapper valueWrapper = (ValueWrapper) getStorageValue(key);
        if (valueWrapper == null ||
                valueWrapper.getValue() == null) {
            return put(key, value);
        }
        return null;
    }

    @Override
    public Object putIfAbsent(Object key, Object value, long timeToLive, TimeUnit timeUnit) {
        ValueWrapper valueWrapper = (ValueWrapper) getStorageValue(key);
        if (valueWrapper == null ||
                valueWrapper.getValue() == null) {
            return put(key, value, timeToLive, timeUnit);
        }
        return null;
    }

    @Override
    public boolean expire(Object key, long timeToLive, TimeUnit timeUnit) {
        Assert.notNull(key, "Parameter \"key\" must not null. ");
        if (timeToLive >= ZERO && timeUnit == null) {
            throw new IllegalArgumentException("Parameter \"timeUnit\" must not null. ");
        }
        ValueWrapper valueWrapper = (ValueWrapper) getStorageValue(key);
        if (valueWrapper != null && valueWrapper.isExpired()) {
            removeStorageValue(key);
            recordEviction(key, TWO);
            valueWrapper = null;
        }
        if (valueWrapper == null) { return false; }
        if (timeToLive >= ZERO) {
            valueWrapper.expire(timeUnit.toMillis(timeToLive));
        }
        else { valueWrapper.expire(MINUS_ONE); }
        return true;
    }

    @Override
    public boolean expireAt(Object key, Date date) {
        Assert.notNull(date, "Parameter \"date\" must not null. ");
        Assert.notNull(key, "Parameter \"key\" must not null. ");
        long timeToLive = date.getTime() - currentTimeMillis();
        if (timeToLive <= ZERO) {
            ValueWrapper valueWrapper = (ValueWrapper) getStorageValue(key);
            if (valueWrapper != null) {
                removeStorageValue(key);
                recordEviction(key, TWO);
            }
            return true;
        }
        else { return expire(key, timeToLive, TimeUnit.MILLISECONDS); }
    }

    @Override
    public boolean persist(Object key) {
        Assert.notNull(key, "Parameter \"key\" must not null. ");
        return expire(key, MINUS_ONE, null);
    }

    @Override
    public Object remove(Object key) {
        Assert.notNull(key, "Parameter \"key\" must not null. ");
        ValueWrapper remove = (ValueWrapper) removeStorageValue(key);
        recordEviction(key, ONE);
        return remove != null ? remove.getValue() : null;
    }

    @Override
    public void removeAll(Collection<?> keys) {
        if (CollectionUtils.isEmpty(keys)) { return; }
        for (Object key : keys) {
            if (key == null) { continue; }
            removeStorageValue(key);
            recordEviction(key, ONE);
        }
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

        public ValueWrapper(Object key, Object value) {
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
