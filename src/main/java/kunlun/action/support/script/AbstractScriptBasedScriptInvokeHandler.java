/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.action.support.script;

import kunlun.action.support.AbstractInvokeActionHandler;
import kunlun.data.validation.support.ValidationConfig;
import kunlun.logging.Logger;
import kunlun.logging.LoggerFactory;
import kunlun.util.handler.ScriptHandler;
import kunlun.util.handler.support.ScriptHandlerImpl;

import java.util.Collection;

/**
 * The abstract script-based script invoke action handler.
 * @author Kahle
 */
public abstract class AbstractScriptBasedScriptInvokeHandler extends AbstractInvokeActionHandler {
    private static final Logger log = LoggerFactory.getLogger(AbstractScriptBasedScriptInvokeHandler.class);
    private final ScriptHandler scriptHandler = new ScriptHandlerImpl();

    protected ScriptHandler getScriptHandler() {

        return scriptHandler;
    }

    @Override
    protected void validateInput(InvokeContext context) {
        ScriptInvokeConfig config = (ScriptInvokeConfig) context.getConfig();
        Collection<ValidationConfig> validations = config.getInputValidations();
        getScriptHandler().validate(config.getEngine(), validations, context);
    }

    @Override
    protected void validateOutput(InvokeContext context) {
        ScriptInvokeConfig config = (ScriptInvokeConfig) context.getConfig();
        Collection<ValidationConfig> validations = config.getOutputValidations();
        getScriptHandler().validate(config.getEngine(), validations, context);
    }

    @Override
    protected void doInvoke(InvokeContext context) {
        ScriptInvokeConfig config = (ScriptInvokeConfig) context.getConfig();
        Object eval = getScriptHandler().eval(config.getEngine(), config.getContent(), context);
        context.setRawOutput(eval);
    }

}
