package artoria.cache;

import artoria.util.Assert;

/**
 * Cache configuration simple implement by jdk.
 * @param <K> The type of key
 * @param <V> The type of value
 * @author Kahle
 */
public class SimpleConfiguration<K, V> implements Configuration<K, V> {
    private CacheLoader<K, V> cacheLoader;
    private Class<V> valueType;
    private Class<K> keyType;

    public SimpleConfiguration(Class<K> keyType, Class<V> valueType) {
        Assert.notNull(keyType, "Parameter \"keyType\" must not null. ");
        Assert.notNull(valueType, "Parameter \"valueType\" must not null. ");
        this.valueType = valueType;
        this.keyType = keyType;
    }

    @Override
    public Class<K> getKeyType() {

        return keyType;
    }

    @Override
    public Class<V> getValueType() {

        return valueType;
    }

    @Override
    public CacheLoader<K, V> getCacheLoader() {

        return cacheLoader;
    }

    public void setCacheLoader(CacheLoader<K, V> cacheLoader) {
        Assert.notNull(cacheLoader, "Parameter \"cacheLoader\" must not null. ");
        this.cacheLoader = cacheLoader;
    }

}
