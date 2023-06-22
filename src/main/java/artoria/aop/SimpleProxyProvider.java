package artoria.aop;

import artoria.logging.Logger;
import artoria.logging.LoggerFactory;
import artoria.util.Assert;
import artoria.util.MapUtils;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The simple proxy provider.
 * @author Kahle
 */
public class SimpleProxyProvider implements ProxyProvider {
    private static final Logger log = LoggerFactory.getLogger(SimpleProxyProvider.class);
    protected final Map<String, ProxyHandler> handlers;
    protected final Map<String, Object> commonProperties;

    protected SimpleProxyProvider(Map<String, Object> commonProperties,
                                  Map<String, ProxyHandler> handlers) {
        Assert.notNull(commonProperties, "Parameter \"commonProperties\" must not null. ");
        Assert.notNull(handlers, "Parameter \"handlers\" must not null. ");
        this.commonProperties = commonProperties;
        this.handlers = handlers;
    }

    public SimpleProxyProvider() {
        this(new ConcurrentHashMap<String, Object>(),
                new ConcurrentHashMap<String, ProxyHandler>());
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
    public void registerHandler(String name, ProxyHandler proxyHandler) {
        Assert.notNull(proxyHandler, "Parameter \"proxyHandler\" must not null. ");
        Assert.notBlank(name, "Parameter \"name\" must not blank. ");
        String className = proxyHandler.getClass().getName();
        proxyHandler.setCommonProperties(getCommonProperties());
        handlers.put(name, proxyHandler);
        log.info("Register the proxy handler \"{}\" to \"{}\". ", className, name);
    }

    @Override
    public void deregisterHandler(String name) {
        Assert.notBlank(name, "Parameter \"name\" must not blank. ");
        ProxyHandler remove = handlers.remove(name);
        if (remove != null) {
            String className = remove.getClass().getName();
            log.info("Deregister the proxy handler \"{}\" from \"{}\". ", className, name);
        }
    }

    @Override
    public ProxyHandler getProxyHandler(String name) {
        Assert.notBlank(name, "Parameter \"name\" must not blank. ");
        ProxyHandler proxyHandler = handlers.get(name);
        Assert.notNull(proxyHandler
                , "The corresponding proxy handler could not be found by name. ");
        return proxyHandler;
    }

    @Override
    public Object proxy(String handlerName, Class<?> originalClass, Interceptor interceptor) {

        return getProxyHandler(handlerName).proxy(originalClass, interceptor);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T proxy(String handlerName, AbstractInterceptor<T> interceptor) {

        return (T) proxy(handlerName, interceptor.getOriginalClass(), interceptor);
    }

}
