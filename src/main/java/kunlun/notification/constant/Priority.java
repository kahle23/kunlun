/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.notification.constant;

/**
 * 优先级常量 —— {@link kunlun.notification.model.Notification#getPriority()} 的取值域，
 * 影响推送通道的提醒强度（是否弹横幅 / 穿透勿扰）、通知列表排序等，
 * 各通道的具体映射由消费者实现决定。
 *
 * @author Kahle
 * @see kunlun.notification.model.Notification
 */
public final class Priority {

    /**
     * 紧急：强提醒，可穿透勿扰时段（各通道具体映射由消费者实现决定）。
     */
    public static final String URGENT = "URGENT";
    /**
     * 高：加急提醒。
     */
    public static final String HIGH = "HIGH";
    /**
     * 普通（默认）。
     */
    public static final String NORMAL = "NORMAL";
    /**
     * 低：仅写入通知中心，不主动打扰。
     */
    public static final String LOW = "LOW";

    private Priority() {

        throw new UnsupportedOperationException("Don't allow instantiation. ");
    }
}
