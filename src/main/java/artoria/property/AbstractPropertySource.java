package artoria.property;

import artoria.logging.Logger;
import artoria.logging.LoggerFactory;
import artoria.util.Assert;
import artoria.util.MapUtils;

import java.util.Collections;
import java.util.Map;

public abstract class AbstractPropertySource implements PropertySource {
    private static Logger log = LoggerFactory.getLogger(AbstractPropertySource.class);
    private Map<Object, Object> attrs = Collections.emptyMap();
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
    public void attrs(Map<?, ?> attrs) {
        Assert.notNull(attrs, "Parameter \"attrs\" must not null. ");
        this.attrs = Collections.unmodifiableMap(attrs);
    }

    @Override
    public Map<Object, Object> attrs() {

        return attrs;
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
