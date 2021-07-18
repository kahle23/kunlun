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

    protected abstract Object getStorageValue(Object key);

    protected abstract Object putStorageValue(Object key, Object value);

    protected abstract Object removeStorageValue(Object key);

    protected boolean isFull() {

        return false;
    }

    protected long currentTimeMillis() {

        return System.currentTimeMillis();
    }

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

    public Boolean getPrintLog() {

        return printLog;
    }

    public void setPrintLog(Boolean printLog) {

        this.printLog = printLog;
    }

    @Override
    public String getName() {

        return name;
    }

    @Override
    public <T> T get(Object key, Callable<T> callable) {
        Assert.notNull(callable, "Parameter \"callable\" must not null. ");
        Assert.notNull(key, "Parameter \"key\" must not null. ");
        Object value = get(key);
        if (value != null) {
            return ObjectUtils.cast(value);
        }
        synchronized (String.valueOf(key).intern()) {
            if ((value = getStorageValue(key)) != null) {
                return ObjectUtils.cast(value);
            }
            try {
                value = callable.call();
            }
            catch (Exception e) {
                throw ExceptionUtils.wrap(e);
            }
            if (value != null) {
                putStorageValue(key, value);
            }
        }
        return ObjectUtils.cast(value);
    }

    @Override
    public <T> T get(Object key, Class<T> type) {
        Assert.notNull(key, "Parameter \"key\" must not null. ");
        Assert.notNull(type, "Parameter \"type\" must not null. ");
        Object value = get(key);
        if (value == null) { return null; }
        value = ConversionUtils.convert(value, type);
        return ObjectUtils.cast(value, type);
    }

    @Override
    public Object get(Object key) {
        Assert.notNull(key, "Parameter \"key\" must not null. ");
        Object value = getStorageValue(key);
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
        Assert.notNull(value, "Parameter \"value\" must not null. ");
        Assert.notNull(key, "Parameter \"key\" must not null. ");
        Object preValue = putStorageValue(key, value);
        if (isFull()) { prune(); }
        return preValue;
    }

    @Override
    public Object put(Object key, Object value, long timeToLive, TimeUnit timeUnit) {
        Assert.notNull(timeUnit, "Parameter \"timeUnit\" must not null. ");
        Object preValue = put(key, value);
        expire(key, timeToLive, timeUnit);
        return preValue;
    }

    @Override
    public Object putIfAbsent(Object key, Object value) {
        Object nowValue = getStorageValue(key);
        if (nowValue == null) {
            return put(key, value);
        }
        return null;
    }

    @Override
    public Object putIfAbsent(Object key, Object value, long timeToLive, TimeUnit timeUnit) {
        Object nowValue = getStorageValue(key);
        if (nowValue == null) {
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
    public boolean expireAt(Object key, Date date) {
        Assert.notNull(date, "Parameter \"date\" must not null. ");
        Assert.notNull(key, "Parameter \"key\" must not null. ");
        long timeToLive = date.getTime() - currentTimeMillis();
        if (timeToLive <= ZERO) {
            Object value = getStorageValue(key);
            if (value != null) {
                removeStorageValue(key);
                recordEviction(key, TWO);
            }
            return true;
        }
        else { return expire(key, timeToLive, TimeUnit.MILLISECONDS); }
    }

    @Override
    public Object remove(Object key) {
        Assert.notNull(key, "Parameter \"key\" must not null. ");
        Object remove = removeStorageValue(key);
        recordEviction(key, ONE);
        return remove;
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
    public long prune() {

        return ZERO;
    }

    @Override
    public Collection<Object> keys() {

        return entries().keySet();
    }

}
