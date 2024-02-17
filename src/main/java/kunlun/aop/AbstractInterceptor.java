/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.aop;

import kunlun.util.Assert;

/**
 * The abstract interceptor for enhance object method.
 * @author Kahle
 */
public abstract class AbstractInterceptor<T> implements Interceptor {
    private final Class<T> originalClass;
    private final T originalObject;

    @SuppressWarnings("unchecked")
    public AbstractInterceptor(T originalObject) {
        Assert.notNull(originalObject, "Parameter \"originalObject\" must not null. ");
        this.originalClass = (Class<T>) originalObject.getClass();
        this.originalObject = originalObject;
    }

    public Class<T> getOriginalClass() {

        return originalClass;
    }

    public T getOriginalObject() {

        return originalObject;
    }

}
