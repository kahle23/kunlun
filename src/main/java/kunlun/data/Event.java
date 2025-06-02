/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.data;

import kunlun.core.Builder;

import java.util.Map;

import static kunlun.util.Assert.notNull;

/**
 * The event record.
 * @author Kahle
 */
public class Event implements Builder {
    // region ======== The constants ========
    /**
     * The user's operation records (in most cases).
     */
    public static final String OPERATION_LOG = "operation-log";
    /**
     * The data change records (in most cases).
     */
    public static final String CHANGE_LOG = "change-log";
    /**
     * The system's run logs (in most cases).
     */
    public static final String RUN_LOG = "run-log";
    // endregion


    // region ======== The static methods ========

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


    // region ======== The event object ========

    private Level  level = Level.INFO;
    private String name;
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


    // region ======== The event level ========
    /**
     * The event level.
     * @author Kahle
     */
    public enum Level {
        /**
         * The trace level.
         */
        TRACE(1),
        /**
         * The debug level.
         */
        DEBUG(2),
        /**
         * The info level.
         */
        INFO(3),
        /**
         * The warning level.
         */
        WARN(4),
        /**
         * The error level.
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
