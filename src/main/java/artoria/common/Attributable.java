package artoria.common;

import java.util.Map;

/**
 * Attribute able.
 * @author Kahle
 */
public interface Attributable {

    /**
     * Add attribute.
     * @param attributeName Attribute name
     * @param attributeValue Attribute value
     * @return Old attribute value if exist
     */
    Object addAttribute(String attributeName, Object attributeValue);

    /**
     * Remove attribute by name.
     * @param attributeName Attribute name
     * @return Old attribute value if exist
     */
    Object removeAttribute(String attributeName);

    /**
     * Get attribute by name.
     * @param attributeName Attribute name
     * @return Attribute value
     */
    Object getAttribute(String attributeName);

    /**
     * Get attributes to unmodifiable map.
     * @return Unmodifiable map that contain attributes
     */
    Map<String, Object> getAttributes();

    /**
     * Add attributes.
     * @param attributes Map that contain attributes
     */
    void addAttributes(Map<String, Object> attributes);

}
