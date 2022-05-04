package artoria.exchange;

import java.lang.reflect.Type;

/**
 * Xml provider.
 * @author Kahle
 */
public interface XmlProvider {

    /**
     * Serialize java object to xml string.
     * @param object The java object
     * @param features The xml features
     * @return The xml string
     */
    String toXmlString(Object object, XmlFeature... features);

    /**
     * Parse xml string to java object.
     * @param xmlString The xml string
     * @param type The java object type
     * @param features The xml features
     * @param <T> The java object type
     * @return The java object
     */
    <T> T parseObject(String xmlString, Type type, XmlFeature... features);

}
