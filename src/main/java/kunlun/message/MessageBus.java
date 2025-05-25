/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.message;

import kunlun.core.Strategy;
import kunlun.message.model.Message;
import kunlun.message.model.MessageRt;
import kunlun.message.model.Subscribe;
import kunlun.message.model.SubscribeRt;
import kunlun.util.Assert;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;

/**
 * The message handler for producer-consumer models.
 * @author Kahle
 */
public interface MessageBus extends Strategy {

    /**
     * Send the messages.
     * @param messages The messages to be sent
     * @return The result of send
     */
    <T extends Message> MessageRt send(Collection<T> messages);

    /**
     * Receive a message.
     * @param condition The message receiving condition
     * @return The received message or null
     */
    Message receive(Base condition);

    /**
     * Subscribe to a topic.
     * @param subscribe The parameters when subscribing
     * @return The subscription result or null
     */
    SubscribeRt subscribe(Subscribe subscribe);

    /**
     * The message related base object.
     * @author Kahle
     */
    abstract class Base implements Serializable {
        private Map<String, Object> properties;
        private String topic;

        public Base(String topic, Map<String, Object> properties) {
            this.properties = Assert.notNull(properties);
            this.topic = Assert.notBlank(topic);
        }

        public Base() {

        }

        public String getTopic() {

            return topic;
        }

        public void setTopic(String topic) {

            this.topic = topic;
        }

        public Map<String, Object> getProperties() {

            return properties;
        }

        public void setProperties(Map<String, Object> properties) {

            this.properties = properties;
        }
    }

}
