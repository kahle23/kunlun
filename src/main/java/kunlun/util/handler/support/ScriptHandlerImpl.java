/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.util.handler.support;

import kunlun.data.bean.BeanUtils;
import kunlun.data.validation.ValidatorUtils;
import kunlun.data.validation.support.ValidationConfig;
import kunlun.polyglot.PolyglotUtils;
import kunlun.util.CollUtils;
import kunlun.util.StrUtils;
import kunlun.util.handler.ScriptHandler;

import java.util.Collection;

/**
 * The script processing tool default implementation.
 * @author Kahle
 */
public class ScriptHandlerImpl implements ScriptHandler {

    @Override
    public Object eval(String scriptName, String script, Object data) {
        if (StrUtils.isBlank(script)) { return null; }
        return PolyglotUtils.eval(scriptName, script, BeanUtils.beanToMap(data));
    }

    @Override
    public void validate(String scriptName, Collection<ValidationConfig> configs, Object data) {
        if (CollUtils.isEmpty(configs)) { return; }
        data = BeanUtils.beanToMap(data);
        for (ValidationConfig config : configs) {
            String expression = config.getExpression();
            String validator = config.getValidator();
            String message = config.getMessage();
            if (StrUtils.isBlank(expression)) { continue; }
            Object eval = eval(scriptName, expression, data);
            boolean validate = ValidatorUtils.validateToBoolean(validator, eval);
            throwValidationException(validate, message);
        }
    }

    @Override
    public void throwValidationException(boolean validate, String message) {
        if (!validate) {
            throw new IllegalArgumentException(message);
        }
    }

}
