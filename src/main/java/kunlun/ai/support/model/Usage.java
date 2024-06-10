/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.ai.support.model;

import java.io.Serializable;

/**
 * The usage statistics for the completion request.
 * @author Kahle
 */
public class Usage implements Serializable {
    /**
     * Number of tokens in the generated completion.
     */
    private Integer completionTokens;
    /**
     * Number of tokens in the prompt.
     */
    private Integer promptTokens;
    /**
     * Total number of tokens used in the request (prompt + completion).
     */
    private Integer totalTokens;

    public Integer getCompletionTokens() {

        return completionTokens;
    }

    public void setCompletionTokens(Integer completionTokens) {

        this.completionTokens = completionTokens;
    }

    public Integer getPromptTokens() {

        return promptTokens;
    }

    public void setPromptTokens(Integer promptTokens) {

        this.promptTokens = promptTokens;
    }

    public Integer getTotalTokens() {

        return totalTokens;
    }

    public void setTotalTokens(Integer totalTokens) {

        this.totalTokens = totalTokens;
    }

    /**
     * The usage statistics builder.
     * @author Kahle
     */
    public static class Builder implements kunlun.core.Builder {

        public static Builder of(Integer totalTokens) {

            return of().setTotalTokens(totalTokens);
        }

        public static Builder of() {

            return new Builder();
        }

        private Integer completionTokens;
        private Integer promptTokens;
        private Integer totalTokens;

        public Integer getCompletionTokens() {

            return completionTokens;
        }

        public Builder setCompletionTokens(Integer completionTokens) {
            this.completionTokens = completionTokens;
            return this;
        }

        public Integer getPromptTokens() {

            return promptTokens;
        }

        public Builder setPromptTokens(Integer promptTokens) {
            this.promptTokens = promptTokens;
            return this;
        }

        public Integer getTotalTokens() {

            return totalTokens;
        }

        public Builder setTotalTokens(Integer totalTokens) {
            this.totalTokens = totalTokens;
            return this;
        }

        @Override
        public Usage build() {
            Usage usage = new Usage();
            usage.setCompletionTokens(completionTokens);
            usage.setPromptTokens(promptTokens);
            usage.setTotalTokens(totalTokens);
            return usage;
        }
    }

}
