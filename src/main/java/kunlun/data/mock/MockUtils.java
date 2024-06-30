/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.data.mock;

import kunlun.logging.Logger;
import kunlun.logging.LoggerFactory;
import kunlun.util.Assert;

import java.lang.reflect.Type;

import static kunlun.util.ObjectUtils.cast;

/**
 * The data mock tools.
 * @author Kahle
 */
public class MockUtils {
    private static final Logger log = LoggerFactory.getLogger(MockUtils.class);
    private static volatile MockProvider mockProvider;

    public static MockProvider getMockProvider() {
        if (mockProvider != null) { return mockProvider; }
        synchronized (MockUtils.class) {
            if (mockProvider != null) { return mockProvider; }
            setMockProvider(new SimpleMockProvider());
            return mockProvider;
        }
    }

    public static void setMockProvider(MockProvider mockProvider) {
        Assert.notNull(mockProvider, "Parameter \"mockProvider\" must not null. ");
        log.info("Set mock provider: {}", mockProvider.getClass().getName());
        MockUtils.mockProvider = mockProvider;
    }

    public static String getDefaultHandlerName() {

        return getMockProvider().getDefaultHandlerName();
    }

    public static void setDefaultHandlerName(String defaultHandlerName) {

        getMockProvider().setDefaultHandlerName(defaultHandlerName);
    }

    public static void registerHandler(String name, MockHandler mockHandler) {

        getMockProvider().registerHandler(name, mockHandler);
    }

    public static void deregisterHandler(String name) {

        getMockProvider().deregisterHandler(name);
    }

    public static MockHandler getMockHandler(String name) {

        return getMockProvider().getMockHandler(name);
    }

    public static <T> T mock(String name, Class<T> clazz, Object... arguments) {

        return cast(getMockHandler(name).mock(clazz, arguments));
    }

    public static <T> T mock(String name, Type type, Object... arguments) {

        return cast(getMockHandler(name).mock(type, arguments));
    }

    public static <T> T mock(Class<T> clazz, Object... arguments) {

        return mock(getDefaultHandlerName(), clazz, arguments);
    }

    public static <T> T mock(Type type, Object... arguments) {

        return mock(getDefaultHandlerName(), type, arguments);
    }

}
