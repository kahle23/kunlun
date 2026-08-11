/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.action.invoke;

import kunlun.action.AbstractAction;
import kunlun.core.function.Consumer;
import kunlun.core.function.Function;
import kunlun.data.bean.BeanUtil;
import kunlun.data.validation.ValidatorUtil;
import kunlun.data.validation.support.ValidationConfig;
import kunlun.exception.ExceptionUtil;
import kunlun.polyglot.PolyglotUtil;
import kunlun.util.Assert;
import kunlun.util.CollUtil;
import kunlun.util.StrUtil;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

/**
 * The abstract dynamic invoke action.
 * @author Kahle
 */
public class InvokeAction extends AbstractAction {
    private Consumer<InvokeContext> configLoader;
    private Consumer<InvokeContext> inputValidator;
    private Consumer<InvokeContext> inputConverter;
    private Consumer<InvokeContext> coreExecutor;
    private Consumer<InvokeContext> outputValidator;
    private Consumer<InvokeContext> outputConverter;
    private Consumer<InvokeContext> logRecorder;
    private Function<InvokeContext, Object> resultExtractor;

    public InvokeAction() {
        this.setInputValidator(new CommonValidator(TRUE));
        this.setInputConverter(new CommonConverter(TRUE));
        this.setOutputValidator(new CommonValidator(FALSE));
        this.setOutputConverter(new CommonConverter(FALSE));
        this.setLogRecorder(new Consumer<InvokeContext>() {
            @Override
            public void accept(InvokeContext context) {
            }
        });
        this.setResultExtractor(new Function<InvokeContext, Object>() {
            @Override
            public Object apply(InvokeContext context) {

                return context.getConvertedOutput();
            }
        });
    }

    public Consumer<InvokeContext> getConfigLoader() {

        return configLoader;
    }

    public void setConfigLoader(Consumer<InvokeContext> configLoader) {

        this.configLoader = Assert.notNull(configLoader);
    }

    public Consumer<InvokeContext> getInputValidator() {

        return inputValidator;
    }

    public void setInputValidator(Consumer<InvokeContext> inputValidator) {

        this.inputValidator = Assert.notNull(inputValidator);
    }

    public Consumer<InvokeContext> getInputConverter() {

        return inputConverter;
    }

    public void setInputConverter(Consumer<InvokeContext> inputConverter) {

        this.inputConverter = Assert.notNull(inputConverter);
    }

    public Consumer<InvokeContext> getCoreExecutor() {

        return coreExecutor;
    }

    public void setCoreExecutor(Consumer<InvokeContext> coreExecutor) {

        this.coreExecutor = Assert.notNull(coreExecutor);
    }

    public Consumer<InvokeContext> getOutputValidator() {

        return outputValidator;
    }

    public void setOutputValidator(Consumer<InvokeContext> outputValidator) {

        this.outputValidator = Assert.notNull(outputValidator);
    }

    public Consumer<InvokeContext> getOutputConverter() {

        return outputConverter;
    }

    public void setOutputConverter(Consumer<InvokeContext> outputConverter) {

        this.outputConverter = Assert.notNull(outputConverter);
    }

    public Consumer<InvokeContext> getLogRecorder() {

        return logRecorder;
    }

    public void setLogRecorder(Consumer<InvokeContext> logRecorder) {

        this.logRecorder = Assert.notNull(logRecorder);
    }

    public Function<InvokeContext, Object> getResultExtractor() {

        return resultExtractor;
    }

    public void setResultExtractor(Function<InvokeContext, Object> resultExtractor) {

        this.resultExtractor = Assert.notNull(resultExtractor);
    }

    /**
     * Build the context object from the parameters.
     * @param invokeName The invoked name
     * @param input The input object
     * @return The core context object
     */
    protected InvokeContext buildContext(String invokeName, Object input) {

        return new InvokeContextImpl(Assert.notBlank(invokeName), input);
    }

    @Override
    public Object execute(String strategy, Object input, Object[] arguments) {
        // Build context.
        InvokeContext context = buildContext(strategy, input);
        Assert.state(context != null, "Build the context failure! ");
        try {
            // Load config and set to context.
            Assert.notNull(getConfigLoader()).accept(context);
            // Validate input by config.
            getInputValidator().accept(context);
            // Convert input by config.
            getInputConverter().accept(context);
            // Do invoke.
            Assert.notNull(getCoreExecutor()).accept(context);
            // Validate output by config.
            getOutputValidator().accept(context);
            // Convert output by config.
            getOutputConverter().accept(context);
            // Get result by context.
            return getResultExtractor().apply(context);
        }
        catch (Exception e) {
            context.setError(e);
            throw ExceptionUtil.wrap(e);
        }
        finally { getLogRecorder().accept(context); }
    }

    public static abstract class AbstractConfig implements Serializable {
        /**
         * The invoked name.
         */
        private String name;
        /**
         * The invoked description.
         */
        private String description;
        /**
         * The script engine name.
         */
        private String scriptEngine;
        /**
         * The text renderer name.
         */
        private String rendererName;
        /**
         * The input data validation configs.
         */
        private Collection<ValidationConfig> inputValidations;
        /**
         * The jdbc input conversion script.
         */
        private String input;
        /**
         * The jdbc output conversion script.
         */
        private String output;
        /**
         * The output data validation configs.
         */
        private Collection<ValidationConfig> outputValidations;
        /**
         * The other configs.
         */
        private Map<String, Object> otherConfigs;

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

        public String getScriptEngine() {

            return scriptEngine;
        }

        public void setScriptEngine(String scriptEngine) {

            this.scriptEngine = scriptEngine;
        }

        public String getRendererName() {

            return rendererName;
        }

        public void setRendererName(String rendererName) {

            this.rendererName = rendererName;
        }

        public Collection<ValidationConfig> getInputValidations() {

            return inputValidations;
        }

        public void setInputValidations(Collection<ValidationConfig> inputValidations) {

            this.inputValidations = inputValidations;
        }

        public String getInput() {

            return input;
        }

        public void setInput(String input) {

            this.input = input;
        }

        public String getOutput() {

            return output;
        }

        public void setOutput(String output) {

            this.output = output;
        }

        public Collection<ValidationConfig> getOutputValidations() {

            return outputValidations;
        }

        public void setOutputValidations(Collection<ValidationConfig> outputValidations) {

            this.outputValidations = outputValidations;
        }

        public Map<String, Object> getOtherConfigs() {

            return otherConfigs;
        }

        @SuppressWarnings("unchecked")
        public void setOtherConfigs(Map<?, ?> otherConfigs) {

            this.otherConfigs = (Map<String, Object>) otherConfigs;
        }
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
         * Get the configuration data.
         * @return The configuration data
         */
        AbstractConfig getConfig();

        /**
         * Set the configuration data.
         * @param config The configuration data
         */
        void setConfig(AbstractConfig config);

        /**
         * Get the raw input data.
         * @return The raw input data
         */
        Object getRawInput();

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
        private final Map<String, Object> runtimeData = new ConcurrentHashMap<String, Object>();
        private String invokeName;
        private AbstractConfig config;
        private Object rawInput;
        private Object convertedInput;
        private Object rawOutput;
        private Object convertedOutput;
        private Throwable error;

        public InvokeContextImpl(String invokeName, Object rawInput) {
            this.invokeName = invokeName;
            this.rawInput = rawInput;
        }

        public InvokeContextImpl() {

        }

        public Map<String, Object> getRuntimeData() {

            return runtimeData;
        }

        @Override
        public String getInvokeName() {

            return invokeName;
        }

        public void setInvokeName(String invokeName) {

            this.invokeName = invokeName;
        }

        @Override
        public AbstractConfig getConfig() {

            return config;
        }

        @Override
        public void setConfig(AbstractConfig config) {

            this.config = config;
        }

        @Override
        public Object getRawInput() {

            return rawInput;
        }

        public void setRawInput(Object rawInput) {

            this.rawInput = rawInput;
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

    public static class CommonValidator implements Consumer<InvokeContext> {
        private final boolean processInput;

        public CommonValidator(boolean processInput) {

            this.processInput = processInput;
        }

        protected void throwValidationException(boolean validate, String message) {
            if (!validate) {
                throw new IllegalArgumentException(message);
            }
        }

        protected void validate(String scriptName, Collection<ValidationConfig> configs, Object data) {
            if (CollUtil.isEmpty(configs)) { return; }
            data = BeanUtil.beanToMap(data);
            for (ValidationConfig config : configs) {
                String expression = config.getExpression();
                String validator = config.getValidator();
                String message = config.getMessage();
                if (StrUtil.isBlank(expression)) { continue; }
                Object eval = PolyglotUtil.eval(scriptName, expression, data);
                boolean validate = ValidatorUtil.validateToBoolean(validator, eval);
                throwValidationException(validate, message);
            }
        }

        @Override
        public void accept(InvokeContext context) {
            AbstractConfig config = context.getConfig();
            Collection<ValidationConfig> validations =
                    processInput ? config.getInputValidations() : config.getOutputValidations();
            validate(config.getScriptEngine(), validations, context);
        }
    }

    public static class CommonConverter implements Consumer<InvokeContext> {
        private final boolean processInput;

        public CommonConverter(boolean processInput) {

            this.processInput = processInput;
        }

        @Override
        public void accept(InvokeContext context) {
            // Get config.
            AbstractConfig config = context.getConfig();
            String scriptEngine = config.getScriptEngine();
            String inputScript = config.getInput();
            String outputScript = config.getOutput();
            if (processInput) {
                // Eval input script.
                if (StrUtil.isNotBlank(inputScript)) {
                    Object convertedInput = StrUtil.isBlank(inputScript) ? null :
                            PolyglotUtil.eval(scriptEngine, inputScript, BeanUtil.beanToMap(context));
                    context.setConvertedInput(convertedInput);
                }
                else { context.setConvertedInput(context.getRawInput()); }
            } else {
                // Eval output script.
                if (StrUtil.isNotBlank(outputScript)) {
                    Object convertedOutput = StrUtil.isBlank(outputScript) ? null :
                            PolyglotUtil.eval(scriptEngine, outputScript, BeanUtil.beanToMap(context));
                    context.setConvertedOutput(convertedOutput);
                }
                else { context.setConvertedOutput(context.getRawOutput()); }
            }
            // End.
        }
    }

}
