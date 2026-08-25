/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.action;

import kunlun.core.Action;

import java.lang.reflect.Type;

/**
 * Action 管理器.<br />
 * <p>
 * action 体系的核心调度器：维护 "action 名称 → action 实例" 的注册表，
 * 以及 "输入类型 → command" 的 shortcut（快捷方式）注册表，
 * 并按 command 或输入类型将调用路由到对应的 action.
 *
 * @author Kahle
 */
public interface ActionManager {

    /**
     * 注册 action.<br />
     * 若同名 action 已存在，则覆盖旧的注册.
     *
     * @param actionName action 名称（command 的第一段，如 "notification-service"）
     * @param action 被注册的 action
     */
    void registerAction(String actionName, Action action);

    /**
     * 注销 action.<br />
     *
     * @param actionName action 名称
     */
    void deregisterAction(String actionName);

    /**
     * 按 action 名称获取 action（名称是固定的，不包含策略部分）.
     *
     * @param actionName action 名称
     * @return 对应的 action 或 null
     */
    Action getAction(String actionName);

    /**
     * 为 action 注册 shortcut（快捷方式）.<br />
     * 注册后，当 execute 的 command 为空时，可按输入参数的类型直接路由到对应的 command，
     * 调用方无需显式指定 action 名称.
     *
     * @param inputType 输入参数的类型
     * @param command 包含 action 名称与策略的命令
     */
    void registerShortcut(Type inputType, String command);

    /**
     * 注销 shortcut.<br />
     *
     * @param inputType 输入参数的类型
     */
    void deregisterShortcut(Type inputType);

    /**
     * 获取 shortcut.<br />
     *
     * @param inputType 输入参数的类型
     * @return 对应的 command 或 null
     */
    String getShortcut(Type inputType);

    /**
     * 执行指定逻辑.<br />
     * <p>
     * command 的格式为 "actionName.strategy"（按第一个 "." 切分）：
     * 如 "notification-service.email-send" 解析为
     * actionName = "notification-service" 与 strategy = "email-send"；
     * 当 command 为空时，按输入参数的类型走 shortcut 路由.
     * <p>
     * 策略优先级: command 策略 > shortcut 策略
     *
     * @param command 包含 action 名称与策略的命令
     * @param input 执行指定逻辑时的主要输入对象
     * @param arguments 执行指定逻辑时的其他相关参数
     * @return 指定逻辑的执行结果或 null
     */
    Object execute(String command, Object input, Object[] arguments);

    /**
     * 执行指定逻辑，返回类型由输入参数的类型推断得出.<br />
     * <p>
     * 输入必须实现 {@link kunlun.core.Action.Input} 以声明其返回类型 R（当输入以 raw 方式使用时，R 为 Object）.
     * <p>
     * 策略优先级: command 策略 > shortcut 策略
     *
     * @param command 包含 action 名称与策略的命令
     * @param arguments 执行指定逻辑时的其他相关参数
     * @param <I> 输入参数的类型
     * @param <R> 输入声明的返回类型
     * @return 指定逻辑的执行结果
     */
    <I extends Action.Input<R>, R> R execute(String command, I input, Object[] arguments);

}
