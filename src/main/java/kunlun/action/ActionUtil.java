/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.action;

import kunlun.action.util.net.MediaTypeAction;
import kunlun.common.constant.Nil;
import kunlun.core.Action;
import kunlun.data.Event;
import kunlun.action.event.support.SimpleEventCollector;
import kunlun.logging.Logger;
import kunlun.logging.LoggerFactory;
import kunlun.message.model.Message;
import kunlun.message.model.Subscribe;
import kunlun.message.support.SimpleMessageBus;
import kunlun.util.Assert;
import kunlun.util.CastUtil;

import java.lang.reflect.Type;

/**
 * Action 工具类（对 {@link ActionManager} 的静态门面）.<br />
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

    /**
     * 执行指定逻辑，返回类型由调用处的赋值目标推断得出（无赋值目标时为 Object），
     * 实际是按 T 强转，类型不符会抛 ClassCastException；
     * 若输入实现了 {@link kunlun.core.Action.Input}，建议改用带类型声明的重载，由输入类型直接推断返回类型.
     *
     * @param command 包含 action 名称与策略的命令
     * @param input 执行指定逻辑时的主要输入对象
     * @param arguments 执行指定逻辑时的其他相关参数
     * @param <T> 调用处期望的返回类型
     * @return 指定逻辑的执行结果或 null
     */
    public static <T> T execute(String command, Object input, Object... arguments) {

        return CastUtil.cast(getActionManager().execute(command, input, arguments));
    }

    /**
     * 按输入类型的 shortcut 执行指定逻辑（不指定 command），返回类型由调用处的赋值目标推断得出.
     *
     * @param input 执行指定逻辑时的主要输入对象
     * @param <T> 调用处期望的返回类型
     * @return 指定逻辑的执行结果或 null
     */
    public static <T> T execute(Object input) {

        return execute(Nil.STR, input);
    }

    /**
     * 执行指定逻辑，返回类型由输入参数的类型推断得出.<br />
     * <p>
     * 输入必须实现 {@link kunlun.core.Action.Input} 以声明其返回类型 R（当输入以 raw 方式使用时，R 为 Object）.
     *
     * @param command 包含 action 名称与策略的命令
     * @param arguments 执行指定逻辑时的其他相关参数
     * @param <I> 输入参数的类型
     * @param <R> 输入声明的返回类型
     * @return 指定逻辑的执行结果
     */
    public static <I extends Action.Input<R>, R> R execute(String command, I input, Object... arguments) {

        return getActionManager().execute(command, input, arguments);
    }

    /**
     * 按输入类型的 shortcut 执行指定逻辑（不指定 command），返回类型由输入参数的类型推断得出.<br />
     *
     * @param <I> 输入参数的类型
     * @param <R> 输入声明的返回类型
     * @return 指定逻辑的执行结果
     */
    public static <I extends Action.Input<R>, R> R execute(I input) {

        return execute(Nil.STR, input);
    }

}
