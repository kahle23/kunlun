package artoria.property;

import artoria.common.constant.Words;
import artoria.convert.ConversionUtils;
import artoria.logging.Logger;
import artoria.logging.LoggerFactory;
import artoria.util.Assert;
import artoria.util.MapUtils;
import artoria.util.StringUtils;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The simple property provider.
 * @author Kahle
 */
public class SimplePropertyProvider implements PropertyProvider {
    private static final Logger log = LoggerFactory.getLogger(SimplePropertyProvider.class);
    protected final Map<String, PropertySource> propertySources;
    protected final Map<String, Object> commonProperties;
    protected String defaultSourceName;

    protected SimplePropertyProvider(Map<String, Object> commonProperties,
                                     Map<String, PropertySource> propertySources) {
        Assert.notNull(commonProperties, "Parameter \"commonProperties\" must not null. ");
        Assert.notNull(propertySources, "Parameter \"propertySources\" must not null. ");
        this.defaultSourceName = Words.DEFAULT;
        this.commonProperties = commonProperties;
        this.propertySources = propertySources;
    }

    public SimplePropertyProvider() {
        this(new ConcurrentHashMap<String, Object>(),
                new ConcurrentHashMap<String, PropertySource>());
        registerSource(new SimplePropertySource(Words.DEFAULT));
    }

    private PropertySource getPropertySourceInner(String sourceName) {
        PropertySource propertySource = getPropertySource(sourceName);
        Assert.state(propertySource != null, "The property source does not exist. ");
        return propertySource;
    }

    public String getDefaultSourceName() {

        return defaultSourceName;
    }

    public void setDefaultSourceName(String defaultSourceName) {
        Assert.notBlank(defaultSourceName, "Parameter \"defaultSourceName\" must not blank. ");
        this.defaultSourceName = defaultSourceName;
    }

    @Override
    public void registerCommonProperties(Map<?, ?> commonProperties) {
        if (MapUtils.isEmpty(commonProperties)) { return; }
        for (Map.Entry<?, ?> entry : commonProperties.entrySet()) {
            String keyStr = String.valueOf(entry.getKey());
            this.commonProperties.put(keyStr, entry.getValue());
        }
    }

    @Override
    public void clearCommonProperties() {

        this.commonProperties.clear();
    }

    @Override
    public Map<String, Object> getCommonProperties() {

        return Collections.unmodifiableMap(commonProperties);
    }

    @Override
    public void registerSource(PropertySource propertySource) {
        Assert.notNull(propertySource, "Parameter \"propertySource\" must not null. ");
        String sourceName = propertySource.getName();
        Assert.notBlank(sourceName, "Parameter \"sourceName\" must not blank. ");
        String className = propertySource.getClass().getName();
        propertySources.put(sourceName, propertySource);
        propertySource.setCommonProperties(getCommonProperties());
        log.info("Register the property source \"{}\" to \"{}\". ", className, sourceName);
    }

    @Override
    public void deregisterSource(String sourceName) {
        Assert.notBlank(sourceName, "Parameter \"sourceName\" must not blank. ");
        PropertySource remove = propertySources.remove(sourceName);
        if (remove != null) {
            String className = remove.getClass().getName();
            log.info("Deregister the property source \"{}\" from \"{}\". ", className, sourceName);
        }
    }

    @Override
    public PropertySource getPropertySource(String sourceName) {
        if (StringUtils.isBlank(sourceName)) { sourceName = getDefaultSourceName(); }
        return propertySources.get(sourceName);
    }

    @Override
    public <T> T getRequiredProperty(String source, String name, Class<T> targetType) {
        Assert.notNull(targetType, "Parameter \"targetType\" must not null. ");
        T value = getProperty(source, name, targetType, null);
        Assert.state(value != null, "The property value is null. ");
        return value;
    }

    @Override
    public <T> T getProperty(String source, String name, Class<T> targetType, T defaultValue) {
        Assert.notNull(targetType, "Parameter \"targetType\" must not null. ");
        Object value = getProperty(source, name, defaultValue);
        if (value == null) { return defaultValue; }
        return ConversionUtils.convert(value, targetType);
    }

    @Override
    public void setProperties(String source, Map<?, ?> properties) {
        Assert.notEmpty(properties, "Parameter \"properties\" must not empty. ");
        getPropertySourceInner(source).setProperties(properties);
    }

    @Override
    public Map<String, Object> getProperties(String source) {

        return getPropertySourceInner(source).getProperties();
    }

    @Override
    public boolean containsProperty(String source, String name) {
        Assert.notBlank(name, "Parameter \"name\" must not blank. ");
        return getPropertySourceInner(source).containsProperty(name);
    }

    @Override
    public Object getProperty(String source, String name, Object defaultValue) {
        Assert.notBlank(name, "Parameter \"name\" must not blank. ");
        Object value = getPropertySourceInner(source).getProperty(name);
        return value != null ? value : defaultValue;
    }

    @Override
    public Object setProperty(String source, String name, Object value) {
        Assert.notBlank(name, "Parameter \"name\" must not blank. ");
        return getPropertySourceInner(source).setProperty(name, value);
    }

    @Override
    public Object removeProperty(String source, String name) {
        Assert.notBlank(name, "Parameter \"name\" must not blank. ");
        return getPropertySourceInner(source).removeProperty(name);
    }

}
