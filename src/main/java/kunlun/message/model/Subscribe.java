/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.message.model;

import kunlun.data.Dict;
import kunlun.message.MessageHandler;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * The message subscription parameters.
 * @author Kahle
 */
public class Subscribe extends MessageHandler.Base {
    private String subExpression;
    private Object messageListener;

    public Subscribe(String topic, Object messageListener) {
        super(topic, new LinkedHashMap<String, Object>());
        this.messageListener = messageListener;
    }

    public Subscribe(String topic) {

        super(topic, new LinkedHashMap<String, Object>());
    }

    public Subscribe() {

        this.subExpression = "*";
    }

    public String getSubExpression() {

        return subExpression;
    }

    public void setSubExpression(String subExpression) {

        this.subExpression = subExpression;
    }

    public Object getMessageListener() {

        return messageListener;
    }

    public void setMessageListener(Object messageListener) {

        this.messageListener = messageListener;
    }

    /**
     * The message subscription parameters builder.
     * @author Kahle
     */
    public static class Builder implements kunlun.core.Builder {

        public static Builder of(String topic) {

            return of().setTopic(topic);
        }

        public static Builder of() {

            return new Builder();
        }

        private String topic;
        private String subExpression = "*";
        private Object messageListener;
        private Dict   properties = Dict.of();

        public String getTopic() {

            return topic;
        }

        public Builder setTopic(String topic) {
            this.topic = topic;
            return this;
        }

        public String getSubExpression() {

            return subExpression;
        }

        public Builder setSubExpression(String subExpression) {
            this.subExpression = subExpression;
            return this;
        }

        public Object getMessageListener() {

            return messageListener;
        }

        public Builder setMessageListener(Object messageListener) {
            this.messageListener = messageListener;
            return this;
        }

        public Dict getProperties() {

            return properties;
        }

        public Builder setProperties(Dict properties) {
            this.properties = properties;
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
        public Subscribe build() {
            Subscribe subscribe = new Subscribe();
            subscribe.setTopic(topic);
            subscribe.setSubExpression(subExpression);
            subscribe.setMessageListener(messageListener);
            subscribe.setProperties(properties);
            return subscribe;
        }
    }

}
