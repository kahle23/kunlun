/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.action.support;

import kunlun.action.AbstractAction;
import kunlun.data.tuple.Triple;
import kunlun.util.ArgumentUtils;
import kunlun.util.Assert;
import kunlun.util.ObjectUtils;

import java.lang.reflect.Type;

/**
 * The abstract classic action handler.
 * @author Kahle
 */
@Deprecated
public abstract class AbstractClassicAction extends AbstractAction {

    @Override
    public Object execute(Object[] arguments) {
        Triple<Object, String, Type> triple = ArgumentUtils.parseToObjStrType(arguments);
        return execute(triple.getLeft(), triple.getRight());
    }

    /**
     * Execute a specific logic.
     * @param input The input parameters to be executed
     * @param type The type of the return value
     * @param <T> The generic type of the return value
     * @return The return value
     */
    public <T> T execute(Object input, Type type) {
        Assert.isInstanceOf(Class.class, type, "Parameter \"type\" must instance of class. ");
        Class<T> clazz = ObjectUtils.cast(type);
        return execute(input, clazz);
    }

    /**
     * Execute a specific logic.
     * @param input The input parameters to be executed
     * @param clazz The class of the return value
     * @param <T> The generic type of the return value
     * @return The return value
     */
    public abstract <T> T execute(Object input, Class<T> clazz);

}
