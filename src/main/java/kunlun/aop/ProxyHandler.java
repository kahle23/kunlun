/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.aop;

import kunlun.core.Handler;

/**
 * The proxy handler for generate proxy object.
 * @author Kahle
 */
public interface ProxyHandler extends Handler {

    /**
     * Get proxy object instance.
     * @param originalClass Be proxied object class
     * @param interceptor Method interceptor
     * @return Proxy object
     */
    Object proxy(Class<?> originalClass, Interceptor interceptor);

}
