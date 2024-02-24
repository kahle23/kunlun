/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.action;

import kunlun.logging.Logger;
import kunlun.logging.LoggerFactory;
import kunlun.util.Assert;
import kunlun.util.MapUtils;
import kunlun.util.ObjectUtils;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

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

    protected ActionHandler getActionHandlerInner(String actionName) {
        Assert.notBlank(actionName, "Parameter \"actionName\" must not blank. ");
        ActionHandler actionHandler = actionHandlers.get(actionName);
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
        log.debug("Register the action handler \"{}\" to \"{}\". ", className, actionName);
    }

    @Override
    public void deregisterHandler(String actionName) {
        Assert.notBlank(actionName, "Parameter \"actionName\" must not blank. ");
        ActionHandler remove = actionHandlers.remove(actionName);
        if (remove != null) {
            String className = remove.getClass().getName();
            log.debug("Deregister the action handler \"{}\" from \"{}\". ", className, actionName);
        }
    }

    @Override
    public ActionHandler getActionHandler(String actionName) {
        Assert.notBlank(actionName, "Parameter \"actionName\" must not blank. ");
        return actionHandlers.get(actionName);
    }

    @Override
    public Object execute(String actionName, Object[] arguments) {
        // Parameter "arguments" is usually: 0 strategy or operation, 1 input, 2 type
        return getActionHandlerInner(actionName).execute(arguments);
    }

    @Override
    public <T> T execute(String actionName, Object input, String operation, Type type) {

        return ObjectUtils.cast(execute(actionName, new Object[]{ operation, input, type }));
    }

}
