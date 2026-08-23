/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

/**
 * 事件子系统 —— 审计事实的采集、加工与分发，参照 <b>Windows 事件日志（Event Log）</b> 设计，
 * 与 {@code kunlun.message}（总线载体）、{@code kunlun.notification}（用户通知）平级，构成三载体体系：
 * <pre>
 *   Event（发生了什么）——订阅/转化——&gt; Notification（告诉谁）
 *   Notification——发送——&gt; Message（怎么送达：经 MessageBus 上总线投递）
 * </pre>
 * <ul>
 *   <li>{@code kunlun.data.Event} = 审计事实（长期、归档、回答「发生了什么」）。</li>
 *   <li>{@code Notification} = 用户消息（瞬时、已读/未读、回答「告诉谁」）。</li>
 *   <li>{@code Message} = 总线消息单元（投递载荷、回答「怎么送达」）。</li>
 * </ul>
 *
 * <h3>本包不是事件总线</h3>
 * <p>「事件总线」（Guava {@code EventBus}、Spring {@code ApplicationEvent} 一脉）指发布-订阅分发机制，
 * 该能力在本库中由 {@code kunlun.message.MessageBus} 承担；本包的 {@code Event} 是审计记录，
 * 若需扇出/订阅，事件可作为载荷交由 {@code MessageBus} 投递，而不给本包增加总线职能。
 *
 * <h3>组成（三维度模型）</h3>
 * <ul>
 *   <li>{@code kunlun.data.Event}：事件载体，按<b>类型</b>划分（name：操作日志/变更日志/运行日志...）。
 *       <b>类型承载类暂留原包</b>——大量下游将其写进方法签名，包移动属破坏性变更，
 *       迁入本包的事项延后至下一个大版本一并协调。</li>
 *   <li>{@code kunlun.data.event.EventCollector}：收集器，按<b>实现</b>划分（日志/MQ/MySQL...），
 *       各自是完整的加工-落地管线（批量为核心，单条方法为兼容覆写点、已弃用）；
 *       注册名有 {@code DEFAULT_COLLECTOR_NAME} 约定槽位。同上，暂留原包。</li>
 *   <li>{@code kunlun.event.EventConsumer}：消费者，按<b>事件类型</b>挂载（类型由注册处指定），
 *       一类事件可挂多个消费者、同一消费者也可挂多个类型；批量消费为核心。</li>
 *   <li>{@code kunlun.event.EventProvider} / {@code kunlun.event.EventUtil}：管理器抽象与静态门面，
 *       统一管理收集器与消费者两张注册表并提供两类相互独立的投递入口（均以批量为核心，单条便捷
 *       入口由门面包装）：{@code collect} 路由收集器（加工由收集器负责，如默认收集器补全时间后
 *       本地桥接分发消费者），<b>不分发消费者</b>；{@code consume} 按事件类型分组分发消费者。
 *       典型跨服务链路：终点型收集器推送（如发 MQ）→ 对端服务监听解析 → {@code EventUtil.consume}。
 *       {@code Action} 总线入口（{@code ActionUtil.execute(event)}）已弃用，请改用
 *       {@code EventUtil.collect(...)}。</li>
 * </ul>
 * <p>默认预挂 {@code SimpleEventCollector}（默认收集器名：补全时间 + 本地桥接分发消费者）与
 * {@code LogEventConsumer}（任意类型打印事件摘要），可与其他收集器 / 消费者自由组合。
 *
 * <p>子包：{@code support}（默认提供者、日志消费者与批量优先收集器基类实现）。
 */
package kunlun.event;
