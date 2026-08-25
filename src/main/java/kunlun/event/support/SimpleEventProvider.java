/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.event.support;

import kunlun.action.event.support.SimpleEventCollector;
import kunlun.data.Event;
import kunlun.data.event.EventCollector;
import kunlun.event.EventConsumer;
import kunlun.event.EventProvider;
import kunlun.logging.Logger;
import kunlun.logging.LoggerFactory;
import kunlun.util.StrUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static kunlun.util.Assert.notBlank;
import static kunlun.util.Assert.notNull;

/**
 * 简单事件提供者 —— {@link EventProvider} 的默认实现。
 * <p>两张注册表：收集器按实现名（{@code ConcurrentMap<String, EventCollector>}，投递时按名路由，
 * 不传名走 {@link #DEFAULT_COLLECTOR_NAME}）、消费者按事件类型
 * （{@code ConcurrentMap<String, List<EventConsumer>>}，类型由注册处指定，同类追加、重复忽略）。
 * 默认预挂 {@link SimpleEventCollector}（默认收集器名：补全时间 + 本地桥接分发消费者）与
 * {@link LogEventConsumer}（任意类型打印事件摘要，承接日志兜底职责）。
 * <p>{@code collect} 与 {@code consume} 相互独立：前者路由指定收集器（加工由收集器自身负责，
 * 如默认收集器补全 {@code time}），<b>不分发消费者</b>（跨服务链路中由对端服务的监听器解析后
 * 调用 {@code EventUtil.consume}）；后者按事件类型分组后整批交给匹配的消费者。
 * 注册通常在启动期完成，投递期仅做无锁遍历（注册表与消费者列表均按快照分发，
 * 避免遍历期间的挂载引起并发修改）。单个收集器 / 消费者的异常被隔离吞掉
 * （消费者由本类隔离；收集器约定自吞异常，本类另行兜底，均记录错误日志），
 * 不影响其余分发与业务主流程。
 *
 * @author Kahle
 */
public class SimpleEventProvider implements EventProvider {
    private static final Logger log = LoggerFactory.getLogger(SimpleEventProvider.class);
    protected final ConcurrentMap<String, List<EventConsumer>> consumers;
    protected final ConcurrentMap<String, EventCollector> collectors;

    protected SimpleEventProvider(ConcurrentMap<String, EventCollector> collectors,
                                  ConcurrentMap<String, List<EventConsumer>> consumers) {
        notNull(collectors, "Parameter \"collectors\" must not null. ");
        notNull(consumers, "Parameter \"consumers\" must not null. ");
        this.collectors = collectors;
        this.consumers = consumers;
    }

    public SimpleEventProvider() {
        this(new ConcurrentHashMap<String, EventCollector>(),
                new ConcurrentHashMap<String, List<EventConsumer>>());
        // 默认预挂：简单收集器（补全时间 + 本地桥接分发消费者）与日志消费者（任意类型打印）。
        registerCollector(DEFAULT_COLLECTOR_NAME, new SimpleEventCollector());
        registerConsumer(Event.ANY, new LogEventConsumer());
    }

    @Override
    public void registerCollector(String name, EventCollector collector) {
        notNull(collector, "Parameter \"collector\" must not null. ");
        notBlank(name, "Parameter \"name\" must not blank. ");
        collectors.put(name, collector);
        log.debug("Register the event collector \"{}\" to \"{}\". ", collector.getClass().getName(), name);
    }

    @Override
    public void deregisterCollector(String name) {
        notBlank(name, "Parameter \"name\" must not blank. ");
        EventCollector remove = collectors.remove(name);
        if (remove != null) {
            log.debug("Deregister the event collector \"{}\" from \"{}\". ", remove.getClass().getName(), name);
        }
    }

    @Override
    public EventCollector getCollector(String name) {

        return collectors.get(name);
    }

    @Override
    public void collect(String collectorName, Collection<Event> events) {
        if (events == null || events.isEmpty()) { return; }
        // 收集器名为 null 或空白时走默认收集器。
        String name = StrUtil.isBlank(collectorName) ? DEFAULT_COLLECTOR_NAME : collectorName;
        // 路由到指定收集器（未注册则跳过并记日志，事件管道以尽力而为为原则）；
        // 校验、补全时间与异常自吞均由收集器的 collect 负责。
        EventCollector collector = collectors.get(name);
        if (collector == null) {
            log.debug("The event collector \"{}\" is not registered, skip. ", name);
            return;
        }
        // 收集事件记录（校验 + 补全时间 + 加工 + 分发）；收集器约定自吞异常，此处再兜底隔离，
        // 保证不守约的收集器也不影响业务主流程。
        try {
            collector.collect(events);
        }
        catch (Exception e) {
            log.error("An error has occurred with the event collector \""
                    + collector.getClass().getName() + "\". ", e);
        }
    }

    @Override
    public void registerConsumer(String eventType, EventConsumer consumer) {
        notBlank(eventType, "Parameter \"eventType\" must not blank. ");
        notNull(consumer, "Parameter \"consumer\" must not null. ");
        List<EventConsumer> consumerList = consumers.get(eventType);
        if (consumerList == null) {
            List<EventConsumer> newValue = new ArrayList<EventConsumer>();
            consumerList = consumers.putIfAbsent(eventType, newValue);
            if (consumerList == null) { consumerList = newValue; }
        }
        if (!consumerList.contains(consumer)) { consumerList.add(consumer); }
        log.debug("Register the event consumer \"{}\" to \"{}\". ", consumer.getClass().getName(), eventType);
    }

    @Override
    public void deregisterConsumer(String eventType, EventConsumer consumer) {
        notBlank(eventType, "Parameter \"eventType\" must not blank. ");
        notNull(consumer, "Parameter \"consumer\" must not null. ");
        List<EventConsumer> consumerList = consumers.get(eventType);
        if (consumerList != null) { consumerList.remove(consumer); }
    }

    @Override
    public List<EventConsumer> getConsumers(String eventType) {
        List<EventConsumer> consumerList = consumers.get(eventType);
        if (consumerList == null) { return new ArrayList<EventConsumer>(); }
        return new ArrayList<EventConsumer>(consumerList);
    }

    @Override
    public void consume(Collection<Event> events) {
        if (events == null || events.isEmpty()) { return; }
        // 先按事件类型分组（跳过 null 事件），再按注册类型取子批分发；ANY 类型匹配整批。
        List<Event> all = new ArrayList<Event>(events.size());
        Map<String, List<Event>> group = new HashMap<String, List<Event>>();
        for (Event event : events) {
            if (event == null) { continue; }
            all.add(event);
            List<Event> sameType = group.get(event.getName());
            if (sameType == null) {
                sameType = new ArrayList<Event>();
                group.put(event.getName(), sameType);
            }
            sameType.add(event);
        }
        if (all.isEmpty()) { return; }
        for (Map.Entry<String, List<EventConsumer>> entry : consumers.entrySet()) {
            String eventType = entry.getKey();
            List<Event> matched = Event.ANY.equals(eventType) ? all : group.get(eventType);
            if (matched == null) { continue; }
            // 按快照分发，避免遍历期间的挂载/摘除引起并发修改。
            for (EventConsumer consumer : new ArrayList<EventConsumer>(entry.getValue())) {
                try {
                    consumer.consume(matched);
                }
                catch (Exception e) {
                    log.error("An error has occurred with the event consumer \""
                            + consumer.getClass().getName() + "\". ", e);
                }
            }
        }
    }

}
