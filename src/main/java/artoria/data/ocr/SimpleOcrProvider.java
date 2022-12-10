package artoria.data.ocr;

import artoria.logging.Logger;
import artoria.logging.LoggerFactory;
import artoria.util.Assert;
import artoria.util.MapUtils;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The simple optical character recognition provider.
 * @author Kahle
 */
public class SimpleOcrProvider implements OcrProvider {
    private static final Logger log = LoggerFactory.getLogger(SimpleOcrProvider.class);
    protected final Map<String, OcrHandler> handlers;
    protected final Map<String, Object> commonProperties;

    protected SimpleOcrProvider(Map<String, Object> commonProperties,
                                Map<String, OcrHandler> handlers) {
        Assert.notNull(commonProperties, "Parameter \"commonProperties\" must not null. ");
        Assert.notNull(handlers, "Parameter \"handlers\" must not null. ");
        this.commonProperties = commonProperties;
        this.handlers = handlers;
    }

    public SimpleOcrProvider() {
        this(new ConcurrentHashMap<String, Object>(),
                new ConcurrentHashMap<String, OcrHandler>());
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
    public void registerHandler(String name, OcrHandler ocrHandler) {
        Assert.notNull(ocrHandler, "Parameter \"ocrHandler\" must not null. ");
        Assert.notBlank(name, "Parameter \"name\" must not blank. ");
        String className = ocrHandler.getClass().getName();
        ocrHandler.setCommonProperties(getCommonProperties());
        handlers.put(name, ocrHandler);
        log.info("Register the ocr handler \"{}\" to \"{}\". ", className, name);
    }

    @Override
    public void deregisterHandler(String name) {
        Assert.notBlank(name, "Parameter \"name\" must not blank. ");
        OcrHandler remove = handlers.remove(name);
        if (remove != null) {
            String className = remove.getClass().getName();
            log.info("Deregister the ocr handler \"{}\" from \"{}\". ", className, name);
        }
    }

    @Override
    public OcrHandler getOcrHandler(String name) {
        Assert.notBlank(name, "Parameter \"name\" must not blank. ");
        return handlers.get(name);
    }

    @Override
    public <T> T handle(String name, Object type, Object data, Class<T> clazz, OcrFeature... features) {
        OcrHandler ocrHandler = getOcrHandler(name);
        Assert.notNull(ocrHandler
                , "The corresponding ocr handler could not be found by name. ");
        return ocrHandler.handle(type, data, clazz, features);
    }

}
