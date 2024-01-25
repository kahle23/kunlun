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
    private Map<Object, Object> commonProperties = Collections.emptyMap();

    @Override
    public Map<Object, Object> getCommonProperties() {

        return commonProperties;
    }

    @Override
    public void setCommonProperties(Map<?, ?> properties) {
        Assert.notNull(properties, "Parameter \"properties\" must not null. ");
        this.commonProperties = Collections.unmodifiableMap(properties);
    }

}
