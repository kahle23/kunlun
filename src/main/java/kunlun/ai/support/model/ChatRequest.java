/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.ai.support.model;

import kunlun.core.function.Consumer;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ChatRequest implements Serializable {
    private String  model;
    private Double  temperature;
    private Double  topP;
    private Integer maxTokens;
    private Boolean stream;
    private Consumer<Object> streamConsumer;
    private List<Message> messages;
    private Map<String, Object> config;

    public ChatRequest(String model) {

        this.model = model;
    }

    public ChatRequest() {

    }

    public String getModel() {

        return model;
    }

    public void setModel(String model) {

        this.model = model;
    }

    public Double getTemperature() {

        return temperature;
    }

    public void setTemperature(Double temperature) {

        this.temperature = temperature;
    }

    public Double getTopP() {

        return topP;
    }

    public void setTopP(Double topP) {

        this.topP = topP;
    }

    public Integer getMaxTokens() {

        return maxTokens;
    }

    public void setMaxTokens(Integer maxTokens) {

        this.maxTokens = maxTokens;
    }

    public Boolean getStream() {

        return stream;
    }

    public void setStream(Boolean stream) {

        this.stream = stream;
    }

    public Consumer<Object> getStreamConsumer() {

        return streamConsumer;
    }

    public void setStreamConsumer(Consumer<Object> streamConsumer) {

        this.streamConsumer = streamConsumer;
    }

    public List<Message> getMessages() {

        return messages;
    }

    public void setMessages(List<Message> messages) {

        this.messages = messages;
    }

    public Map<String, Object> getConfig() {

        return config;
    }

    public void setConfig(Map<String, Object> config) {

        this.config = config;
    }

    public static class Builder implements kunlun.core.Builder {

        public static Builder of(String model) {

            return of().setModel(model);
        }

        public static Builder of() {

            return new Builder();
        }

        private String  model;
        private Double  temperature;
        private Double  topP;
        private Integer maxTokens;
        private Boolean stream;
        private Consumer<Object> streamConsumer;
        private final List<Message> messages = new ArrayList<Message>();
        private final Map<String, Object> config = new LinkedHashMap<String, Object>();

        public String getModel() {

            return model;
        }

        public Builder setModel(String model) {
            this.model = model;
            return this;
        }

        public Double getTemperature() {

            return temperature;
        }

        public Builder setTemperature(Double temperature) {
            this.temperature = temperature;
            return this;
        }

        public Double getTopP() {

            return topP;
        }

        public Builder setTopP(Double topP) {
            this.topP = topP;
            return this;
        }

        public Integer getMaxTokens() {

            return maxTokens;
        }

        public Builder setMaxTokens(Integer maxTokens) {
            this.maxTokens = maxTokens;
            return this;
        }

        public Boolean getStream() {

            return stream;
        }

        public Builder setStream(Boolean stream) {
            this.stream = stream;
            return this;
        }

        public Consumer<Object> getStreamConsumer() {

            return streamConsumer;
        }

        public Builder setStreamConsumer(Consumer<Object> streamConsumer) {
            this.streamConsumer = streamConsumer;
            return this;
        }

        public List<Message> getMessages() {

            return messages;
        }

        public Builder addMessages(List<Message> messages) {
            this.messages.addAll(messages);
            return this;
        }

        public Builder addMessage(Message message) {
            this.messages.add(message);
            return this;
        }

        public Builder addMessage(String name, String role, Object content) {
            this.messages.add(new Message(name, role, content));
            return this;
        }

        public Builder addMessage(String role, Object content) {
            this.messages.add(new Message(role, content));
            return this;
        }

        public Map<String, Object> getConfig() {

            return config;
        }

        @SuppressWarnings("unchecked")
        public Builder addConfigs(Map<?, ?> config) {
            this.config.putAll((Map<String, ?>) config);
            return this;
        }

        public Builder addConfig(String key, Object value) {
            this.config.put(key, value);
            return this;
        }

        public Builder removeConfig(String key) {
            this.config.remove(key);
            return this;
        }

        @Override
        public ChatRequest build() {
            ChatRequest request = new ChatRequest(model);
            request.setMessages(messages);
            request.setTemperature(temperature);
            request.setStream(stream);
            request.setStreamConsumer(streamConsumer);
            request.setConfig(config);
            return request;
        }
    }

}
