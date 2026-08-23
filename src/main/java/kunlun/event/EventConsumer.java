/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.event;

import kunlun.data.Event;

import java.util.Collection;

/**
 * 事件消费者 —— 按<b>事件类型</b>挂载的订阅端点，回答<i>「消费这类事件做什么」</i>。
 * <ul>
 *   <li><b>挂载维度：</b>挂载时由注册处指定事件类型（取值为 {@link Event#getName()} 的
 *       类型词汇，如 {@link Event#OPERATION_LOG} / {@link Event#CHANGE_LOG} /
 *       {@link Event#RUN_LOG}；{@link Event#ANY} 通配任意类型）——同一类型可挂多个消费者，
 *       同一消费者也可挂到多个类型，逐一分发。</li>
 *   <li><b>批量为核心：</b>契约只有批量方法 {@link #consume(Collection)}，分发管道按事件类型
 *       分组后整批交给消费者（存储型消费者如 MySQL 获得批量写入收益）；
 *       单条便捷入口由 {@link EventUtil} 基于批量方法包装，用户通常不直接调用实现类。</li>
 *   <li><b>与收集器的区别：</b>收集器（{@link kunlun.data.event.EventCollector}）按实现划分，是一条完整的
 *       加工-落地管线；消费者按类型划分，是轻量端点，只做业务处理，不参与事件的加工。</li>
 *   <li><b>异常边界：</b>实现只管业务逻辑，抛出的异常由分发管道隔离吞掉（记录日志），
 *       不影响其他消费者，也不影响业务主流程。</li>
 * </ul>
 *
 * @author Kahle
 * @see kunlun.event.EventProvider
 * @see kunlun.event.support.LogEventConsumer
 */
public interface EventConsumer {

    /**
     * 批量消费事件记录 —— 分发管道按事件类型分组后整批交给消费者。
     *
     * @param events 待消费的事件记录集合
     */
    void consume(Collection<Event> events);

}
