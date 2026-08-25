/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.notification;

import kunlun.common.constant.Words;
import kunlun.notification.model.Notification;
import kunlun.notification.model.NotificationRt;

import java.util.Collection;

/**
 * 通知提供者 —— 通知子系统的管理器抽象：同时管理<b>发送器</b>与<b>投递器</b>两张注册表，
 * 并提供两类相互独立的管线入口：{@link #send(String, Collection)}（路由发送器）与
 * {@link #deliver(Collection)}（分发投递器）。单条便捷入口由静态门面
 * {@link kunlun.notification.NotificationUtil} 基于批量方法包装，本接口只保留批量 API。
 * <p>
 * <h3>三维度模型</h3>
 * <ul>
 *   <li>通知（{@link kunlun.notification.model.Notification}）以 {@code channels} 声明<b>投递通道</b>：
 *       取值见 {@link kunlun.notification.constant.Channel}，
 *       一条通知可同时走多个通道；为空时按默认通道
 *       （{@link kunlun.notification.constant.Channel#INTERNAL}）处理。</li>
 *   <li>发送器（{@link kunlun.notification.NotificationSender}）按<b>实现</b>划分：本地直通 /
 *       写 MySQL / 发 MQ 等，是业务侧（生产方）统一入口的传输介质适配，按实现名注册；
 *       大部分场景注册到 {@link #DEFAULT_SENDER_NAME} 即可。</li>
 *   <li>投递器（{@link kunlun.notification.NotificationDeliverer}）按<b>通道</b>挂载：挂载时由注册处
 *       指定通道（与事件消费者按事件类型挂载是同一约定），一条通知声明多通道时逐通道各投一份。</li>
 * </ul>
 * <p>
 * <h3>投递语义（send 与 deliver 相互独立）</h3>
 * <ul>
 *   <li>{@link #send(String, Collection)}：路由到<b>指定发送器</b>（{@code senderName} 为 null
 *       或空白字符串时走 {@link #DEFAULT_SENDER_NAME}，未注册则跳过并记日志），
 *       传输介质由发送器决定（本地直通将桥接 {@code deliver}），
 *       <b>不直接分发投递器</b>。</li>
 *   <li>{@link #deliver(Collection)}：按通知的 {@code channels} 分组后整批交给挂载在对应通道的
 *       投递器（未挂载的通道跳过并记日志），<b>不经发送器</b>。</li>
 * </ul>
 * <p>
 * 两个入口的异常均不被隔离：发送器 / 投递器抛出的异常直接外抛给调用方（由调用方按需防护）。
 * <p>
 * 典型跨服务链路：业务侧发送器推送（如写 MySQL / 发 MQ，原始通知原样保存）→ 通知微服务的
 * 监听器 / 分发循环解析通知（ID 解析、模板渲染等加工在其内部完成，Notification 可承载加工
 * 后的数据）→ 调用 {@code NotificationUtil.deliver} 分发本端投递器。
 * 本地同进程投递由默认发送器 {@code LocalNotificationSender} 直通 {@code deliver}
 * （其自身绝不再回调 {@code send}）。
 * <p>
 * 通知中心的读取侧（未读数 / 已读标记，小红点）不在投递入口 —— 由
 * {@link kunlun.notification.BadgeService} 能力接口承载，经 {@link #getBadgeService(String)}
 * 按通道获取（{@link kunlun.notification.NotificationUtil} 另有默认渠道（默认站内信、可设置）
 * 的无参便捷入口与非空版本）。
 *
 * @author Kahle
 * @see kunlun.notification.NotificationUtil
 * @see kunlun.notification.NotificationSender
 * @see kunlun.notification.NotificationDeliverer
 * @see kunlun.notification.BadgeService
 * @see kunlun.notification.support.SimpleNotificationProvider
 */
public interface NotificationProvider {

    /**
     * 默认发送器的注册名 —— 大部分场景将发送器注册到该名下即可。
     */
    String DEFAULT_SENDER_NAME = Words.DEFAULT;

    // region ======== 发送器 ========
    /**
     * 按实现名注册发送器（大部分场景用 {@link #DEFAULT_SENDER_NAME}）。
     *
     * @param name   发送器的实现名
     * @param sender 待注册的发送器
     */
    void registerSender(String name, NotificationSender sender);

    /**
     * 按实现名注销发送器。
     *
     * @param name 发送器的实现名
     */
    void deregisterSender(String name);

    /**
     * 按实现名获取发送器。
     *
     * @param name 发送器的实现名
     * @return 发送器或 null（未注册）
     */
    NotificationSender getSender(String name);

    /**
     * 批量发送通知到指定发送器：路由到指定发送器（由其决定传输介质：本地直通将桥接
     * {@link #deliver(Collection)}，跨服务则原样写入 MySQL / MQ 等介质；未注册则跳过并记日志），
     * 不直接分发投递器。发送器抛出的异常不被隔离，直接外抛给调用方（由调用方按需防护）。
     *
     * @param senderName    发送器的实现名（null 或空白字符串时走 {@link #DEFAULT_SENDER_NAME}）
     * @param notifications 待发送的通知集合
     * @return 发送器的受理回执（notifications 为空或发送器未注册时为空回执，见
     *         {@link kunlun.notification.model.NotificationRt}）
     */
    NotificationRt send(String senderName, Collection<Notification> notifications);

    /**
     * 批量发送通知到默认发送器（{@link #DEFAULT_SENDER_NAME}，等价于 {@code send(null, notifications)}）。
     *
     * @param notifications 待发送的通知集合
     * @return 发送器的受理回执（notifications 为空或发送器未注册时为空回执）
     */
    NotificationRt send(Collection<Notification> notifications);
    // endregion ======== 发送器 ========


    // region ======== 投递器 ========
    /**
     * 按通道挂载投递器（通道由注册处指定；一条通知声明多通道时逐通道各投一份 —— 单条通知内
     * 重复声明的通道由 {@link #deliver(Collection)} 去重，同一通道重复挂载以后注册者为准）。
     *
     * @param channel   通道常量（见 {@link kunlun.notification.constant.Channel}）
     * @param deliverer 待挂载的投递器
     */
    void registerDeliverer(String channel, NotificationDeliverer deliverer);

    /**
     * 按通道摘除投递器。
     *
     * @param channel 通道常量
     */
    void deregisterDeliverer(String channel);

    /**
     * 按通道获取投递器。
     *
     * @param channel 通道常量
     * @return 投递器或 null（未挂载）
     */
    NotificationDeliverer getDeliverer(String channel);

    /**
     * 分发投递器：按通知的 {@code channels} 分组后整批交给挂载在对应通道的投递器
     * （{@code channels} 为空时按 {@link kunlun.notification.constant.Channel#INTERNAL} 处理，
     * 单条通知内重复声明的通道去重；未挂载的通道跳过并记日志），不经发送器。
     * 加工（ID 解析、模板渲染等）由调用前完成 —— {@code Notification} 可承载加工后的数据。
     * 投递器抛出的异常不被隔离，直接外抛给调用方（其后通道不再分发）。
     *
     * @param notifications 待投递通知集合
     * @return 按通道合并的受理回执：{@code details} 的 key 为通道名、value 为该通道投递器的
     *         回执（未挂载的通道缺省；notifications 为空时为空回执）
     */
    NotificationRt deliver(Collection<Notification> notifications);
    // endregion ======== 投递器 ========


    // region ======== 小红点服务 ========
    /**
     * 获取小红点服务：取指定通道挂载的投递器，实现了
     * {@link kunlun.notification.BadgeService} 能力接口则返回，否则返回 null
     * （通道未挂载、通道名为空白或投递器未实现均返回 null；站内信通道的便捷入口与非空版本见
     * {@link kunlun.notification.NotificationUtil}）。
     *
     * @param channel 通道常量（见 {@link kunlun.notification.constant.Channel}）
     * @return 小红点服务或 null
     */
    BadgeService getBadgeService(String channel);
    // endregion ======== 小红点服务 ========

}
