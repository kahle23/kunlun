package artoria.cache;

import artoria.data.ReferenceType;
import artoria.util.Assert;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import static artoria.common.Constants.DEFAULT;
import static artoria.data.bean.BeanUtils.beanToBean;

/**
 * The simple cache factory.
 * @author Kahle
 */
public class SimpleCacheFactory implements CacheFactory {
    private final Map<String, CacheConfig> configs = new ConcurrentHashMap<String, CacheConfig>();

    public SimpleCacheFactory() {
        SimpleCacheConfig cacheConfig = new SimpleCacheConfig(ReferenceType.SOFT);
        cacheConfig.setTimeToLive(20L);
        cacheConfig.setTimeToLiveUnit(TimeUnit.MINUTES);
        register(DEFAULT, cacheConfig);
    }

    @Override
    public void register(String pattern, CacheConfig cacheConfig) {
        Assert.notNull(cacheConfig, "Parameter \"cacheConfig\" must not null. ");
        Assert.notBlank(pattern, "Parameter \"pattern\" must not blank. ");
        configs.put(pattern, cacheConfig);
    }

    @Override
    public CacheConfig deregister(String pattern) {
        Assert.notBlank(pattern, "Parameter \"pattern\" must not blank. ");
        return configs.remove(pattern);
    }

    @Override
    public Cache getInstance(String cacheName) {
        Assert.notBlank(cacheName, "Parameter \"cacheName\" must not blank. ");
        if (configs.containsKey(cacheName)) {
            CacheConfig cacheConfig = configs.get(cacheName);
            return createCache(cacheName, beanToBean(cacheConfig, cacheConfig.getClass()));
        }
        else {
            SimpleCacheConfig cacheConfig = new SimpleCacheConfig(ReferenceType.WEAK);
            cacheConfig.setTimeToLive(5L);
            cacheConfig.setTimeToLiveUnit(TimeUnit.MINUTES);
            return createCache(cacheName, cacheConfig);
        }
    }

    /**
     * Create cache object based on the input parameters.
     * @param cacheName The cache name
     * @param cacheConfig The cache configuration
     * @return The cache object created
     */
    protected Cache createCache(String cacheName, CacheConfig cacheConfig) {

        return new SimpleCache(cacheName, cacheConfig);
    }

}
