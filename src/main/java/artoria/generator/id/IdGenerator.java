package artoria.generator.id;

import artoria.generator.Generator;

import java.util.Map;

/**
 * The identifier generator.
 * @author Kahle
 */
public interface IdGenerator extends Generator {

    /**
     * Get the common properties of the settings.
     * @return The common properties that is set
     */
    Map<Object, Object> getCommonProperties();

    /**
     * Set common properties for the id generator.
     * @param properties The properties to be set
     */
    void setCommonProperties(Map<?, ?> properties);

    /**
     * Generate the next identifier.
     * The arguments are not required in most scenarios.
     * The return value is determined in the subclass, most likely string or number.
     * @param arguments The arguments at generation time
     * @return The next identifier
     */
    Object next(Object... arguments);

}
