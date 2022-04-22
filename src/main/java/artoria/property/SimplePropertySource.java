package artoria.property;

import artoria.util.Assert;
import artoria.util.MapUtils;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SimplePropertySource extends AbstractPropertySource {
    private final Map<String, Object> properties;

    public SimplePropertySource(String name, Map<String, Object> properties) {
        super(name);
        Assert.notNull(properties, "Parameter \"properties\" must not null. ");
        this.properties = properties;
    }

    public SimplePropertySource(String name) {

        this(name, new ConcurrentHashMap<String, Object>());
    }

    @Override
    public Map<String, Object> getProperties() {

        return Collections.unmodifiableMap(properties);
    }

    @Override
    public Object setProperty(String name, Object value) {

        return properties.put(name, value);
    }

    @Override
    public Object getProperty(String name) {

        return properties.get(name);
    }

    @Override
    public Object removeProperty(String name) {

        return properties.remove(name);
    }

}
