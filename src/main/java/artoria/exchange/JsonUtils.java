package artoria.exchange;

import artoria.util.Assert;

import java.lang.reflect.Type;
import java.util.logging.Logger;

/**
 * Json tools.
 * @author Kahle
 */
public class JsonUtils {
    private static final JsonProvider DEFAULT_JSON_HANDLER = new SimpleJsonProvider();
    private static Logger log = Logger.getLogger(JsonUtils.class.getName());
    private static JsonProvider jsonProvider;

    public static JsonProvider getJsonProvider() {
        return jsonProvider != null
                ? jsonProvider : DEFAULT_JSON_HANDLER;
    }

    public static void setJsonProvider(JsonProvider jsonProvider) {
        Assert.notNull(jsonProvider, "Parameter \"jsonProvider\" must not null. ");
        log.info("Set json provider: " + jsonProvider.getClass().getName());
        JsonUtils.jsonProvider = jsonProvider;
    }

    public static String toJsonString(Object object) {

        return getJsonProvider().toJsonString(object);
    }

    public static <T> T parseObject(String text, Type type) {

        return getJsonProvider().parseObject(text, type);
    }

}
