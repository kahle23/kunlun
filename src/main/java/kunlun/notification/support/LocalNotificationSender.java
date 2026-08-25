/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.notification.support;

import kunlun.notification.NotificationSender;
import kunlun.notification.NotificationUtil;
import kunlun.notification.model.Notification;
import kunlun.notification.model.NotificationRt;

import java.util.ArrayList;
import java.util.Collection;

/**
 * 本地通知发送器 —— {@link kunlun.notification.NotificationSender} 的默认实现，
 * 由 {@code SimpleNotificationProvider} 预挂到
 * {@code NotificationProvider.DEFAULT_SENDER_NAME}（也可自行注册到其他名下）。
 * <p>
 * 进程内直通：{@link #send(Collection)} 直接桥接
 * {@link kunlun.notification.NotificationUtil#deliver(java.util.Collection)} 分发本地投递器
 * （入口是 {@code NotificationUtil.send}，发送器内不再回调 {@code send}，避免自环），
 * 并将 {@code deliver} 按通道合并的受理回执原样返回。跨服务场景应替换为终点型发送器
 * （如写 MySQL、发 MQ），由通知微服务的监听器 / 分发循环解析后自行调用
 * {@code NotificationUtil.deliver}。发送异常直接外抛给调用方（由调用方按需防护）。
 *
 * @author Kahle
 */
public class LocalNotificationSender implements NotificationSender {

    @Override
    public <T extends Notification> NotificationRt send(Collection<T> notifications) {
        if (notifications == null || notifications.isEmpty()) { return new NotificationRt(); }
        // 本地桥接：分发本地投递器（入口是 NotificationUtil.send，这里不再回调 send）；
        // 拷贝一份入参，隔离调用方集合在 deliver 分组遍历期间的后续修改。
        return NotificationUtil.deliver(new ArrayList<Notification>(notifications));
    }

}
