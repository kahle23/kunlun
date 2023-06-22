package artoria.aop;

import artoria.util.Assert;

import java.util.Collections;
import java.util.Map;

/**
 * The abstract proxy handler.
 * @author Kahle
 */
public abstract class AbstractProxyHandler implements ProxyHandler {
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
