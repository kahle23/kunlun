package artoria.renderer;

import artoria.core.Renderer;
import artoria.util.Assert;

import java.util.Collections;
import java.util.Map;

/**
 * The abstract renderer.
 * @author Kahle
 */
public abstract class AbstractRenderer implements Renderer {
    private Map<Object, Object> commonProperties = Collections.emptyMap();

    public Map<Object, Object> getCommonProperties() {

        return commonProperties;
    }

    public void setCommonProperties(Map<?, ?> properties) {
        Assert.notNull(properties, "Parameter \"properties\" must not null. ");
        this.commonProperties = Collections.unmodifiableMap(properties);
    }

}
