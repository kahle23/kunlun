/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.action;

import kunlun.core.Action;

import java.lang.reflect.Type;
import java.util.Map;

/**
 * The action tools provider.
 * @author Kahle
 */
public interface ActionProvider {

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
     * Register the action.
     * @param actionName The action name
     * @param action The action
     */
    void registerAction(String actionName, Action action);

    /**
     * Deregister the action.
     * @param actionName The action name
     */
    void deregisterAction(String actionName);

    /**
     * Get the action (the action name is fixed).
     * @param actionName The action name
     * @return The action or null
     */
    Action getAction(String actionName);

    /**
     * Register the shortcut for action.
     * @param inputType The type of the input parameter
     * @param command The command that contain the action name and strategy
     */
    void registerShortcut(Type inputType, String command);

    /**
     * Deregister the shortcut for action.
     * @param inputType The type of the input parameter
     */
    void deregisterShortcut(Type inputType);

    /**
     * Get the shortcut.
     * @param inputType The type of the input parameter
     * @return The command that contain the action name and strategy
     */
    String getShortcut(Type inputType);

    /**
     * Execute a specific logic.
     *
     * The arguments mean (most of the scenes):
     *      0 strategy or operation or null,
     *      1 input object,
     *
     * Strategy priority: command strategy > shortcut strategy > arguments strategy
     *
     * @param command The command that contain the action name and strategy
     * @param arguments The arguments to the execution of a specific logic
     * @return The execution result of a specific logic
     */
    Object execute(String command, Object[] arguments);

    /**
     * Execute a specific logic.
     *
     * @param command The command that contain the action name and strategy
     * @param input The input parameter to be handled
     * @param <T> The generic type of the return value
     * @return The return value corresponding to the action
     */
    <T> T execute(String command, Object input);

}
