/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.message.model;

import kunlun.data.Dict;
import kunlun.util.Assert;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * The common delay message object.
 * @author Kahle
 */
public class DelayMessage extends Message {
    private Long     delayTime;
    private TimeUnit delayTimeUnit;
    private Long     createTime;

    public DelayMessage(String topic, Object body) {

        super(topic, body);
    }

    public DelayMessage(String topic) {

        super(topic);
    }

    public DelayMessage() {

    }

    public Long getDelayTime() {

        return delayTime;
    }

    public void setDelayTime(Long delayTime) {

        this.delayTime = delayTime;
    }

    public TimeUnit getDelayTimeUnit() {

        return delayTimeUnit;
    }

    public void setDelayTimeUnit(TimeUnit delayTimeUnit) {

        this.delayTimeUnit = delayTimeUnit;
    }

    public Long getCreateTime() {

        return createTime;
    }

    public void setCreateTime(Long createTime) {

        this.createTime = createTime;
    }

    /**
     * The common delay message builder.
     * @author Kahle
     */
    public static class Builder implements kunlun.core.Builder {

        public static Builder of(String topic, Object body) {

            return of(topic).setBody(body);
        }

        public static Builder of(String topic) {

            return of().setTopic(topic);
        }

        public static Builder of() {

            return new Builder();
        }

        private String   topic;
        private Object   body;
        private Long     delayTime;
        private TimeUnit delayTimeUnit;
        private Long     createTime;
        private Dict     properties = Dict.of();

        public String getTopic() {

            return topic;
        }

        public Builder setTopic(String topic) {
            this.topic = Assert.notBlank(topic);
            return this;
        }

        public Object getBody() {

            return body;
        }

        public Builder setBody(Object body) {
            this.body = Assert.notNull(body);
            return this;
        }

        public Long getDelayTime() {

            return delayTime;
        }

        public Builder setDelayTime(Long delayTime) {
            this.delayTime = Assert.notNull(delayTime);
            return this;
        }

        public TimeUnit getDelayTimeUnit() {

            return delayTimeUnit;
        }

        public Builder setDelayTimeUnit(TimeUnit delayTimeUnit) {
            this.delayTimeUnit = Assert.notNull(delayTimeUnit);
            return this;
        }

        public Long getCreateTime() {

            return createTime;
        }

        public Builder setCreateTime(Long createTime) {
            this.createTime = Assert.notNull(createTime);
            return this;
        }

        public Dict getProperties() {

            return properties;
        }

        public Builder setProperties(Dict properties) {
            this.properties = Assert.notNull(properties);
            return this;
        }

        public Builder putProperties(String key, Object value) {
            this.properties.set(key, value);
            return this;
        }

        public Builder putProperties(Map<?, ?> map) {
            this.properties.set(map);
            return this;
        }

        @Override
        public DelayMessage build() {
            DelayMessage message = new DelayMessage();
            message.setTopic(topic);
            message.setBody(body);
            message.setDelayTime(delayTime);
            message.setDelayTimeUnit(delayTimeUnit);
            message.setCreateTime(createTime);
            message.setProperties(properties);
            return message;
        }
    }

}
