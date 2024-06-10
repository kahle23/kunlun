/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.ai.support.model;

import java.io.Serializable;

/**
 * The AI chat tool.
 * @author Kahle
 */
public class Tool implements Serializable {
    /**
     * The type of the tool. Currently, only function is supported.
     */
    private String type;
    /**
     * The function description.
     */
    private Function function;

    public Tool(String type) {

        this.type = type;
    }

    public Tool() {

    }

    public String getType() {

        return type;
    }

    public void setType(String type) {

        this.type = type;
    }

    public Function getFunction() {

        return function;
    }

    public void setFunction(Function function) {

        this.function = function;
    }

    /**
     * The function description.
     * @author Kahle
     */
    public static class Function implements Serializable {
        /**
         * The name of the function to be called.
         */
        private String name;
        /**
         * A description of what the function does,
         *      used by the model to choose when and how to call the function.
         */
        private String description;
        /**
         * The parameters the functions accepts.
         */
        private Object parameters;
        /**
         * Whether to enable strict schema adherence when generating the function call.
         * If set to true, the model will follow the exact schema defined in the parameters field.
         */
        private Boolean strict;

        public Function(String name, String description, Object parameters) {
            this.description = description;
            this.parameters = parameters;
            this.name = name;
        }

        public Function() {

        }

        public String getName() {

            return name;
        }

        public void setName(String name) {

            this.name = name;
        }

        public String getDescription() {

            return description;
        }

        public void setDescription(String description) {

            this.description = description;
        }

        public Object getParameters() {

            return parameters;
        }

        public void setParameters(Object parameters) {

            this.parameters = parameters;
        }

        public Boolean getStrict() {

            return strict;
        }

        public void setStrict(Boolean strict) {

            this.strict = strict;
        }
    }

    /**
     * The AI chat tool builder.
     * @author Kahle
     */
    public static class Builder implements kunlun.core.Builder {

        public static Builder of(String type) {

            return of().setType(type);
        }

        public static Builder of() {

            return new Builder();
        }

        private String type;
        private Function function;

        public String getType() {

            return type;
        }

        public Builder setType(String type) {
            this.type = type;
            return this;
        }

        public Function getFunction() {

            return function;
        }

        public Builder setFunction(Function function) {
            this.function = function;
            return this;
        }

        public Builder setFunction(String name, String description, Object parameters) {
            this.function = new Function(name, description, parameters);
            return this;
        }

        public Builder setFunction(String name, String description, Object parameters, Boolean strict) {
            this.function = new Function(name, description, parameters);
            this.function.setStrict(strict);
            return this;
        }

        @Override
        public Tool build() {
            Tool tool = new Tool();
            tool.setType(type);
            tool.setFunction(function);
            return tool;
        }
    }

}
