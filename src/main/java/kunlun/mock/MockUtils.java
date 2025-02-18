/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.mock;

import kunlun.data.mock.MockProvider;
import kunlun.logging.Logger;
import kunlun.logging.LoggerFactory;

import java.lang.reflect.Type;

/**
 * The mock tools.
 * @author Kahle
 */
@Deprecated // TODO: Can delete
public class MockUtils {
    private static final Logger log = LoggerFactory.getLogger(MockUtils.class);

    public static MockProvider getMockProvider() {

        return kunlun.data.mock.MockUtils.getMockProvider();
    }

    public static <T> T mock(Class<T> clazz, Object... arguments) {

        return kunlun.data.mock.MockUtils.mock(clazz, arguments);
    }

    public static <T> T mock(Type type, Object... arguments) {

        return kunlun.data.mock.MockUtils.mock(type, arguments);
    }

}
