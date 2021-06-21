package artoria.cache;

import artoria.convert.ConversionUtils;
import artoria.exception.ExceptionUtils;
import artoria.logging.Logger;
import artoria.logging.LoggerFactory;
import artoria.util.Assert;
import artoria.util.CollectionUtils;
import artoria.util.MapUtils;
import artoria.util.ObjectUtils;

import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import static artoria.common.Constants.*;

/**
 * Abstract cache.
 * @author Kahle
 */
public abstract class AbstractCache implements Cache {
    /**
     * The log object.
     */
    private static Logger log = LoggerFactory.getLogger(AbstractCache.class);
    /**
     * Cache name.
     */
    private final String name;
    /**
     * Print log.
     */
    private Boolean printLog;

    public AbstractCache(String name, Boolean printLog) {
        Assert.notBlank(name, "Parameter \"name\" must not blank. ");
        if (printLog == null) { printLog = false; }
        this.printLog = printLog;
        this.name = name;
    }

    public Boolean getPrintLog() {

        return printLog;
    }

    public void setPrintLog(Boolean printLog) {

        this.printLog = printLog;
    }

    protected abstract ValueWrapper getValueWrapper(Object key);

    protected abstract ValueWrapper putValueWrapper(Object key, ValueWrapper valueWrapper);

    protected abstract ValueWrapper removeValueWrapper(Object key);

    protected boolean isFull() {

        return false;
    }

    protected long currentTimeMillis() {

        return System.currentTimeMillis();
    }

    protected void recordTouch(Object key, boolean touched) {
        if (!printLog) { return; }
        String content = NEWLINE +
                "---- Begin Cache ----" + NEWLINE +
                "Name:        " + getName() + NEWLINE +
                "Key:         " + key + NEWLINE +
                "Touched:     " + touched + NEWLINE +
                "---- End Cache ----" + NEWLINE;
        log.info(content);
    }

    protected void recordEviction(Object key, int reasonCode) {
        if (!printLog) { return; }
        String reason = reasonCode == ONE ? "removed" :
                reasonCode == TWO ? "expired" : EMPTY_STRING;
        String content = NEWLINE +
                "---- Begin Cache ----" + NEWLINE +
                "Name:        " + getName() + NEWLINE +
                "Key:         " + key + NEWLINE +
                "Eviction:    " + reason + NEWLINE +
                "---- End Cache ----" + NEWLINE;
        log.info(content);
    }

    @Override
    public String getName() {

        return name;
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
    public <T> T get(Object key, Class<T> type) {
        Assert.notNull(key, "Parameter \"key\" must not null. ");
        Assert.notNull(type, "Parameter \"type\" must not null. ");
        Object object = get(key);
        if (object == null) { return null; }
        object = ConversionUtils.convert(object, type);
        return ObjectUtils.cast(object, type);
    }

    @Override
    public Object get(Object key) {
        Assert.notNull(key, "Parameter \"key\" must not null. ");
        ValueWrapper valueWrapper = getValueWrapper(key);
        boolean notNull = valueWrapper != null;
        if (notNull && valueWrapper.isExpired()) {
            removeValueWrapper(key);
            recordEviction(key, TWO);
            notNull = false;
        }
        Object value = notNull ? valueWrapper.getValue() : null;
        recordTouch(key, value != null);
        return value;
    }

    @Override
    public boolean containsKey(Object key) {
        Assert.notNull(key, "Parameter \"key\" must not null. ");
        return get(key) != null;
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
        ValueWrapper valueWrapper = getValueWrapper(key);
        Object preValue = null;
        if (valueWrapper != null && valueWrapper.isExpired()) {
            preValue = valueWrapper.getValue();
            removeValueWrapper(key);
            recordEviction(key, TWO);
            valueWrapper = null;
        }
        if (valueWrapper != null) {
            valueWrapper.setValue(value);
        }
        else {
            valueWrapper = new ValueWrapper(key, value);
        }
        if (timeToLive >= ZERO) {
            valueWrapper.expire(timeUnit.toMillis(timeToLive));
        }
        else { valueWrapper.expire(MINUS_ONE); }
        putValueWrapper(key, valueWrapper);
        if (isFull()) { prune(); }
        return preValue;
    }

    @Override
    public Object putIfAbsent(Object key, Object value) {
        ValueWrapper valueWrapper = getValueWrapper(key);
        if (valueWrapper == null ||
                valueWrapper.getValue() == null) {
            return put(key, value);
        }
        return null;
    }

    @Override
    public Object putIfAbsent(Object key, Object value, long timeToLive, TimeUnit timeUnit) {
        ValueWrapper valueWrapper = getValueWrapper(key);
        if (valueWrapper == null ||
                valueWrapper.getValue() == null) {
            return put(key, value, timeToLive, timeUnit);
        }
        return null;
    }

    @Override
    public void putAll(Map<?, ?> map) {
        Assert.notNull(map, "Parameter \"map\" must not null. ");
        if (MapUtils.isEmpty(map)) { return; }
        for (Map.Entry<?, ?> entry : map.entrySet()) {
            put(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public boolean expire(Object key, long timeToLive, TimeUnit timeUnit) {
        Assert.notNull(key, "Parameter \"key\" must not null. ");
        if (timeToLive >= ZERO && timeUnit == null) {
            throw new IllegalArgumentException("Parameter \"timeUnit\" must not null. ");
        }
        ValueWrapper valueWrapper = getValueWrapper(key);
        if (valueWrapper != null && valueWrapper.isExpired()) {
            removeValueWrapper(key);
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
            ValueWrapper valueWrapper = getValueWrapper(key);
            if (valueWrapper != null) {
                removeValueWrapper(key);
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
        ValueWrapper remove = removeValueWrapper(key);
        recordEviction(key, ONE);
        return remove != null ? remove.getValue() : null;
    }

    @Override
    public void removeAll(Collection<?> keys) {
        if (CollectionUtils.isEmpty(keys)) { return; }
        for (Object key : keys) {
            if (key == null) { continue; }
            removeValueWrapper(key);
            recordEviction(key, ONE);
        }
    }

    @Override
    public Collection<Object> keys() {

        return entries().keySet();
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
