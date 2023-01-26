package artoria.data.json;

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

    public static JsonProvider getJsonProvider() {
        if (jsonProvider != null) { return jsonProvider; }
        synchronized (JsonUtils.class) {
            if (jsonProvider != null) { return jsonProvider; }
            JsonUtils.setJsonProvider(new SimpleJsonProvider());
            return jsonProvider;
        }
    }

    public static void setJsonProvider(JsonProvider jsonProvider) {
        Assert.notNull(jsonProvider, "Parameter \"jsonProvider\" must not null. ");
        log.info("Set json provider: {}", jsonProvider.getClass().getName());
        JsonUtils.jsonProvider = jsonProvider;
    }

    public static String toJsonString(Object object, JsonFeature... features) {

        return getJsonProvider().toJsonString(object, features);
    }

    public static <T> T parseObject(String jsonString, Type type, JsonFeature... features) {

        return getJsonProvider().parseObject(jsonString, type, features);
    }

}
