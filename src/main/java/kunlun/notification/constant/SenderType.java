/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.notification.constant;

/**
 * 发送者类型常量 —— {@link kunlun.notification.model.Notification#getSenderType()} 的取值域，
 * 决定 {@code senderId} 的语义（与接收人侧 {@code targetType} 决定 {@code targetIds}
 * 语义是同一套约定）。
 *
 * @author Kahle
 * @see kunlun.notification.model.Notification
 */
public final class SenderType {

    /**
     * 真人用户：如聊天消息转通知，此时 {@code senderId} 即消息发送人，
     * 展示端以用户头像呈现、可跳个人主页。
     */
    public static final String USER = "USER";
    /**
     * 系统（默认）：{@code senderId} 可空，展示端以官方标识呈现。
     */
    public static final String SYSTEM = "SYSTEM";
    /**
     * 机器人：审批助手、监控播报等，介于系统与用户之间的拟人化发送者。
     */
    public static final String BOT = "BOT";

    private SenderType() {

        throw new UnsupportedOperationException("Don't allow instantiation. ");
    }
}
