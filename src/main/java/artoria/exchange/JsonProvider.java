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
     * @param object Java object
     * @return Json string
     */
    String toJsonString(Object object);

    /**
     * Parse json string to java object.
     * @param text Json string
     * @param type Java object type
     * @param <T> Java object type you want return
     * @return Java object
     */
    <T> T parseObject(String text, Type type);

}
