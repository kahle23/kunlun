/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.aop;

import kunlun.logging.Logger;
import kunlun.logging.LoggerFactory;
import kunlun.util.Assert;

/**
 * The proxy tools.
 * @author Kahle
 */
public class ProxyUtils {
    private static final Logger log = LoggerFactory.getLogger(ProxyUtils.class);
    private static volatile ProxyProvider proxyProvider;

    public static ProxyProvider getProxyProvider() {
        if (proxyProvider != null) { return proxyProvider; }
        synchronized (ProxyUtils.class) {
            if (proxyProvider != null) { return proxyProvider; }
            ProxyUtils.setProxyProvider(new SimpleProxyProvider());
            return proxyProvider;
        }
    }

    public static void setProxyProvider(ProxyProvider proxyProvider) {
        Assert.notNull(proxyProvider, "Parameter \"proxyProvider\" must not null. ");
        log.info("Set proxy provider: {}", proxyProvider.getClass().getName());
        ProxyUtils.proxyProvider = proxyProvider;
    }

    public static String getDefaultHandlerName() {

        return getProxyProvider().getDefaultHandlerName();
    }

    public static void setDefaultHandlerName(String defaultHandlerName) {

        getProxyProvider().setDefaultHandlerName(defaultHandlerName);
    }

    public static void registerHandler(String name, ProxyHandler proxyHandler) {

        getProxyProvider().registerHandler(name, proxyHandler);
    }

    public static void deregisterHandler(String name) {

        getProxyProvider().deregisterHandler(name);
    }

    public static ProxyHandler getProxyHandler(String name) {

        return getProxyProvider().getProxyHandler(name);
    }

    public static Object proxy(String handlerName, Class<?> originalClass, Interceptor interceptor) {

        return getProxyProvider().proxy(handlerName, originalClass, interceptor);
    }

    public static Object proxy(Class<?> originalClass, Interceptor interceptor) {

        return getProxyProvider().proxy(getDefaultHandlerName(), originalClass, interceptor);
    }

    public static <T> T proxy(String handlerName, AbstractInterceptor<T> interceptor) {

        return getProxyProvider().proxy(handlerName, interceptor);
    }

    public static <T> T proxy(AbstractInterceptor<T> interceptor) {

        return getProxyProvider().proxy(getDefaultHandlerName(), interceptor);
    }

}
