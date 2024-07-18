/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.ai.support.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ChatResponse implements Serializable {
    private String id;
    private String model;
    private List<Choice> choices;

    public ChatResponse(String model) {

        this.model = model;
    }

    public ChatResponse() {

    }

    public String getId() {

        return id;
    }

    public void setId(String id) {

        this.id = id;
    }

    public String getModel() {

        return model;
    }

    public void setModel(String model) {

        this.model = model;
    }

    public List<Choice> getChoices() {

        return choices;
    }

    public void setChoices(List<Choice> choices) {

        this.choices = choices;
    }

    public static class Choice implements Serializable {
        private String  finishReason;
        private Message message;
        private Integer index;

        public Choice(Integer index, Message message, String finishReason) {
            this.finishReason = finishReason;
            this.message = message;
            this.index = index;
        }

        public Choice(Message message, String finishReason) {
            this.finishReason = finishReason;
            this.message = message;
        }

        public Choice() {

        }

        public Integer getIndex() {

            return index;
        }

        public void setIndex(Integer index) {

            this.index = index;
        }

        public Message getMessage() {

            return message;
        }

        public void setMessage(Message message) {

            this.message = message;
        }

        public String getFinishReason() {

            return finishReason;
        }

        public void setFinishReason(String finishReason) {

            this.finishReason = finishReason;
        }
    }

    public static class Builder implements kunlun.core.Builder {

        public static Builder of(String model) {

            return of().setModel(model);
        }

        public static Builder of() {

            return new Builder();
        }

        private String id;
        private String model;
        private final List<Choice> choices = new ArrayList<Choice>();

        public String getId() {

            return id;
        }

        public Builder setId(String id) {
            this.id = id;
            return this;
        }

        public String getModel() {

            return model;
        }

        public Builder setModel(String model) {
            this.model = model;
            return this;
        }

        public List<Choice> getChoices() {

            return choices;
        }

        public Builder addChoices(List<Choice> choices) {
            this.choices.addAll(choices);
            return this;
        }

        public Builder addChoice(Choice choice) {
            this.choices.add(choice);
            return this;
        }

        public Builder addChoice(Integer index, Message message, String finishReason) {
            this.choices.add(new Choice(index, message, finishReason));
            return this;
        }

        public Builder addChoice(Message message, String finishReason) {
            this.choices.add(new Choice(message, finishReason));
            return this;
        }

        @Override
        public ChatResponse build() {
            ChatResponse response = new ChatResponse(model);
            response.setId(id);
            response.setChoices(choices);
            return response;
        }
    }

}
