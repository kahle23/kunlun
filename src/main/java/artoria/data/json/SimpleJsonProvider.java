package artoria.data.json;

import artoria.logging.Logger;
import artoria.logging.LoggerFactory;
import artoria.util.Assert;
import artoria.util.MapUtils;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The simple json conversion provider.
 * @author Kahle
 */
public class SimpleJsonProvider implements JsonProvider {
    private static final Logger log = LoggerFactory.getLogger(SimpleJsonProvider.class);
    protected final Map<String, JsonHandler> handlers;
    protected final Map<String, Object> commonProperties;

    protected SimpleJsonProvider(Map<String, Object> commonProperties,
                                 Map<String, JsonHandler> handlers) {
        Assert.notNull(commonProperties, "Parameter \"commonProperties\" must not null. ");
        Assert.notNull(handlers, "Parameter \"handlers\" must not null. ");
        this.commonProperties = commonProperties;
        this.handlers = handlers;
    }

    public SimpleJsonProvider() {
        this(new ConcurrentHashMap<String, Object>(),
                new ConcurrentHashMap<String, JsonHandler>());
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
    public void registerHandler(String name, JsonHandler jsonHandler) {
        Assert.notNull(jsonHandler, "Parameter \"jsonHandler\" must not null. ");
        Assert.notBlank(name, "Parameter \"name\" must not blank. ");
        String className = jsonHandler.getClass().getName();
        jsonHandler.setCommonProperties(getCommonProperties());
        handlers.put(name, jsonHandler);
        log.info("Register the json handler \"{}\" to \"{}\". ", className, name);
    }

    @Override
    public void deregisterHandler(String name) {
        Assert.notBlank(name, "Parameter \"name\" must not blank. ");
        JsonHandler remove = handlers.remove(name);
        if (remove != null) {
            String className = remove.getClass().getName();
            log.info("Deregister the json handler \"{}\" from \"{}\". ", className, name);
        }
    }

    @Override
    public JsonHandler getJsonHandler(String name) {
        Assert.notBlank(name, "Parameter \"name\" must not blank. ");
        JsonHandler jsonHandler = handlers.get(name);
        Assert.notNull(jsonHandler
                , "The corresponding json handler could not be found by name. ");
        return jsonHandler;
    }

    @Override
    public boolean isJsonObject(String name, String jsonString) {

        return getJsonHandler(name).isJsonObject(jsonString);
    }

    @Override
    public boolean isJsonArray(String name, String jsonString) {

        return getJsonHandler(name).isJsonArray(jsonString);
    }

    @Override
    public String toJsonString(String name, Object object, Object... arguments) {

        return getJsonHandler(name).toJsonString(object, arguments);
    }

    @Override
    public <T> T parseObject(String name, String jsonString, Type type, Object... arguments) {

        return getJsonHandler(name).parseObject(jsonString, type, arguments);
    }

}
