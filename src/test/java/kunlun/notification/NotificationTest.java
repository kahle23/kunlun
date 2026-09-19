/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.notification;

import kunlun.common.model.Link;
import kunlun.notification.constant.Channel;
import kunlun.notification.constant.ContentType;
import kunlun.notification.constant.Priority;
import kunlun.notification.constant.SenderType;
import kunlun.notification.constant.TargetType;
import kunlun.notification.model.Notification;
import kunlun.notification.model.NotificationRt;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * 通知模型测试 —— 构建器默认值、全字段填充、null 防护与受理回执。
 *
 * @author Kahle
 */
public class NotificationTest {

    @Test
    public void testBuilderDefaults() {
        Notification notification = Notification.Builder.of().build();
        assertEquals(TargetType.USER, notification.getTargetType());
        assertEquals(SenderType.SYSTEM, notification.getSenderType());
        assertEquals(ContentType.TEXT, notification.getContentType());
        assertEquals(Priority.NORMAL, notification.getPriority());
        assertTrue("collections should be initialized as empty", notification.getChannels().isEmpty());
        assertTrue(notification.getTargetIds().isEmpty());
        assertTrue(notification.getVariables().isEmpty());
        assertTrue(notification.getLinks().isEmpty());
        assertNull(notification.getType());
        assertNull(notification.getExpireTime());
        // 构建器路径下 title/content 为 StringBuilder 累积，未追加时是空串而非 null。
        assertEquals("title should be empty string when built via builder", "", notification.getTitle());
        assertEquals("content should be empty string when built via builder", "", notification.getContent());
    }

    @Test
    public void testBuilderFillsAllFields() {
        Notification notification = Notification.Builder.of(1L, 2L)
                .addChannel(Channel.INTERNAL)
                .addChannel(Channel.PUSH)
                .setSenderType(SenderType.USER)
                .setSenderName("订单中心")
                .setSenderId(1001L)
                .appendTitle("订单已发货")
                .appendContent("您的订单已由顺丰揽收。")
                .setContentType(ContentType.MARKDOWN)
                .setType("order")
                .setBusinessType("order")
                .setBusinessId("SO20260824001")
                .addLink(Link.of("查看详情", "/order/detail/SO20260824001"))
                .setPriority(Priority.HIGH)
                .setExpireTime(1756089600000L) // 2025-08-25 00:00:00 UTC（毫秒时间戳）
                .build();
        assertEquals(Arrays.asList("INTERNAL", "PUSH"), notification.getChannels());
        assertEquals(Arrays.asList(1L, 2L), notification.getTargetIds());
        assertEquals(SenderType.USER, notification.getSenderType());
        assertEquals("订单中心", notification.getSenderName());
        assertEquals(1001L, notification.getSenderId());
        assertEquals("订单已发货", notification.getTitle());
        assertEquals("您的订单已由顺丰揽收。", notification.getContent());
        assertEquals(ContentType.MARKDOWN, notification.getContentType());
        assertEquals("order", notification.getType());
        assertEquals("order", notification.getBusinessType());
        assertEquals("SO20260824001", notification.getBusinessId());
        assertEquals(1, notification.getLinks().size());
        assertEquals("查看详情", notification.getLinks().get(0).getName());
        assertEquals("/order/detail/SO20260824001", notification.getLinks().get(0).getAddr());
        assertEquals(Priority.HIGH, notification.getPriority());
        assertEquals(Long.valueOf(1756089600000L), notification.getExpireTime());
    }

    @Test
    public void testAppendTitleAndContentNullIgnored() {
        Notification notification = Notification.Builder.of(1L)
                .appendTitle(null)
                .appendContent(null)
                .appendTitle("标题")
                .build();
        assertEquals("标题", notification.getTitle());
        assertEquals("null fragments should not be appended", "", notification.getContent());
    }

    @Test
    public void testSetTitleAndContentStringOverloads() {
        Notification notification = Notification.Builder.of(1L)
                .appendTitle("旧标题")
                .setTitle("新标题")
                .appendContent("旧正文")
                .setContent("新正文")
                .build();
        assertEquals("setTitle(String) should replace the accumulated title", "新标题", notification.getTitle());
        assertEquals("setContent(String) should replace the accumulated content", "新正文", notification.getContent());
    }

    @Test
    public void testBuilderRejectsNull() {
        Notification.Builder builder = Notification.Builder.of();
        try {
            builder.setChannels(null);
            fail("setChannels should reject null");
        }
        catch (IllegalArgumentException expected) { }
        try {
            builder.setTargetType(null);
            fail("setTargetType should reject null");
        }
        catch (IllegalArgumentException expected) { }
        try {
            builder.setTitle((String) null);
            fail("setTitle should reject null");
        }
        catch (IllegalArgumentException expected) { }
    }

    @Test
    public void testReceiptDetails() {
        NotificationRt rt = new NotificationRt();
        assertNull(rt.getDetails());
        Map<String, Object> details = Collections.<String, Object>singletonMap("messageId", "m-1");
        rt.setDetails(details);
        assertSame(details, rt.getDetails());
        assertSame(details, new NotificationRt(details).getDetails());
    }

}
