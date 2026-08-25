/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.event;

import kunlun.common.constant.Words;
import kunlun.data.Event;
import kunlun.data.event.EventCollector;

import java.util.Collection;
import java.util.List;

/**
 * 事件提供者 —— 事件子系统的管理器抽象：同时管理<b>收集器</b>与<b>消费者</b>两张注册表，
 * 并提供两类相互独立的投递入口：{@link #collect(String, Collection)}（路由收集器）与
 * {@link #consume(Collection)}（分发消费者）。单条便捷入口由静态门面
 * {@link kunlun.event.EventUtil} 基于批量方法包装，本接口只保留批量 API。
 *
 * <h3>三维度模型</h3>
 * <ul>
 *   <li>事件（{@link kunlun.data.Event}）按<b>类型</b>划分：操作日志 / 变更日志 / 运行日志等
 *       （类型词汇即事件的 {@code name}）。</li>
 *   <li>收集器（{@link kunlun.data.event.EventCollector}）按<b>实现</b>划分：如日志收集器、MQ 收集器、
 *       MySQL 收集器，各自是一条完整的加工-落地管线，按实现名注册；
 *       大部分场景注册到 {@link #DEFAULT_COLLECTOR_NAME} 即可。</li>
 *   <li>消费者（{@link kunlun.event.EventConsumer}）按<b>事件类型</b>挂载：挂载时由注册处指定类型，
 *       同一类型可挂多个消费者、同一消费者也可挂到多个类型，逐一分发，
 *       {@link kunlun.data.Event#ANY} 通配任意类型。</li>
 * </ul>
 *
 * <h3>投递语义（collect 与 consume 相互独立）</h3>
 * <ul>
 *   <li>{@link #collect(String, Collection)}：路由到<b>指定收集器</b>（经其 {@code collect}
 *       组合入口一次完成校验、补全时间、加工与分发；{@code collectorName} 为 null 或空白字符串时
 *       走 {@link #DEFAULT_COLLECTOR_NAME}，未注册则跳过并记日志），<b>不分发消费者</b>。</li>
 *   <li>{@link #consume(Collection)}：按事件类型分组后整批交给匹配的消费者
 *       （含 ANY 通配，单点异常隔离），<b>不走收集器</b>。</li>
 * </ul>
 * <p>典型跨服务链路：收集器的推送方（如发 MQ）→ 对端服务的监听器解析事件 → 调用
 * {@code EventUtil.consume} 分发本端消费者。本地同进程消费由默认收集器
 * {@code SimpleEventCollector} 的 {@code push} 桥接（其自身绝不再回调 {@code collect}）。
 * <p>默认实现预挂了 {@code SimpleEventCollector}（{@link #DEFAULT_COLLECTOR_NAME}：
 * 补全时间 + 本地桥接分发消费者）与 {@code LogEventConsumer}（任意类型打印事件摘要，
 * 承接日志兜底职责）。
 *
 * @author Kahle
 * @see kunlun.event.EventUtil
 * @see kunlun.event.support.SimpleEventProvider
 */
public interface EventProvider {

    /**
     * 默认收集器的注册名 —— 大部分场景将收集器注册到该名下即可。
     */
    String DEFAULT_COLLECTOR_NAME = Words.DEFAULT;

    // region ======== 收集器 ========
    /**
     * 按实现名注册收集器（大部分场景用 {@link #DEFAULT_COLLECTOR_NAME}）。
     *
     * @param name      收集器的实现名
     * @param collector 待注册的收集器
     */
    void registerCollector(String name, EventCollector collector);

    /**
     * 按实现名注销收集器。
     *
     * @param name 收集器的实现名
     */
    void deregisterCollector(String name);

    /**
     * 按实现名获取收集器。
     *
     * @param name 收集器的实现名
     * @return 收集器或 null（未注册）
     */
    EventCollector getCollector(String name);

    /**
     * 批量投递事件记录到指定收集器：路由到指定收集器（经其 {@code collect} 组合入口一次完成
     * 校验、补全时间、批量加工与分发；未注册则跳过并记日志），不分发消费者。
     *
     * @param collectorName 收集器的实现名（null 或空白字符串时走 {@link #DEFAULT_COLLECTOR_NAME}）
     * @param events        待投递的事件记录集合
     */
    void collect(String collectorName, Collection<Event> events);
    // endregion ======== 收集器 ========


    // region ======== 消费者 ========
    /**
     * 按事件类型挂载消费者（同一类型可挂多个，同一消费者也可挂到多个类型，
     * 重复挂载同一消费者到同一类型将被忽略）。
     *
     * @param eventType 事件类型（{@link kunlun.data.Event#ANY} 通配任意类型）
     * @param consumer  待挂载的消费者
     */
    void registerConsumer(String eventType, EventConsumer consumer);

    /**
     * 按事件类型摘除消费者。
     *
     * @param eventType 事件类型
     * @param consumer  待摘除的消费者
     */
    void deregisterConsumer(String eventType, EventConsumer consumer);

    /**
     * 获取某事件类型已挂载的全部消费者。
     *
     * @param eventType 事件类型
     * @return 消费者列表快照（未挂载时为空列表）
     */
    List<EventConsumer> getConsumers(String eventType);

    /**
     * 分发消费者：按事件类型分组后整批交给匹配的消费者（含 ANY 通配，单点异常隔离），
     * 不走收集器。
     *
     * @param events 待分发的事件记录集合
     */
    void consume(Collection<Event> events);
    // endregion ======== 消费者 ========

}
