/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.notification;

import kunlun.notification.model.Notification;
import kunlun.notification.model.NotificationRt;

import java.util.Collection;

/**
 * 通知发送器 —— 业务侧（生产方）统一入口的<b>传输介质</b>适配契约（对应事件子系统的收集器一维），
 * 按实现划分：本地直通（{@link kunlun.notification.support.LocalNotificationSender}）、
 * 写 MySQL、发 MQ 等，按实现名注册到 {@link kunlun.notification.NotificationProvider}
 * （大部分场景用 {@link kunlun.notification.NotificationProvider#DEFAULT_SENDER_NAME}）。
 * <p>
 * <h3>实现约定</h3>
 * <ul>
 *   <li>跨服务实现（MySQL / MQ）负责将通知<b>原样</b>送抵通知微服务（原始通知即原始数据，
 *       ID 解析、模板渲染等加工在通知微服务的监听器 / 分发循环内完成），随后调用
 *       {@code NotificationUtil.deliver} 分发投递器；MQ 实现的约定载荷形态为将
 *       {@link kunlun.notification.model.Notification} 包进 {@code kunlun.message.model.Message} 走
 *       {@code MessageBus} —— 即三载体关系中「Notification——发送——&gt; Message」的落地；</li>
 *   <li>本地直通实现只桥接 {@code deliver}，<b>绝不再回调 {@code send}</b>（避免自环）；</li>
 *   <li>异常不隔离：发送器抛出的异常直接外抛给调用方，「发送不影响业务主流程」由调用方
 *       按需防护（try/catch 包装、异步发送等）；投递器（{@code deliver} 侧）是同一异常边界。</li>
 * </ul>
 *
 * @author Kahle
 * @see kunlun.notification.NotificationProvider
 * @see kunlun.notification.NotificationDeliverer
 */
public interface NotificationSender {

    /**
     * 批量发送通知（由实现决定传输介质：本地直通 / 写库 / 上总线等）。
     *
     * @param notifications 待发送的通知集合
     * @return 受理回执（可携带传输层的差异化回执，见
     *         {@link kunlun.notification.model.NotificationRt}；失败直接抛异常，不经回执返回；
     *         经 {@link kunlun.notification.NotificationProvider#send(String, java.util.Collection)}
     *         原样透传给发送调用方）
     */
    <T extends Notification> NotificationRt send(Collection<T> notifications);

}
