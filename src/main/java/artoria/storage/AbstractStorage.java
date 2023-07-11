package artoria.storage;

import artoria.util.Assert;
import artoria.util.CollectionUtils;
import artoria.util.MapUtils;

import java.util.Collection;
import java.util.Map;

/**
 * The abstract storage.
 * @author Kahle
 */
@Deprecated // TODO: 2023/3/23 Deletable
public abstract class AbstractStorage implements Storage {
    /**
     * The storage name.
     */
    private final String name;

    public AbstractStorage(String name) {
        Assert.notBlank(name, "Parameter \"name\" must not blank. ");
        this.name = name;
    }

    protected String getKeyString(Object key) {
        Assert.notNull(key, "Parameter \"key\" must not null. ");
        String keyString = String.valueOf(key);
        Assert.notBlank(keyString, "Parameter \"key\" must not blank. ");
        return keyString;
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
    public Object putAll(Map<?, ?> map) {
        Assert.notNull(map, "Parameter \"map\" must not null. ");
        if (MapUtils.isEmpty(map)) { return null; }
        for (Map.Entry<?, ?> entry : map.entrySet()) {
            put(entry.getKey(), entry.getValue());
        }
        return null;
    }

    @Override
    public Object removeAll(Collection<?> keys) {
        if (CollectionUtils.isEmpty(keys)) { return null; }
        for (Object key : keys) {
            if (key == null) { continue; }
            remove(key);
        }
        return null;
    }

    @Override
    public Object clear() {

        throw new UnsupportedOperationException();
    }

    @Override
    public <T> Collection<T> keys(Object pattern, Class<T> type) {

        throw new UnsupportedOperationException();
    }

}
