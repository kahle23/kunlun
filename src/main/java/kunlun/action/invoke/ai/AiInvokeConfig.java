/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.action.invoke.ai;

import kunlun.data.validation.support.ValidationConfig;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;

/**
 * The AI invoke configuration.
 * @author Kahle
 */
@Deprecated
public class AiInvokeConfig implements Serializable {
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
     * The AI handler input conversion script.
     */
    private String inputConversionScript;
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
    /**
     * The AI handler name.
     */
    private String handlerName;
    /**
     * The method name of AI handler.
     */
    private String methodName;
    /**
     * The AI handler output conversion script.
     */
    private String outputConversionScript;
    /**
     * The output data validation configs.
     */
    private Collection<ValidationConfig> outputValidations;
    /**
     * The cache configuration.
     * Mainly includes "cacheName" and "cacheKey".
     */
    private Map<String, String> cacheConfig;
    /**
     * The other configs.
     */
    private Map<String, Object> otherConfigs;

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

    public String getInputConversionScript() {

        return inputConversionScript;
    }

    public void setInputConversionScript(String inputConversionScript) {

        this.inputConversionScript = inputConversionScript;
    }

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

    public String getHandlerName() {

        return handlerName;
    }

    public void setHandlerName(String handlerName) {

        this.handlerName = handlerName;
    }

    public String getMethodName() {

        return methodName;
    }

    public void setMethodName(String methodName) {

        this.methodName = methodName;
    }

    public String getOutputConversionScript() {

        return outputConversionScript;
    }

    public void setOutputConversionScript(String outputConversionScript) {

        this.outputConversionScript = outputConversionScript;
    }

    public Collection<ValidationConfig> getOutputValidations() {

        return outputValidations;
    }

    public void setOutputValidations(Collection<ValidationConfig> outputValidations) {

        this.outputValidations = outputValidations;
    }

    public Map<String, String> getCacheConfig() {

        return cacheConfig;
    }

    public void setCacheConfig(Map<String, String> cacheConfig) {

        this.cacheConfig = cacheConfig;
    }

    public Map<String, Object> getOtherConfigs() {

        return otherConfigs;
    }

    @SuppressWarnings("unchecked")
    public void setOtherConfigs(Map<?, ?> otherConfigs) {

        this.otherConfigs = (Map<String, Object>) otherConfigs;
    }

}
