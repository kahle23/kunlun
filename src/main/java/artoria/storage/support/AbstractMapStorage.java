package artoria.storage.support;

import artoria.convert.ConversionUtils;
import artoria.storage.AbstractStorage;
import artoria.util.Assert;
import artoria.util.ObjectUtils;

import java.util.Collection;
import java.util.Map;

/**
 * The abstract map storage.
 * @author Kahle
 */
public abstract class AbstractMapStorage extends AbstractStorage {

    public AbstractMapStorage(String name) {

        super(name);
    }

    /**
     * Build the bucket.
     * @return The bucket
     */
    protected abstract Map<Object, Object> buildBucket();

    /**
     * Get the bucket.
     * @return The bucket
     */
    protected abstract Map<Object, Object> getBucket();

    @Override
    public Object getNative() {

        return getBucket();
    }

    @Override
    public <T> T get(Object key, Class<T> type) {
        Object value = get(key);
        if (value == null) { return null; }
        return ConversionUtils.convert(value, type);
    }

    @Override
    public Object get(Object key) {

        return getBucket().get(key);
    }

    @Override
    public boolean containsKey(Object key) {

        return getBucket().containsKey(key);
    }

    @Override
    public Object put(Object key, Object value) {

        return getBucket().put(key, value);
    }

    @Override
    public Object putAll(Map<?, ?> map) {
        getBucket().putAll(map);
        return null;
    }

    @Override
    public Object remove(Object key) {

        return getBucket().remove(key);
    }

    @Override
    public Object clear() {
        getBucket().clear();
        return null;
    }

    @Override
    public <T> Collection<T> keys(Object pattern, Class<T> type) {
        Assert.isTrue(Object.class.equals(type), "Parameter \"type\" can only be \"Object\". ");
        Assert.isNull(pattern, "Parameter \"pattern\" can only be null. ");
        return ObjectUtils.cast(getBucket().keySet());
    }

}
