package artoria.data.xml;

import java.lang.reflect.Type;
import java.util.Map;

/**
 * The xml conversion provider.
 * @author Kahle
 */
public interface XmlProvider {

    /**
     * Register common properties' information.
     * @param commonProperties The common properties
     */
    void registerCommonProperties(Map<?, ?> commonProperties);

    /**
     * Clears common properties' information.
     */
    void clearCommonProperties();

    /**
     * Get common properties' information.
     * @return The common properties
     */
    Map<String, Object> getCommonProperties();

    /**
     * Get the default handler name.
     * @return The default handler name
     */
    String getDefaultHandlerName();

    /**
     * Set the default handler name.
     * Depending on the implementation class, this method may throw an error
     *  (i.e. it does not allow the modification of the default handler name).
     * @param defaultHandlerName The default handler name
     */
    void setDefaultHandlerName(String defaultHandlerName);

    /**
     * Register the json handler.
     * @param name The json handler name
     * @param xmlHandler The json handler
     */
    void registerHandler(String name, XmlHandler xmlHandler);

    /**
     * Deregister the json handler.
     * @param name The json handler name
     */
    void deregisterHandler(String name);

    /**
     * Get the json handler by name.
     * @param name The json handler name
     * @return The json handler
     */
    XmlHandler getXmlHandler(String name);

    /**
     * Serialize java object to xml string.
     * @param name The xml handler name
     * @param object The java object
     * @param arguments The arguments (maybe is configuration or feature)
     * @return The xml string
     */
    String toXmlString(String name, Object object, Object... arguments);

    /**
     * Parse xml string to java object.
     * @param name The xml handler name
     * @param xmlString The xml string
     * @param type The java object type
     * @param arguments The arguments (maybe is configuration or feature)
     * @param <T> The java object type
     * @return The java object
     */
    <T> T parseObject(String name, String xmlString, Type type, Object... arguments);

}
