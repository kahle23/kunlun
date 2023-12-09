package artoria.property;

import artoria.cache.CacheUtils;
import artoria.util.Assert;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * The cache property source (based on static proxies).
 * @author Kahle
 */
public class CachePropertySource implements PropertySource {
    private final PropertySource propertySource;
    private final String   cacheName;
    private final Long     timeToLive;
    private final TimeUnit timeUnit;

    public CachePropertySource(PropertySource propertySource, String cacheName) {

        this(propertySource, cacheName, null, null);
    }

    public CachePropertySource(PropertySource propertySource,
                               String cacheName,
                               Long timeToLive,
                               TimeUnit timeUnit) {
        Assert.notNull(propertySource, "Parameter \"propertySource\" must not null. ");
        Assert.notBlank(cacheName, "Parameter \"cacheName\" must not blank. ");
        this.propertySource = propertySource;
        this.cacheName = cacheName;
        this.timeToLive = timeToLive;
        this.timeUnit = timeUnit;
    }

    @Override
    public String getName() {

        return propertySource.getName();
    }

    @Override
    public Map<Object, Object> getCommonProperties() {

        return propertySource.getCommonProperties();
    }

    @Override
    public void setCommonProperties(Map<?, ?> properties) {

        propertySource.setCommonProperties(properties);
    }

    @Override
    public void setProperties(Map<?, ?> properties) {

        propertySource.setProperties(properties);
    }

    @Override
    public Map<String, Object> getProperties() {

        return propertySource.getProperties();
    }

    @Override
    public boolean containsProperty(String name) {

        return propertySource.containsProperty(name);
    }

    @Override
    public Object setProperty(String name, Object value) {
        Assert.notNull(name, "Parameter \"name\" must not null. ");
        Object result = propertySource.setProperty(name, value);
        CacheUtils.remove(cacheName, name);
        return result;
    }

    @Override
    public Object getProperty(String name) {
        Assert.notNull(name, "Parameter \"name\" must not null. ");
        Object val = CacheUtils.get(cacheName, name);
        if (val != null) { return val; }
        synchronized (name.intern()) {
            if ((val = CacheUtils.get(cacheName, name)) != null) { return val; }
            val = propertySource.getProperty(name);
            if (val == null) { return null; }
            if (timeToLive != null && timeUnit != null) {
                CacheUtils.put(cacheName, name, val, timeToLive, timeUnit);
            }
            else {
                CacheUtils.put(cacheName, name, val);
            }
        }
        return val;
    }

    @Override
    public Object removeProperty(String name) {
        Assert.notNull(name, "Parameter \"name\" must not null. ");
        Object result = propertySource.removeProperty(name);
        CacheUtils.remove(cacheName, name);
        return result;
    }

}
