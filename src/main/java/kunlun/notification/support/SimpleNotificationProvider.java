/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.notification.support;

import kunlun.common.constant.Nil;
import kunlun.logging.Logger;
import kunlun.logging.LoggerFactory;
import kunlun.notification.BadgeService;
import kunlun.notification.NotificationDeliverer;
import kunlun.notification.NotificationProvider;
import kunlun.notification.NotificationSender;
import kunlun.notification.constant.Channel;
import kunlun.notification.model.Notification;
import kunlun.notification.model.NotificationRt;
import kunlun.util.StrUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static kunlun.util.Assert.notBlank;
import static kunlun.util.Assert.notNull;

/**
 * 简单通知提供者 —— {@link kunlun.notification.NotificationProvider} 的默认实现。
 * <p>
 * 两张注册表：发送器按实现名（{@code ConcurrentMap<String, NotificationSender>}，发送时按名路由，
 * 不传名走 {@link kunlun.notification.NotificationProvider#DEFAULT_SENDER_NAME}）、
 * 投递器按通道（{@code ConcurrentMap<String, NotificationDeliverer>}，通道由注册处指定，
 * 后注册者覆盖）。默认预挂 {@link kunlun.notification.support.LocalNotificationSender}
 * （默认发送器名：进程内直通 {@code deliver}）。
 * <p>
 * {@code send} 与 {@code deliver} 相互独立：前者路由指定发送器（传输介质由发送器自身负责，
 * 本地直通将桥接 {@code deliver}），<b>不直接分发投递器</b>（跨服务链路中由通知微服务的
 * 监听器 / 分发循环解析后调用 {@code NotificationUtil.deliver}）；后者按通知的
 * {@code channels} 分组后整批交给对应通道的投递器（为空时按
 * {@link kunlun.notification.constant.Channel#INTERNAL} 处理）。
 * 两者的受理回执原样 / 合并透传（见各方法的返回值说明）。
 * 注册通常在启动期完成，投递期仅做无锁读取（通道分组在局部容器中完成，避免遍历期间的
 * 挂载引起并发修改）。发送器与投递器抛出的异常均不被隔离，直接外抛给调用方
 * （由调用方按需防护）。
 * <p>
 * 通知中心的读取侧不在本类 —— 由 {@code BadgeService} 独立承载（见 {@code NotificationUtil}）。
 *
 * @author Kahle
 */
public class SimpleNotificationProvider implements NotificationProvider {
    private static final Logger log = LoggerFactory.getLogger(SimpleNotificationProvider.class);
    protected final ConcurrentMap<String, NotificationSender> senders;
    protected final ConcurrentMap<String, NotificationDeliverer> deliverers;


    protected SimpleNotificationProvider(ConcurrentMap<String, NotificationSender> senders,
                                         ConcurrentMap<String, NotificationDeliverer> deliverers) {
        notNull(senders, "Parameter \"senders\" must not null. ");
        notNull(deliverers, "Parameter \"deliverers\" must not null. ");
        this.senders = senders;
        this.deliverers = deliverers;
    }

    public SimpleNotificationProvider() {
        this(new ConcurrentHashMap<String, NotificationSender>(),
                new ConcurrentHashMap<String, NotificationDeliverer>());
        // 默认预挂：本地发送器（进程内直通投递）。
        registerSender(DEFAULT_SENDER_NAME, new LocalNotificationSender());
    }

    @Override
    public void registerSender(String name, NotificationSender sender) {
        notNull(sender, "Parameter \"sender\" must not null. ");
        notBlank(name, "Parameter \"name\" must not blank. ");
        senders.put(name, sender);
        log.debug("Register the notification sender \"{}\" to \"{}\". ", sender.getClass().getName(), name);
    }

    @Override
    public void deregisterSender(String name) {
        notBlank(name, "Parameter \"name\" must not blank. ");
        NotificationSender remove = senders.remove(name);
        if (remove != null) {
            log.debug("Deregister the notification sender \"{}\" from \"{}\". ", remove.getClass().getName(), name);
        }
    }

    @Override
    public NotificationSender getSender(String name) {

        return senders.get(name);
    }

    @Override
    public NotificationRt send(String senderName, Collection<Notification> notifications) {
        if (notifications == null || notifications.isEmpty()) { return new NotificationRt(); }
        // 发送器名为 null 或空白时走默认发送器。
        String name = StrUtil.isBlank(senderName) ? DEFAULT_SENDER_NAME : senderName;
        // 路由到指定发送器（未注册则跳过并记日志，通知发送以尽力而为为原则）；
        // 传输介质由发送器自身负责，异常不被隔离、直接外抛给调用方。
        NotificationSender sender = senders.get(name);
        if (sender == null) {
            log.debug("The notification sender \"{}\" is not registered, skip. ", name);
            return new NotificationRt();
        }
        // 发送通知并透传受理回执（本地直通将桥接 deliver，跨服务则原样写入 MySQL / MQ 等介质）。
        return sender.send(notifications);
    }

    @Override
    public NotificationRt send(Collection<Notification> notifications) {

        return send(Nil.STR, notifications);
    }

    @Override
    public void registerDeliverer(String channel, NotificationDeliverer deliverer) {
        notNull(deliverer, "Parameter \"deliverer\" must not null. ");
        notBlank(channel, "Parameter \"channel\" must not blank. ");
        deliverers.put(channel, deliverer);
        log.debug("Register the notification deliverer \"{}\" to \"{}\". ", deliverer.getClass().getName(), channel);
    }

    @Override
    public void deregisterDeliverer(String channel) {
        notBlank(channel, "Parameter \"channel\" must not blank. ");
        NotificationDeliverer remove = deliverers.remove(channel);
        if (remove != null) {
            log.debug("Deregister the notification deliverer \"{}\" from \"{}\". ", remove.getClass().getName(), channel);
        }
    }

    @Override
    public NotificationDeliverer getDeliverer(String channel) {

        return deliverers.get(channel);
    }

    @Override
    public NotificationRt deliver(Collection<Notification> notifications) {
        if (notifications == null || notifications.isEmpty()) { return new NotificationRt(); }
        // 按投递通道分组（分组在局部容器中完成），同通道整批投递；
        // 一条通知声明多通道时逐通道各投一份（单条通知内重复声明的通道去重），
        // 通道为空时按默认通道（站内信）处理。
        Map<String, List<Notification>> grouped = new LinkedHashMap<String, List<Notification>>();
        for (Notification notification : notifications) {
            if (notification == null) { continue; }
            List<String> channels = notification.getChannels();
            if (channels == null || channels.isEmpty()) {
                channels = Collections.singletonList(Channel.INTERNAL);
            }
            for (String channel : new LinkedHashSet<String>(channels)) {
                if (StrUtil.isBlank(channel)) { continue; }
                List<Notification> matched = grouped.get(channel);
                if (matched == null) {
                    matched = new ArrayList<Notification>();
                    grouped.put(channel, matched);
                }
                matched.add(notification);
            }
        }
        // 合并各通道的受理回执：key 为通道名、value 为该通道投递器的回执
        // （未挂载的通道缺省，不拍平合并以免各通道自定义的明细 key 相互覆盖）；
        // 投递器抛出的异常不被隔离 —— 直接外抛给调用方，其后通道不再分发。
        Map<String, Object> details = new LinkedHashMap<String, Object>();
        for (Map.Entry<String, List<Notification>> entry : grouped.entrySet()) {
            NotificationDeliverer deliverer = deliverers.get(entry.getKey());
            if (deliverer == null) {
                log.debug("The notification deliverer of channel \"{}\" is not registered, skip. ", entry.getKey());
                continue;
            }
            NotificationRt receipt = deliverer.deliver(entry.getValue());
            if (receipt != null) {
                details.put(entry.getKey(), receipt);
            }
        }
        return new NotificationRt(details);
    }

    @Override
    public BadgeService getBadgeService(String channel) {
        if (StrUtil.isBlank(channel)) { return null; }
        // 投递器注册表按通道取，实现了能力接口则返回（一份存储的读取面），否则 null。
        NotificationDeliverer deliverer = deliverers.get(channel);
        return deliverer instanceof BadgeService ? (BadgeService) deliverer : null;
    }

}
