/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.action;

import kunlun.action.event.support.SimpleEventCollector;
import kunlun.action.tool.net.MediaTypeAction;
import kunlun.common.constant.Nil;
import kunlun.core.Action;
import kunlun.data.Event;
import kunlun.logging.Logger;
import kunlun.logging.LoggerFactory;
import kunlun.message.model.Message;
import kunlun.message.model.Subscribe;
import kunlun.message.support.SimpleMessageBus;
import kunlun.util.Assert;
import kunlun.util.ObjUtil;

import java.lang.reflect.Type;

/**
 * The action tools.
 * @author Kahle
 */
public class ActionUtil {
    private static final Logger log = LoggerFactory.getLogger(ActionUtil.class);
    private static volatile ActionManager actionManager;

    protected static ActionManager init(ActionManager mgr) {
        String name = "event-collector";
        mgr.registerAction(name, new SimpleEventCollector());
        mgr.registerShortcut(Event.class, name);
        name = "internal-bus";
        mgr.registerAction(name, new SimpleMessageBus());
        mgr.registerShortcut(Message.class,   name);
        mgr.registerShortcut(Subscribe.class, name);
        name = "media-type";
        mgr.registerAction(name, new MediaTypeAction());
        return mgr;
    }

    public static ActionManager getActionManager() {
        if (actionManager != null) { return actionManager; }
        synchronized (ActionUtil.class) {
            if (actionManager != null) { return actionManager; }
            ActionUtil.setActionManager(init(new SimpleActionManager()));
            return actionManager;
        }
    }

    public static void setActionManager(ActionManager actionManager) {
        Assert.notNull(actionManager, "Parameter \"actionManager\" must not null. ");
        log.debug("Set action manager: {}", actionManager.getClass().getName());
        ActionUtil.actionManager = actionManager;
    }

    public static void registerAction(String actionName, Action action) {

        getActionManager().registerAction(actionName, action);
    }

    public static void deregisterAction(String actionName) {

        getActionManager().deregisterAction(actionName);
    }

    public static Action getAction(String actionName) {

        return getActionManager().getAction(actionName);
    }

    public static void registerShortcut(Type inputType, String command) {

        getActionManager().registerShortcut(inputType, command);
    }

    public static void deregisterShortcut(Type inputType) {

        getActionManager().deregisterShortcut(inputType);
    }

    public static Object rawExecute(String command, Object input, Object[] arguments) {

        return getActionManager().execute(command, input, arguments);
    }

    public static <T> T execute(String command, Object input, Object... arguments) {

        return ObjUtil.cast(getActionManager().execute(command, input, arguments));
    }

    public static <T> T execute(Object input) {

        return execute(Nil.STR, input);
    }

}
