/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.notification;

import kunlun.common.model.Link;
import kunlun.notification.model.Notification;
import kunlun.notification.model.NotificationConstants;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * The notification model Test.
 * @author Kahle
 */
public class NotificationTest {

    @Test
    public void testBuilderDefaults() {
        Notification notification = (Notification) Notification.Builder.of().build();
        assertEquals(NotificationConstants.TARGET_TYPE_USER, notification.getTargetType());
        assertEquals(NotificationConstants.SENDER_TYPE_SYSTEM, notification.getSenderType());
        assertEquals(NotificationConstants.CONTENT_TEXT, notification.getContentType());
        assertEquals(NotificationConstants.PRIORITY_NORMAL, notification.getPriority());
        assertTrue("collections should be initialized as empty", notification.getChannels().isEmpty());
        assertTrue(notification.getTargetIds().isEmpty());
        assertTrue(notification.getVariables().isEmpty());
        assertTrue(notification.getLinks().isEmpty());
        assertNull(notification.getType());
        assertNull(notification.getExpireTime());
    }

    @Test
    public void testBuilderFillsAllFields() {
        Notification notification = (Notification) Notification.Builder.of(1L, 2L)
                .addChannel(NotificationConstants.CHANNEL_INTERNAL)
                .addChannel(NotificationConstants.CHANNEL_PUSH)
                .setSenderType(NotificationConstants.SENDER_TYPE_USER)
                .setSenderName("订单中心")
                .setSenderId(1001L)
                .appendTitle("订单已发货")
                .appendContent("您的订单已由顺丰揽收。")
                .setContentType(NotificationConstants.CONTENT_MARKDOWN)
                .setType("order")
                .setBusinessType("order")
                .setBusinessId("SO20260824001")
                .addLink(Link.of("查看详情", "/order/detail/SO20260824001"))
                .setPriority(NotificationConstants.PRIORITY_HIGH)
                .setExpireTime(1756089600000L)
                .build();
        assertEquals(Arrays.asList("INTERNAL", "PUSH"), notification.getChannels());
        assertEquals(Arrays.asList(1L, 2L), notification.getTargetIds());
        assertEquals(NotificationConstants.SENDER_TYPE_USER, notification.getSenderType());
        assertEquals("订单中心", notification.getSenderName());
        assertEquals(1001L, notification.getSenderId());
        assertEquals("订单已发货", notification.getTitle());
        assertEquals("您的订单已由顺丰揽收。", notification.getContent());
        assertEquals(NotificationConstants.CONTENT_MARKDOWN, notification.getContentType());
        assertEquals("order", notification.getType());
        assertEquals("order", notification.getBusinessType());
        assertEquals("SO20260824001", notification.getBusinessId());
        assertEquals(1, notification.getLinks().size());
        assertEquals("查看详情", notification.getLinks().get(0).getName());
        assertEquals("/order/detail/SO20260824001", notification.getLinks().get(0).getAddr());
        assertEquals(NotificationConstants.PRIORITY_HIGH, notification.getPriority());
        assertEquals(Long.valueOf(1756089600000L), notification.getExpireTime());
    }

}
