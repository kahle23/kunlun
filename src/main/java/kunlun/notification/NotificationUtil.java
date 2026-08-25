/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.notification;

import kunlun.common.constant.Nil;
import kunlun.logging.Logger;
import kunlun.logging.LoggerFactory;
import kunlun.notification.constant.Channel;
import kunlun.notification.model.Notification;
import kunlun.notification.model.NotificationRt;
import kunlun.notification.support.SimpleNotificationProvider;

import java.util.Collection;
import java.util.Collections;

import static kunlun.util.Assert.*;

/**
 * 通知工具类 —— 通知子系统的静态门面，承载两类职责：
 * <p>
 * <b>① 投递管线</b>（对 {@link kunlun.notification.NotificationProvider} 的转发）：统一管理
 * 发送器（按实现名注册，发送时按名路由，大部分场景注册到
 * {@link kunlun.notification.NotificationProvider#DEFAULT_SENDER_NAME} 并以不传名的
 * {@link #send(Collection)} 发送）与投递器（按通道挂载，通道由注册处指定）。
 * {@code send} 与 {@code deliver} 相互独立且均以<b>批量为核心</b>（单条便捷入口由本门面以
 * 单元素集合包装批量）：{@code send} 路由发送器（本地直通将桥接 {@code deliver}），
 * <b>不在发送器之外分发投递器</b>；典型跨服务链路为「终点型发送器原样写入 MySQL / MQ →
 * 通知微服务监听 / 分发循环解析加工（ID 解析、模板渲染等）→
 * {@link #deliver(Collection)} 分发本端投递器」。两者的受理回执（见
 * {@link kunlun.notification.model.NotificationRt}）经本门面透传：{@code send} 原样透传
 * 发送器回执，{@code deliver} 返回按通道合并的回执。发送器与投递器抛出的异常均不被隔离，
 * 直接外抛给调用方（由调用方按需防护）。
 * <p>
 * <b>② 小红点服务</b>（通知中心读取侧）：{@link kunlun.notification.BadgeService} 为能力接口
 * （未读数统计 + 已读标记），按通道从投递器注册表获取 —— 带参版 {@link #getBadgeService(String)} /
 * {@link #getBadgeServiceOrThrow(String)} 转发
 * {@link kunlun.notification.NotificationProvider#getBadgeService(String)}（OrThrow 缺失即抛异常）；
 * 无参版走默认渠道（{@link #getDefaultBadgeChannel()}，默认
 * {@link kunlun.notification.constant.Channel#INTERNAL}，经 {@link #setDefaultBadgeChannel(String)}
 * 可调整），{@link #getBadgeService()} 缺失返回 null（未挂通知中心的纯推送场景属合法状态），
 * {@link #getBadgeServiceOrThrow()} 为非空版本。实现通常与站内信投递器同体
 * （同一存储的写入面与读取面）。
 *
 * @author Kahle
 * @see kunlun.notification.NotificationProvider
 * @see kunlun.notification.NotificationSender
 * @see kunlun.notification.NotificationDeliverer
 * @see kunlun.notification.BadgeService
 */
public final class NotificationUtil {
    private static final Logger log = LoggerFactory.getLogger(NotificationUtil.class);
    private static volatile NotificationProvider notificationProvider;
    private static volatile String defaultBadgeChannel = Channel.INTERNAL;

    public static NotificationProvider getNotificationProvider() {
        if (notificationProvider != null) { return notificationProvider; }
        synchronized (NotificationUtil.class) {
            if (notificationProvider != null) { return notificationProvider; }
            NotificationUtil.setNotificationProvider(new SimpleNotificationProvider());
            return notificationProvider;
        }
    }

    public static void setNotificationProvider(NotificationProvider notificationProvider) {
        notNull(notificationProvider, "Parameter \"notificationProvider\" must not null. ");
        log.debug("Set notification provider: {}", notificationProvider.getClass().getName());
        NotificationUtil.notificationProvider = notificationProvider;
    }


    // region ======== 发送器 ========
    /**
     * 按实现名注册发送器 —— 转发
     * {@link kunlun.notification.NotificationProvider#registerSender(String, kunlun.notification.NotificationSender)}，
     * 语义见其文档。
     */
    public static void registerSender(String name, NotificationSender sender) {

        getNotificationProvider().registerSender(name, sender);
    }

    /**
     * 按实现名注销发送器 —— 转发
     * {@link kunlun.notification.NotificationProvider#deregisterSender(String)}，语义见其文档。
     */
    public static void deregisterSender(String name) {

        getNotificationProvider().deregisterSender(name);
    }

    /**
     * 按实现名获取发送器 —— 转发
     * {@link kunlun.notification.NotificationProvider#getSender(String)}，语义见其文档。
     */
    public static NotificationSender getSender(String name) {

        return getNotificationProvider().getSender(name);
    }

    /**
     * 批量发送通知到指定发送器（核心方法）—— 转发
     * {@link kunlun.notification.NotificationProvider#send(String, java.util.Collection)}，语义见其文档。
     */
    public static NotificationRt send(String senderName, Collection<Notification> notifications) {

        return getNotificationProvider().send(senderName, notifications);
    }

    /**
     * 批量发送通知到默认发送器 —— 转发
     * {@link kunlun.notification.NotificationProvider#send(java.util.Collection)}
     * （等价于 {@code send(null, notifications)}），语义见其文档。
     */
    public static NotificationRt send(Collection<Notification> notifications) {

        return getNotificationProvider().send(Nil.STR, notifications);
    }

    /**
     * 发送一条通知到指定发送器（单条便捷入口，包装批量方法）。
     *
     * @param senderName   发送器的实现名（null 或空白字符串时走
     *                     {@link kunlun.notification.NotificationProvider#DEFAULT_SENDER_NAME}）
     * @param notification 待发送的通知
     * @return 发送器的受理回执（notification 为 null 时为空回执）
     */
    public static NotificationRt send(String senderName, Notification notification) {
        if (notification == null) { return new NotificationRt(); }
        return send(senderName, Collections.singleton(notification));
    }

    /**
     * 发送一条通知到默认发送器（{@link kunlun.notification.NotificationProvider#DEFAULT_SENDER_NAME}，
     * 单条便捷入口，包装批量方法）。
     *
     * @param notification 待发送的通知
     * @return 发送器的受理回执（notification 为 null 时为空回执）
     */
    public static NotificationRt send(Notification notification) {
        if (notification == null) { return new NotificationRt(); }
        return send(Nil.STR, notification);
    }
    // endregion ======== 发送器 ========


    // region ======== 投递器 ========
    /**
     * 按通道挂载投递器 —— 转发
     * {@link kunlun.notification.NotificationProvider#registerDeliverer(String, kunlun.notification.NotificationDeliverer)}，
     * 语义见其文档。
     */
    public static void registerDeliverer(String channel, NotificationDeliverer deliverer) {

        getNotificationProvider().registerDeliverer(channel, deliverer);
    }

    /**
     * 按通道摘除投递器 —— 转发
     * {@link kunlun.notification.NotificationProvider#deregisterDeliverer(String)}，语义见其文档。
     */
    public static void deregisterDeliverer(String channel) {

        getNotificationProvider().deregisterDeliverer(channel);
    }

    /**
     * 按通道获取投递器 —— 转发
     * {@link kunlun.notification.NotificationProvider#getDeliverer(String)}，语义见其文档。
     */
    public static NotificationDeliverer getDeliverer(String channel) {

        return getNotificationProvider().getDeliverer(channel);
    }

    /**
     * 分发投递器（核心方法）—— 转发
     * {@link kunlun.notification.NotificationProvider#deliver(java.util.Collection)}，语义见其文档。
     */
    public static NotificationRt deliver(Collection<Notification> notifications) {

        return getNotificationProvider().deliver(notifications);
    }

    /**
     * 分发投递器（单条便捷入口，包装批量方法）：按通知的 {@code channels} 分发，不经发送器。
     *
     * @param notification 待投递通知
     * @return 按通道合并的受理回执（notification 为 null 时为空回执）
     */
    public static NotificationRt deliver(Notification notification) {
        if (notification == null) { return new NotificationRt(); }
        return deliver(Collections.singleton(notification));
    }
    // endregion ======== 投递器 ========


    // region ======== 小红点服务 ========
    /**
     * 获取无参小红点服务入口使用的默认渠道（默认
     * {@link kunlun.notification.constant.Channel#INTERNAL}，经
     * {@link #setDefaultBadgeChannel(String)} 可调整）。
     *
     * @return 默认小红点渠道
     */
    public static String getDefaultBadgeChannel() {

        return defaultBadgeChannel;
    }

    /**
     * 设置无参小红点服务入口（{@link #getBadgeService()} /
     * {@link #getBadgeServiceOrThrow()}）使用的默认渠道（仅影响无参入口，默认站内信）。
     *
     * @param channel 通道常量（见 {@link kunlun.notification.constant.Channel}，须非空白）
     */
    public static void setDefaultBadgeChannel(String channel) {
        notBlank(channel, "Parameter \"channel\" must not blank. ");
        defaultBadgeChannel = channel;
    }

    /**
     * 获取指定通道的小红点服务 —— 转发
     * {@link kunlun.notification.NotificationProvider#getBadgeService(String)}，语义见其文档。
     */
    public static BadgeService getBadgeService(String channel) {

        return getNotificationProvider().getBadgeService(channel);
    }

    /**
     * 获取小红点服务（默认渠道便捷入口）：等价于
     * {@code getBadgeService(getDefaultBadgeChannel())} —— 该渠道挂载的投递器实现了
     * {@link kunlun.notification.BadgeService} 能力接口则返回，否则返回 null
     * （未挂通知中心的纯推送场景属合法状态，需非空语义用 {@link #getBadgeServiceOrThrow()}）。
     *
     * @return 小红点服务或 null（默认渠道的投递器未实现该能力接口或未挂载）
     */
    public static BadgeService getBadgeService() {

        return getBadgeService(getDefaultBadgeChannel());
    }

    /**
     * 获取指定通道的小红点服务（非空版本）：与 {@link #getBadgeService(String)} 同源，
     * 但该通道的投递器未实现 {@link kunlun.notification.BadgeService}、未挂载或通道名为空白时
     * 抛 {@link IllegalStateException} 而非返回 null。
     *
     * @param channel 通道常量（见 {@link kunlun.notification.constant.Channel}）
     * @return 小红点服务（非 null）
     */
    public static BadgeService getBadgeServiceOrThrow(String channel) {
        BadgeService badgeService = getBadgeService(channel);
        state(badgeService != null, "The badge service is not available: the deliverer of channel \""
                + channel + "\" does not implement BadgeService or is not registered. ");
        return badgeService;
    }

    /**
     * 获取小红点服务（非空版本，默认渠道）：与 {@link #getBadgeService()} 同源，
     * 但默认渠道的投递器未实现 {@link kunlun.notification.BadgeService} 或未挂载时抛
     * {@link IllegalStateException} 而非返回 null —— 用于「通知中心必须存在，缺失即配置错误」
     * 的调用点。
     *
     * @return 小红点服务（非 null）
     */
    public static BadgeService getBadgeServiceOrThrow() {

        return getBadgeServiceOrThrow(getDefaultBadgeChannel());
    }
    // endregion ======== 小红点服务 ========


    // region ======== 私有的构造方法 ========
    private NotificationUtil() {

        throw new UnsupportedOperationException("Don't allow instantiation. ");
    }
    // endregion

}
