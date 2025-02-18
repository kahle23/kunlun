package kunlun.ai.support.model;

import java.io.Serializable;

/**
 * The tool calls generated by the model.
 * @author Kahle
 */
public class ToolCall implements Serializable {
    /**
     * The ID of the tool call.
     */
    private String id;
    /**
     * The type of the tool.
     */
    private String type;
    /**
     * The function that the model called.
     */
    private Function function;

    public String getId() {

        return id;
    }

    public void setId(String id) {

        this.id = id;
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
     * The called function.
     * @author Kahle
     */
    public static class Function implements Serializable {
        /**
         * The name of the function to call.
         */
        private String name;
        /**
         * The arguments to call the function with,
         *      as generated by the model in JSON format or others.
         */
        private String arguments;

        public Function(String name, String arguments) {
            this.arguments = arguments;
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

        public String getArguments() {

            return arguments;
        }

        public void setArguments(String arguments) {

            this.arguments = arguments;
        }
    }

    /**
     * The tool call builder.
     * @author Kahle
     */
    public static class Builder implements kunlun.core.Builder {

        public static Builder of() {

            return new Builder();
        }

        private String id;
        private String type;
        private Function function;

        public String getId() {

            return id;
        }

        public Builder setId(String id) {
            this.id = id;
            return this;
        }

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

        public Builder setFunction(String name, String arguments) {
            this.function = new Function(name, arguments);
            return this;
        }

        @Override
        public ToolCall build() {
            ToolCall toolCall = new ToolCall();
            toolCall.setId(id);
            toolCall.setType(type);
            toolCall.setFunction(function);
            return toolCall;
        }
    }

}
