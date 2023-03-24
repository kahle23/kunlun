package artoria.data.json;

import artoria.data.json.support.SimpleJsonHandler;
import artoria.logging.Logger;
import artoria.logging.LoggerFactory;
import artoria.util.Assert;

import java.lang.reflect.Type;

/**
 * The json conversion tools.
 * @author Kahle
 */
public class JsonUtils {
    private static final Logger log = LoggerFactory.getLogger(JsonUtils.class);
    private static volatile JsonProvider jsonProvider;
    private static String defaultHandlerName = "default";

    public static JsonProvider getJsonProvider() {
        if (jsonProvider != null) { return jsonProvider; }
        synchronized (JsonUtils.class) {
            if (jsonProvider != null) { return jsonProvider; }
            JsonUtils.setJsonProvider(new SimpleJsonProvider());
            JsonUtils.registerHandler(defaultHandlerName, new SimpleJsonHandler());
            return jsonProvider;
        }
    }

    public static void setJsonProvider(JsonProvider jsonProvider) {
        Assert.notNull(jsonProvider, "Parameter \"jsonProvider\" must not null. ");
        log.info("Set json provider: {}", jsonProvider.getClass().getName());
        JsonUtils.jsonProvider = jsonProvider;
    }

    public static String getDefaultHandlerName() {

        return defaultHandlerName;
    }

    public static void setDefaultHandlerName(String defaultHandlerName) {
        Assert.notBlank(defaultHandlerName, "Parameter \"defaultHandlerName\" must not blank. ");
        JsonUtils.defaultHandlerName = defaultHandlerName;
    }

    public static void registerHandler(String name, JsonHandler jsonHandler) {

        getJsonProvider().registerHandler(name, jsonHandler);
    }

    public static void deregisterHandler(String name) {

        getJsonProvider().deregisterHandler(name);
    }

    public static JsonHandler getJsonHandler(String name) {

        return getJsonProvider().getJsonHandler(name);
    }

    public static boolean isJsonObject(String jsonString) {

        return getJsonProvider().isJsonObject(getDefaultHandlerName(), jsonString);
    }

    public static boolean isJsonObject(String name, String jsonString) {

        return getJsonProvider().isJsonObject(name, jsonString);
    }

    public static boolean isJsonArray(String jsonString) {

        return getJsonProvider().isJsonArray(getDefaultHandlerName(), jsonString);
    }

    public static boolean isJsonArray(String name, String jsonString) {

        return getJsonProvider().isJsonArray(name, jsonString);
    }

    public static String toJsonString(Object object, Object... arguments) {

        return getJsonProvider().toJsonString(getDefaultHandlerName(), object, arguments);
    }

    public static String toJsonString(String name, Object object, Object... arguments) {

        return getJsonProvider().toJsonString(name, object, arguments);
    }

    public static <T> T parseObject(String jsonString, Type type, Object... arguments) {

        return getJsonProvider().parseObject(getDefaultHandlerName(), jsonString, type, arguments);
    }

    public static <T> T parseObject(String name, String jsonString, Type type, Object... arguments) {

        return getJsonProvider().parseObject(name, jsonString, type, arguments);
    }

}
