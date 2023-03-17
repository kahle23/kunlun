package artoria.ai;

import artoria.core.ArtificialIntelligence;
import artoria.util.Assert;

import java.util.Collections;
import java.util.Map;

/**
 * The abstract ai engine.
 * @author Kahle
 */
public abstract class AbstractAiEngine implements ArtificialIntelligence {
    private Map<Object, Object> commonProperties = Collections.emptyMap();

    protected void isSupport(Class<?>[] supportClasses, Class<?> clazz) {
        if (Object.class.equals(clazz)) { return; }
        for (Class<?> supportClass : supportClasses) {
            if (supportClass.equals(clazz)) { return; }
        }
        throw new IllegalArgumentException("Parameter \"clazz\" is not supported. ");
    }

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
