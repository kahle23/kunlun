package artoria.data.xml;

import java.lang.reflect.Type;

/**
 * The xml conversion provider.
 * @author Kahle
 */
public interface XmlProvider {

    /**
     * Serialize java object to xml string.
     * @param object The java object
     * @param arguments The arguments (maybe is configuration or feature)
     * @return The xml string
     */
    String toXmlString(Object object, Object... arguments);

    /**
     * Parse xml string to java object.
     * @param xmlString The xml string
     * @param type The java object type
     * @param arguments The arguments (maybe is configuration or feature)
     * @param <T> The java object type
     * @return The java object
     */
    <T> T parseObject(String xmlString, Type type, Object... arguments);

}
