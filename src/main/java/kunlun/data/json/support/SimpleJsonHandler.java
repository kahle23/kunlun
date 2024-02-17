/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.data.json.support;

import java.lang.reflect.Type;

/**
 * The json conversion handler based on jdk simple implementation.
 * @author Kahle
 */
public class SimpleJsonHandler extends AbstractJsonHandler {

    @Override
    public String toJsonString(Object object, Object... arguments) {

        throw new UnsupportedOperationException();
    }

    @Override
    public <T> T parseObject(String jsonString, Type type, Object... arguments) {

        throw new UnsupportedOperationException();
    }

}
