package artoria.cache;

import artoria.logging.Logger;
import artoria.logging.LoggerFactory;
import artoria.util.Assert;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import static artoria.common.Constants.*;

/**
 * Abstract value wrapper cache.
 * @author Kahle
 */
public abstract class AbstractValueWrapperCache extends AbstractCache {
    /**
     * The log object.
     */
    private static Logger log = LoggerFactory.getLogger(AbstractValueWrapperCache.class);

    public AbstractValueWrapperCache(String name) {

        super(name);
    }

    /**
     * Get the value of the specified type stored in the cache.
     * @param key The specified key
     * @return The stored value
     */
    protected abstract ValueWrapper getValueWrapper(Object key);

    /**
     * Put the specified key and value into the cache.
     * @param key The specified key
     * @param valueWrapper The specified value
     * @return The old value for this key, if present
     */
    protected abstract ValueWrapper putValueWrapper(Object key, ValueWrapper valueWrapper);

    /**
     * Remove the value of the specified key.
     * @param key The specified key
     * @return The removed value
     */
    protected abstract ValueWrapper removeValueWrapper(Object key);

    @Override
    protected Object getStorageValue(Object key) {
        ValueWrapper valueWrapper = getValueWrapper(key);
        boolean notNull = valueWrapper != null;
        if (notNull && valueWrapper.isExpired()) {
            removeValueWrapper(key);
            recordEviction(key, TWO);
            notNull = false;
        }
        return notNull ? valueWrapper.getValue() : null;
    }

    @Override
    protected Object putStorageValue(Object key, Object value) {
        // Querying and reusing old values requires resetting the expiration time.
        // So it's a good idea to construct a new value and set it.
        ValueWrapper valueWrapper = putValueWrapper(key, new ValueWrapper(key, value));
        return valueWrapper != null ? valueWrapper.getValue() : null;
    }

    @Override
    protected Object removeStorageValue(Object key) {
        ValueWrapper valueWrapper = removeValueWrapper(key);
        return valueWrapper != null ? valueWrapper.getValue() : null;
    }

    @Override
    public boolean expire(Object key, long timeToLive, TimeUnit timeUnit) {
        Assert.notNull(key, "Parameter \"key\" must not null. ");
        if (timeToLive >= ZERO) {
            Assert.notNull(timeUnit, "Parameter \"timeUnit\" must not null. ");
        }
        if (getStorageValue(key) == null) { return false; }
        ValueWrapper valueWrapper = getValueWrapper(key);
        if (timeToLive >= ZERO) {
            valueWrapper.expire(timeUnit.toMillis(timeToLive));
        }
        else { valueWrapper.expire(MINUS_ONE); }
        return true;
    }

    @Override
    public boolean persist(Object key) {
        Assert.notNull(key, "Parameter \"key\" must not null. ");
        return expire(key, MINUS_ONE, TimeUnit.MILLISECONDS);
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
