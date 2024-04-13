/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.action;

import kunlun.logging.Logger;
import kunlun.logging.LoggerFactory;
import kunlun.util.Assert;

import java.lang.reflect.Type;

import static kunlun.common.constant.Symbols.EMPTY_STRING;

/**
 * The action tools.
 * @author Kahle
 */
public class ActionUtils {
    private static final Logger log = LoggerFactory.getLogger(ActionUtils.class);
    private static volatile ActionProvider actionProvider;

    public static ActionProvider getActionProvider() {
        if (actionProvider != null) { return actionProvider; }
        synchronized (ActionUtils.class) {
            if (actionProvider != null) { return actionProvider; }
            ActionUtils.setActionProvider(new SimpleActionProvider());
            return actionProvider;
        }
    }

    public static void setActionProvider(ActionProvider actionProvider) {
        Assert.notNull(actionProvider, "Parameter \"actionProvider\" must not null. ");
        log.info("Set action provider: {}", actionProvider.getClass().getName());
        ActionUtils.actionProvider = actionProvider;
    }

    public static void registerHandler(String actionName, ActionHandler actionHandler) {

        getActionProvider().registerHandler(actionName, actionHandler);
    }

    public static void deregisterHandler(String actionName) {

        getActionProvider().deregisterHandler(actionName);
    }

    public static ActionHandler getActionHandler(String actionName) {

        return getActionProvider().getActionHandler(actionName);
    }

    public static Object execute(String actionName, Object[] arguments) {

        return getActionProvider().execute(actionName, arguments);
    }

    public static <T> T execute(String actionName, Object input, String operation, Type type) {

        return getActionProvider().execute(actionName, input, operation, type);
    }

    public static <T> T execute(String actionName, Object input, Type type) {

        return getActionProvider().execute(actionName, input, EMPTY_STRING, type);
    }

    public static <T> T execute(String actionName, Object input) {

        return getActionProvider().execute(actionName, input, EMPTY_STRING, Object.class);
    }

}
