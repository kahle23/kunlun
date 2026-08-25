/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.notification.constant;

/**
 * 内容格式常量 —— {@link kunlun.notification.model.Notification#getContentType()} 的取值域，
 * 决定 {@code content} 正文的格式；不支持富文本的通道（如短信）由消费者实现自行降级处理。
 *
 * @author Kahle
 * @see kunlun.notification.model.Notification
 */
public final class ContentType {

    /**
     * 纯文本（默认）。
     */
    public static final String TEXT = "TEXT";
    /**
     * HTML：邮件正文、富文本站内信。
     */
    public static final String HTML = "HTML";
    /**
     * Markdown：钉钉 / 企微 / 飞书机器人消息。
     */
    public static final String MARKDOWN = "MARKDOWN";

    private ContentType() {

        throw new UnsupportedOperationException("Don't allow instantiation. ");
    }
}
