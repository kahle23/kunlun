package artoria.generator.id.support;

import artoria.generator.id.IdGenerator;
import artoria.util.Assert;

import java.util.Collections;
import java.util.Map;

/**
 * The abstract identifier generator.
 * @author Kahle
 */
public abstract class AbstractIdGenerator implements IdGenerator {
    private Map<Object, Object> properties = Collections.emptyMap();

    @Override
    public void setProperties(Map<?, ?> properties) {
        Assert.notNull(properties, "Parameter \"properties\" must not null. ");
        this.properties = Collections.unmodifiableMap(properties);
    }

    @Override
    public Map<Object, Object> getProperties() {

        return properties;
    }

}
