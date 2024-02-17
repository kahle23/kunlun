/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.data.xml;

import kunlun.data.xml.support.SimpleXmlHandler;
import kunlun.logging.Logger;
import kunlun.logging.LoggerFactory;
import kunlun.util.Assert;
import kunlun.util.MapUtils;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The xml conversion provider based on jdk simple implementation.
 * @author Kahle
 */
public class SimpleXmlProvider implements XmlProvider {
    private static final Logger log = LoggerFactory.getLogger(SimpleXmlProvider.class);
    protected final Map<String, XmlHandler> handlers;
    protected final Map<String, Object> commonProperties;
    private String defaultHandlerName = "default";

    protected SimpleXmlProvider(Map<String, Object> commonProperties,
                                Map<String, XmlHandler> handlers) {
        Assert.notNull(commonProperties, "Parameter \"commonProperties\" must not null. ");
        Assert.notNull(handlers, "Parameter \"handlers\" must not null. ");
        this.commonProperties = commonProperties;
        this.handlers = handlers;
        // Register the default handler.
        registerHandler(getDefaultHandlerName(), new SimpleXmlHandler());
    }

    public SimpleXmlProvider() {
        this(new ConcurrentHashMap<String, Object>(),
                new ConcurrentHashMap<String, XmlHandler>());
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
    public String getDefaultHandlerName() {

        return defaultHandlerName;
    }

    @Override
    public void setDefaultHandlerName(String defaultHandlerName) {
        Assert.notBlank(defaultHandlerName, "Parameter \"defaultHandlerName\" must not blank. ");
        this.defaultHandlerName = defaultHandlerName;
    }

    @Override
    public void registerHandler(String name, XmlHandler xmlHandler) {
        Assert.notNull(xmlHandler, "Parameter \"xmlHandler\" must not null. ");
        Assert.notBlank(name, "Parameter \"name\" must not blank. ");
        String className = xmlHandler.getClass().getName();
        xmlHandler.setCommonProperties(getCommonProperties());
        handlers.put(name, xmlHandler);
        log.info("Register the xml handler \"{}\" to \"{}\". ", className, name);
    }

    @Override
    public void deregisterHandler(String name) {
        Assert.notBlank(name, "Parameter \"name\" must not blank. ");
        XmlHandler remove = handlers.remove(name);
        if (remove != null) {
            String className = remove.getClass().getName();
            log.info("Deregister the xml handler \"{}\" from \"{}\". ", className, name);
        }
    }

    @Override
    public XmlHandler getXmlHandler(String name) {
        Assert.notBlank(name, "Parameter \"name\" must not blank. ");
        XmlHandler xmlHandler = handlers.get(name);
        Assert.notNull(xmlHandler
                , "The corresponding xml handler could not be found by name. ");
        return xmlHandler;
    }

    @Override
    public String toXmlString(String name, Object object, Object... arguments) {

        return getXmlHandler(name).toXmlString(object, arguments);
    }

    @Override
    public <T> T parseObject(String name, String xmlString, Type type, Object... arguments) {

        return getXmlHandler(name).parseObject(xmlString, type, arguments);
    }

}
