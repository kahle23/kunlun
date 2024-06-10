/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.ai.support.model;

import kunlun.core.function.Consumer;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * The AI chat request.
 * @author Kahle
 */
public class ChatRequest implements Serializable {
    /**
     * The ID of the model to use.
     */
    private String  model;
    /**
     * What sampling temperature to use. Higher values will make the output more random,
     *      while lower values will make it more focused and deterministic.
     */
    private Double  temperature;
    /**
     * An alternative to sampling with temperature, called nucleus sampling,
     *      where the model considers the results of the tokens with top_p probability mass.
     */
    private Double  topP;
    /**
     * The maximum number of tokens that can be generated in the chat completion.
     * The total length of input tokens and generated tokens is limited by the model's context length.
     */
    private Integer maxTokens;
    /**
     * How many chat completion choices to generate for each input message.
     */
    private Integer n;
    /**
     * If set, partial message deltas will be sent.
     */
    private Boolean stream;
    /**
     * The stream message consumer.
     */
    private Consumer<Object> streamConsumer;
    /**
     * A list of messages comprising the conversation so far.
     */
    private List<Message> messages;
    /**
     * A list of tools the model may call.
     */
    private List<Tool> tools;
    /**
     * The configuration information or configuration ID.
     */
    private Object config;

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

    public Integer getN() {

        return n;
    }

    public void setN(Integer n) {

        this.n = n;
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

    public List<Tool> getTools() {

        return tools;
    }

    public void setTools(List<Tool> tools) {

        this.tools = tools;
    }

    public Object getConfig() {

        return config;
    }

    public void setConfig(Object config) {

        this.config = config;
    }

    /**
     * The AI chat request builder.
     * @author Kahle
     */
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
        private Integer n;
        private Boolean stream;
        private Consumer<Object> streamConsumer;
        private final List<Message> messages = new ArrayList<Message>();
        private final List<Tool> tools = new ArrayList<Tool>();
        private Object config;

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

        public Integer getN() {

            return n;
        }

        public Builder setN(Integer n) {
            this.n = n;
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
            Message message = new Message(role, content);
            message.setName(name);
            this.messages.add(message);
            return this;
        }

        public Builder addMessage(String role, Object content) {
            this.messages.add(new Message(role, content));
            return this;
        }

        public List<Tool> getTools() {

            return tools;
        }

        public Builder addTools(List<Tool> tools) {
            this.tools.addAll(tools);
            return this;
        }

        public Builder addTool(Tool tool) {
            this.tools.add(tool);
            return this;
        }

        public Object getConfig() {

            return config;
        }

        public void setConfig(Object config) {

            this.config = config;
        }

        @Override
        public ChatRequest build() {
            ChatRequest request = new ChatRequest(model);
            request.setTemperature(temperature);
            request.setTopP(topP);
            request.setMaxTokens(maxTokens);
            request.setN(n);
            request.setStream(stream);
            request.setStreamConsumer(streamConsumer);
            request.setMessages(messages);
            request.setTools(tools);
            request.setConfig(config);
            return request;
        }
    }

}
