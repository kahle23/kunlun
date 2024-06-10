/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.ai.support.model;

import java.io.Serializable;

/**
 * The AI embeddings request.
 * @author Kahle
 */
public class EmbedRequest implements Serializable {
    /**
     * Input text to embed, encoded as a string or array of tokens.
     * To embed multiple inputs in a single request, pass an array of strings or array of token arrays.
     */
    private Object input;
    /**
     * The ID of the model to use.
     */
    private String model;
    /**
     * The format to return the embeddings in.
     */
    private String encodingFormat;
    /**
     * The number of dimensions the resulting output embeddings should have.
     */
    private Integer dimensions;
    /**
     * The configuration information or configuration ID.
     */
    private Object config;

    public Object getInput() {

        return input;
    }

    public void setInput(Object input) {

        this.input = input;
    }

    public String getModel() {

        return model;
    }

    public void setModel(String model) {

        this.model = model;
    }

    public String getEncodingFormat() {

        return encodingFormat;
    }

    public void setEncodingFormat(String encodingFormat) {

        this.encodingFormat = encodingFormat;
    }

    public Integer getDimensions() {

        return dimensions;
    }

    public void setDimensions(Integer dimensions) {

        this.dimensions = dimensions;
    }

    public Object getConfig() {

        return config;
    }

    public void setConfig(Object config) {

        this.config = config;
    }

    /**
     * The AI embeddings request builder.
     * @author Kahle
     */
    public static class Builder implements kunlun.core.Builder {

        public static Builder of(String model) {

            return of().setModel(model);
        }

        public static Builder of() {

            return new Builder();
        }

        private Object input;
        private String model;
        private String encodingFormat;
        private Integer dimensions;
        private Object config;

        public Object getInput() {

            return input;
        }

        public Builder setInput(Object input) {
            this.input = input;
            return this;
        }

        public String getModel() {

            return model;
        }

        public Builder setModel(String model) {
            this.model = model;
            return this;
        }

        public String getEncodingFormat() {

            return encodingFormat;
        }

        public Builder setEncodingFormat(String encodingFormat) {
            this.encodingFormat = encodingFormat;
            return this;
        }

        public Integer getDimensions() {

            return dimensions;
        }

        public Builder setDimensions(Integer dimensions) {
            this.dimensions = dimensions;
            return this;
        }

        public Object getConfig() {

            return config;
        }

        public Builder setConfig(Object config) {
            this.config = config;
            return this;
        }

        @Override
        public EmbedRequest build() {
            EmbedRequest request = new EmbedRequest();
            request.setInput(input);
            request.setModel(model);
            request.setEncodingFormat(encodingFormat);
            request.setDimensions(dimensions);
            request.setConfig(config);
            return request;
        }
    }

}
