/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.data.event;

import kunlun.data.Event;

import java.util.Collection;

/**
 * 事件收集器 —— 事件日志子系统的服务接口（定位对齐 {@code MessageBus} / {@code NotificationService}），
 * 负责审计事件的加工与分发，参照 <b>Windows 事件日志服务</b> 的职责设计。
 *
 * <h3>管线契约</h3>
 * <pre>
 *   业务侧构造 {@link Event} 并投递
 *      → {@link #process(Collection)}  加工：补全上下文（用户 / 租户 / 平台 / 模块、请求信息等）
 *      → {@link #push(Collection)}     分发：交给具体处理器（本地桥接、落库、转发通知等）
 *      → {@link #collect(Collection)}  组合入口：校验与补全时间 → 上两步一气呵成（投递方常用）
 * </pre>
 * <ul>
 *   <li>{@code collect} 是组合便捷入口，负责校验（如事件名非空）、补全缺失的 {@code time}，
 *       再依次执行 {@code process} 与 {@code push}，投递方（如 {@code EventProvider.collect}）
 *       经此一次完成收集。</li>
 *   <li>{@code process} 只做补全，不产生对外投递；{@code push} 是事件的唯一出口。</li>
 *   <li>实现应吞掉自身异常（记录日志），不得让采集失败影响业务主流程。</li>
 *   <li><b>批量为核心：</b>存储型收集器（如 MySQL）覆写批量方法获得批量写入收益；
 *       单条方法为存量下游的兼容覆写点（已弃用，将随下一个大版本移除）。</li>
 * </ul>
 *
 * @author Kahle
 * @see kunlun.data.Event
 * @see kunlun.action.event.support.SimpleEventCollector
 */
public interface EventCollector {

    /**
     * 加工事件记录 —— 补全上下文字段（用户、租户、平台、模块、请求信息等）。
     * <p>只做补全、不产生对外投递；实现不应抛出异常中断采集流程。
     *
     * @param event 待加工的事件记录
     * @deprecated 单条方法为存量下游的兼容覆写点，将随下一个大版本移除，
     *             请改用 {@link #process(Collection)}（批量为核心）。
     */
    @Deprecated
    void process(Event event);

    /**
     * 分发事件记录 —— 交给具体处理器落地（本地桥接、落库、转发通知等），
     * 是事件流转的唯一出口。实现应自行吞掉异常，不得影响业务主流程。
     *
     * @param event 待分发的事件记录
     * @deprecated 单条方法为存量下游的兼容覆写点，将随下一个大版本移除，
     *             请改用 {@link #push(Collection)}（批量为核心）。
     */
    @Deprecated
    void push(Event event);

    /**
     * 批量加工事件记录 —— 默认逐条回调 {@link #process(Event)}，
     * 存储型收集器（如 MySQL）覆写此方法获得批量写入收益。
     *
     * @param events 待加工的事件记录集合
     */
    void process(Collection<Event> events);

    /**
     * 批量分发事件记录 —— 默认逐条回调 {@link #push(Event)}，
     * 存储型收集器（如 MySQL）覆写此方法获得批量写入收益。
     *
     * @param events 待分发的事件记录集合
     */
    void push(Collection<Event> events);

    /**
     * 收集事件记录 —— 组合便捷入口：校验（如事件名非空）与补全缺失的 {@code time} 后，
     * 依次执行 {@link #process(Collection)} 与 {@link #push(Collection)}，
     * 投递方（如 {@code EventProvider.collect}）经此一次完成收集。
     *
     * @param events 待收集的事件记录集合
     */
    void collect(Collection<Event> events);

}
