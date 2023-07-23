package artoria.ai;

import artoria.core.ArtificialIntelligence;
import artoria.core.Handler;
import artoria.util.Assert;

import java.util.Collections;
import java.util.Map;

/**
 * The abstract ai handler.
 * @author Kahle
 */
public abstract class AbstractAiHandler implements ArtificialIntelligence, Handler {
    private Map<Object, Object> commonProperties = Collections.emptyMap();

    public Map<Object, Object> getCommonProperties() {

        return commonProperties;
    }

    public void setCommonProperties(Map<?, ?> properties) {
        Assert.notNull(properties, "Parameter \"properties\" must not null. ");
        this.commonProperties = Collections.unmodifiableMap(properties);
    }

}
