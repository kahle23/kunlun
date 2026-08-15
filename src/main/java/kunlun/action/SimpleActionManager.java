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
import kunlun.util.CastUtil;
import kunlun.util.StrUtil;

import java.lang.reflect.Type;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static kunlun.common.constant.Numbers.ONE;
import static kunlun.common.constant.Numbers.ZERO;
import static kunlun.common.constant.Symbols.DOT;

/**
 * 简单的 action 管理器.<br />
 * <p>
 * 使用两张并发 Map 维护注册表："action 名称 → action 实例" 与 "输入类型 → command"，
 * 注册与执行可在多线程环境下并发进行.
 *
 * @author Kahle
 */
public class SimpleActionManager implements ActionManager {
    private static final Logger log = LoggerFactory.getLogger(SimpleActionManager.class);
    /**
     * action 注册表：action 名称 → action 实例
     * */
    protected final Map<String, Action> actions;
    /**
     * shortcut 注册表：输入类型 → command（含 action 名称与策略）
     * */
    protected final Map<Type, String> shortcuts;

    /**
     * 以指定的两张注册表构造（便于外部定制 Map 的实现）.
     *
     * @param actions action 注册表
     * @param shortcuts shortcut 注册表
     */
    protected SimpleActionManager(Map<String, Action> actions, Map<Type, String> shortcuts) {
        Assert.notNull(shortcuts, "Parameter \"shortcuts\" must not null. ");
        Assert.notNull(actions, "Parameter \"actions\" must not null. ");
        this.shortcuts = shortcuts;
        this.actions = actions;
    }

    /**
     * 默认构造方法：使用两张 ConcurrentHashMap.
     */
    public SimpleActionManager() {

        this(new ConcurrentHashMap<String, Action>(), new ConcurrentHashMap<Type, String>());
    }

    /**
     * 将 command 解析为 "action 名称 + 策略".<br />
     * 按第一个 "." 切分：如 "notification-service.email-send" 解析为
     * actionName = "notification-service" 与 strategy = "email-send"；
     * 不含 "." 时策略为空串，command 为空串时两者都为空串.
     *
     * @param command 包含 action 名称与策略的命令
     * @return 左值为 action 名称、右值为策略的键值对
     */
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

    /**
     * 按 action 名称获取 action，找不到时抛异常.
     *
     * @param actionName action 名称
     * @return 对应的 action
     */
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
        // 策略优先级: command 策略 > shortcut 策略
        Pair<String, String> pair = parseCommand(command);
        String actionName = pair.getLeft();
        String strategy = pair.getRight();
        // 走 shortcut 路由：当 actionName 为空时，策略必然也为空
        // （例如 ".test" 这种 command 是不支持的）
        if (StrUtil.isBlank(actionName) && input != null) {
            pair = parseCommand(getShortcut(input.getClass()));
            actionName = pair.getLeft();
            strategy = pair.getRight();
        }
        // 执行
        return getActionOrThrow(actionName).execute(strategy, input, arguments);
    }

    @Override
    public <I extends Action.Input<R>, R> R execute(String command, I input, Object[] arguments) {
        // 将 input 转成 Object，让重载决议走 raw 版 execute，否则本方法会递归调用自己
        return CastUtil.cast(execute(command, (Object) input, arguments));
    }

}
