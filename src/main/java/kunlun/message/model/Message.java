/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.message.model;

import kunlun.message.MessageBus;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * The common message object.
 * @author Kahle
 */
public class Message extends MessageBus.Base {
    private String id;
    private Object body;

    public Message(String topic, Object body, Map<String, Object> properties) {
        super(topic, properties);
        this.body = body;
    }

    public Message(String topic, Object body) {
        super(topic, new LinkedHashMap<String, Object>());
        this.body = body;
    }

    public Message(String topic) {

        super(topic, new LinkedHashMap<String, Object>());
    }

    public Message() {

        this.setProperties(new LinkedHashMap<String, Object>());
    }

    public String getId() {

        return id;
    }

    public void setId(String id) {

        this.id = id;
    }

    public Object getBody() {

        return body;
    }

    public void setBody(Object body) {

        this.body = body;
    }

}
