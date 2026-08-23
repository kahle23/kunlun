/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.event;

import kunlun.common.constant.Nil;
import kunlun.data.Event;
import kunlun.data.event.EventCollector;
import kunlun.event.support.SimpleEventProvider;
import kunlun.logging.Logger;
import kunlun.logging.LoggerFactory;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static kunlun.util.Assert.notNull;

/**
 * 事件工具类 —— 事件子系统的静态门面（对 {@link EventProvider} 的转发）。
 * <p>统一管理收集器（按实现，投递时按名路由，大部分场景注册到
 * {@link EventProvider#DEFAULT_COLLECTOR_NAME} 并以不传名的 {@link #collect(Collection)} 投递）
 * 与消费者（按事件类型挂载，类型由注册处指定）：注册 / 注销 / 查询全部转发至
 * {@link #getEventProvider()}。
 * <p>{@code collect} 与 {@code consume} 相互独立且均以<b>批量为核心</b>
 * （单条便捷入口由本门面以单元素集合包装批量，{@link EventProvider} 只保留批量 API）：
 * {@code collect} 路由收集器（加工由收集器负责，如默认收集器补全时间后本地桥接分发消费者），
 * <b>不在收集器之外分发消费者</b>；典型跨服务链路为「终点型收集器推送（如发 MQ）→
 * 对端服务监听解析 → {@link #consume(Collection)} 分发本端消费者」。
 *
 * @author Kahle
 * @see EventProvider
 * @see EventConsumer
 * @see EventCollector
 */
public class EventUtil {
    private static final Logger log = LoggerFactory.getLogger(EventUtil.class);
    private static volatile EventProvider eventProvider;

    public static EventProvider getEventProvider() {
        if (eventProvider != null) { return eventProvider; }
        synchronized (EventUtil.class) {
            if (eventProvider != null) { return eventProvider; }
            EventUtil.setEventProvider(new SimpleEventProvider());
            return eventProvider;
        }
    }

    public static void setEventProvider(EventProvider eventProvider) {
        notNull(eventProvider, "Parameter \"eventProvider\" must not null. ");
        log.debug("Set event provider: {}", eventProvider.getClass().getName());
        EventUtil.eventProvider = eventProvider;
    }

    // region ======== 收集器 ========
    /**
     * 按实现名注册收集器（大部分场景用 {@link EventProvider#DEFAULT_COLLECTOR_NAME}）。
     *
     * @param name      收集器的实现名
     * @param collector 待注册的收集器
     */
    public static void registerCollector(String name, EventCollector collector) {

        getEventProvider().registerCollector(name, collector);
    }

    /**
     * 按实现名注销收集器。
     *
     * @param name 收集器的实现名
     */
    public static void deregisterCollector(String name) {

        getEventProvider().deregisterCollector(name);
    }

    /**
     * 按实现名获取收集器。
     *
     * @param name 收集器的实现名
     * @return 收集器或 null（未注册）
     */
    public static EventCollector getCollector(String name) {

        return getEventProvider().getCollector(name);
    }

    /**
     * 批量投递事件记录到指定收集器（核心方法）：路由到指定收集器（经其 {@code collect}
     * 组合入口一次完成校验、补全时间、批量加工与分发，未注册则跳过并记日志），
     * 不分发消费者。
     *
     * @param collectorName 收集器的实现名（null 或空白字符串时走
     *                      {@link EventProvider#DEFAULT_COLLECTOR_NAME}）
     * @param events        待投递的事件记录集合
     */
    public static void collect(String collectorName, Collection<Event> events) {

        getEventProvider().collect(collectorName, events);
    }

    /**
     * 批量投递事件记录到默认收集器（{@link EventProvider#DEFAULT_COLLECTOR_NAME}，
     * 等价于 {@code collect(null, events)}）。
     *
     * @param events 待投递的事件记录集合
     */
    public static void collect(Collection<Event> events) {

        getEventProvider().collect(Nil.STR, events);
    }

    /**
     * 投递一条事件记录到指定收集器（单条便捷入口，包装批量方法）。
     *
     * @param collectorName 收集器的实现名（null 或空白字符串时走
     *                      {@link EventProvider#DEFAULT_COLLECTOR_NAME}）
     * @param event         待投递的事件记录
     */
    public static void collect(String collectorName, Event event) {
        if (event == null) { return; }
        collect(collectorName, Collections.singleton(event));
    }

    /**
     * 投递一条事件记录到默认收集器（{@link EventProvider#DEFAULT_COLLECTOR_NAME}）。
     *
     * @param event 待投递的事件记录
     */
    public static void collect(Event event) {
        if (event == null) { return; }
        collect(Nil.STR, event);
    }
    // endregion ======== 收集器 ========


    // region ======== 消费者 ========
    /**
     * 按事件类型挂载消费者（同一类型可挂多个，同一消费者也可挂到多个类型，
     * 重复挂载同一消费者到同一类型将被忽略）。
     *
     * @param eventType 事件类型（{@link Event#ANY} 通配任意类型）
     * @param consumer  待挂载的消费者
     */
    public static void registerConsumer(String eventType, EventConsumer consumer) {

        getEventProvider().registerConsumer(eventType, consumer);
    }

    /**
     * 按事件类型摘除消费者。
     *
     * @param eventType 事件类型
     * @param consumer  待摘除的消费者
     */
    public static void deregisterConsumer(String eventType, EventConsumer consumer) {

        getEventProvider().deregisterConsumer(eventType, consumer);
    }

    /**
     * 获取某事件类型已挂载的全部消费者。
     *
     * @param eventType 事件类型
     * @return 消费者列表快照或 null（未挂载）
     */
    public static List<EventConsumer> getConsumers(String eventType) {

        return getEventProvider().getConsumers(eventType);
    }

    /**
     * 分发消费者（核心方法）：按事件类型分组后整批交给匹配的消费者
     * （含 ANY 通配，单点异常隔离），不走收集器。
     *
     * @param events 待分发的事件记录集合
     */
    public static void consume(Collection<Event> events) {

        getEventProvider().consume(events);
    }

    /**
     * 分发消费者（单条便捷入口，包装批量方法）：按事件类型（含 ANY 通配）分发，不走收集器。
     *
     * @param event 待分发的事件记录
     */
    public static void consume(Event event) {
        if (event == null) { return; }
        consume(Collections.singleton(event));
    }
    // endregion ======== 消费者 ========

}
