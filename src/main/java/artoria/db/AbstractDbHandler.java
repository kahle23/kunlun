package artoria.db;

import artoria.util.Assert;

import java.util.Collections;
import java.util.Map;

/**
 * The abstract database handler.
 * @author Kahle
 */
public abstract class AbstractDbHandler implements DbHandler {
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
