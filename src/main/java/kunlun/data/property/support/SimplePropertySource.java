/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.data.property.support;

import kunlun.data.property.AbstractPropertySource;
import kunlun.util.Assert;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The simple property source.
 * @author Kahle
 */
public class SimplePropertySource extends AbstractPropertySource {
    private final Map<String, Object> properties;

    public SimplePropertySource(Map<String, Object> properties) {
        Assert.notNull(properties, "Parameter \"properties\" must not null. ");
        this.properties = properties;
    }

    public SimplePropertySource() {

        this(new ConcurrentHashMap<String, Object>());
    }

    @Override
    public Object getProperty(String name) {

        return properties.get(name);
    }

    @Override
    public Object setProperty(String name, Object value) {

        return properties.put(name, value);
    }

    @Override
    public Object removeProperty(String name) {

        return properties.remove(name);
    }

    @Override
    public Set<String> getPropertyNames() {

        return Collections.unmodifiableSet(properties.keySet());
    }

    @Override
    public Map<String, Object> getProperties() {

        return Collections.unmodifiableMap(properties);
    }

}
