package artoria.action;

import artoria.action.handler.ActionHandler;
import artoria.action.handler.ExecuteHandler;
import artoria.action.handler.InfoHandler;
import artoria.action.handler.SearchHandler;
import artoria.logging.Logger;
import artoria.logging.LoggerFactory;
import artoria.util.Assert;
import artoria.util.MapUtils;
import artoria.util.ObjectUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static artoria.util.StringUtils.isNotBlank;

/**
 * The abstract action tools provider.
 * @author Kahle
 */
public abstract class AbstractActionProvider implements ActionProvider {
    private static Logger log = LoggerFactory.getLogger(AbstractActionProvider.class);
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

    public <T extends ActionHandler> T getActionHandler(String actionName, Class<T> clazz) {
        Assert.notBlank(actionName, "Parameter \"actionName\" must not blank. ");
        Assert.notNull(clazz, "Parameter \"clazz\" must not null. ");
        ActionHandler actionHandler = actionHandlers.get(actionName);
        Assert.notNull(actionHandler,
                "The corresponding action handler could not be found by input name. ");
        Assert.isInstanceOf(clazz, actionHandler,
                "The action handler found does not support the current action. ");
        return ObjectUtils.cast(actionHandler, clazz);
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
        actionHandler.attrs(getCommonProperties());
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
    public <T> T execute(Object input, String actionName, Class<T> clazz) {
        Assert.notNull(input, "Parameter \"input\" must not null. ");
        actionName = isNotBlank(actionName) ? actionName : input.getClass().getName();
        ExecuteHandler handler = getActionHandler(actionName, ExecuteHandler.class);
        return handler.execute(input, clazz);
    }

    @Override
    public <T> T info(Object input, String actionName, Class<T> clazz) {
        Assert.notNull(input, "Parameter \"input\" must not null. ");
        actionName = isNotBlank(actionName) ? actionName : input.getClass().getName();
        InfoHandler handler = getActionHandler(actionName, InfoHandler.class);
        return handler.info(input, clazz);
    }

    @Override
    public <T> List<T> search(Object input, String actionName, Class<T> clazz) {
        Assert.notNull(input, "Parameter \"input\" must not null. ");
        actionName = isNotBlank(actionName) ? actionName : input.getClass().getName();
        SearchHandler handler = getActionHandler(actionName, SearchHandler.class);
        return handler.search(input, clazz);
    }

}
