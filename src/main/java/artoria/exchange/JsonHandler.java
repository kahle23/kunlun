package artoria.exchange;

import java.util.List;

/**
 * Json handler.
 * @author Kahle
 */
public interface JsonHandler {

    /**
     * Parse json string to java object.
     * @param text Json string
     * @param clazz Java object type
     * @param <T> Java object type
     * @return Java object
     */
    <T> T parseObject(String text, Class<T> clazz);

    /**
     * Parse json string to java array.
     * @param text Json string
     * @param clazz Java object type in array
     * @param <T> Java object type in array
     * @return Java array
     */
    <T> List<T> parseArray(String text, Class<T> clazz);

    /**
     * Serialize java object to json string.
     * @param object Java object
     * @return Json string
     */
    String toJsonString(Object object);

    /**
     * Serialize java object to json string.
     * @param object Java object
     * @param prettyFormat Serialize for pretty format
     * @return Json string
     */
    String toJsonString(Object object, boolean prettyFormat);

}
