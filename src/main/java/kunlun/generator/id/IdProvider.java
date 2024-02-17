/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.generator.id;

import java.util.Map;

/**
 * The identifier provider.
 * @author Kahle
 */
public interface IdProvider {

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
     * Register the id generator.
     * @param name The id generator name
     * @param idGenerator The id generator
     */
    void registerGenerator(String name, IdGenerator idGenerator);

    /**
     * Deregister the id generator.
     * @param name The id generator name
     */
    void deregisterGenerator(String name);

    /**
     * Get the id generator by name.
     * @param name The id generator name
     * @return The id generator
     */
    IdGenerator getIdGenerator(String name);

    /**
     * Generate the next identifier based on the generator name.
     * @param name The id generator name
     * @param arguments The arguments at generation time
     * @return The next identifier
     */
    Object next(String name, Object... arguments);

}
