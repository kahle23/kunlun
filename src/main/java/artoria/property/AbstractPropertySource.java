package artoria.property;

import artoria.logging.Logger;
import artoria.logging.LoggerFactory;
import artoria.util.Assert;
import artoria.util.MapUtils;

import java.util.Collections;
import java.util.Map;

/**
 * The abstract property source.
 * @author Kahle
 */
public abstract class AbstractPropertySource implements PropertySource {
    private static final Logger log = LoggerFactory.getLogger(AbstractPropertySource.class);
    private Map<Object, Object> commonProperties = Collections.emptyMap();
    private final String name;

    public AbstractPropertySource(String name) {
        Assert.notNull(name, "Parameter \"name\" must not null. ");
        this.name = name;
    }

    @Override
    public String getName() {

        return name;
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

    @Override
    public void setProperties(Map<?, ?> properties) {
        if (MapUtils.isEmpty(properties)) { return; }
        for (Map.Entry<?, ?> entry : properties.entrySet()) {
            setProperty(String.valueOf(entry.getKey()), entry.getValue());
        }
    }

    @Override
    public boolean containsProperty(String name) {

        return getProperty(name) != null;
    }

}
