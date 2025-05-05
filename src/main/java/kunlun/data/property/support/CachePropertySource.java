/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.data.property.support;

import kunlun.cache.Cache;
import kunlun.common.constant.Nil;
import kunlun.data.property.AbstractPropertySource;
import kunlun.data.property.PropertySource;
import kunlun.util.Assert;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * The cache property source (based on static proxies).
 * @author Kahle
 */
public class CachePropertySource extends AbstractPropertySource {
    private final PropertySource propertySource;
    private final Cache    cache;
    private final Long     timeToLive;
    private final TimeUnit timeUnit;

    public CachePropertySource(PropertySource propertySource, Cache cache) {

        this(propertySource, cache, Nil.INT3, Nil.<TimeUnit>g());
    }

    public CachePropertySource(PropertySource propertySource,
                               Cache cache,
                               Long timeToLive,
                               TimeUnit timeUnit) {
        this.propertySource = Assert.notNull(propertySource);
        this.cache = Assert.notNull(cache);
        this.timeToLive = timeToLive;
        this.timeUnit = timeUnit;
    }

    @Override
    public boolean containsProperty(String name) {

        return propertySource.containsProperty(name);
    }

    @Override
    public Object getProperty(String name) {
        Assert.notNull(name, "Parameter \"name\" must not null. ");
        Object val = cache.get(name);
        if (val != null) { return val; }
        synchronized (name.intern()) {
            if ((val = cache.get(name)) != null) { return val; }
            val = propertySource.getProperty(name);
            if (val == null) { return null; }
            if (timeToLive != null && timeUnit != null) {
                cache.put(name, val, timeToLive, timeUnit);
            }
            else {
                cache.put(name, val);
            }
        }
        return val;
    }

    @Override
    public Object setProperty(String name, Object value) {
        Assert.notNull(name, "Parameter \"name\" must not null. ");
        Object result = propertySource.setProperty(name, value);
        cache.remove(name);
        return result;
    }

    @Override
    public Object removeProperty(String name) {
        Assert.notNull(name, "Parameter \"name\" must not null. ");
        Object result = propertySource.removeProperty(name);
        cache.remove(name);
        return result;
    }

    @Override
    public Set<String> getPropertyNames() {

        return propertySource.getPropertyNames();
    }

    @Override
    public Map<String, Object> getProperties() {

        return propertySource.getProperties();
    }

    @Override
    public void setProperties(Map<?, ?> properties) {

        propertySource.setProperties(properties);
    }

}
