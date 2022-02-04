package artoria.action;

import artoria.action.handler.ActionHandler;
import artoria.logging.Logger;
import artoria.logging.LoggerFactory;
import artoria.util.Assert;

import java.util.List;

import static artoria.common.Constants.EMPTY_STRING;

/**
 * The action tools.
 * @author Kahle
 */
public class ActionUtils {
    private static Logger log = LoggerFactory.getLogger(ActionUtils.class);
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

    public static void registerHandler(Class<?> type, ActionHandler actionHandler) {
        Assert.notNull(type, "Parameter \"type\" must not null. ");
        getActionProvider().registerHandler(type.getName(), actionHandler);
    }

    public static void deregisterHandler(String actionName) {

        getActionProvider().deregisterHandler(actionName);
    }

    public static void deregisterHandler(Class<?> type) {
        Assert.notNull(type, "Parameter \"type\" must not null. ");
        getActionProvider().deregisterHandler(type.getName());
    }

    public static <T> T execute(Object input, String actionName, Class<T> clazz) {

        return getActionProvider().execute(input, actionName, clazz);
    }

    public static <T> T execute(Object input, Class<T> clazz) {

        return getActionProvider().execute(input, EMPTY_STRING, clazz);
    }

    public static <T> T info(Object input, String actionName, Class<T> clazz) {

        return getActionProvider().info(input, actionName, clazz);
    }

    public static <T> T info(Object input, Class<T> clazz) {

        return getActionProvider().info(input, EMPTY_STRING, clazz);
    }

    public static <T> List<T> search(Object input, String actionName, Class<T> clazz) {

        return getActionProvider().search(input, actionName, clazz);
    }

    public static <T> List<T> search(Object input, Class<T> clazz) {

        return getActionProvider().search(input, EMPTY_STRING, clazz);
    }

}
