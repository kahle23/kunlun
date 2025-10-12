/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.data.mock;

import kunlun.data.mock.support.AbstractMockGenerator;
import kunlun.logging.Logger;
import kunlun.logging.LoggerFactory;
import kunlun.util.Assert;
import kunlun.util.MapUtil;

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
    protected final Map<String, MockGenerator> handlers;
    protected final Map<String, Object> commonProperties;
    private String defaultHandlerName = "default";

    protected SimpleMockProvider(Map<String, Object> commonProperties,
                                 Map<String, MockGenerator> handlers) {
        Assert.notNull(commonProperties, "Parameter \"commonProperties\" must not null. ");
        Assert.notNull(handlers, "Parameter \"handlers\" must not null. ");
        this.commonProperties = commonProperties;
        this.handlers = handlers;
    }

    public SimpleMockProvider() {
        this(new ConcurrentHashMap<String, Object>(),
                new ConcurrentHashMap<String, MockGenerator>());
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
    public String getDefaultHandlerName() {

        return defaultHandlerName;
    }

    @Override
    public void setDefaultHandlerName(String defaultHandlerName) {
        Assert.notBlank(defaultHandlerName, "Parameter \"defaultHandlerName\" must not blank. ");
        this.defaultHandlerName = defaultHandlerName;
    }

    @Override
    public void registerGenerator(String name, MockGenerator mockHandler) {
        Assert.notNull(mockHandler, "Parameter \"mockHandler\" must not null. ");
        Assert.notBlank(name, "Parameter \"name\" must not blank. ");
        String className = mockHandler.getClass().getName();
        handlers.put(name, mockHandler);
        if (mockHandler instanceof AbstractMockGenerator) {
            ((AbstractMockGenerator) mockHandler).setCommonProperties(getCommonProperties());
        }
        log.debug("Register the mock handler \"{}\" to \"{}\". ", className, name);
    }

    @Override
    public void deregisterGenerator(String name) {
        Assert.notBlank(name, "Parameter \"name\" must not blank. ");
        MockGenerator remove = handlers.remove(name);
        if (remove != null) {
            String className = remove.getClass().getName();
            log.debug("Deregister the mock handler \"{}\" from \"{}\". ", className, name);
        }
    }

    @Override
    public MockGenerator getMockGenerator(String name) {
        Assert.notBlank(name, "Parameter \"name\" must not blank. ");
        MockGenerator mockHandler = handlers.get(name);
        Assert.notNull(mockHandler
                , "The corresponding mock handler could not be found by name. ");
        return mockHandler;
    }

    @Override
    public Object mock(String name, Type type, Object... arguments) {

        return getMockGenerator(name).mock(type, arguments);
    }

}
