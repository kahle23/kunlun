/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.notification.model;

/**
 * 通知模块通用常量 —— {@link Notification} 各枚举型字段的取值域。
 *
 * <p>按字段分四组，均以字段名为前缀便于检索：</p>
 * <ul>
 *   <li><b>接收人类型</b>（{@code TARGET_TYPE_} 前缀）：{@link Notification#getTargetType()} 的取值；</li>
 *   <li><b>发送者类型</b>（{@code SENDER_TYPE_} 前缀）：{@link Notification#getSenderType()} 的取值；</li>
 *   <li><b>投递通道</b>（{@code CHANNEL_} 前缀）：{@link Notification#getChannels()} 的取值；</li>
 *   <li><b>内容格式</b>（{@code CONTENT_} 前缀）：{@link Notification#getContentType()} 的取值；</li>
 *   <li><b>优先级</b>（{@code PRIORITY_} 前缀）：{@link Notification#getPriority()} 的取值。</li>
 * </ul>
 *
 * <p>通道与「端」的关系（站内信 / 小红点 / APP 推送如何由通道组合而成）
 * 详见 {@link Notification} 类注释的 <i>通道 ≠ 端</i> 一节。</p>
 *
 * @author Kahle
 * @see Notification
 */
public final class NotificationConstants {

    private NotificationConstants() {
    }


    // region ======== 常量：接收人类型 ========
    /**
     * 接收人类型：指定用户（{@code targetIds} 为用户标识列表，默认值）。
     */
    public static final String TARGET_TYPE_USER = "USER";
    /**
     * 接收人类型：部门（{@code targetIds} 为部门标识，是否包含子部门由服务实现决定）。
     */
    public static final String TARGET_TYPE_DEPARTMENT = "DEPARTMENT";
    /**
     * 接收人类型：公司 / 租户（{@code targetIds} 为公司标识，通常配合 {@code excludedUserIds} 使用）。
     */
    public static final String TARGET_TYPE_COMPANY = "COMPANY";
    /**
     * 接收人类型：用户组 / 角色（{@code targetIds} 为组或角色标识）。
     */
    public static final String TARGET_TYPE_GROUP = "GROUP";
    /**
     * 接收人类型：全员（{@code targetIds} 可为空，配合 {@code excludedUserIds} 排除个别用户）。
     */
    public static final String TARGET_TYPE_ALL = "ALL";
    // endregion

    // region ======== 常量：发送者类型 ========
    /**
     * 发送者类型：真人用户（如聊天消息转通知，此时 {@code senderId} 即消息发送人，
     * 展示端以用户头像呈现、可跳个人主页）。
     */
    public static final String SENDER_TYPE_USER = "USER";
    /**
     * 发送者类型：系统（默认，{@code senderId} 可空，展示端以官方标识呈现）。
     */
    public static final String SENDER_TYPE_SYSTEM = "SYSTEM";
    /**
     * 发送者类型：机器人（审批助手、监控播报等，介于系统与用户之间的拟人化发送者）。
     */
    public static final String SENDER_TYPE_BOT = "BOT";
    // endregion

    // region ======== 常量：投递通道 ========
    /**
     * 投递通道：站内信 —— 写入通知中心，是已读未读状态与小红点未读数统计的数据源。
     */
    public static final String CHANNEL_INTERNAL = "INTERNAL";
    /**
     * 投递通道：手机 APP 推送（厂商通道 / 极光 / 个推等，由服务实现对接）。
     */
    public static final String CHANNEL_PUSH = "PUSH";
    /**
     * 投递通道：微信小程序订阅消息（一次性授权与下发限制由服务实现处理）。
     */
    public static final String CHANNEL_WECHAT_MINI = "WECHAT_MINI";
    /**
     * 投递通道：微信公众号模板消息。
     */
    public static final String CHANNEL_WECHAT_MP = "WECHAT_MP";
    /**
     * 投递通道：短信。
     */
    public static final String CHANNEL_SMS = "SMS";
    /**
     * 投递通道：邮件。
     */
    public static final String CHANNEL_EMAIL = "EMAIL";
    /**
     * 投递通道：钉钉（群机器人 / 工作通知）。
     */
    public static final String CHANNEL_DINGTALK = "DINGTALK";
    /**
     * 投递通道：企业微信（群机器人 / 应用消息）。
     */
    public static final String CHANNEL_WE_COM = "WE_COM";
    /**
     * 投递通道：飞书（群机器人 / 应用消息）。
     */
    public static final String CHANNEL_FEISHU = "FEISHU";
    /**
     * 投递通道：Webhook —— 回调第三方系统的通用出口。
     */
    public static final String CHANNEL_WEBHOOK = "WEBHOOK";
    /**
     * 投递通道：WebSocket 实时下发 —— 用户在线时的即时弹窗与小红点刷新，
     * 只管「推一下」，不负责存储与统计，通常与 {@link #CHANNEL_INTERNAL} 搭配使用。
     */
    public static final String CHANNEL_WEBSOCKET = "WEBSOCKET";
    // endregion

    // region ======== 常量：内容格式 ========
    /**
     * 内容格式：纯文本（默认）。
     */
    public static final String CONTENT_TEXT = "TEXT";
    /**
     * 内容格式：HTML（邮件正文、富文本站内信）。
     */
    public static final String CONTENT_HTML = "HTML";
    /**
     * 内容格式：Markdown（钉钉 / 企微 / 飞书机器人消息）。
     */
    public static final String CONTENT_MARKDOWN = "MARKDOWN";
    // endregion

    // region ======== 常量：优先级 ========
    /**
     * 优先级：紧急 —— 强提醒，可穿透勿扰时段（各通道具体映射由服务实现决定）。
     */
    public static final String PRIORITY_URGENT = "URGENT";
    /**
     * 优先级：高 —— 加急提醒。
     */
    public static final String PRIORITY_HIGH = "HIGH";
    /**
     * 优先级：普通（默认）。
     */
    public static final String PRIORITY_NORMAL = "NORMAL";
    /**
     * 优先级：低 —— 仅写入通知中心，不主动打扰。
     */
    public static final String PRIORITY_LOW = "LOW";
    // endregion

}
