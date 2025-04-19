/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.action;

import kunlun.core.Action;
import kunlun.logging.Logger;
import kunlun.logging.LoggerFactory;

import java.lang.reflect.Type;

/**
 * The action tools.
 * @author Kahle
 */
@Deprecated
public class ActionUtils {
    private static final Logger log = LoggerFactory.getLogger(ActionUtils.class);

    public static ActionManager getActionProvider() {

        return ActionUtil.getActionManager();
    }

    public static void setActionProvider(ActionManager actionProvider) {

        ActionUtil.setActionManager(actionProvider);
    }

    public static void registerAction(String actionName, Action action) {

        getActionProvider().registerAction(actionName, action);
    }

    public static void deregisterAction(String actionName) {

        getActionProvider().deregisterAction(actionName);
    }

    public static Action getAction(String actionName) {

        return getActionProvider().getAction(actionName);
    }

    public static void registerShortcut(Type inputType, String command) {

        getActionProvider().registerShortcut(inputType, command);
    }

    public static void deregisterShortcut(Type inputType) {

        getActionProvider().deregisterShortcut(inputType);
    }

//    public static Object execute(String command, Object[] arguments) {
//
//        return getActionProvider().execute(command, arguments);
//    }

    public static <T> T execute(String command, Object input) {

        return ActionUtil.execute(command, input);
    }

    public static <T> T execute(Object input) {

        return ActionUtil.execute(input);
    }

}
