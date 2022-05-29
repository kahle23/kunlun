package artoria.cache;

import artoria.util.Assert;
import artoria.util.CollectionUtils;
import artoria.util.MapUtils;
import artoria.util.ObjectUtils;

import java.util.Collection;
import java.util.Map;

import static artoria.common.Constants.ZERO;

/**
 * The abstract cache.
 * @author Kahle
 */
public abstract class AbstractCache implements Cache {
    /**
     * The cache name.
     */
    private final String name;

    /**
     * The default constructor.
     * @param name The cache name
     */
    public AbstractCache(String name) {
        Assert.notBlank(name, "Parameter \"name\" must not blank. ");
        this.name = name;
    }

    /**
     * Returns the current time in milliseconds.
     * @return The current time in milliseconds
     */
    protected long currentTimeMillis() {

        return System.currentTimeMillis();
    }

    /**
     * Determines whether the current cache is full.
     * @return If true means full, otherwise not full
     */
    protected boolean isFull() {

        return false;
    }

    @Override
    public String getName() {

        return name;
    }

    @Override
    public Object getNative() {

        throw new UnsupportedOperationException();
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

        return ZERO;
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
    public void clear() {

    }

    @Override
    public long prune() {

        return ZERO;
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
