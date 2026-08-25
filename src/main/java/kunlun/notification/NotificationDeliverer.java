/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.notification;

import kunlun.notification.model.Notification;
import kunlun.notification.model.NotificationRt;

import java.util.Collection;

/**
 * 通知投递器 —— <b>单一投递通道</b>的投递契约：短信 / 邮件 / 站内信落库等，
 * 每个通道一个实现类（如 {@code SmsNotificationDeliverer}、{@code EmailNotificationDeliverer}），
 * 是通知体系的多实现群体，与事件子系统的 {@code kunlun.event.EventConsumer} 相对应
 * （事件侧消费处理审计事实，本侧投递用户消息，故不取「消费者」之名）。
 * <p>
 * <h3>挂载与调用</h3>
 * <ul>
 *   <li>经 {@link kunlun.notification.NotificationProvider} 的
 *       {@code registerDeliverer(channel, deliverer)} 按通道挂载 —— 通道由注册处指定
 *       （与事件消费者按事件类型挂载是同一约定）—— 之后由 {@code deliver} 在分发时调用，
 *       收到的通知<b>已按本通道过滤</b>。</li>
 * </ul>
 * <p>
 * 异常不隔离：投递器抛出的异常直接外抛给调用方（与发送器是同一异常边界），
 * 「投递不影响业务主流程」由调用方按需防护；批内粒度的容错由实现自行决定。
 *
 * @author Kahle
 * @see kunlun.notification.NotificationProvider
 * @see kunlun.notification.NotificationSender
 */
public interface NotificationDeliverer {

    /**
     * 批量投递通知（经 {@link kunlun.notification.NotificationProvider#deliver(java.util.Collection)}
     * 分发时，收到的已按本通道过滤），由实现完成本通道的实际投递。
     *
     * @param notifications 待投递的通知集合
     * @return 受理回执（见 {@link kunlun.notification.model.NotificationRt}；
     *         异步通道仅代表受理成功，不代表最终送达；失败直接抛异常，不经回执返回；
     *         经 Provider 的 {@code deliver} 以「key = 通道名」合并进总回执透传）
     */
    <T extends Notification> NotificationRt deliver(Collection<T> notifications);

}
