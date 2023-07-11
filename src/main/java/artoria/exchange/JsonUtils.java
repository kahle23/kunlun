package artoria.exchange;

import artoria.data.json.JsonProvider;

import java.lang.reflect.Type;

/**
 * The json tools.
 * @author Kahle
 */
@Deprecated // TODO: 2023/3/7 Deletable
public class JsonUtils {

    public static JsonProvider getJsonProvider() {

        return artoria.data.json.JsonUtils.getJsonProvider();
    }

    public static void setJsonProvider(JsonProvider jsonProvider) {

        artoria.data.json.JsonUtils.setJsonProvider(jsonProvider);
    }

    public static String toJsonString(Object object, Object... arguments) {

        return artoria.data.json.JsonUtils.toJsonString(object, arguments);
    }

    public static <T> T parseObject(String jsonString, Type type, Object... arguments) {

        return artoria.data.json.JsonUtils.parseObject(jsonString, type, arguments);
    }

}
