/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.data;

import kunlun.core.Builder;
import kunlun.exception.ExceptionUtil;

import java.util.Map;

import static kunlun.util.Assert.notNull;

/**
 * 事件记录，参照 <b>Windows 事件查看器</b> 中的一条记录设计 ——
 * 不可变、追加式的审计/诊断记录，回答<i>「发生了什么」</i>，面向运维与审计人员。
 * <p>
 * <h3>定位</h3>
 * <ul>
 *   <li><b>用途：</b>记录已发生的事实（用户操作、数据变动、系统运行），供长期归档与事后回溯。</li>
 *   <li><b>受众：</b>运维 / 审计人员，而非终端用户。</li>
 *   <li><b>生命周期：</b>一次写入、长期保留，不可消除。</li>
 *   <li><b>不是用户通知：</b>{@code Event} 没有接收人、没有标题/图标、没有已读/未读状态。
 *       面向用户的消息归属 {@link kunlun.notification.model.Notification}；
 *       {@code Event} 可以<i>触发</i>一条通知（由订阅型消费者完成，类似 Windows
 *       任务计划程序订阅事件），但二者是不同的载体，不得合并。</li>
 * </ul>
 *
 * <h3>字段映射（参照 Windows 事件查看器）</h3>
 * <table>
 *   <tr><th>概念</th><th>Event 字段</th></tr>
 *   <tr><td>事件类型</td><td>{@code name}（{@link #OPERATION_LOG}/{@link #CHANGE_LOG}/{@link #RUN_LOG}）</td></tr>
 *   <tr><td>事件 ID</td><td>{@code id}</td></tr>
 *   <tr><td>级别 (Verbose/Information/Warning/Error/Critical)</td><td>{@link Level}（TRACE/DEBUG/INFO/WARN/ERROR）</td></tr>
 *   <tr><td>来源 / 提供程序</td><td>{@code module}</td></tr>
 *   <tr><td>用户 / 计算机 / 时间</td><td>{@code userId} / {@code platform} / {@code time}</td></tr>
 *   <tr><td>描述</td><td>{@code message}</td></tr>
 *   <tr><td>业务关联（非 Windows 概念）</td><td>{@code businessType} / {@code businessId}</td></tr>
 * </table>
 *
 * <p>事件经 {@link kunlun.event.EventUtil#collect(kunlun.data.Event)} 投递，由
 * {@link kunlun.data.event.EventCollector} 加工分发，消费者默认包含日志打印
 * （{@code LogEventConsumer}），也可挂载通知转发器等；{@code Action} 总线入口
 * （{@code ActionUtil.execute(event)}）已弃用。
 *
 * @author Kahle
 * @see kunlun.notification.model.Notification
 * @see kunlun.data.event.EventCollector
 */
public class Event implements Builder {
    // region ======== 常量 ========
    /**
     * 用户操作记录（多数场景）。
     */
    public static final String OPERATION_LOG = "operation-log";
    /**
     * 数据变更记录（多数场景）。
     */
    public static final String CHANGE_LOG = "change-log";
    /**
     * 系统运行日志（多数场景）。
     */
    public static final String RUN_LOG = "run-log";
    /**
     * 任意事件类型（通配符，仅用于消费者注册，不作为事件的真实类型）。
     */
    public static final String ANY = "*";
    // endregion


    // region ======== 静态方法 ========

    public static Event ofOperationLog() {

        return of(OPERATION_LOG);
    }

    public static Event ofChangeLog() {

        return of(CHANGE_LOG);
    }

    public static Event ofRunLog() {

        return of(RUN_LOG);
    }

    public static Event of(String name) {

        return of().setName(name);
    }

    public static Event of() {

        return new Event();
    }
    // endregion


    // region ======== 事件字段 ========

    private Level  level = Level.INFO;
    private String name;
    private Object id;
    private Long   time;
    private Object userId;
    private Object userType;
    private String platform;
    private String tenantId;
    private Object businessId;
    private Object businessType;
    private String module;
    private StringBuilder message = new StringBuilder();
    private StringBuilder error   = new StringBuilder();
    private Dict data = Dict.of();

    public Level getLevel() {

        return level;
    }

    public Event setLevel(Level level) {
        this.level = notNull(level);
        return this;
    }

    public String getName() {

        return name;
    }

    public Event setName(String name) {
        this.name = name;
        return this;
    }

    public Object getId() {

        return id;
    }

    public Event setId(Object id) {
        this.id = id;
        return this;
    }

    public Long getTime() {

        return time;
    }

    public Event setTime(Long time) {
        this.time = time;
        return this;
    }

    public Object getUserId() {

        return userId;
    }

    public Event setUserId(Object userId) {
        this.userId = userId;
        return this;
    }

    public Object getUserType() {

        return userType;
    }

    public Event setUserType(Object userType) {
        this.userType = userType;
        return this;
    }

    public String getPlatform() {

        return platform;
    }

    public Event setPlatform(String platform) {
        this.platform = platform;
        return this;
    }

    public String getTenantId() {

        return tenantId;
    }

    public Event setTenantId(String tenantId) {
        this.tenantId = tenantId;
        return this;
    }

    public Object getBusinessId() {

        return businessId;
    }

    public Event setBusinessId(Object businessId) {
        this.businessId = businessId;
        return this;
    }

    public Object getBusinessType() {

        return businessType;
    }

    public Event setBusinessType(Object businessType) {
        this.businessType = businessType;
        return this;
    }

    public String getModule() {

        return module;
    }

    public Event setModule(String module) {
        this.module = module;
        return this;
    }

    public StringBuilder getMessage() {

        return message;
    }

    public Event setMessage(StringBuilder message) {
        this.message = notNull(message);
        return this;
    }

    public Event appendMessage(Object message) {
        this.message.append(message);
        return this;
    }

    public Event appendMessage(String format, Object... args) {
        this.message.append(String.format(format, args));
        return this;
    }

    public Event appendMessage(Builder builder) {
        this.message.append(builder.build());
        return this;
    }

    public StringBuilder getError() {

        return error;
    }

    public Event setError(StringBuilder error) {
        this.error = notNull(error);
        return this;
    }

    public Event appendError(Object error) {
        this.error.append(error);
        return this;
    }

    /**
     * 追加错误信息，写入 Throwable 的完整堆栈（区别于 {@link #appendError(Object)} 只写入 {@code toString()}）。
     *
     * @param error 待追加的异常对象
     * @return 当前事件记录
     */
    public Event appendError(Throwable error) {
        this.error.append(ExceptionUtil.toString(error));
        return this;
    }

    public Dict getData() {

        return data;
    }

    public Event setData(Dict data) {
        this.data = notNull(data);
        return this;
    }

    public Event putData(String key, Object value) {
        this.data.set(key, value);
        return this;
    }

    public Event putData(Map<?, ?> map) {
        this.data.set(map);
        return this;
    }

    @Override
    public Dict build() {
        return Dict.of("level",  level.getValue())
                .set("name",     name)
                .set("id",       id)
                .set("time",     time)
                .set("userId",   userId)
                .set("userType", userType)
                .set("platform", platform)
                .set("tenantId", tenantId)
                .set("businessId",   businessId)
                .set("businessType", businessType)
                .set("module",  module)
                .set("message", message.toString())
                .set("error",   error.toString())
                .set("data",    data)
        ;
    }
    // endregion


    // region ======== 事件级别 ========
    /**
     * 事件级别。
     *
     * @author Kahle
     */
    public enum Level {
        /**
         * 跟踪级别。
         */
        TRACE(1),
        /**
         * 调试级别。
         */
        DEBUG(2),
        /**
         * 信息级别。
         */
        INFO(3),
        /**
         * 警告级别。
         */
        WARN(4),
        /**
         * 错误级别。
         */
        ERROR(5),
        ;

        private final Integer value;

        Level(Integer value) {

            this.value = value;
        }

        public Integer getValue() {

            return value;
        }

        public static Level parse(Integer value) {
            if (value == null) { return null; }
            for (Level level : values()) {
                if (level.getValue().equals(value)) {
                    return level;
                }
            }
            return null;
        }
    }
    // endregion

}
