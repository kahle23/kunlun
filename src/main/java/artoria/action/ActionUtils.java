package artoria.action;

import artoria.logging.Logger;
import artoria.logging.LoggerFactory;
import artoria.util.Assert;

import java.lang.reflect.Type;

import static artoria.common.constant.Symbols.EMPTY_STRING;

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

    public static <T> T execute(Object input, String actionName, String operation, Type type) {

        return getActionProvider().execute(input, actionName, operation, type);
    }

    public static <T> T execute(Object input, String actionName, Type type) {

        return getActionProvider().execute(input, actionName, EMPTY_STRING, type);
    }

}
