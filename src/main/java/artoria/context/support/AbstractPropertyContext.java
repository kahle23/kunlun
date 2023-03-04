package artoria.context.support;

import artoria.util.Assert;

import java.util.Map;

import static artoria.util.ObjectUtils.cast;

/**
 * The abstract property context.
 * @author Kahle
 */
public abstract class AbstractPropertyContext implements PropertyContext {

    /**
     * Get an operable storage object.
     * @return The storage object
     */
    protected abstract Map<String, Object> getBucket();

    public <T> T getRequiredProperty(String name, Class<T> targetType) {
        Object value = getRequiredProperty(name);
        return cast(value, targetType);
    }

    public Object getRequiredProperty(String name) {
        Object value = getProperty(name);
        Assert.state(value != null,
                "The property value corresponding to the name \"" + name + "\" cannot be null. ");
        return value;
    }

    public <T> T getProperty(String name, Class<T> targetType, T defaultValue) {
        T value = getProperty(name, targetType);
        return value != null ? value : defaultValue;
    }

    public <T> T getProperty(String name, Class<T> targetType) {
        Object value = getProperty(name);
        return cast(value, targetType);
    }

    public Object getProperty(String name, Object defaultValue) {
        Object value = getProperty(name);
        return value != null ? value : defaultValue;
    }

    @Override
    public boolean containsProperty(String name) {

        return getBucket().containsKey(name);
    }

    @Override
    public Object setProperty(String name, Object value) {

        return getBucket().put(name, value);
    }

    @Override
    public Object getProperty(String name) {

        return getBucket().get(name);
    }

    @Override
    public Object removeProperty(String name) {

        return getBucket().remove(name);
    }

}
