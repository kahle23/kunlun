/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.action.invoke.ai;

import kunlun.action.invoke.AbstractInvokeAction;
import kunlun.ai.AIUtils;
import kunlun.data.validation.support.ValidationConfig;
import kunlun.logging.Logger;
import kunlun.logging.LoggerFactory;
import kunlun.util.StringUtils;
import kunlun.util.handler.RenderHandler;
import kunlun.util.handler.ScriptHandler;
import kunlun.util.handler.support.RenderHandlerImpl;
import kunlun.util.handler.support.ScriptHandlerImpl;

import java.io.Serializable;
import java.util.Collection;

/**
 * The abstract script-based AI invoke action handler.
 * @author Kahle
 */
@Deprecated
public abstract class AbstractScriptBasedAiInvokeAction extends AbstractInvokeAction {
    private static final Logger log = LoggerFactory.getLogger(AbstractScriptBasedAiInvokeAction.class);
    private final ScriptHandler scriptHandler = new ScriptHandlerImpl();
    private final RenderHandler renderHandler = new RenderHandlerImpl();

    protected ScriptHandler getScriptHandler() {

        return scriptHandler;
    }

    protected RenderHandler getRenderHandler() {

        return renderHandler;
    }

    @Override
    protected void validateInput(InvokeContext context) {
        AiInvokeConfig config = (AiInvokeConfig) context.getConfig();
        Collection<ValidationConfig> validations = config.getInputValidations();
        getScriptHandler().validate(config.getScriptEngine(), validations, context);
    }

    @Override
    protected void convertInput(InvokeContext context) {
        // Get config.
        AiInvokeConfig config = (AiInvokeConfig) context.getConfig();
        String scriptEngine = config.getScriptEngine();
        String rendererName = config.getRendererName();
        String templateName = "prompt";
        // Rendering AI's prompts templates.
        ConvertedInput convertedInput = new ConvertedInput();
        context.setConvertedInput(convertedInput);
        String systemPrompt = config.getSystemPrompt();
        if (StringUtils.isNotBlank(systemPrompt)) {
            convertedInput.setSystemPrompt(getRenderHandler()
                    .render(rendererName, systemPrompt, templateName, context));
        }
        String userPrompt = config.getUserPrompt();
        if (StringUtils.isNotBlank(userPrompt)) {
            convertedInput.setUserPrompt(getRenderHandler()
                    .render(rendererName, userPrompt, templateName, context));
        }
        String toolPrompt = config.getToolPrompt();
        if (StringUtils.isNotBlank(toolPrompt)) {
            convertedInput.setToolPrompt(getRenderHandler()
                    .render(rendererName, toolPrompt, templateName, context));
        }
        //
        String script = config.getInputConversionScript();
        Object eval = getScriptHandler().eval(scriptEngine, script, context);
        convertedInput.setData(eval);
        // End.
    }

    @Override
    protected void doInvoke(InvokeContext context) {
        // Get config.
        AiInvokeConfig config = (AiInvokeConfig) context.getConfig();
        ConvertedInput convertedInput = (ConvertedInput) context.getConvertedInput();
        //
        String handlerName = config.getHandlerName();
        String methodName = config.getMethodName();
        Object rawOutput = AIUtils.execute(handlerName, convertedInput.getData(), methodName, Object.class);
        context.setRawOutput(rawOutput);
    }

    @Override
    protected void validateOutput(InvokeContext context) {
        AiInvokeConfig config = (AiInvokeConfig) context.getConfig();
        Collection<ValidationConfig> validations = config.getOutputValidations();
        getScriptHandler().validate(config.getScriptEngine(), validations, context);
    }

    @Override
    protected void convertOutput(InvokeContext context) {
        // Get config and get script engine name.
        AiInvokeConfig config = (AiInvokeConfig) context.getConfig();
        String scriptEngine = config.getScriptEngine();
        String outputScript = config.getOutputConversionScript();
        // Eval output script.
        if (StringUtils.isNotBlank(outputScript)) {
            context.setConvertedOutput(getScriptHandler().eval(scriptEngine, outputScript, context));
        }
        else { context.setConvertedOutput(context.getRawOutput()); }
        // End.
    }

    /**
     * The converted input object.
     * @author Kahle
     */
    public static class ConvertedInput implements Serializable {
        /**
         * The system prompt for AI handler.
         */
        private String systemPrompt;
        /**
         * The user prompt for AI handler.
         */
        private String userPrompt;
        /**
         * The tool prompt for AI handler.
         */
        private String toolPrompt;

        private Object data;

        public String getSystemPrompt() {

            return systemPrompt;
        }

        public void setSystemPrompt(String systemPrompt) {

            this.systemPrompt = systemPrompt;
        }

        public String getUserPrompt() {

            return userPrompt;
        }

        public void setUserPrompt(String userPrompt) {

            this.userPrompt = userPrompt;
        }

        public String getToolPrompt() {

            return toolPrompt;
        }

        public void setToolPrompt(String toolPrompt) {

            this.toolPrompt = toolPrompt;
        }

        public Object getData() {

            return data;
        }

        public void setData(Object data) {

            this.data = data;
        }
    }

}
