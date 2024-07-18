/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.action.support.jdbc;

import kunlun.action.support.AbstractInvokeActionHandler;
import kunlun.action.support.util.OutputPostConverter;
import kunlun.core.function.BiFunction;
import kunlun.data.validation.support.ValidationConfig;
import kunlun.logging.Logger;
import kunlun.logging.LoggerFactory;
import kunlun.util.StringUtils;
import kunlun.util.handler.ScriptHandler;
import kunlun.util.handler.support.ScriptHandlerImpl;

import java.util.Collection;

/**
 * The abstract script-based jdbc invoke action handler.
 * @author Kahle
 */
public abstract class AbstractScriptBasedJdbcInvokeHandler extends AbstractInvokeActionHandler {
    private static final Logger log = LoggerFactory.getLogger(AbstractScriptBasedJdbcInvokeHandler.class);
    private final BiFunction<Object, Class<?>, Object> outputPostConverter = new OutputPostConverter();
    private final ScriptHandler scriptHandler = new ScriptHandlerImpl();

    protected ScriptHandler getScriptHandler() {

        return scriptHandler;
    }

    protected BiFunction<Object, Class<?>, Object> getOutputPostConverter() {

        return outputPostConverter;
    }

    @Override
    protected void validateInput(InvokeContext context) {
        JdbcInvokeConfig config = (JdbcInvokeConfig) context.getConfig();
        Collection<ValidationConfig> validations = config.getInputValidations();
        getScriptHandler().validate(config.getScriptEngine(), validations, context);
    }

    @Override
    protected void convertInput(InvokeContext context) {
        // Get config.
        JdbcInvokeConfig config = (JdbcInvokeConfig) context.getConfig();
        String scriptEngine = config.getScriptEngine();
        String inputScript = config.getInput();
        // Eval input script.
        if (StringUtils.isNotBlank(inputScript)) {
            Object convertedInput = getScriptHandler().eval(scriptEngine, inputScript, context);
            context.setConvertedInput(convertedInput);
        }
        else { context.setConvertedInput(context.getRawInput()); }
        // End.
    }

    @Override
    protected void validateOutput(InvokeContext context) {
        JdbcInvokeConfig config = (JdbcInvokeConfig) context.getConfig();
        Collection<ValidationConfig> validations = config.getOutputValidations();
        getScriptHandler().validate(config.getScriptEngine(), validations, context);
    }

    @Override
    protected void convertOutput(InvokeContext context) {
        // Get config and get script engine name.
        JdbcInvokeConfig config = (JdbcInvokeConfig) context.getConfig();
        Class<?> expectedClass = context.getExpectedClass();
        String   scriptEngine = config.getScriptEngine();
        String   outputScript = config.getOutput();
        // Eval output script.
        if (StringUtils.isNotBlank(outputScript)) {
            context.setConvertedOutput(getScriptHandler().eval(scriptEngine, outputScript, context));
        }
        else { context.setConvertedOutput(context.getRawOutput()); }
        // Convert converted output.
        Object convertedOutput = context.getConvertedOutput();
        context.setConvertedOutput(getOutputPostConverter().apply(convertedOutput, expectedClass));
        // End.
    }

}
