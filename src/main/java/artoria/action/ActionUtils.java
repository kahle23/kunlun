package artoria.action;

import artoria.logging.Logger;
import artoria.logging.LoggerFactory;
import artoria.util.Assert;

import java.lang.reflect.Type;

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
        getActionProvider().registerHandler("class:" + type.getName(), actionHandler);
    }

    public static void deregisterHandler(String actionName) {

        getActionProvider().deregisterHandler(actionName);
    }

    public static void deregisterHandler(Class<?> type) {
        Assert.notNull(type, "Parameter \"type\" must not null. ");
        getActionProvider().deregisterHandler(type.getName());
    }

    public static Object execute(String actionName, Object[] arguments) {

        return getActionProvider().execute(actionName, arguments);
    }

    public static <T> T execute(Object input, String actionName, Type type) {

        return getActionProvider().execute(input, actionName, type);
    }

    public static <T> T execute(Object input, Type type) {

        return getActionProvider().execute(input, EMPTY_STRING, type);
    }

}
