/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.action.support.script;

import kunlun.data.validation.support.ValidationConfig;
import kunlun.logging.Logger;
import kunlun.logging.LoggerFactory;

import java.util.Collection;

/**
 * The abstract script-based script invoke action handler.
 * @author Kahle
 */
public abstract class AbstractScriptBasedScriptInvokeHandler extends AbstractScriptBasedInvokeHandler {
    private static final Logger log = LoggerFactory.getLogger(AbstractScriptBasedScriptInvokeHandler.class);

    @Override
    protected void validateInput(InvokeContext context) {
        ScriptInvokeConfig config = (ScriptInvokeConfig) context.getConfig();
        Collection<ValidationConfig> validations = config.getInputValidations();
        validate(config.getEngine(), context, validations);
    }

    @Override
    protected void validateOutput(InvokeContext context) {
        ScriptInvokeConfig config = (ScriptInvokeConfig) context.getConfig();
        Collection<ValidationConfig> validations = config.getOutputValidations();
        validate(config.getEngine(), context, validations);
    }

    @Override
    protected void doInvoke(InvokeContext context) {
        ScriptInvokeConfig config = (ScriptInvokeConfig) context.getConfig();
        Object eval = eval(config.getEngine(), config.getContent(), context);
        context.setRawOutput(eval);
    }

}
