/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.notification;

import java.util.Collection;
import java.util.Map;

/**
 * 小红点服务 —— 通知中心<b>读取侧</b>的能力接口：未读数统计（小红点）与已读标记。
 * <p>
 * 与投递管线相互独立：管线只管「投出去」（写入面），本接口只管「投出去之后每接收人的
 * 已读未读」（读取面），其数据源是站内信（{@link kunlun.notification.constant.Channel#INTERNAL}）
 * 投递落下的「每接收人一条」投递记录。短信 / 手机推送等通道没有查看口子，不实现本接口。
 * <p>
 * 不设独立注册表 —— 作为<b>能力接口</b>由投递器实现：典型形态是通知中心实现类
 * {@code implements NotificationDeliverer, BadgeService}（写入面落投递记录、读取面查改同一份存储），
 * 挂载到通道后经 {@link kunlun.notification.NotificationProvider#getBadgeService(String)}
 * 按通道获取（未实现本接口、未挂载或通道名为空白时返回 null，默认渠道的无参便捷入口与非空版本见
 * {@link kunlun.notification.NotificationUtil}）。接口之外的能力（列表查询、删除等）
 * 由实现自定义扩展方法，持有具体实例的调用方直接调用。
 * <p>
 * 未读数按<b>通知类型</b>（{@code type}）统计而非通道 —— 未读状态只存在于站内信落库
 * 且跨端共享，通道不是统计维度。
 *
 * @author Kahle
 * @see kunlun.notification.NotificationUtil
 * @see kunlun.notification.NotificationDeliverer
 * @see kunlun.notification.model.Notification
 */
public interface BadgeService {

    /**
     * 获取指定用户的未读通知数（红点数）。
     *
     * @param userId 用户标识
     * @param types  通知类型列表（null 或空集合时统计全部类型）
     * @return 未读数映射，key 为通知类型（{@code Notification.type}）
     */
    Map<String, Integer> getUnreadCounts(Object userId, Collection<String> types);

    /**
     * 标记指定用户的通知为已读。
     *
     * @param userId          用户标识
     * @param notificationIds 通知记录标识列表
     */
    void markAsRead(Object userId, Collection<?> notificationIds);

    /**
     * 标记指定用户的通知全部为已读（一键清除红点）。
     *
     * @param userId 用户标识
     * @param types  通知类型列表（null 或空集合时标记全部类型）
     */
    void markAllAsRead(Object userId, Collection<String> types);

}
