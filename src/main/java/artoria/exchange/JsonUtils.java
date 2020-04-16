package artoria.exchange;

import artoria.logging.Logger;
import artoria.logging.LoggerFactory;
import artoria.util.Assert;

import java.lang.reflect.Type;

/**
 * Json tools.
 * @author Kahle
 */
public class JsonUtils {
    private static final JsonProvider DEFAULT_JSON_PROVIDER = new SimpleJsonProvider();
    private static Logger log = LoggerFactory.getLogger(JsonUtils.class);
    private static JsonProvider jsonProvider;

    public static JsonProvider getJsonProvider() {

        return jsonProvider != null ? jsonProvider : DEFAULT_JSON_PROVIDER;
    }

    public static void setJsonProvider(JsonProvider jsonProvider) {
        Assert.notNull(jsonProvider, "Parameter \"jsonProvider\" must not null. ");
        log.info("Set json provider: {}", jsonProvider.getClass().getName());
        JsonUtils.jsonProvider = jsonProvider;
    }

    public static String toJsonString(Object object) {

        return getJsonProvider().toJsonString(object);
    }

    public static <T> T parseObject(String jsonString, Type type) {

        return getJsonProvider().parseObject(jsonString, type);
    }

}
