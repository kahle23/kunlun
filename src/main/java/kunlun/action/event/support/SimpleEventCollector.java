/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.action.event.support;

import kunlun.core.Action;
import kunlun.data.event.EventCollector;
import kunlun.event.EventUtil;
import kunlun.data.Event;
import kunlun.logging.Logger;
import kunlun.logging.LoggerFactory;
import kunlun.util.Assert;

import java.util.Collection;
import java.util.Collections;

/**
 * 简单事件收集器 —— {@link EventCollector} 的默认实现，由 {@code SimpleEventProvider}
 * 预挂到 {@code EventProvider.DEFAULT_COLLECTOR_NAME}（也可自行注册到其他名下）。
 * <p><b>批量为核心：</b>{@link #collect(Collection)} 参照 {@link #execute(String, Object, Object[])}
 * 的逻辑组合全流程 —— 校验事件（非空、{@code name} 非空）→ 补全缺失的 {@code time} →
 * {@link #process(Collection)}（加工，默认空实现，子类覆写以补全上下文）→
 * {@link #push(Collection)}（本地桥接：调用 {@link EventUtil#consume(java.util.Collection)}
 * 分发本地消费者），全程异常自吞（仅记错误日志）。跨服务场景应替换为终点型收集器（如推送 MQ），
 * 由对端服务的监听器解析后自行调用 {@code EventUtil.consume}。
 * <p><b>兼容期单条与 Action 入口（均已弃用）：</b>{@link #process(Event)} / {@link #push(Event)}
 * 为存量下游的覆写点，委托批量实现；{@link #execute(String, Object, Object[])} 为
 * {@code Action} 总线入口（{@code ActionUtil.execute(event)}），后续事件投递不再经 {@code ActionUtil}。
 * 事件采集不影响业务主流程。
 *
 * @author Kahle
 */
public class SimpleEventCollector implements EventCollector, Action {
    private static final Logger log = LoggerFactory.getLogger(SimpleEventCollector.class);

    @Override
    public void process(Collection<Event> events) {

    }

    @Override
    public void push(Collection<Event> events) {
        if (events == null || events.isEmpty()) { return; }
        // 本地桥接：分发本地消费者（入口是 EventUtil.collect，收集器内不再回调 collect）。
        EventUtil.consume(events);
    }

    @Override
    public void collect(Collection<Event> events) {
        if (events == null || events.isEmpty()) { return; }
        try {
            // 校验事件并补全缺失的时间。
            for (Event event : events) {
                Assert.notNull(event); Assert.notBlank(event.getName());
                if (event.getTime() == null) {
                    event.setTime(System.currentTimeMillis());
                }
            }
            // 加工事件记录。
            process(events);
            // 分发事件记录。
            push(events);
        }
        catch (Exception e) {
            log.error("An error has occurred with \"" + getClass().getSimpleName() + "\". ", e);
        }
    }

    /**
     * @deprecated 单条方法为存量下游的兼容覆写点，将随下一个大版本移除，
     *             请改用 {@link #process(Collection)}（批量为核心）。
     */
    @Deprecated
    @Override
    public void process(Event event) {
        if (event == null) { return; }
        process(Collections.singleton(event));
    }

    /**
     * @deprecated 单条方法为存量下游的兼容覆写点，将随下一个大版本移除，
     *             请改用 {@link #push(Collection)}（批量为核心）。
     */
    @Deprecated
    @Override
    public void push(Event event) {
        if (event == null) { return; }
        push(Collections.singleton(event));
    }

    /**
     * @deprecated {@code Action} 总线入口（{@code ActionUtil.execute(event)}），
     *             后续事件投递不再经 {@code ActionUtil}，请改用 {@code EventUtil.collect(...)}。
     */
    @Deprecated
    @Override
    public Object execute(String strategy, Object input, Object[] arguments) {
        Assert.isInstanceOf(Event.class, Assert.notNull(input));
        Assert.notBlank(((Event) input).getName());
        try {
            // 转换事件对象。
            Event event = (Event) input;
            if (event.getTime() == null) {
                event.setTime(System.currentTimeMillis());
            }
            // 加工事件记录。
            process(event);
            // 分发事件记录。
            push(event);
        }
        catch (Exception e) {
            log.error("An error has occurred with \"" + getClass().getSimpleName() + "\". ", e);
        }
        return null;
    }

}
