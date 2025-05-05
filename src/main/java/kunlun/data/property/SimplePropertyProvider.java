/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.data.property;

import kunlun.common.constant.Symbols;
import kunlun.convert.ConversionUtil;
import kunlun.logging.Logger;
import kunlun.logging.LoggerFactory;
import kunlun.util.Assert;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The simple property provider.
 * @author Kahle
 */
public class SimplePropertyProvider implements PropertyProvider {
    private static final Logger log = LoggerFactory.getLogger(SimplePropertyProvider.class);
    protected final Map<String, PropertySource> propertySources;
    protected String defaultSourceName;

    protected SimplePropertyProvider(Map<String, PropertySource> propertySources) {
        Assert.notNull(propertySources, "Parameter \"propertySources\" must not null. ");
        this.defaultSourceName = Symbols.EMPTY_STRING;
        this.propertySources = propertySources;
    }

    public SimplePropertyProvider() {

        this(new ConcurrentHashMap<String, PropertySource>());
    }


    // region ======== internal methods ========

    protected PropertySource getPropSourceOrThrow(String sourceName) {
        PropertySource propertySource = getPropertySource(sourceName);
        Assert.notNull(propertySource
            , "The corresponding property source could not be found by source name. ");
        return propertySource;
    }

    // endregion


    // region ======== source manage ========

    @Override
    public String getDefaultSourceName() {

        return defaultSourceName;
    }

    @Override
    public void setDefaultSourceName(String defaultSourceName) {

        this.defaultSourceName = Assert.notNull(defaultSourceName);
    }

    @Override
    public void registerSource(String sourceName, PropertySource propertySource) {
        Assert.notNull(propertySource, "Parameter \"propertySource\" must not null. ");
        Assert.notNull(sourceName, "Parameter \"sourceName\" must not null. ");
        String className = propertySource.getClass().getName();
        propertySources.put(sourceName, propertySource);
        log.debug("Register the property source \"{}\" to \"{}\". ", className, sourceName);
    }

    @Override
    public void deregisterSource(String sourceName) {
        Assert.notNull(sourceName, "Parameter \"sourceName\" must not null. ");
        PropertySource remove = propertySources.remove(sourceName);
        if (remove != null) {
            String className = remove.getClass().getName();
            log.debug("Deregister the property source \"{}\" from \"{}\". ", className, sourceName);
        }
    }

    @Override
    public PropertySource getPropertySource(String sourceName) {
        if (sourceName == null) { sourceName = getDefaultSourceName(); }
        return propertySources.get(sourceName);
    }

    // endregion


    // region ======== source methods ========

    @Override
    public boolean containsProperty(String source, String name) {
        Assert.notBlank(name, "Parameter \"name\" must not blank. ");
        return getPropSourceOrThrow(source).containsProperty(name);
    }

    @Override
    public Object getProperty(String source, String name) {
        Assert.notBlank(name, "Parameter \"name\" must not blank. ");
        return getPropSourceOrThrow(source).getProperty(name);
    }

    @Override
    public Object setProperty(String source, String name, Object value) {
        Assert.notBlank(name, "Parameter \"name\" must not blank. ");
        return getPropSourceOrThrow(source).setProperty(name, value);
    }

    @Override
    public Object removeProperty(String source, String name) {
        Assert.notBlank(name, "Parameter \"name\" must not blank. ");
        return getPropSourceOrThrow(source).removeProperty(name);
    }

    @Override
    public Set<String> getPropertyNames(String source) {

        return getPropSourceOrThrow(source).getPropertyNames();
    }

    @Override
    public Map<String, Object> getProperties(String source) {

        return getPropSourceOrThrow(source).getProperties();
    }

    @Override
    public void setProperties(String source, Map<?, ?> properties) {
        Assert.notEmpty(properties, "Parameter \"properties\" must not empty. ");
        getPropSourceOrThrow(source).setProperties(properties);
    }

    // endregion


    // region ======== extension methods ========

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
        return ConversionUtil.convert(value, targetType);
    }

    @Override
    public Object getProperty(String source, String name, Object defaultValue) {
        Assert.notBlank(name, "Parameter \"name\" must not blank. ");
        Object value = getPropSourceOrThrow(source).getProperty(name);
        return value != null ? value : defaultValue;
    }

    // endregion

}
