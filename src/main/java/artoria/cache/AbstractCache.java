package artoria.cache;

import artoria.exception.ExceptionUtils;
import artoria.lock.LockUtils;
import artoria.util.Assert;
import artoria.util.CollectionUtils;
import artoria.util.MapUtils;
import artoria.util.ObjectUtils;

import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

/**
 * The abstract cache.
 * @author Kahle
 */
public abstract class AbstractCache implements Cache {

    /**
     * Returns the current time in milliseconds.
     * @return The current time in milliseconds
     */
    protected long currentTimeMillis() {

        return System.currentTimeMillis();
    }

    /**
     * Gets the lock manager object.
     * @return The lock manager object
     */
    protected String getLockManager() {

        return null;
    }

    /**
     * Determines whether the current cache is full.
     * @return If true means full, otherwise not full
     */
    protected boolean isFull() {

        return false;
    }

    @Override
    public Object getNative() {

        throw new UnsupportedOperationException();
    }

    @Override
    public <T> T get(Object key, Callable<T> callable) {
        Assert.notNull(callable, "Parameter \"callable\" must not null. ");
        Assert.notNull(key, "Parameter \"key\" must not null. ");
        Object value = get(key);
        if (value != null) { return ObjectUtils.cast(value); }
        String lockName = "lock-name:" + getClass().getName() + ":" + key;
        LockUtils.lock(getLockManager(), lockName);
        try {
            // Try to get again.
            value = get(key);
            if (value != null) { return ObjectUtils.cast(value); }
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
            return ObjectUtils.cast(value);
        }
        finally {
            LockUtils.unlock(getLockManager(), lockName);
        }
    }

    @Override
    public <T> T get(Object key, Class<T> type) {
        Assert.notNull(type, "Parameter \"type\" must not null. ");
        Assert.notNull(key, "Parameter \"key\" must not null. ");
        Object value = get(key);
        if (value == null) { return null; }
        return ObjectUtils.cast(value, type);
    }

    @Override
    public boolean containsKey(Object key) {
        Assert.notNull(key, "Parameter \"key\" must not null. ");
        return get(key) != null;
    }

    @Override
    public long size() {

        throw new UnsupportedOperationException();
    }

    @Override
    public Object putIfAbsent(Object key, Object value) {
        Assert.notNull(key, "Parameter \"key\" must not null. ");
        Object oldValue = get(key);
        if (oldValue == null) {
            put(key, value);
        }
        return oldValue;
    }

    @Override
    public Object putIfAbsent(Object key, Object value, long timeToLive, TimeUnit timeUnit) {
        Assert.notNull(key, "Parameter \"key\" must not null. ");
        Object oldValue = get(key);
        if (oldValue == null) {
            put(key, value);
            expire(key, timeToLive, timeUnit);
        }
        return oldValue;
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
    public boolean expireAt(Object key, Date date) {
        Assert.notNull(date, "Parameter \"date\" must not null. ");
        Assert.notNull(key, "Parameter \"key\" must not null. ");
        Assert.isTrue(date.after(new Date()), "Parameter \"date\" must after now date. ");
        long timeToLive = date.getTime() - System.currentTimeMillis();
        return expire(key, timeToLive, TimeUnit.MILLISECONDS);
    }

    @Override
    public void removeAll(Collection<?> keys) {
        if (CollectionUtils.isEmpty(keys)) { return; }
        for (Object key : keys) {
            if (key == null) { continue; }
            remove(key);
        }
    }

    @Override
    public void clear() {

        throw new UnsupportedOperationException();
    }

    @Override
    public long prune() {

        throw new UnsupportedOperationException();
    }

    @Override
    public Collection<Object> keys() {

        return entries().keySet();
    }

    @Override
    public Map<Object, Object> entries() {

        throw new UnsupportedOperationException();
    }

}
