/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.notification.constant;

/**
 * 投递通道常量 —— {@link kunlun.notification.model.Notification#getChannels()} 的取值域，
 * 一条通知可同时声明多个通道。
 * <p>
 * 通道描述「经哪种介质投递」，而非「在哪个端展示」—— 常见的端是通道的组合
 * （详见 {@link kunlun.notification.model.Notification} 类注释的 <i>通道 ≠ 端</i> 一节）：
 * WEB 站内信 / 小红点由 {@link #INTERNAL} 写入通知中心（已读未读状态与未读数统计的数据源）。
 * <p>
 * 框架只内置通用通道；微信 / 钉钉 / 飞书等业务通道由调用方以字符串常量自行扩展 ——
 * 通道即字符串，无枚举约束。
 *
 * @author Kahle
 * @see kunlun.notification.model.Notification
 */
public final class Channel {

    /**
     * 站内信：写入通知中心，是已读未读状态与小红点未读数统计的数据源。
     */
    public static final String INTERNAL = "INTERNAL";
    /**
     * 手机 APP 推送：厂商通道 / 极光 / 个推等，由消费者实现对接。
     */
    public static final String PUSH = "PUSH";
    /**
     * 短信。
     */
    public static final String SMS = "SMS";
    /**
     * 邮件。
     */
    public static final String EMAIL = "EMAIL";

    private Channel() {

        throw new UnsupportedOperationException("Don't allow instantiation. ");
    }
}
