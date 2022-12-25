package artoria.action;

import artoria.logging.Logger;
import artoria.logging.LoggerFactory;
import artoria.util.*;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static artoria.common.Constants.ONE;
import static artoria.common.Constants.TWO;

/**
 * The abstract action tools provider.
 * @author Kahle
 */
public abstract class AbstractActionProvider implements ActionProvider {
    private static final Logger log = LoggerFactory.getLogger(AbstractActionProvider.class);
    protected final Map<String, ActionHandler> actionHandlers;
    protected final Map<String, Object> commonProperties;

    protected AbstractActionProvider(Map<String, Object> commonProperties,
                                     Map<String, ActionHandler> actionHandlers) {
        Assert.notNull(commonProperties, "Parameter \"commonProperties\" must not null. ");
        Assert.notNull(actionHandlers, "Parameter \"actionHandlers\" must not null. ");
        this.commonProperties = commonProperties;
        this.actionHandlers = actionHandlers;
    }

    public AbstractActionProvider() {
        this(new ConcurrentHashMap<String, Object>(),
                new ConcurrentHashMap<String, ActionHandler>());
    }

    protected ActionHandler getActionHandlerInner(String actionName, Object input) {
        String name = null;
        // TODO: 2023/5/5 The action name is required in the future, no longer handle "class:"
        if (StringUtils.isNotBlank(actionName)) { name = actionName; }
        else {
            // The input parameter should not be null.
            if (input != null) {
                // Other scenarios concatenate class names directly.
                name = "class:" + input.getClass().getName();
            }
        }
        Assert.notBlank(name, "Parameter \"actionName\" must not blank. ");
        ActionHandler actionHandler = actionHandlers.get(name);
        Assert.notNull(actionHandler,
                "The corresponding action handler could not be found by name. ");
        return actionHandler;
    }

    @Override
    public void registerCommonProperties(Map<?, ?> commonProperties) {
        if (MapUtils.isEmpty(commonProperties)) { return; }
        for (Map.Entry<?, ?> entry : commonProperties.entrySet()) {
            String keyStr = String.valueOf(entry.getKey());
            this.commonProperties.put(keyStr, entry.getValue());
        }
    }

    @Override
    public void clearCommonProperties() {

        this.commonProperties.clear();
    }

    @Override
    public Map<String, Object> getCommonProperties() {

        return Collections.unmodifiableMap(commonProperties);
    }

    @Override
    public void registerHandler(String actionName, ActionHandler actionHandler) {
        Assert.notNull(actionHandler, "Parameter \"actionHandler\" must not null. ");
        Assert.notBlank(actionName, "Parameter \"actionName\" must not blank. ");
        String className = actionHandler.getClass().getName();
        actionHandlers.put(actionName, actionHandler);
        actionHandler.setCommonProperties(getCommonProperties());
        log.info("Register the action handler \"{}\" to \"{}\". ", className, actionName);
    }

    @Override
    public void deregisterHandler(String actionName) {
        Assert.notBlank(actionName, "Parameter \"actionName\" must not blank. ");
        ActionHandler remove = actionHandlers.remove(actionName);
        if (remove != null) {
            String className = remove.getClass().getName();
            log.info("Deregister the action handler \"{}\" from \"{}\". ", className, actionName);
        }
    }

    @Override
    public ActionHandler getActionHandler(String actionName) {
        Assert.notBlank(actionName, "Parameter \"actionName\" must not blank. ");
        return actionHandlers.get(actionName);
    }

    @Override
    public Object execute(String actionName, Object[] arguments) {
        // Parameter "arguments" usually is: 0 strategy or operation, 1 input, 2 type
        Object input = null;
        if (ArrayUtils.isNotEmpty(arguments) && arguments.length >= TWO) {
            input = arguments[ONE];
        }
        return getActionHandlerInner(actionName, input).execute(arguments);
    }

    @Override
    public <T> T execute(Object input, String actionName, String operation, Type type) {

        return ObjectUtils.cast(execute(actionName, new Object[]{ operation, input, type }));
    }

}
