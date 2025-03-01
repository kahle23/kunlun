/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.util.handler.support;

import kunlun.data.bean.BeanUtil;
import kunlun.data.validation.ValidatorUtil;
import kunlun.data.validation.support.ValidationConfig;
import kunlun.polyglot.PolyglotUtil;
import kunlun.util.CollUtil;
import kunlun.util.StrUtil;
import kunlun.util.handler.ScriptHandler;

import java.util.Collection;

/**
 * The script processing tool default implementation.
 * @author Kahle
 */
public class ScriptHandlerImpl implements ScriptHandler {

    @Override
    public Object eval(String scriptName, String script, Object data) {
        if (StrUtil.isBlank(script)) { return null; }
        return PolyglotUtil.eval(scriptName, script, BeanUtil.beanToMap(data));
    }

    @Override
    public void validate(String scriptName, Collection<ValidationConfig> configs, Object data) {
        if (CollUtil.isEmpty(configs)) { return; }
        data = BeanUtil.beanToMap(data);
        for (ValidationConfig config : configs) {
            String expression = config.getExpression();
            String validator = config.getValidator();
            String message = config.getMessage();
            if (StrUtil.isBlank(expression)) { continue; }
            Object eval = eval(scriptName, expression, data);
            boolean validate = ValidatorUtil.validateToBoolean(validator, eval);
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
