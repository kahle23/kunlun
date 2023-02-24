package artoria.property;

import java.util.Map;

/**
 * The property source.
 * @author Kahle
 */
public interface PropertySource {

    /**
     * Get the property source name.
     * @return The source name
     */
    String getName();

    /**
     * Get the common properties of the settings.
     * @return The common properties that is set
     */
    Map<Object, Object> getCommonProperties();

    /**
     * Set common properties for the property source.
     * @param properties The properties to be set
     */
    void setCommonProperties(Map<?, ?> properties);

    /**
     * Set property data in batches.
     * @param properties The property data to be set
     */
    void setProperties(Map<?, ?> properties);

    /**
     * Return all the properties.
     * @return The properties
     */
    Map<String, Object> getProperties();

    /**
     * Return whether the given property name is available.
     * @param name The given property name
     * @return The available or not
     */
    boolean containsProperty(String name);

    /**
     * Set the property name and property value.
     * @param name The property name
     * @param value The property value
     * @return The previous value associated with name, or null
     */
    Object setProperty(String name, Object value);

    /**
     * Return the property value associated with the given name.
     * @param name The property name
     * @return The property value
     */
    Object getProperty(String name);

    /**
     * Remove the property value based on the property name.
     * @param name The property name
     * @return The property value or null
     */
    Object removeProperty(String name);

}
