/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.action.support.script;

import kunlun.action.support.AbstractInvokeActionHandler;
import kunlun.data.bean.BeanUtils;
import kunlun.data.validation.ValidatorUtils;
import kunlun.data.validation.support.ValidationConfig;
import kunlun.polyglot.PolyglotUtils;
import kunlun.util.CollectionUtils;
import kunlun.util.StringUtils;

import java.util.Collection;

/**
 * The abstract script-based invoke action handler.
 * @author Kahle
 */
public abstract class AbstractScriptBasedInvokeHandler extends AbstractInvokeActionHandler {

    /**
     * Eval the script by script engine.
     * @param engine The script engine name
     * @param script The script
     * @param data The data
     * @return The eval result
     */
    protected Object eval(String engine, String script, Object data) {
        if (StringUtils.isBlank(script)) { return null; }
        return PolyglotUtils.eval(engine, script, BeanUtils.beanToMap(data));
    }

    /**
     * Validate data by validation configs.
     * @param scriptEngine The script engine name
     * @param data The data
     * @param configs The validation configs
     */
    protected void validate(String scriptEngine, Object data, Collection<ValidationConfig> configs) {
        if (CollectionUtils.isEmpty(configs)) { return; }
        data = BeanUtils.beanToMap(data);
        for (ValidationConfig config : configs) {
            String expression = config.getExpression();
            String validator = config.getValidator();
            String message = config.getMessage();
            if (StringUtils.isBlank(expression)) { continue; }
            Object eval = eval(scriptEngine, expression, data);
            boolean validate = ValidatorUtils.validateToBoolean(validator, eval);
            throwException(validate, message);
        }
    }

    /**
     * Throw exception if validate failure.
     * @param validate The validate result
     * @param message The failure message
     */
    protected void throwException(boolean validate, String message) {
        if (!validate) {
            throw new IllegalArgumentException(message);
        }
    }

}
