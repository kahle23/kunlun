/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.data.mock;

import kunlun.data.mock.support.SimpleMockHandler;
import kunlun.logging.Logger;
import kunlun.logging.LoggerFactory;
import kunlun.util.Assert;
import kunlun.util.MapUtils;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The simple data mock provider.
 * @author Kahle
 */
public class SimpleMockProvider implements MockProvider {
    private static final Logger log = LoggerFactory.getLogger(SimpleMockProvider.class);
    protected final Map<String, MockHandler> handlers;
    protected final Map<String, Object> commonProperties;
    private String defaultHandlerName = "default";

    protected SimpleMockProvider(Map<String, Object> commonProperties,
                                 Map<String, MockHandler> handlers) {
        Assert.notNull(commonProperties, "Parameter \"commonProperties\" must not null. ");
        Assert.notNull(handlers, "Parameter \"handlers\" must not null. ");
        this.commonProperties = commonProperties;
        this.handlers = handlers;
        // Register the default handler.
        registerHandler(getDefaultHandlerName(), new SimpleMockHandler());
    }

    public SimpleMockProvider() {
        this(new ConcurrentHashMap<String, Object>(),
                new ConcurrentHashMap<String, MockHandler>());
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
    public void registerHandler(String name, MockHandler mockHandler) {
        Assert.notNull(mockHandler, "Parameter \"mockHandler\" must not null. ");
        Assert.notBlank(name, "Parameter \"name\" must not blank. ");
        String className = mockHandler.getClass().getName();
        mockHandler.setCommonProperties(getCommonProperties());
        handlers.put(name, mockHandler);
        log.info("Register the mock handler \"{}\" to \"{}\". ", className, name);
    }

    @Override
    public void deregisterHandler(String name) {
        Assert.notBlank(name, "Parameter \"name\" must not blank. ");
        MockHandler remove = handlers.remove(name);
        if (remove != null) {
            String className = remove.getClass().getName();
            log.info("Deregister the mock handler \"{}\" from \"{}\". ", className, name);
        }
    }

    @Override
    public MockHandler getMockHandler(String name) {
        Assert.notBlank(name, "Parameter \"name\" must not blank. ");
        MockHandler mockHandler = handlers.get(name);
        Assert.notNull(mockHandler
                , "The corresponding mock handler could not be found by name. ");
        return mockHandler;
    }

    @Override
    public Object mock(String name, Type type, Object... arguments) {

        return getMockHandler(name).mock(type, arguments);
    }

}
