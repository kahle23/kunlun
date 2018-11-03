package artoria.exchange;

import artoria.util.Assert;

import java.util.List;
import java.util.logging.Logger;

/**
 * Json tools.
 * @author Kahle
 */
public class JsonUtils {
    private static final JsonHandler DEFAULT_JSON_HANDLER = new SimpleJsonHandler();
    private static Logger log = Logger.getLogger(JsonUtils.class.getName());
    private static JsonHandler jsonHandler;

    public static JsonHandler getJsonHandler() {
        return jsonHandler != null
                ? jsonHandler : DEFAULT_JSON_HANDLER;
    }

    public static void setJsonHandler(JsonHandler jsonHandler) {
        Assert.notNull(jsonHandler, "Parameter \"jsonHandler\" must not null. ");
        log.info("Set json handler: " + jsonHandler.getClass().getName());
        JsonUtils.jsonHandler = jsonHandler;
    }

    public static <T> T parseObject(String text, Class<T> clazz) {

        return getJsonHandler().parseObject(text, clazz);
    }

    public static <T> List<T> parseArray(String text, Class<T> clazz) {

        return getJsonHandler().parseArray(text, clazz);
    }

    public static String toJsonString(Object object) {

        return getJsonHandler().toJsonString(object);
    }

    public static String toJsonString(Object object, boolean prettyFormat) {

        return getJsonHandler().toJsonString(object, prettyFormat);
    }

}
