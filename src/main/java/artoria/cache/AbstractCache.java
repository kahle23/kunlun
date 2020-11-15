package artoria.cache;

import artoria.convert.type.TypeConvertUtils;
import artoria.exception.ExceptionUtils;
import artoria.logging.Logger;
import artoria.logging.LoggerFactory;
import artoria.util.Assert;
import artoria.util.CollectionUtils;
import artoria.util.MapUtils;
import artoria.util.ObjectUtils;

import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import static artoria.common.Constants.MINUS_ONE;
import static artoria.common.Constants.ZERO;

/**
 * Abstract memory cache implementation.
 * @author Kahle
 */
public abstract class AbstractCache implements Cache {
    /**
     * The log object.
     */
    private static Logger log = LoggerFactory.getLogger(AbstractCache.class);
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
     * Cache name.
     */
    private final String name;
    /**
     * Cache capacity. 0 indicates unlimited.
     */
    private final Long capacity;
    /**
     * Print log.
     */
    private Boolean printLog;

    public AbstractCache(String name, long capacity, Map<Object, ValueWrapper> storage) {
        Assert.notNull(storage, "Parameter \"storage\" must not null. ");
        Assert.notBlank(name, "Parameter \"name\" must not blank. ");
        this.capacity = capacity < ZERO ? ZERO : capacity;
        this.storage = storage;
        this.name = name;
        this.printLog = false;
    }

    public boolean getPrintLog() {

        return printLog;
    }

    public void setPrintLog(boolean printLog) {

        this.printLog = printLog;
    }

    protected Map<Object, ValueWrapper> getStorage() {

        return storage;
    }

    protected void hitStatistic(boolean hit) {
        if (printLog) {
            log.info("The size of the cache named \"{}\" is \"{}\"" +
                    " and its hit counts are \"{}\" and its miss counts are \"{}\". ", name, size(), hitCount, missCount);
        }
        (hit ? hitCount : missCount).incrementAndGet();
    }

    protected boolean isFull() {

        return capacity > ZERO && size() >= capacity;
    }

    @Override
    public String getName() {

        return name;
    }

    @Override
    public Object getNativeCache() {

        return storage;
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
        object = TypeConvertUtils.convert(object, type);
        return ObjectUtils.cast(object, type);
    }

    @Override
    public Object get(Object key) {
        Assert.notNull(key, "Parameter \"key\" must not null. ");
        ValueWrapper valueWrapper = storage.get(key);
        boolean notNull = valueWrapper != null;
        if (valueWrapper != null
                && valueWrapper.isExpired()) {
            storage.remove(key);
            notNull = false;
        }
        hitStatistic(notNull);
        return notNull ? valueWrapper.getValue() : null;
    }

    @Override
    public boolean containsKey(Object key) {
        Assert.notNull(key, "Parameter \"key\" must not null. ");
        boolean containsKey = storage.containsKey(key);
        hitStatistic(containsKey);
        return containsKey;
    }

    @Override
    public int size() {

        return storage.size();
    }

    @Override
    public Object put(Object key, Object value) {
        Assert.notNull(value, "Parameter \"value\" must not null. ");
        Assert.notNull(key, "Parameter \"key\" must not null. ");
        ValueWrapper valueWrapper = storage.get(key);
        Object preValue = null;
        if (valueWrapper != null && valueWrapper.isExpired()) {
            preValue = valueWrapper.getValue();
            storage.remove(key);
            valueWrapper = null;
        }
        if (valueWrapper != null) {
            valueWrapper.setValue(value);
        }
        else {
            if (isFull()) {
                prune();
                Assert.state(!isFull(), "The cache is still full after pruning. ");
            }
            valueWrapper = new ValueWrapper(key, value);
            storage.put(key, valueWrapper);
        }
        return preValue;
    }

    @Override
    public Object putIfAbsent(Object key, Object value) {

        throw new UnsupportedOperationException();
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
        ValueWrapper valueWrapper = storage.get(key);
        if (valueWrapper != null && valueWrapper.isExpired()) {
            storage.remove(key);
            valueWrapper = null;
        }
        if (valueWrapper == null) { return false; }
        if (timeToLive >= ZERO) {
            Assert.notNull(timeUnit, "Parameter \"timeUnit\" must not null. ");
            valueWrapper.expire(timeUnit.toMillis(timeToLive));
        }
        else { valueWrapper.expire(MINUS_ONE); }
        return true;
    }

    @Override
    public boolean expireAt(Object key, Date date) {
        Assert.notNull(date, "Parameter \"date\" must not null. ");
        Assert.notNull(key, "Parameter \"key\" must not null. ");
        return expire(key, date.getTime(), TimeUnit.MILLISECONDS);
    }

    @Override
    public boolean persist(Object key) {
        Assert.notNull(key, "Parameter \"key\" must not null. ");
        return expire(key, MINUS_ONE, null);
    }

    @Override
    public Object remove(Object key) {
        Assert.notNull(key, "Parameter \"key\" must not null. ");
        ValueWrapper remove = storage.remove(key);
        return remove != null ? remove.getValue() : null;
    }

    @Override
    public void removeAll(Collection<?> keys) {
        if (CollectionUtils.isEmpty(keys)) { return; }
        for (Object key : keys) {
            if (key == null) { continue; }
            storage.remove(key);
        }
    }

    @Override
    public void clear() {
        storage.clear();
        hitCount.set(ZERO);
        missCount.set(ZERO);
    }

    @Override
    public int prune() {
        if (MapUtils.isEmpty(storage)) { return ZERO; }
        List<Object> deleteList = new ArrayList<Object>();
        for (Map.Entry<Object, ValueWrapper> entry : storage.entrySet()) {
            ValueWrapper valueWrapper = entry.getValue();
            if (valueWrapper == null) { continue; }
            Object key = entry.getKey();
            if (valueWrapper.isExpired()) {
                deleteList.add(key);
            }
        }
        if (CollectionUtils.isNotEmpty(deleteList)) {
            for (Object key : deleteList) {
                if (key == null) { continue; }
                storage.remove(key);
            }
        }
        return deleteList.size();
    }

    @Override
    public Collection<Object> keys() {

        return storage.keySet();
    }

    @Override
    public Map<Object, Object> entries() {
        Map<Object, Object> result = new HashMap<Object, Object>(storage.size());
        if (MapUtils.isEmpty(storage)) {
            return Collections.unmodifiableMap(result);
        }
        for (Map.Entry<Object, ValueWrapper> entry : storage.entrySet()) {
            ValueWrapper val = entry.getValue();
            Object key = entry.getKey();
            if (key == null || val == null) { continue; }
            result.put(key, val.getValue());
        }
        return Collections.unmodifiableMap(result);
    }

    protected static class ValueWrapper {
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
            Assert.notNull(value, "Parameter \"value\" must not null. ");
            Assert.notNull(key, "Parameter \"key\" must not null. ");
            this.lastAccessTime = System.currentTimeMillis();
            this.expirationTime = MINUS_ONE;
            this.value = value;
            this.key = key;
        }

        public Object getKey() {

            return key;
        }

        public Object getValue() {
            lastAccessTime = System.currentTimeMillis();
            accessCount.incrementAndGet();
            return value;
        }

        public void setValue(Object value) {

            this.value = value;
        }

        public void expire(long timeToLive) {
            if (timeToLive < ZERO) { expirationTime = MINUS_ONE; }
            else {
                expirationTime = System.currentTimeMillis() + timeToLive;
            }
        }

        public boolean isExpired() {
            if (expirationTime < ZERO) { return false; }
            long currentTimeMillis = System.currentTimeMillis();
            return (currentTimeMillis - expirationTime) >= ZERO;
        }

        public long accessCount() {

            return accessCount.get();
        }

        public long lastAccessTime() {

            return lastAccessTime;
        }

    }

}
