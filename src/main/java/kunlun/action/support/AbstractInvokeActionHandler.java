/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.action.support;

import kunlun.exception.ExceptionUtils;
import kunlun.util.Assert;

/**
 * The abstract dynamic invoke action handler.
 * @author Kahle
 */
public abstract class AbstractInvokeActionHandler extends AbstractStrategyActionHandler {

    /**
     * Build the context object from the parameters.
     * @param input The input object
     * @param name The invoked name
     * @param clazz The expected class
     * @return The core context object
     */
    protected InvokeContext buildContext(Object input, String name, Class<?> clazz) {
        Assert.notBlank(name, "Parameter \"name\" must not blank. ");
        Assert.notNull(clazz, "Parameter \"clazz\" must not null. ");
        return new InvokeContextImpl(input, name, clazz);
    }

    /**
     * Load the configuration and place it in the context.
     * @param context The core context object
     */
    protected abstract void loadConfig(InvokeContext context);

    /**
     * Validate the input.
     * @param context The core context object
     */
    protected void validateInput(InvokeContext context) {

    }

    /**
     * Convert the input.
     * @param context The core context object
     */
    protected void convertInput(InvokeContext context) {

        context.setConvertedInput(context.getRawInput());
    }

    /**
     * Do invoke.
     * @param context The core context object
     */
    protected abstract void doInvoke(InvokeContext context);

    /**
     * Validate the output.
     * @param context The core context object
     */
    protected void validateOutput(InvokeContext context) {

    }

    /**
     * Convert the output.
     * @param context The core context object
     */
    protected void convertOutput(InvokeContext context) {

        context.setConvertedOutput(context.getRawOutput());
    }

    /**
     * Get result.
     * @param context The core context object
     * @return The result
     */
    protected Object getResult(InvokeContext context) {

        return context.getConvertedOutput();
    }

    /**
     * Record the log.
     * @param context The core context object
     */
    protected void recordLog(InvokeContext context) {

    }

    @Override
    public Object execute(Object input, String strategy, Class<?> clazz) {
        // Build context.
        InvokeContext context = buildContext(input, strategy, clazz);
        Assert.state(context != null, "Build the context failure! ");
        try {
            // Load config and set to context.
            loadConfig(context);
            // Validate input by config.
            validateInput(context);
            // Convert input by config.
            convertInput(context);
            // Do invoke.
            doInvoke(context);
            // Validate output by config.
            validateOutput(context);
            // Convert output by config.
            convertOutput(context);
            // Get result by context.
            return getResult(context);
        }
        catch (Exception e) {
            context.setError(e);
            throw ExceptionUtils.wrap(e);
        }
        finally { recordLog(context); }
    }

    /**
     * The core invoke context.
     * @author Kahle
     */
    public interface InvokeContext {

        /**
         * Get the invoked name.
         * @return The invoked name
         */
        String getInvokeName();

        /**
         * Get the raw input data.
         * @return The raw input data
         */
        Object getRawInput();

        /**
         * Get the expected class.
         * @return The expected class
         */
        Class<?> getExpectedClass();

        /**
         * Get the configuration data.
         * @return The configuration data
         */
        Object getConfig();

        /**
         * Set the configuration data.
         * @param config The configuration data
         */
        void setConfig(Object config);

        /**
         * Get the converted input data.
         * @return The converted input data
         */
        Object getConvertedInput();

        /**
         * Set the converted input data.
         * @param input The converted input data
         */
        void setConvertedInput(Object input);

        /**
         * Get the raw output data.
         * @return The raw output data
         */
        Object getRawOutput();

        /**
         * Set the raw output data.
         * @param output The raw output data
         */
        void setRawOutput(Object output);

        /**
         * Get the converted output data.
         * @return The converted output data
         */
        Object getConvertedOutput();

        /**
         * Set the converted output data.
         * @param output The converted output data
         */
        void setConvertedOutput(Object output);

        /**
         * Get the error information object.
         * @return The error information object
         */
        Throwable getError();

        /**
         * Set the error information object.
         * @param th The error information object
         */
        void setError(Throwable th);

    }

    /**
     * The invoked context simple implementation.
     * @author Kahle
     */
    public static class InvokeContextImpl implements InvokeContext {
        private String invokeName;
        private Object rawInput;
        private Class<?> expectedClass;
        private Object config;
        private Object convertedInput;
        private Object rawOutput;
        private Object convertedOutput;
        private Throwable error;

        public InvokeContextImpl(Object rawInput, String invokeName, Class<?> expectedClass) {
            this.expectedClass = expectedClass;
            this.invokeName = invokeName;
            this.rawInput = rawInput;
        }

        public InvokeContextImpl() {

        }

        @Override
        public String getInvokeName() {

            return invokeName;
        }

        public void setInvokeName(String invokeName) {

            this.invokeName = invokeName;
        }

        @Override
        public Object getRawInput() {

            return rawInput;
        }

        public void setRawInput(Object rawInput) {

            this.rawInput = rawInput;
        }

        @Override
        public Class<?> getExpectedClass() {

            return expectedClass;
        }

        public void setExpectedClass(Class<?> expectedClass) {

            this.expectedClass = expectedClass;
        }

        @Override
        public Object getConfig() {

            return config;
        }

        @Override
        public void setConfig(Object config) {

            this.config = config;
        }

        @Override
        public Object getConvertedInput() {

            return convertedInput;
        }

        @Override
        public void setConvertedInput(Object input) {

            this.convertedInput = input;
        }

        @Override
        public Object getRawOutput() {

            return rawOutput;
        }

        @Override
        public void setRawOutput(Object output) {

            this.rawOutput = output;
        }

        @Override
        public Object getConvertedOutput() {

            return convertedOutput;
        }

        @Override
        public void setConvertedOutput(Object output) {

            this.convertedOutput = output;
        }

        @Override
        public Throwable getError() {

            return error;
        }

        @Override
        public void setError(Throwable th) {

            this.error = th;
        }

    }

}
