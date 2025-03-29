/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.action;

import kunlun.action.event.Event;
import kunlun.action.event.support.SimpleEventCollector;
import kunlun.action.message.support.SimpleMessageHandler;
import kunlun.core.Action;
import kunlun.logging.Logger;
import kunlun.logging.LoggerFactory;
import kunlun.message.model.Message;
import kunlun.message.model.Subscribe;
import kunlun.util.Assert;

import java.lang.reflect.Type;

import static kunlun.common.constant.Symbols.EMPTY_STRING;

/**
 * The action tools.
 * @author Kahle
 */
public class ActionUtil {
    private static final Logger log = LoggerFactory.getLogger(ActionUtil.class);
    private static volatile ActionProvider actionProvider;

    public static ActionProvider getActionProvider() {
        if (actionProvider != null) { return actionProvider; }
        synchronized (ActionUtil.class) {
            if (actionProvider != null) { return actionProvider; }
            ActionUtil.setActionProvider(new SimpleActionProvider());
            String name = "event-collector";
            registerAction(name, new SimpleEventCollector());
            registerShortcut(Event.class, name);
            name = "mq";
            registerAction(name, new SimpleMessageHandler());
            registerShortcut(Message.class,   name);
            registerShortcut(Subscribe.class, name);
            return actionProvider;
        }
    }

    public static void setActionProvider(ActionProvider actionProvider) {
        Assert.notNull(actionProvider, "Parameter \"actionProvider\" must not null. ");
        log.debug("Set action provider: {}", actionProvider.getClass().getName());
        ActionUtil.actionProvider = actionProvider;
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

    public static Object execute(String command, Object[] arguments) {

        return getActionProvider().execute(command, arguments);
    }

    public static <T> T execute(String command, Object input) {

        return getActionProvider().execute(command, input);
    }

    public static <T> T execute(Object input) {

        return getActionProvider().execute(EMPTY_STRING, input);
    }

}
