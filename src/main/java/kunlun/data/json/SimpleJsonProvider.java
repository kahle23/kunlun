/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.data.json;

import kunlun.logging.Logger;
import kunlun.logging.LoggerFactory;
import kunlun.util.Assert;
import kunlun.util.MapUtil;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The simple json conversion provider.
 * @author Kahle
 */
public class SimpleJsonProvider implements JsonProvider {
    private static final Logger log = LoggerFactory.getLogger(SimpleJsonProvider.class);
    protected final Map<String, JsonProcessor> processors;
    protected final Map<String, Object> commonProperties;
    private String defaultProcessorName = "default";

    protected SimpleJsonProvider(Map<String, Object> commonProperties,
                                 Map<String, JsonProcessor> processors) {
        Assert.notNull(commonProperties, "Parameter \"commonProperties\" must not null. ");
        Assert.notNull(processors, "Parameter \"processors\" must not null. ");
        this.commonProperties = commonProperties;
        this.processors = processors;
    }

    public SimpleJsonProvider() {
        this(new ConcurrentHashMap<String, Object>(),
                new ConcurrentHashMap<String, JsonProcessor>());
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
    public void registerProcessor(String name, JsonProcessor jsonProcessor) {
        Assert.notNull(jsonProcessor, "Parameter \"jsonProcessor\" must not null. ");
        Assert.notBlank(name, "Parameter \"name\" must not blank. ");
        String className = jsonProcessor.getClass().getName();
        jsonProcessor.setCommonProperties(getCommonProperties());
        processors.put(name, jsonProcessor);
        log.debug("Register the json processor \"{}\" to \"{}\". ", className, name);
    }

    @Override
    public void deregisterProcessor(String name) {
        Assert.notBlank(name, "Parameter \"name\" must not blank. ");
        JsonProcessor remove = processors.remove(name);
        if (remove != null) {
            String className = remove.getClass().getName();
            log.debug("Deregister the json processor \"{}\" from \"{}\". ", className, name);
        }
    }

    @Override
    public JsonProcessor getJsonProcessor(String name) {
        Assert.notBlank(name, "Parameter \"name\" must not blank. ");
        JsonProcessor jsonProcessor = processors.get(name);
        Assert.notNull(jsonProcessor
                , "The corresponding json processor could not be found by name. ");
        return jsonProcessor;
    }

    @Override
    public boolean isJsonObject(String name, String jsonString) {

        return getJsonProcessor(name).isJsonObject(jsonString);
    }

    @Override
    public boolean isJsonArray(String name, String jsonString) {

        return getJsonProcessor(name).isJsonArray(jsonString);
    }

    @Override
    public String toJsonString(String name, Object object, Object... arguments) {

        return getJsonProcessor(name).toJsonString(object, arguments);
    }

    @Override
    public <T> T parseObject(String name, String jsonString, Type type, Object... arguments) {

        return getJsonProcessor(name).parseObject(jsonString, type, arguments);
    }

}
