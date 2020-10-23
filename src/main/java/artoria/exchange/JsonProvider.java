package artoria.exchange;

import java.lang.reflect.Type;

/**
 * Json provider.
 * @author Kahle
 */
public interface JsonProvider {

    /**
     * Serialize java object to json string.
     * @param object The java object
     * @param features The json features
     * @return The json string
     */
    String toJsonString(Object object, JsonFeature... features);

    /**
     * Parse json string to java object.
     * @param jsonString The json string
     * @param type The java object type
     * @param features The json features
     * @param <T> The java object type
     * @return The java object
     */
    <T> T parseObject(String jsonString, Type type, JsonFeature... features);

}
