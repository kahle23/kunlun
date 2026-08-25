/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.notification;

import kunlun.notification.constant.Channel;
import kunlun.notification.model.Notification;
import kunlun.notification.model.NotificationRt;
import kunlun.notification.support.LocalNotificationSender;
import kunlun.notification.support.SimpleNotificationProvider;
import org.junit.After;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.fail;

/**
 * 通知工具类测试 —— 静态门面的发送器路由、投递器分发、回执透传与小红点服务获取。
 *
 * @author Kahle
 */
public class NotificationUtilTest {
    private final List<String> registeredChannels = new ArrayList<String>();
    private final List<String> registeredSenders = new ArrayList<String>();

    @After
    public void cleanup() {
        // 只摘除本测试自己挂载的投递器与发送器，保留默认发送器的预挂语义。
        for (String channel : registeredChannels) {
            NotificationUtil.deregisterDeliverer(channel);
        }
        registeredChannels.clear();
        for (String name : registeredSenders) {
            NotificationUtil.deregisterSender(name);
        }
        registeredSenders.clear();
        // 恢复默认发送器的预挂状态与默认小红点渠道，避免影响其他测试。
        NotificationUtil.deregisterSender(NotificationProvider.DEFAULT_SENDER_NAME);
        NotificationUtil.registerSender(NotificationProvider.DEFAULT_SENDER_NAME, new LocalNotificationSender());
        NotificationUtil.setDefaultBadgeChannel(Channel.INTERNAL);
    }

    private void registerDeliverer(String channel, NotificationDeliverer deliverer) {
        NotificationUtil.registerDeliverer(channel, deliverer);
        registeredChannels.add(channel);
    }

    private void registerSender(String name, NotificationSender sender) {
        NotificationUtil.registerSender(name, sender);
        registeredSenders.add(name);
    }

    private static Notification notificationOf(String channel) {
        return Notification.Builder.of(1L).addChannel(channel).appendTitle("test").build();
    }

    @Test
    public void testDefaultSenderPreRegistered() {
        assertNotNull("default sender should be pre-registered",
                NotificationUtil.getSender(NotificationProvider.DEFAULT_SENDER_NAME));
    }

    @Test
    public void testLocalSenderBridgesToDeliver() {
        RecordingDeliverer deliverer = new RecordingDeliverer();
        registerDeliverer(Channel.INTERNAL, deliverer);
        Notification notification = Notification.Builder.of(1L).appendTitle("via local sender").build();
        NotificationUtil.send(notification);
        assertEquals("local sender should bridge to deliver", 1, deliverer.received.size());
    }

    @Test
    public void testEmptyChannelsDefaultsToInternal() {
        RecordingDeliverer deliverer = new RecordingDeliverer();
        registerDeliverer(Channel.INTERNAL, deliverer);
        NotificationUtil.deliver(Notification.Builder.of(1L).build());
        assertEquals("empty channels should be routed to the default channel INTERNAL", 1, deliverer.received.size());
    }

    @Test
    public void testDeliverFansOutByChannels() {
        RecordingDeliverer internal = new RecordingDeliverer();
        RecordingDeliverer sms = new RecordingDeliverer();
        registerDeliverer(Channel.INTERNAL, internal);
        registerDeliverer(Channel.SMS, sms);
        Notification notification = Notification.Builder.of(1L)
                .addChannel(Channel.INTERNAL)
                .addChannel(Channel.SMS)
                .addChannel(Channel.EMAIL) // 未挂载的通道：跳过不抛异常
                .appendTitle("fan out").build();
        NotificationUtil.deliver(notification);
        assertEquals(1, internal.received.size());
        assertEquals(1, sms.received.size());
    }

    @Test
    public void testBatchDeliverGroupsByChannel() {
        RecordingDeliverer sms = new RecordingDeliverer();
        registerDeliverer(Channel.SMS, sms);
        Notification first = notificationOf(Channel.SMS);
        Notification second = notificationOf(Channel.SMS);
        Notification third = notificationOf(Channel.INTERNAL); // 未挂载
        NotificationUtil.deliver(Arrays.asList(first, second, third));
        assertEquals("same-channel notifications should be dispatched as one batch", 1, sms.batchSizes.size());
        assertEquals(Integer.valueOf(2), sms.batchSizes.get(0));
    }

    @Test
    public void testDuplicateChannelsDeliveredOnce() {
        RecordingDeliverer sms = new RecordingDeliverer();
        registerDeliverer(Channel.SMS, sms);
        Notification notification = Notification.Builder.of(1L)
                .addChannel(Channel.SMS)
                .addChannel(Channel.SMS) // 单条通知内重复声明：去重后只投一份
                .build();
        NotificationUtil.deliver(notification);
        assertEquals("duplicate channel declarations should be deduplicated", 1, sms.received.size());
    }

    @Test
    public void testDeliverSkipsNullElementsAndBlankChannels() {
        RecordingDeliverer sms = new RecordingDeliverer();
        registerDeliverer(Channel.SMS, sms);
        Notification valid = notificationOf(Channel.SMS);
        Notification blankChannel = Notification.Builder.of(1L).appendTitle("blank").build();
        // Builder 的 addChannel 会滤掉空白，这里直接 set 模拟外部构造的通知携带空白通道。
        blankChannel.setChannels(Arrays.asList("  ", Channel.SMS));
        NotificationUtil.deliver(Arrays.asList(null, blankChannel, valid));
        assertEquals("null elements and blank channels should be skipped", 2, sms.received.size());
    }

    @Test
    public void testDelivererRegisterAndDeregister() {
        RecordingDeliverer deliverer = new RecordingDeliverer();
        registerDeliverer(Channel.PUSH, deliverer);
        assertSame(deliverer, NotificationUtil.getDeliverer(Channel.PUSH));
        NotificationUtil.deregisterDeliverer(Channel.PUSH);
        assertNull(NotificationUtil.getDeliverer(Channel.PUSH));
    }

    @Test
    public void testNamedSenderRouting() {
        RecordingSender senderA = new RecordingSender();
        RecordingSender senderB = new RecordingSender();
        registerSender("test-sender-a", senderA);
        registerSender("test-sender-b", senderB);
        Notification notification = notificationOf(Channel.INTERNAL);
        NotificationUtil.send("test-sender-a", notification);
        assertEquals("the named sender should be invoked", 1, senderA.sent.size());
        assertEquals("senders of other names must not be invoked", 0, senderB.sent.size());
        // 未注册的发送器名：跳过不抛异常。
        NotificationUtil.send("not-registered-sender", notification);
        assertEquals(0, senderB.sent.size());
    }

    @Test
    public void testNullAndBlankSenderNameRoutesToDefault() {
        RecordingSender sender = new RecordingSender();
        NotificationUtil.registerSender(NotificationProvider.DEFAULT_SENDER_NAME, sender);
        NotificationUtil.send(null, notificationOf(Channel.INTERNAL));
        NotificationUtil.send("  ", notificationOf(Channel.INTERNAL));
        assertEquals("null or blank name should route to the default sender", 2, sender.sent.size());
    }

    @Test
    public void testDeliverDoesNotInvokeSenders() {
        RecordingSender sender = new RecordingSender();
        registerSender("test-deliver-no-sender", sender);
        RecordingDeliverer deliverer = new RecordingDeliverer();
        registerDeliverer(Channel.INTERNAL, deliverer);
        NotificationUtil.deliver(notificationOf(Channel.INTERNAL));
        assertEquals(1, deliverer.received.size());
        assertEquals("senders must not be invoked by deliver", 0, sender.sent.size());
    }

    @Test
    public void testDelivererExceptionPropagates() {
        NotificationDeliverer bad = new NotificationDeliverer() {
            @Override
            public <T extends Notification> NotificationRt deliver(Collection<T> notifications) {
                throw new IllegalStateException("boom");
            }
        };
        registerDeliverer(Channel.SMS, bad);
        RecordingDeliverer good = new RecordingDeliverer();
        registerDeliverer(Channel.INTERNAL, good);
        Notification notification = Notification.Builder.of(1L)
                .addChannel(Channel.SMS)
                .addChannel(Channel.INTERNAL).build();
        try {
            NotificationUtil.deliver(notification);
            fail("the deliverer's exception should propagate to the caller");
        }
        catch (IllegalStateException expected) {
            assertEquals("boom", expected.getMessage());
        }
        // 异常外抛后，分组顺序中靠后的通道不再分发。
        assertEquals(0, good.received.size());
    }

    @Test
    public void testSendReturnsSenderReceipt() {
        RecordingSender sender = new RecordingSender();
        NotificationRt receipt = new NotificationRt();
        sender.receipt = receipt;
        registerSender("test-receipt-sender", sender);
        NotificationRt rt = NotificationUtil.send("test-receipt-sender", notificationOf(Channel.INTERNAL));
        assertSame("send should pass the sender's receipt through", receipt, rt);
    }

    @Test
    public void testDeliverMergesReceiptsByChannel() {
        RecordingDeliverer internal = new RecordingDeliverer();
        RecordingDeliverer sms = new RecordingDeliverer();
        NotificationRt internalReceipt = new NotificationRt();
        NotificationRt smsReceipt = new NotificationRt();
        internal.receipt = internalReceipt;
        sms.receipt = smsReceipt;
        registerDeliverer(Channel.INTERNAL, internal);
        registerDeliverer(Channel.SMS, sms);
        Notification notification = Notification.Builder.of(1L)
                .addChannel(Channel.INTERNAL)
                .addChannel(Channel.SMS).build();
        NotificationRt merged = NotificationUtil.deliver(notification);
        Map<String, Object> details = merged.getDetails();
        assertEquals("receipts should be merged by channel name", 2, details.size());
        assertSame(internalReceipt, details.get(Channel.INTERNAL));
        assertSame(smsReceipt, details.get(Channel.SMS));
    }

    @Test
    public void testEmptyInputReturnsEmptyReceipt() {
        // 空集合 / null 单条入参：返回空回执而非 null。
        assertNotNull(NotificationUtil.send((Notification) null));
        assertNotNull(NotificationUtil.deliver((Notification) null));
        assertNotNull(NotificationUtil.send(Collections.<Notification>emptyList()));
        assertNotNull(NotificationUtil.deliver(Collections.<Notification>emptyList()));
    }

    @Test
    public void testLocalSenderEmptyInputReturnsEmptyReceipt() {
        LocalNotificationSender sender = new LocalNotificationSender();
        assertNotNull("null input should return an empty receipt", sender.send(null));
        assertNotNull("empty input should return an empty receipt",
                sender.send(Collections.<Notification>emptyList()));
    }

    @Test
    public void testSetNotificationProviderInjection() {
        NotificationProvider original = NotificationUtil.getNotificationProvider();
        try {
            SimpleNotificationProvider custom = new SimpleNotificationProvider();
            NotificationUtil.setNotificationProvider(custom);
            assertSame(custom, NotificationUtil.getNotificationProvider());
            try {
                NotificationUtil.setNotificationProvider(null);
                fail("setNotificationProvider should reject null");
            }
            catch (IllegalArgumentException expected) { }
        }
        finally {
            NotificationUtil.setNotificationProvider(original);
        }
    }

    @Test
    public void testBadgeServiceFromDelivererRegistry() {
        // 站内信投递器实现了 BadgeService 能力接口：无参 / 指定通道 / 非空版本均返回同一实例。
        NotificationCenterFake center = new NotificationCenterFake();
        registerDeliverer(Channel.INTERNAL, center);
        assertSame(center, NotificationUtil.getBadgeService());
        assertSame(center, NotificationUtil.getBadgeService(Channel.INTERNAL));
        assertSame(center, NotificationUtil.getBadgeServiceOrThrow());
        // 指定通道挂的是普通投递器（未实现 BadgeService）：返回 null。
        RecordingDeliverer plain = new RecordingDeliverer();
        registerDeliverer(Channel.SMS, plain);
        assertNull(NotificationUtil.getBadgeService(Channel.SMS));
        // 站内信通道换成普通投递器：返回 null，非空版本抛异常。
        registerDeliverer(Channel.INTERNAL, plain);
        assertNull(NotificationUtil.getBadgeService());
        try {
            NotificationUtil.getBadgeServiceOrThrow();
            fail("getBadgeServiceOrThrow should throw when no badge capability is available");
        }
        catch (IllegalStateException expected) { }
    }

    @Test
    public void testGetBadgeServiceWithBlankChannelReturnsNull() {
        assertNull(NotificationUtil.getBadgeService(null));
        assertNull(NotificationUtil.getBadgeService("  "));
    }

    @Test
    public void testSetDefaultBadgeChannelRejectsBlank() {
        try {
            NotificationUtil.setDefaultBadgeChannel(null);
            fail("setDefaultBadgeChannel should reject null");
        }
        catch (IllegalArgumentException expected) { }
        try {
            NotificationUtil.setDefaultBadgeChannel("  ");
            fail("setDefaultBadgeChannel should reject blank");
        }
        catch (IllegalArgumentException expected) { }
        assertEquals(Channel.INTERNAL, NotificationUtil.getDefaultBadgeChannel());
    }

    @Test
    public void testBadgeServiceDefaultChannel() {
        NotificationCenterFake center = new NotificationCenterFake();
        registerDeliverer(Channel.SMS, center);
        // 默认渠道为站内信：SMS 上挂了小红点能力也不取。
        assertEquals(Channel.INTERNAL, NotificationUtil.getDefaultBadgeChannel());
        assertNull(NotificationUtil.getBadgeService());
        // 切换默认渠道为短信：无参普通 / 非空入口都取 SMS 上的实例。
        NotificationUtil.setDefaultBadgeChannel(Channel.SMS);
        try {
            assertSame(center, NotificationUtil.getBadgeService());
            assertSame(center, NotificationUtil.getBadgeServiceOrThrow());
        }
        finally {
            NotificationUtil.setDefaultBadgeChannel(Channel.INTERNAL);
        }
    }

    private static class RecordingDeliverer implements NotificationDeliverer {
        private final List<Notification> received = new ArrayList<Notification>();
        private final List<Integer> batchSizes = new ArrayList<Integer>();
        private NotificationRt receipt = new NotificationRt();

        @Override
        public <T extends Notification> NotificationRt deliver(Collection<T> notifications) {
            received.addAll(notifications);
            batchSizes.add(notifications.size());
            return receipt;
        }
    }

    private static class RecordingSender implements NotificationSender {
        private final List<Notification> sent = new ArrayList<Notification>();
        private NotificationRt receipt = new NotificationRt();

        @Override
        public <T extends Notification> NotificationRt send(Collection<T> notifications) {
            sent.addAll(notifications);
            return receipt;
        }
    }

    // 通知中心的典型形态：站内信投递器（写入面）与小红点服务（读取面）同体，一份存储两张面。
    private static class NotificationCenterFake implements NotificationDeliverer, BadgeService {

        @Override
        public <T extends Notification> NotificationRt deliver(Collection<T> notifications) {
            return new NotificationRt();
        }

        @Override
        public Map<String, Integer> getUnreadCounts(Object userId, Collection<String> types) {
            return Collections.emptyMap();
        }

        @Override
        public void markAsRead(Object userId, Collection<?> notificationIds) { }

        @Override
        public void markAllAsRead(Object userId, Collection<String> types) { }
    }

}
