package artoria.exchange;

import java.lang.reflect.Type;

/**
 * Json provider.
 * @author Kahle
 */
public interface JsonProvider {

    /**
     * Serialize java object to pretty format json string.
     * @return If true will serialize pretty format json else false
     */
    boolean getPrettyFormat();

    /**
     * Serialize java object to json string.
     * @param object The java object
     * @return The json string
     */
    String toJsonString(Object object);

    /**
     * Parse json string to java object.
     * @param jsonString The json string
     * @param type The java object type
     * @param <T> The java object type
     * @return The java object
     */
    <T> T parseObject(String jsonString, Type type);

}
