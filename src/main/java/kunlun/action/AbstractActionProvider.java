/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.action;

import kunlun.common.constant.Nulls;
import kunlun.core.Action;
import kunlun.data.tuple.Pair;
import kunlun.data.tuple.PairImpl;
import kunlun.logging.Logger;
import kunlun.logging.LoggerFactory;
import kunlun.util.Assert;
import kunlun.util.MapUtils;
import kunlun.util.ObjectUtils;
import kunlun.util.StringUtils;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static kunlun.common.constant.Numbers.*;
import static kunlun.common.constant.Symbols.DOT;

/**
 * The abstract action tools provider.
 * @author Kahle
 */
public abstract class AbstractActionProvider implements ActionProvider {
    private static final Logger log = LoggerFactory.getLogger(AbstractActionProvider.class);
    protected final Map<String, Object> commonProperties;
    protected final Map<String, Action> actions;
    protected final Map<Type, String> shortcuts;

    protected AbstractActionProvider(Map<String, Object> commonProperties,
                                     Map<String, Action> actions,
                                     Map<Type,   String> shortcuts) {
        Assert.notNull(commonProperties, "Parameter \"commonProperties\" must not null. ");
        Assert.notNull(actions, "Parameter \"actions\" must not null. ");
        Assert.notNull(shortcuts, "Parameter \"shortcuts\" must not null. ");
        this.commonProperties = commonProperties;
        this.shortcuts = shortcuts;
        this.actions = actions;
    }

    public AbstractActionProvider() {
        this(new ConcurrentHashMap<String, Object>(),
                new ConcurrentHashMap<String, Action>(),
                new ConcurrentHashMap<Type, String>());
    }

    protected Pair<String, String> parseCommand(String command) {
        if (StringUtils.isBlank(command)) {
            return new PairImpl<String, String>(command, Nulls.STR);
        }
        int indexOf = command.indexOf(DOT);
        if (indexOf <= ZERO) {
            return new PairImpl<String, String>(command, Nulls.STR);
        }
        String actionName = command.substring(ZERO, indexOf);
        String strategy = command.substring(indexOf + ONE);
        return new PairImpl<String, String>(actionName, strategy);
    }

    protected Action getActionOrThrow(String actionName) {
        Assert.notBlank(actionName, "Parameter \"actionName\" must not blank. ");
        Action action = actions.get(actionName);
        Assert.notNull(action
                , "The corresponding action handler could not be found by name. ");
        return action;
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
    public void registerAction(String actionName, Action action) {
        Assert.notBlank(actionName, "Parameter \"actionName\" must not blank. ");
        Assert.notNull(action, "Parameter \"action\" must not null. ");
        String className = action.getClass().getName();
        actions.put(actionName, action);
        log.debug("Register the action \"{}\" to \"{}\". ", className, actionName);
    }

    @Override
    public void deregisterAction(String actionName) {
        Assert.notBlank(actionName, "Parameter \"actionName\" must not blank. ");
        Action remove = actions.remove(actionName);
        if (remove != null) {
            String className = remove.getClass().getName();
            log.debug("Deregister the action \"{}\" from \"{}\". ", className, actionName);
        }
    }

    @Override
    public Action getAction(String actionName) {
        Assert.notBlank(actionName, "Parameter \"actionName\" must not blank. ");
        return actions.get(actionName);
    }

    @Override
    public void registerShortcut(Type inputType, String command) {
        Assert.notBlank(command, "Parameter \"command\" must not blank. ");
        Assert.notNull(inputType, "Parameter \"inputType\" must not null. ");
        shortcuts.put(inputType, command);
        log.debug("Register the action shortcut \"{}\" to \"{}\". ", inputType, command);
    }

    @Override
    public void deregisterShortcut(Type inputType) {
        Assert.notNull(inputType, "Parameter \"inputType\" must not null. ");
        String remove = shortcuts.remove(inputType);
        if (remove != null) {
            log.debug("Deregister the action shortcut \"{}\" from \"{}\". ", inputType, remove);
        }
    }

    @Override
    public String getShortcut(Type inputType) {
        Assert.notNull(inputType, "Parameter \"inputType\" must not null. ");
        return shortcuts.get(inputType);
    }

    @Override
    public Object execute(String command, Object[] arguments) {
        // Parameter "arguments" is usually: 0 strategy or operation, 1 input
        // Strategy priority: command strategy > shortcut strategy > arguments strategy
        Pair<String, String> pair = parseCommand(command);
        String actionName = pair.getLeft();
        String strategy = pair.getRight();
        // Process command in the shortcut.
        // When actionName is blank, strategy must also be blank.
        // (for example ".test" is not supported)
        if (StringUtils.isBlank(actionName)) {
            Object input = arguments.length >= TWO ? arguments[ONE] : null;
            if (input != null) {
                pair = parseCommand(getShortcut(input.getClass()));
                actionName = pair.getLeft();
                strategy = pair.getRight();
            }
        }
        // If the external strategy is not blank, replace the strategy in the arguments.
        if (StringUtils.isNotBlank(strategy) && arguments.length >= ONE) {
            arguments[ZERO] = strategy;
        }
        // Do execute.
        return getActionOrThrow(actionName).execute(arguments);
    }

    @Override
    public <T> T execute(String command, Object input) {

        return ObjectUtils.cast(execute(command, new Object[]{ null, input }));
    }

}
