package artoria.common;

import artoria.util.Assert;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Abstract attributable.
 * @author Kahle
 */
public abstract class AbstractAttributable implements Attributable, Serializable {
    private Map<String, Object> attributes = new HashMap<String, Object>();

    @Override
    public void putAll(Map<String, Object> attributes) {
        Assert.notNull(attributes, "Parameter \"attributes\" must not null. ");
        this.attributes.putAll(attributes);
    }

    @Override
    public Object put(String name, Object value) {

        return this.attributes.put(name, value);
    }

    @Override
    public Object remove(String name) {

        return this.attributes.remove(name);
    }

    @Override
    public Object get(String name) {

        return this.attributes.get(name);
    }

    @Override
    public Map<String, Object> entries() {

        return Collections.unmodifiableMap(this.attributes);
    }

    @Override
    public boolean containsName(String name) {

        return this.attributes.containsKey(name);
    }

    @Override
    public void clear() {

        this.attributes.clear();
    }

}
