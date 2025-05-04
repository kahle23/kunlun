/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.action;

import kunlun.common.constant.Nil;
import kunlun.core.Action;
import kunlun.data.tuple.Pair;
import kunlun.data.tuple.PairImpl;
import kunlun.logging.Logger;
import kunlun.logging.LoggerFactory;
import kunlun.util.Assert;
import kunlun.util.StrUtil;

import java.lang.reflect.Type;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static kunlun.common.constant.Numbers.ONE;
import static kunlun.common.constant.Numbers.ZERO;
import static kunlun.common.constant.Symbols.DOT;

/**
 * The simple action manager.
 * @author Kahle
 */
public class SimpleActionManager implements ActionManager {
    private static final Logger log = LoggerFactory.getLogger(SimpleActionManager.class);
    protected final Map<String, Action> actions;
    protected final Map<Type, String> shortcuts;

    protected SimpleActionManager(Map<String, Action> actions, Map<Type, String> shortcuts) {
        Assert.notNull(shortcuts, "Parameter \"shortcuts\" must not null. ");
        Assert.notNull(actions, "Parameter \"actions\" must not null. ");
        this.shortcuts = shortcuts;
        this.actions = actions;
    }

    public SimpleActionManager() {

        this(new ConcurrentHashMap<String, Action>(), new ConcurrentHashMap<Type, String>());
    }

    protected Pair<String, String> parseCommand(String command) {
        if (StrUtil.isBlank(command)) {
            return new PairImpl<String, String>(command, Nil.STR);
        }
        int indexOf = command.indexOf(DOT);
        if (indexOf <= ZERO) {
            return new PairImpl<String, String>(command, Nil.STR);
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
    public Object execute(String command, Object input, Object[] arguments) {
        // Strategy priority: command strategy > shortcut strategy
        Pair<String, String> pair = parseCommand(command);
        String actionName = pair.getLeft();
        String strategy = pair.getRight();
        // Process command in the shortcut.
        // When actionName is blank, strategy must also be blank.
        // (for example ".test" is not supported)
        if (StrUtil.isBlank(actionName) && input != null) {
            pair = parseCommand(getShortcut(input.getClass()));
            actionName = pair.getLeft();
            strategy = pair.getRight();
        }
        // Do execute.
        return getActionOrThrow(actionName).execute(strategy, input, arguments);
    }

}
