/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.db;

import kunlun.core.Handler;

import java.util.Map;

/**
 * The database handler, simplify interaction with the database,
 *      providing a unified interface for executing common database operations.
 * @author Kahle
 */
public interface DbHandler extends Handler {

    /**
     * Get the common properties of the settings.
     * @return The common properties that is set
     */
    Map<Object, Object> getCommonProperties();

    /**
     * Set common properties for the ocr handler.
     * @param properties The properties to be set
     */
    void setCommonProperties(Map<?, ?> properties);

    /**
     * Perform database related operations.
     * The arguments mean (most of the scenes):
     *      0 strategy or operation or null,
     *      1 input object,
     *      2 return value type
     * @param arguments The arguments required for the operation
     * @return The result of the operation
     */
    Object execute(Object[] arguments);

}
