/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.notification;

import kunlun.notification.model.Notification;
import kunlun.notification.model.NotificationRt;

import java.util.Collection;
import java.util.List;

/**
 * 通知投递器 —— <b>单一投递通道</b>的投递契约：短信 / 邮件 / 站内信落库等，
 * 每个通道一个实现类（如 {@code SmsNotificationDeliverer}、{@code EmailNotificationDeliverer}），
 * 是通知体系的多实现群体，与事件模块的 {@code kunlun.event.EventConsumer} 相对应
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
 * <h3>用户ID解析</h3>
 * 「用户类型 + ID列表」的圈人语法到本通道用户标识（站内信=用户ID、短信=手机号…）的
 * 转换因通道而异，是投递器的固有职责，经 {@link #resolveUserIds(String, List)} 承载 ——
 * 通知目标、发送人等各处 ID 转换通用，由 {@code deliver} 的实现内部调用。
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
     * 解析用户ID：把「用户类型 + ID列表」的圈人语法转换为本通道的用户标识
     * （站内信=用户ID、短信=手机号…），供 {@link #deliver} 内部调用，各处 ID 转换通用。
     *
     * @param targetType 用户类型（kunlun TargetType 常量值：USER/DEPARTMENT/GROUP/ALL…）
     * @param ids        用户ID列表（ALL 类型时可空）
     * @return 用户标识集合（空集合 = 无有效对象，调用方跳过）
     */
    List<Object> resolveUserIds(String targetType, List<Object> ids);

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
