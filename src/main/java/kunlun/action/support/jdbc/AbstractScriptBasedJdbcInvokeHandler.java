/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.action.support.jdbc;

import kunlun.action.support.script.AbstractScriptBasedInvokeHandler;
import kunlun.data.Dict;
import kunlun.data.bean.BeanUtils;
import kunlun.data.validation.support.ValidationConfig;
import kunlun.logging.Logger;
import kunlun.logging.LoggerFactory;
import kunlun.util.ClassUtils;
import kunlun.util.StringUtils;

import java.util.Collection;
import java.util.Map;

/**
 * The abstract script-based jdbc invoke action handler.
 * @author Kahle
 */
public abstract class AbstractScriptBasedJdbcInvokeHandler extends AbstractScriptBasedInvokeHandler {
    private static final Logger log = LoggerFactory.getLogger(AbstractScriptBasedJdbcInvokeHandler.class);

    @Override
    protected void validateInput(InvokeContext context) {
        JdbcInvokeConfig config = (JdbcInvokeConfig) context.getConfig();
        Collection<ValidationConfig> validations = config.getInputValidations();
        validate(config.getScriptEngine(), context, validations);
    }

    @Override
    protected void convertInput(InvokeContext context) {
        // Get config.
        JdbcInvokeConfig config = (JdbcInvokeConfig) context.getConfig();
        String scriptEngine = config.getScriptEngine();
        String inputScript = config.getInput();
        // Eval input script.
        if (StringUtils.isNotBlank(inputScript)) {
            Object convertedInput = eval(scriptEngine, inputScript, context);
            context.setConvertedInput(convertedInput);
        }
        else { context.setConvertedInput(context.getRawInput()); }
        // End.
    }

    @Override
    protected void validateOutput(InvokeContext context) {
        JdbcInvokeConfig config = (JdbcInvokeConfig) context.getConfig();
        Collection<ValidationConfig> validations = config.getOutputValidations();
        validate(config.getScriptEngine(), context, validations);
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
            Object convertedOutput = eval(scriptEngine, outputScript, context);
            context.setConvertedOutput(convertedOutput);
        }
        else { context.setConvertedOutput(context.getRawOutput()); }
        // Convert converted output.
        Object convertedOutput = context.getConvertedOutput();
        if (!ClassUtils.isSimpleValueType(expectedClass)) {
            if (convertedOutput instanceof Collection) {
            }
            else if (convertedOutput!=null&&convertedOutput.getClass().isArray()) {
            }
            else if (convertedOutput instanceof Map && Map.class.isAssignableFrom(expectedClass)) {
                context.setConvertedOutput(Dict.of((Map<?, ?>) convertedOutput));
            }
            else if (convertedOutput != null && !expectedClass.isAssignableFrom(convertedOutput.getClass())) {
                context.setConvertedOutput(BeanUtils.beanToBean(convertedOutput, expectedClass));
            }
            else { /* Do nothing. */ }
        }
        // End.
    }

}
