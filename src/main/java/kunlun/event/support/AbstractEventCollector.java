/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.event.support;

import kunlun.data.event.EventCollector;
import kunlun.data.Event;

import java.util.Collection;
import java.util.Collections;

/**
 * 事件收集器基类 —— 面向新写收集器的<b>批量优先</b>骨架：单条方法基于批量包装
 * （单元素集合委托给批量方法），具体收集器实现批量 {@code process(Collection)} /
 * {@code push(Collection)} 即同时具备单条与批量能力。
 *
 * @author Kahle
 * @see kunlun.action.event.support.SimpleEventCollector
 * @deprecated 仅在 {@link kunlun.data.event.EventCollector} 的单条方法（兼容覆写点）被移除前
 * 作为过渡骨架存在，单条方法随大版本删除后本类一并移除。
 */
@Deprecated
public abstract class AbstractEventCollector implements EventCollector {

    @Override
    public void process(Event event) {
        if (event == null) { return; }
        process(Collections.singleton(event));
    }

    @Override
    public void push(Event event) {
        if (event == null) { return; }
        push(Collections.singleton(event));
    }

    @Override
    public void collect(Collection<Event> events) {
        if (events == null || events.isEmpty()) { return; }
        process(events);
        push(events);
    }

}
