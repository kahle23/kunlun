package artoria.cache;

import artoria.convert.ConversionUtils;
import artoria.logging.Logger;
import artoria.logging.LoggerFactory;
import artoria.util.Assert;
import artoria.util.CollectionUtils;
import artoria.util.MapUtils;
import artoria.util.ObjectUtils;

import java.util.Collection;
import java.util.Map;

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
     * Record log.
     */
    private Boolean recordLog;

    public AbstractCache(String name) {
        Assert.notBlank(name, "Parameter \"name\" must not blank. ");
        this.recordLog = false;
        this.name = name;
    }

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
        if (!recordLog) { return; }
        String content = NEWLINE +
                "---- Begin Cache ----" + NEWLINE +
                "Name:        " + getName() + NEWLINE +
                "Key:         " + key + NEWLINE +
                "Touched:     " + touched + NEWLINE +
                "Provider:    " + getClass().getName() + NEWLINE +
                "---- End Cache ----" + NEWLINE;
        log.info(content);
    }

    protected void recordEviction(Object key, int reasonCode) {
        if (!recordLog) { return; }
        String reason = reasonCode == ONE ? "removed" :
                reasonCode == TWO ? "expired" : EMPTY_STRING;
        String content = NEWLINE +
                "---- Begin Cache ----" + NEWLINE +
                "Name:        " + getName() + NEWLINE +
                "Key:         " + key + NEWLINE +
                "Eviction:    " + reason + NEWLINE +
                "Provider:    " + getClass().getName() + NEWLINE +
                "---- End Cache ----" + NEWLINE;
        log.info(content);
    }

    @Override
    public String getName() {

        return name;
    }

    @Override
    public Boolean getRecordLog() {

        return recordLog;
    }

    @Override
    public void setRecordLog(Boolean recordLog) {
        Assert.notNull(recordLog, "Parameter \"recordLog\" must not null. ");
        this.recordLog = recordLog;
    }

    @Override
    public <T> T get(Object key, Class<T> type) {
        Assert.notNull(type, "Parameter \"type\" must not null. ");
        Assert.notNull(key, "Parameter \"key\" must not null. ");
        Object value = get(key);
        if (value == null) { return null; }
        value = ConversionUtils.convert(value, type);
        return ObjectUtils.cast(value, type);
    }

    @Override
    public boolean containsKey(Object key) {
        Assert.notNull(key, "Parameter \"key\" must not null. ");
        return get(key) != null;
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
