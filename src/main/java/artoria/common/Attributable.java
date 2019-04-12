package artoria.common;

import java.util.Map;

/**
 * Attribute able.
 * @author Kahle
 */
public interface Attributable {

    /**
     * Add attributes.
     * @param attributes Map that contain attributes
     */
    void putAll(Map<String, Object> attributes);

    /**
     * Add attribute.
     * @param name Attribute name
     * @param value Attribute value
     * @return Old attribute value if exist
     */
    Object put(String name, Object value);

    /**
     * Remove attribute by name.
     * @param name Attribute name
     * @return Old attribute value if exist
     */
    Object remove(String name);

    /**
     * Get attribute by name.
     * @param name Attribute name
     * @return Attribute value
     */
    Object get(String name);

    /**
     * Get attributes to unmodifiable map.
     * @return Unmodifiable map that contain attributes
     */
    Map<String, Object> entries();

    /**
     * Determine whether the attribute name is included.
     * @param name The attribute name to judge
     * @return If true, the attribute name is included, else false
     */
    boolean containsName(String name);

    /**
     * Clear attributes.
     */
    void clear();

}
