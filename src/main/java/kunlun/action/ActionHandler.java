/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.action;

import kunlun.core.Handler;

import java.util.Map;

/**
 * The action handler.
 * @author Kahle
 */
public interface ActionHandler extends Handler {

    /**
     * Get the common properties of the settings.
     * @return The common properties that is set
     */
    Map<Object, Object> getCommonProperties();

    /**
     * Set common properties for the action handler.
     * @param properties The properties to be set
     */
    void setCommonProperties(Map<?, ?> properties);

    /**
     * Execute a specific logic.
     * The arguments mean (most of the scenes):
     *      0 strategy or operation or null,
     *      1 input object,
     *      2 return value type
     * @param arguments The arguments to the execution of a specific logic
     * @return The execution result of a specific logic
     */
    Object execute(Object[] arguments);

}
