/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.notification.model;

import kunlun.common.model.Link;
import kunlun.io.FileBase;
import kunlun.notification.constant.ContentType;
import kunlun.notification.constant.Priority;
import kunlun.notification.constant.SenderType;
import kunlun.notification.constant.TargetType;
import kunlun.util.CollUtil;
import kunlun.util.StrUtil;

import java.io.Serializable;
import java.util.*;

import static kunlun.util.Assert.notNull;

/**
 * 面向用户的通知消息，参照 <b>Windows 操作中心（通知中心）</b> 中的一条通知设计 ——
 * 可消除的瞬时消息，回答<i>「要告诉谁什么」</i>，面向终端用户。
 * <p>
 * <h3>定位</h3>
 * <ul>
 *   <li><b>用途：</b>承载可展示的消息（{@code title}/{@code content}）与跳转语义（{@code links}），
 *       指定接收人（{@code targetType}/{@code targetIds}），用 {@code channels} 选择投递通道，
 *       {@code attachments} 携带附件。</li>
 *   <li><b>受众：</b>终端用户，而非运维。</li>
 *   <li><b>生命周期：</b>短期 / 瞬时，通常有已读 / 未读状态，可设过期（{@code expireTime}）。</li>
 *   <li><b>必须有接收人：</b>与 {@link kunlun.data.Event} 不同，通知若无
 *       {@code targetType}/{@code targetIds} 便无意义。</li>
 *   <li><b>独立于事件：</b>通知可由 {@link kunlun.data.Event} 转化而来（由订阅型消费者完成，
 *       类似 Windows 任务计划程序对事件作出反应），也可直接产生（ops 反馈、系统公告、人工广播）。
 *       它是独立的载体，不是 {@code Event} 的一个变种。</li>
 * </ul>
 * <p>
 * <h3>三载体关系</h3>
 * <pre>
 *   Event（发生了什么）——订阅/转化——&gt; Notification（告诉谁）
 *   Notification——发送——&gt; Message（怎么送达：经 MessageBus 上总线投递）
 * </pre>
 * <ul>
 *   <li>{@link kunlun.data.Event} = 审计事实（长期、归档）。</li>
 *   <li>{@code Notification} = 用户消息（瞬时、已读/未读）。</li>
 *   <li>{@link kunlun.message.model.Message} = 总线消息单元（投递载荷）。</li>
 * </ul>
 * 三者是各自独立的载体，不得合并。
 * <p>
 * <h3>字段一览</h3>
 * <table>
 *   <tr><th>分组</th><th>字段</th><th>回答的问题</th></tr>
 *   <tr><td>投递</td><td>{@code channels}</td><td>经哪些通道送达</td></tr>
 *   <tr><td>类型</td><td>{@code type}</td><td>属于哪类通知（Tab / 红点 / 订阅偏好的统计维度）</td></tr>
 *   <tr><td>接收人</td><td>{@code targetType} + {@code targetIds}（可减 {@code excludedUserIds}）</td><td>告诉谁</td></tr>
 *   <tr><td>发送者</td><td>{@code senderType} / {@code senderName} / {@code senderId}</td><td>谁发的、什么性质的发送者</td></tr>
 *   <tr><td>内容</td><td>{@code title} / {@code contentType} / {@code content}</td><td>展示什么、什么格式</td></tr>
 *   <tr><td>模板</td><td>{@code templateId} + {@code variables}</td><td>按哪个模板、用什么变量渲染</td></tr>
 *   <tr><td>跳转</td><td>{@code links}</td><td>能点去哪（首个为主跳转）</td></tr>
 *   <tr><td>附件</td><td>{@code attachments}</td><td>附带什么文件</td></tr>
 *   <tr><td>业务关联</td><td>{@code businessType} + {@code businessId}</td><td>关联哪张单据</td></tr>
 *   <tr><td>时效</td><td>{@code priority} / {@code expireTime}</td><td>多紧急、何时失效</td></tr>
 *   <tr><td>扩展</td><td>{@code extras}</td><td>通道差异化细节与纯展示信息</td></tr>
 * </table>
 * <p>
 * <h3>通道 ≠ 端</h3>
 * {@code channels} 描述的是「经哪种通道投递」，而非「在哪个端展示」。
 * 常见的端是通道的组合：
 * <ul>
 *   <li><b>WEB 站内信 / 小红点：</b>{@code Channel.INTERNAL} 写入通知中心
 *       （它才是已读未读状态与未读数统计的数据源）；</li>
 *   <li><b>手机 APP 通知：</b>{@code Channel.PUSH}；</li>
 *   <li><b>短信 / 邮件：</b>{@code Channel.SMS} / {@code Channel.EMAIL}。</li>
 * </ul>
 * 微信 / 钉钉 / 飞书等更多通道不在框架内置，由业务以字符串常量自行扩展（通道即字符串）。
 * <p>
 * <h3>本模型不承载的内容</h3>
 * <ul>
 *   <li><b>接收人联系方式</b>（deviceToken / 手机号 / openId / 邮箱）：属接收人档案，
 *       由消费者实现按 {@code targetIds} 自行解析，避免本模型与具体通道耦合；</li>
 *   <li><b>已读状态 / 阅读时间 / 通知记录 ID</b>：属「每个接收人一条」的投递记录
 *       （通知中心数据），群发命令对象上不存在，读取与已读标记经小红点服务
 *       {@link kunlun.notification.BadgeService} 完成；</li>
 *   <li><b>免打扰时段 / 通道偏好：</b>属用户偏好层，由消费者实现在投递时叠加处理；</li>
 *   <li><b>是否落库（通知中心）、是否转 {@link kunlun.message.model.Message} 走推送通道：</b>
 *       由实现决定，不在本模型上承载。</li>
 * </ul>
 *
 * @author Kahle
 * @see kunlun.notification.constant.Channel
 * @see kunlun.notification.constant.TargetType
 * @see kunlun.notification.constant.SenderType
 * @see kunlun.notification.constant.ContentType
 * @see kunlun.notification.constant.Priority
 * @see kunlun.data.Event
 * @see kunlun.notification.NotificationProvider
 * @see kunlun.message.model.Message
 */
public class Notification implements Serializable {

    // region ======== 字段：投递（通道与类型） ========
    /**
     * 投递通道列表，取值见 {@link kunlun.notification.constant.Channel}
     * （如 {@link kunlun.notification.constant.Channel#INTERNAL}、
     * {@link kunlun.notification.constant.Channel#PUSH}、
     * {@link kunlun.notification.constant.Channel#SMS}）；
     * 框架只内置通用通道，微信 / 钉钉 / 飞书等由业务以字符串常量自行扩展。
     * 一条通知可同时走多个通道（如站内信 + 推送 + 短信），单条通知内重复声明的通道由
     * 投递分发自动去重；为空时按默认通道（{@link kunlun.notification.constant.Channel#INTERNAL} 站内信）处理。
     *
     * <p>注意「通道」不等于「端」，端的常见组合见类注释的
     * <i>通道 ≠ 端</i> 一节。</p>
     */
    private List<String> channels;
    /**
     * 通知类型（如 "approval"、"order"、"system"、"announcement"）：通知中心的分类 Tab、
     * 小红点按业务维度的未读统计、用户订阅偏好（「哪些类型的通知经哪些通道送达」）
     * 均以此字段为挂载点。
     *
     * <p>值域由业务自定义，框架不提供常量（区别于 {@code targetType} 的框架约定值域）。
     * 应保持互斥 —— 一条通知只计入一个类型的红点，否则「各类型红点之和 ≠ 总红点」；
     * 多维标签（同时属于订单与售后）用 {@code extras.tags} 承载，只做展示筛选，不参与统计。</p>
     */
    private String type;
    // endregion


    // region ======== 字段：接收人 ========
    /**
     * 接收人类型，取值见 {@link kunlun.notification.constant.TargetType}，
     * 与 {@code targetIds}、{@code excludedUserIds} 配合圈定接收范围。
     *
     * <p>仅支持单一类型：混搭多类接收人（用户 + 部门 + 角色）时拆成多条一次发送即可 ——
     * {@code send} 本就接受集合，落库后每接收人一条投递记录，去重键相同即为同一件事；
     * 不为少数场景加重接收范围解析的实现。</p>
     */
    private String targetType;
    /**
     * 接收人标识列表：含义随 {@code targetType} 而定 —— 用户 ID、部门 ID、角色 ID 等；
     * {@code targetType} 为 {@link kunlun.notification.constant.TargetType#ALL} 时可为空。
     */
    private List<Object> targetIds;
    /**
     * 排除的用户 ID 列表：从上面圈定的接收范围中剔除
     * （典型用法：面向全员 / 部门发送时排除离职、已屏蔽用户或发起人自己）。
     * 仅当排除对象是「用户」时有意义。
     */
    private List<Object> excludedUserIds;
    // endregion


    // region ======== 字段：发送者 ========
    /**
     * 发送者类型，取值见 {@link kunlun.notification.constant.SenderType}，
     * 默认 {@link kunlun.notification.constant.SenderType#SYSTEM}。
     * 展示端据此区分官方标识与用户头像（可跳个人主页）；
     * 聊天消息转通知时为 USER，此时 {@code senderId} 即消息发送人。
     * 它决定 {@code senderId} 的语义 —— 与 {@code targetType} 决定 {@code targetIds}
     * 语义是同一套约定，见 {@code senderId}。
     */
    private String senderType = SenderType.SYSTEM;
    /**
     * 发送方展示名（如 "订单中心"、"系统管理员"），通知界面通常优先展示此字段，
     * 为空时由展示端回退到 {@code senderId}。
     */
    private String senderName;
    /**
     * 发送者标识，语义随 {@code senderType} 而定：USER → 用户 ID；
     * SYSTEM → 模块 / 系统标识（如 "order-service"，系统发送的来源即在于此）；
     * BOT → 机器人 ID。
     * 按「来源模块」统计通知量时，对 SYSTEM 通知按此字段分组即可；
     * USER / BOT 发送若仍需标注来源模块，放 {@code extras.senderModule}。
     */
    private Object senderId;
    // endregion


    // region ======== 字段：内容 ========
    /**
     * 通知标题：通知中心列表、推送横幅、邮件主题通常使用此字段。
     */
    private String title;
    /**
     * 正文内容格式，取值见 {@link kunlun.notification.constant.ContentType}，
     * 默认 {@link kunlun.notification.constant.ContentType#TEXT}。
     * 不支持富文本的通道（如短信）由消费者实现自行降级处理。
     */
    private String contentType = ContentType.TEXT;
    /**
     * 逻辑模板码：与直接写 {@code title}/{@code content} 二选一。
     * 约定此值为业务侧统一的「逻辑模板」标识 —— 多通道发送时由消费者实现负责映射到
     * 各通道的物理模板（微信模板 ID、短信模板 ID、邮件模板互不相同），
     * 配合 {@code variables} 渲染出最终内容。
     */
    private String templateId;
    /**
     * 模板变量：配合 {@code templateId} 渲染内容，key 为变量名，value 为变量值；
     * 直接写 {@code title}/{@code content} 时通常用不到。
     */
    private Map<String, Object> variables;
    /**
     * 通知正文：格式由 {@code contentType} 决定（纯文本 / HTML / Markdown）。
     */
    private String content;
    /**
     * 跳转项列表，每项为「名称 + 地址」（见 {@link Link}），
     * 如：查看 → /order/1、同意 → /approval/1/accept。
     *
     * <p>约定第一项为主跳转（点击通知本体）。通道仅支持单个跳转时（微信模板消息、
     * 小程序订阅消息、推送点击）由消费者实现取第一项、其余忽略；支持多按钮的通道
     * （APP 通知操作按钮、通知中心展开操作）可全部适配。跳转是投递语义而非纯 UI 细节，
     * 故作为一等字段而非放入 {@code extras}。</p>
     */
    private List<Link> links;
    /**
     * 附件列表：邮件、站内信等支持附件的通道使用；
     * 不支持附件的通道（短信、推送等）由服务实现忽略。
     */
    private List<FileBase> attachments;
    // endregion


    // region ======== 字段：业务关联 ========
    /**
     * 关联业务类型（如 "order"、"approval-ticket"），与 {@code businessId} 配合定位具体单据 ——
     * 点击通知或小红点时可据此跳转到对应业务页面。
     * 与 {@link kunlun.data.Event} 的同名字段对齐，通知由事件转化时可直接透传。
     */
    private String businessType;
    /**
     * 关联业务标识（如订单 ID、审批单 ID），与 {@code businessType} 配合定位具体单据。
     */
    private Object businessId;
    // endregion


    // region ======== 字段：时效（优先级与过期） ========
    /**
     * 优先级，取值见 {@link kunlun.notification.constant.Priority}，
     * 默认 {@link kunlun.notification.constant.Priority#NORMAL}。
     * 影响推送通道的提醒强度（是否弹横幅 / 穿透勿扰）、通知列表排序等，
     * 各通道的具体映射由消费者实现决定。
     */
    private String priority = Priority.NORMAL;
    /**
     * 过期时间（毫秒时间戳，与 {@link kunlun.data.Event#getTime()} 同一量纲），
     * 为空表示不过期。过期后：
     * <ul>
     *   <li>推送通道不再弹出（避免过时打扰，如「会议 5 分钟后开始」迟到了就不必再弹）；</li>
     *   <li>通知中心可据此清理过期消息；</li>
     *   <li>小红点不再计数。</li>
     * </ul>
     */
    private Long expireTime;
    // endregion


    // region ======== 字段：扩展 ========
    /**
     * 扩展属性：承载各通道的差异化细节与纯展示信息，框架本身不感知其内容。
     * 约定的常用 key（服务实现可支持也可忽略）：
     * <ul>
     *   <li>{@code icon} —— 通知图标（纯 UI 细节，故不入一等字段）；</li>
     *   <li>{@code badge} —— 角标数字；</li>
     *   <li>{@code collapseKey} —— 推送合并键，同键通知在设备上合并覆盖（如"你有 N 条新点赞"）；</li>
     *   <li>{@code dedupeKey} —— 去重 / 幂等键，发送重试时防止重复送达；</li>
     *   <li>{@code silent} —— 是否静默送达（只落通知中心不弹横幅）；</li>
     *   <li>{@code tags} —— 多维标签（同时属于订单与售后），只做展示筛选，不参与红点统计。</li>
     * </ul>
     */
    private Map<String, Object> extras;
    // endregion


    // region ======== 读取与写入（getter / setter） ========

    public List<String> getChannels() {

        return channels;
    }

    public void setChannels(List<String> channels) {

        this.channels = channels;
    }

    public String getType() {

        return type;
    }

    public void setType(String type) {

        this.type = type;
    }

    public String getTargetType() {

        return targetType;
    }

    public void setTargetType(String targetType) {

        this.targetType = targetType;
    }

    public List<Object> getTargetIds() {

        return targetIds;
    }

    public void setTargetIds(List<Object> targetIds) {

        this.targetIds = targetIds;
    }

    public List<Object> getExcludedUserIds() {

        return excludedUserIds;
    }

    public void setExcludedUserIds(List<Object> excludedUserIds) {

        this.excludedUserIds = excludedUserIds;
    }

    public String getSenderType() {

        return senderType;
    }

    public void setSenderType(String senderType) {

        this.senderType = senderType;
    }

    public String getSenderName() {

        return senderName;
    }

    public void setSenderName(String senderName) {

        this.senderName = senderName;
    }

    public Object getSenderId() {

        return senderId;
    }

    public void setSenderId(Object senderId) {

        this.senderId = senderId;
    }

    public String getTitle() {

        return title;
    }

    public void setTitle(String title) {

        this.title = title;
    }

    public String getContentType() {

        return contentType;
    }

    public void setContentType(String contentType) {

        this.contentType = contentType;
    }

    public String getTemplateId() {

        return templateId;
    }

    public void setTemplateId(String templateId) {

        this.templateId = templateId;
    }

    public Map<String, Object> getVariables() {

        return variables;
    }

    public void setVariables(Map<String, Object> variables) {

        this.variables = variables;
    }

    public String getContent() {

        return content;
    }

    public void setContent(String content) {

        this.content = content;
    }

    public List<Link> getLinks() {

        return links;
    }

    public void setLinks(List<Link> links) {

        this.links = links;
    }

    public List<FileBase> getAttachments() {

        return attachments;
    }

    public void setAttachments(List<FileBase> attachments) {

        this.attachments = attachments;
    }

    public String getBusinessType() {

        return businessType;
    }

    public void setBusinessType(String businessType) {

        this.businessType = businessType;
    }

    public Object getBusinessId() {

        return businessId;
    }

    public void setBusinessId(Object businessId) {

        this.businessId = businessId;
    }

    public String getPriority() {

        return priority;
    }

    public void setPriority(String priority) {

        this.priority = priority;
    }

    public Long getExpireTime() {

        return expireTime;
    }

    public void setExpireTime(Long expireTime) {

        this.expireTime = expireTime;
    }

    public Map<String, Object> getExtras() {

        return extras;
    }

    public void setExtras(Map<String, Object> extras) {

        this.extras = extras;
    }
    // endregion


    // region ======== 构建器（Builder） ========
    /**
     * {@link Notification} 的构建器。
     * <p>默认值：{@code targetType} 为 {@link kunlun.notification.constant.TargetType#USER}、
     * {@code senderType} 为 {@link kunlun.notification.constant.SenderType#SYSTEM}、
     * {@code contentType} 为 {@link kunlun.notification.constant.ContentType#TEXT}、
     * {@code priority} 为 {@link kunlun.notification.constant.Priority#NORMAL}，
     * 集合类字段初始化为空集合，{@code build()} 时整体回填到 {@code Notification}。
     * 注意 {@code title} / {@code content} 以 {@code StringBuilder} 累积构建，
     * 未追加时 {@code build()} 得到的是<b>空串</b>（区别于直接 {@code new Notification()}
     * 时对应字段的 null）。
     *
     * @author Kahle
     */
    public static class Builder implements kunlun.core.Builder {

        /**
         * 以指定接收人创建构建器（等价于 {@code of().addTargetIds(...)}）。
         *
         * @param targetIds 接收人标识
         * @return 构建器
         */
        public static Builder of(Object... targetIds) {

            return of().addTargetIds(Arrays.asList(targetIds));
        }

        /**
         * 创建空构建器。
         *
         * @return 构建器
         */
        public static Builder of() {

            return new Builder();
        }

        private List<String> channels = new ArrayList<String>();
        private String type;
        private String targetType = TargetType.USER;
        private List<Object> targetIds = new ArrayList<Object>();
        private List<Object> excludedUserIds = new ArrayList<Object>();
        private String senderType = SenderType.SYSTEM;
        private String senderName;
        private Object senderId;
        private StringBuilder title = new StringBuilder();
        private String contentType = ContentType.TEXT;
        private String templateId;
        private Map<String, Object> variables = new LinkedHashMap<String, Object>();
        private StringBuilder content = new StringBuilder();
        private List<Link> links = new ArrayList<Link>();
        private List<FileBase> attachments = new ArrayList<FileBase>();
        private String businessType;
        private Object businessId;
        private String priority = Priority.NORMAL;
        private Long expireTime;
        private Map<String, Object> extras = new LinkedHashMap<String, Object>();

        public List<String> getChannels() {

            return channels;
        }

        public Builder setChannels(List<String> channels) {
            this.channels = notNull(channels);
            return this;
        }

        public Builder addChannels(List<String> channels) {
            if (CollUtil.isNotEmpty(channels)) {
                this.channels.addAll(channels);
            }
            return this;
        }

        /**
         * 追加一个投递通道，空白通道忽略不计。
         *
         * @param channel 通道常量（见 {@link kunlun.notification.constant.Channel}）
         * @return 当前构建器
         */
        public Builder addChannel(String channel) {
            if (StrUtil.isNotBlank(channel)) {
                this.channels.add(channel);
            }
            return this;
        }

        public String getType() {

            return type;
        }

        public Builder setType(String type) {
            this.type = type;
            return this;
        }

        public String getTargetType() {

            return targetType;
        }

        public Builder setTargetType(String targetType) {
            this.targetType = notNull(targetType);
            return this;
        }

        public List<Object> getTargetIds() {

            return targetIds;
        }

        public Builder setTargetIds(List<Object> targetIds) {
            this.targetIds = notNull(targetIds);
            return this;
        }

        public Builder addTargetIds(List<Object> targetIds) {
            if (CollUtil.isNotEmpty(targetIds)) {
                this.targetIds.addAll(targetIds);
            }
            return this;
        }

        public Builder addTargetId(Object targetId) {
            if (targetId != null) {
                this.targetIds.add(targetId);
            }
            return this;
        }

        public List<Object> getExcludedUserIds() {

            return excludedUserIds;
        }

        public Builder setExcludedUserIds(List<Object> excludedUserIds) {
            this.excludedUserIds = notNull(excludedUserIds);
            return this;
        }

        public String getSenderType() {

            return senderType;
        }

        public Builder setSenderType(String senderType) {
            this.senderType = notNull(senderType);
            return this;
        }

        public String getSenderName() {

            return senderName;
        }

        public Builder setSenderName(String senderName) {
            this.senderName = senderName;
            return this;
        }

        public Object getSenderId() {

            return senderId;
        }

        public Builder setSenderId(Object senderId) {
            this.senderId = senderId;
            return this;
        }

        public StringBuilder getTitle() {

            return title;
        }

        public Builder setTitle(StringBuilder title) {
            this.title = notNull(title);
            return this;
        }

        public Builder setTitle(String title) {
            this.title = new StringBuilder(notNull(title));
            return this;
        }

        /**
         * 追加标题片段，null 忽略不计。
         *
         * @param title 片段内容
         * @return 当前构建器
         */
        public Builder appendTitle(Object title) {
            if (title != null) {
                this.title.append(title);
            }
            return this;
        }

        public String getContentType() {

            return contentType;
        }

        public Builder setContentType(String contentType) {
            this.contentType = notNull(contentType);
            return this;
        }

        public String getTemplateId() {

            return templateId;
        }

        public Builder setTemplateId(String templateId) {
            this.templateId = templateId;
            return this;
        }

        public Map<String, Object> getVariables() {

            return variables;
        }

        public Builder setVariables(Map<String, Object> variables) {
            this.variables = notNull(variables);
            return this;
        }

        public StringBuilder getContent() {

            return content;
        }

        public Builder setContent(StringBuilder content) {
            this.content = notNull(content);
            return this;
        }

        public Builder setContent(String content) {
            this.content = new StringBuilder(notNull(content));
            return this;
        }

        /**
         * 追加正文片段，null 忽略不计。
         *
         * @param content 片段内容
         * @return 当前构建器
         */
        public Builder appendContent(Object content) {
            if (content != null) {
                this.content.append(content);
            }
            return this;
        }

        public List<Link> getLinks() {

            return links;
        }

        public Builder setLinks(List<Link> links) {
            this.links = notNull(links);
            return this;
        }

        public Builder addLinks(List<Link> links) {
            if (CollUtil.isNotEmpty(links)) {
                this.links.addAll(links);
            }
            return this;
        }

        /**
         * 追加一个跳转项；约定首个跳转项为主跳转（点击通知本体）。
         *
         * @param link 跳转项（名称 + 地址）
         * @return 当前构建器
         */
        public Builder addLink(Link link) {
            if (link != null) {
                this.links.add(link);
            }
            return this;
        }

        public List<FileBase> getAttachments() {

            return attachments;
        }

        public Builder setAttachments(List<FileBase> attachments) {
            this.attachments = notNull(attachments);
            return this;
        }

        public Builder addAttachments(List<FileBase> attachments) {
            if (CollUtil.isNotEmpty(attachments)) {
                this.attachments.addAll(attachments);
            }
            return this;
        }

        public Builder addAttachment(FileBase attachment) {
            if (attachment != null) {
                this.attachments.add(attachment);
            }
            return this;
        }

        public String getBusinessType() {

            return businessType;
        }

        public Builder setBusinessType(String businessType) {
            this.businessType = businessType;
            return this;
        }

        public Object getBusinessId() {

            return businessId;
        }

        public Builder setBusinessId(Object businessId) {
            this.businessId = businessId;
            return this;
        }

        public String getPriority() {

            return priority;
        }

        public Builder setPriority(String priority) {
            this.priority = notNull(priority);
            return this;
        }

        public Long getExpireTime() {

            return expireTime;
        }

        public Builder setExpireTime(Long expireTime) {
            this.expireTime = expireTime;
            return this;
        }

        public Map<String, Object> getExtras() {

            return extras;
        }

        public Builder setExtras(Map<String, Object> extras) {
            this.extras = notNull(extras);
            return this;
        }

        @Override
        public Notification build() {
            Notification notification = new Notification();
            notification.setChannels(channels);
            notification.setType(type);
            notification.setTargetType(targetType);
            notification.setTargetIds(targetIds);
            notification.setExcludedUserIds(excludedUserIds);
            notification.setSenderType(senderType);
            notification.setSenderName(senderName);
            notification.setSenderId(senderId);
            notification.setTitle(String.valueOf(title));
            notification.setContentType(contentType);
            notification.setTemplateId(templateId);
            notification.setVariables(variables);
            notification.setContent(String.valueOf(content));
            notification.setLinks(links);
            notification.setAttachments(attachments);
            notification.setBusinessType(businessType);
            notification.setBusinessId(businessId);
            notification.setPriority(priority);
            notification.setExpireTime(expireTime);
            notification.setExtras(extras);
            return notification;
        }
    }
    // endregion

}
