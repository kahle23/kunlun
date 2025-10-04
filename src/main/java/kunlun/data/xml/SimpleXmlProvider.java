/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.data.xml;

import kunlun.logging.Logger;
import kunlun.logging.LoggerFactory;
import kunlun.util.Assert;
import kunlun.util.MapUtil;

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
    protected final Map<String, XmlProcessor> processors;
    protected final Map<String, Object> commonProperties;
    private String defaultProcessorName = "default";

    protected SimpleXmlProvider(Map<String, Object> commonProperties,
                                Map<String, XmlProcessor> processors) {
        Assert.notNull(commonProperties, "Parameter \"commonProperties\" must not null. ");
        Assert.notNull(processors, "Parameter \"processors\" must not null. ");
        this.commonProperties = commonProperties;
        this.processors = processors;
    }

    public SimpleXmlProvider() {
        this(new ConcurrentHashMap<String, Object>(),
                new ConcurrentHashMap<String, XmlProcessor>());
    }

    @Override
    public void registerCommonProperties(Map<?, ?> commonProperties) {
        if (MapUtil.isEmpty(commonProperties)) { return; }
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
    public String getDefaultProcessorName() {

        return defaultProcessorName;
    }

    @Override
    public void setDefaultProcessorName(String defaultProcessorName) {
        Assert.notBlank(defaultProcessorName, "Parameter \"defaultProcessorName\" must not blank. ");
        this.defaultProcessorName = defaultProcessorName;
    }

    @Override
    public void registerProcessor(String name, XmlProcessor xmlProcessor) {
        Assert.notNull(xmlProcessor, "Parameter \"xmlProcessor\" must not null. ");
        Assert.notBlank(name, "Parameter \"name\" must not blank. ");
        String className = xmlProcessor.getClass().getName();
        xmlProcessor.setCommonProperties(getCommonProperties());
        processors.put(name, xmlProcessor);
        log.debug("Register the xml processor \"{}\" to \"{}\". ", className, name);
    }

    @Override
    public void deregisterProcessor(String name) {
        Assert.notBlank(name, "Parameter \"name\" must not blank. ");
        XmlProcessor remove = processors.remove(name);
        if (remove != null) {
            String className = remove.getClass().getName();
            log.debug("Deregister the xml processor \"{}\" from \"{}\". ", className, name);
        }
    }

    @Override
    public XmlProcessor getXmlProcessor(String name) {
        Assert.notBlank(name, "Parameter \"name\" must not blank. ");
        XmlProcessor xmlProcessor = processors.get(name);
        Assert.notNull(xmlProcessor
                , "The corresponding xml processor could not be found by name. ");
        return xmlProcessor;
    }

    @Override
    public String toXmlString(String name, Object object, Object... arguments) {

        return getXmlProcessor(name).toXmlString(object, arguments);
    }

    @Override
    public <T> T parseObject(String name, String xmlString, Type type, Object... arguments) {

        return getXmlProcessor(name).parseObject(xmlString, type, arguments);
    }

}
