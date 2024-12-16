/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.collector.support.model;

import kunlun.core.Builder;
import kunlun.data.Dict;
import kunlun.util.Assert;

import java.util.Map;

/**
 * The track record.
 * @author Kahle
 */
public class TrackRecord implements Builder {

    public static TrackRecord of(String code) {

        return of().setCode(code);
    }

    public static TrackRecord of() {

        return new TrackRecord();
    }


    private TrackLevel level = TrackLevel.INFO;
    private String code;
    private Long   time;
    private Object userId;
    private Object userType;
    private StringBuilder message = new StringBuilder();
    private Dict data = Dict.of();

    public TrackLevel getLevel() {

        return level;
    }

    public TrackRecord setLevel(TrackLevel level) {
        Assert.notNull(level, "Parameter \"level\" must not null. ");
        this.level = level;
        return this;
    }

    public String getCode() {

        return code;
    }

    public TrackRecord setCode(String code) {
        Assert.notBlank(code, "Parameter \"code\" must not blank. ");
        this.code = code;
        return this;
    }

    public Long getTime() {

        return time;
    }

    public TrackRecord setTime(Long time) {
        Assert.notNull(time, "Parameter \"time\" must not null. ");
        this.time = time;
        return this;
    }

    public Object getUserId() {

        return userId;
    }

    public TrackRecord setUserId(Object userId) {
        Assert.notNull(userId, "Parameter \"userId\" must not null. ");
        this.userId = userId;
        return this;
    }

    public Object getUserType() {

        return userType;
    }

    public TrackRecord setUserType(Object userType) {
        Assert.notNull(userType, "Parameter \"userType\" must not null. ");
        this.userType = userType;
        return this;
    }

    public StringBuilder getMessage() {

        return message;
    }

    public TrackRecord setMessage(StringBuilder message) {
        Assert.notNull(message, "Parameter \"message\" must not null. ");
        this.message = message;
        return this;
    }

    public TrackRecord appendMessage(Object message) {
        this.message.append(message);
        return this;
    }

    public TrackRecord appendMessage(String format, Object... args) {
        this.message.append(String.format(format, args));
        return this;
    }

    public Dict getData() {

        return data;
    }

    public TrackRecord setData(Dict data) {
        Assert.notNull(data, "Parameter \"data\" must not null. ");
        this.data = data;
        return this;
    }

    public TrackRecord putData(String key, Object value) {
        this.data.set(key, value);
        return this;
    }

    public TrackRecord putData(Map<?, ?> map) {
        this.data.set(map);
        return this;
    }

    @Override
    public Dict build() {
        Assert.notBlank(code, "Parameter \"code\" must not blank. ");
        return Dict.of("level", level.getValue())
                .set("code", code)
                .set("time", time)
                .set("userId", userId)
                .set("userType", userType)
                .set("message", message.toString())
                .set("data", data)
        ;
    }

}
