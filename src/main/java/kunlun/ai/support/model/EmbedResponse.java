/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.ai.support.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * The AI embeddings response.
 * @author Kahle
 */
public class EmbedResponse implements Serializable {
    /**
     * Represents an embedding vector returned by embedding endpoint.
     */
    private List<EmbedData> data;
    /**
     * The model used for the embeddings.
     */
    private String model;
    /**
     * The usage statistics for the completion request.
     */
    private Usage  usage;

    public List<EmbedData> getData() {

        return data;
    }

    public void setData(List<EmbedData> data) {

        this.data = data;
    }

    public String getModel() {

        return model;
    }

    public void setModel(String model) {

        this.model = model;
    }

    public Usage getUsage() {

        return usage;
    }

    public void setUsage(Usage usage) {

        this.usage = usage;
    }

    /**
     * The AI embeddings response builder.
     * @author Kahle
     */
    public static class Builder implements kunlun.core.Builder {

        public static Builder of(String model) {

            return of().setModel(model);
        }

        public static Builder of() {

            return new Builder();
        }

        private List<EmbedData> data = new ArrayList<EmbedData>();
        private String model;
        private Usage  usage;

        public List<EmbedData> getData() {

            return data;
        }

        public Builder setData(List<EmbedData> data) {
            this.data = data;
            return this;
        }

        public Builder addData(List<EmbedData> data) {
            this.data.addAll(data);
            return this;
        }

        public Builder addDatum(EmbedData datum) {
            this.data.add(datum);
            return this;
        }

        public Builder addDatum(Integer index, List<? extends Number> embedding) {
            this.data.add(new EmbedData(index, embedding));
            return this;
        }

        public Builder addDatum(List<? extends Number> embedding) {
            this.data.add(new EmbedData(embedding));
            return this;
        }

        public String getModel() {

            return model;
        }

        public Builder setModel(String model) {
            this.model = model;
            return this;
        }

        public Usage getUsage() {

            return usage;
        }

        public Builder setUsage(Usage usage) {
            this.usage = usage;
            return this;
        }

        @Override
        public EmbedResponse build() {
            EmbedResponse response = new EmbedResponse();
            response.setData(data);
            response.setModel(model);
            response.setUsage(usage);
            return response;
        }
    }

}
