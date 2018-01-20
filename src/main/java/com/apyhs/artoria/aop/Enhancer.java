package com.apyhs.artoria.aop;

import com.apyhs.artoria.logging.Logger;
import com.apyhs.artoria.logging.LoggerFactory;
import com.apyhs.artoria.util.Assert;
import com.apyhs.artoria.util.ClassUtils;

/**
 * Object enhancer.
 * @author Kahle
 */
public class Enhancer {
    private static Logger log = LoggerFactory.getLogger(Enhancer.class);
    private static ProxyFactory proxyFactory;

    static {
        ClassLoader loader = ClassUtils.getDefaultClassLoader();
        boolean hasCglib = ClassUtils.isPresent("net.sf.cglib.proxy.Enhancer", loader);
        Enhancer.setProxyFactory(hasCglib ? new CglibProxyFactory() : new JdkProxyFactory());
    }

    public static ProxyFactory getProxyFactory() {
        return proxyFactory;
    }

    public static void setProxyFactory(ProxyFactory proxyFactory) {
        Assert.notNull(proxyFactory, "Parameter \"proxyFactory\" must not null. ");
        log.info("Set proxy factory: " + proxyFactory.getClass().getName());
        Enhancer.proxyFactory = proxyFactory;
    }

    public static Object enhance(Class<?> originalClass, Interceptor interceptor) {
        return proxyFactory.getInstance(originalClass, interceptor);
    }

    public static Object enhance(Object original, Interceptor interceptor) {
        Assert.notNull(original, "Parameter \"original\" must not null. ");
        return proxyFactory.getInstance(original.getClass(), interceptor);
    }

}
