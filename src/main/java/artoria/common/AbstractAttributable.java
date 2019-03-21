package artoria.common;

import artoria.util.Assert;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Abstract attributable.
 * @author Kahle
 */
public abstract class AbstractAttributable implements Attributable {
    private Map<String, Object> attributes = new HashMap<String, Object>();

    @Override
    public Object addAttribute(String attributeName, Object attributeValue) {

        return this.attributes.put(attributeName, attributeValue);
    }

    @Override
    public Object removeAttribute(String attributeName) {

        return this.attributes.remove(attributeName);
    }

    @Override
    public Object getAttribute(String attributeName) {

        return this.attributes.get(attributeName);
    }

    @Override
    public Map<String, Object> getAttributes() {

        return Collections.unmodifiableMap(attributes);
    }

    @Override
    public void addAttributes(Map<String, Object> attributes) {
        Assert.notNull(attributes, "Parameter \"attributes\" must not null. ");
        this.attributes.putAll(attributes);
    }

    @Override
    public void clearAttributes() {

        this.attributes.clear();
    }

}
