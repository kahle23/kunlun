/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

/**
 * 通知子系统 —— 面向用户的通知的发送与投递，参照 <b>Windows 通知中心</b> 设计，
 * 与 {@code kunlun.event}（审计事实）、{@code kunlun.message}（总线载体）平级，构成三载体体系：
 * <pre>
 *   Event（发生了什么）——订阅/转化——&gt; Notification（告诉谁）
 *   Notification——发送——&gt; Message（怎么送达：经 MessageBus 上总线投递）
 * </pre>
 * <ul>
 *   <li>{@code kunlun.data.Event} = 审计事实（长期、归档、回答「发生了什么」）。</li>
 *   <li>{@code Notification} = 用户消息（瞬时、已读/未读、回答「告诉谁」）。</li>
 *   <li>{@code Message} = 总线消息单元（投递载荷、回答「怎么送达」）。</li>
 * </ul>
 * <p>
 * <h3>两段式管线</h3>
 * <pre>
 *   业务应用（生产方）                              通知微服务（消费方）
 *   NotificationUtil.send(notices)                MQ 监听 / 线程池轮询，读取原始通知
 *     → NotificationProvider.send                   →（分发循环内加工：ID 解析、模板渲染等，
 *     → NotificationSender（传输实现）                   Notification 可承载加工后的数据）
 *         local / mysql（原样保存）/ mq            → NotificationUtil.deliver(notices)
 *                                                   → NotificationProvider.deliver
 *                                                       按 channels 分组 → NotificationDeliverer（渠道投递）
 * </pre>
 * <p>
 * <h3>组成</h3>
 * <ul>
 *   <li>{@code model.Notification}：通知载体（命令对象：告诉谁、展示什么、经哪些通道），
 *       取值域常量见 {@code constant} 子包。</li>
 *   <li>{@code NotificationSender}：<b>发送器</b>，按<b>实现</b>划分（本地直通 / MySQL / MQ...），
 *       是业务侧统一入口的传输介质适配，发送即原样保存原始数据；注册名有
 *       {@code DEFAULT_SENDER_NAME} 约定槽位。MQ 实现的约定载荷形态为将 {@code Notification}
 *       包进 {@code Message} 走 {@code MessageBus}（三载体关系的落地）。</li>
 *   <li>{@code NotificationDeliverer}：<b>投递器</b>，按<b>通道</b>挂载（通道由注册处指定，
 *       与事件消费者按事件类型挂载是同一约定），一个通道一个实现（短信 / 邮件 /
 *       站内信落库...），与事件子系统的 {@code kunlun.event.EventConsumer} 相对应。</li>
 *   <li>{@code NotificationProvider} / {@code NotificationUtil}：管理器抽象与静态门面，
 *       统一管理发送器与投递器两张注册表并提供两类相互独立的投递入口（均以批量为核心，
 *       单条便捷入口由门面包装）：{@code send} 路由发送器（<b>不直接分发投递器</b>）；
 *       {@code deliver} 按通道分组分发投递器。</li>
 *   <li>{@code BadgeService}：<b>小红点服务</b>（通知中心读取侧：未读数统计 + 已读标记），
 *       能力接口、不设独立注册表 —— 由站内信投递器实现（典型形态：通知中心实现类同时实现
 *       {@code NotificationDeliverer} 与 {@code BadgeService}，同一存储的写入面与读取面）。
 *       获取：Provider 的 {@code getBadgeService(channel)} 按通道取（未实现返回 null）；
 *       Util 另有四个入口 —— 带参 / 无参 × 普通 / OrThrow 非空版本，无参走默认渠道
 *       （默认站内信，经 {@code setDefaultBadgeChannel} 可调整）。
 *       接口之外的能力（列表查询、删除等）由实现自定义，持有具体实例的调用方直接调用。</li>
 * </ul>
 * <p>
 * 默认值约定：默认发送器 = 本地直通（{@code support.LocalNotificationSender}，进程内
 * 直通 {@code deliver}）；通知的 {@code channels} 为空时默认按站内信通道
 * （{@code constant.Channel#INTERNAL}）投递。
 * <p>
 * 子包：{@code constant}（通知各枚举型字段的取值域常量）、{@code model}（通知载体与受理回执）、
 * {@code support}（默认提供者与本地发送器实现）。
 */
package kunlun.notification;
