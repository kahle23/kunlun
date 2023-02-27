package artoria.property;

import artoria.core.handler.PropertySupportHandler;

import java.util.Map;

/**
 * The property source.
 * @author Kahle
 */
public interface PropertySource extends PropertySupportHandler {

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

}
