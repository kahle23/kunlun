/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.action.invoke.script;

import kunlun.data.validation.support.ValidationConfig;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;

/**
 * The script invoke configuration.
 * @author Kahle
 */
public class ScriptInvokeConfig implements Serializable {
    /**
     * The script name.
     */
    private String name;
    /**
     * The script description.
     */
    private String description;
    /**
     * The script engine name.
     */
    private String engine;
    /**
     * The script content.
     */
    private String content;
    /**
     * The input data validation configs.
     */
    private Collection<ValidationConfig> inputValidations;
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

    public String getEngine() {

        return engine;
    }

    public void setEngine(String engine) {

        this.engine = engine;
    }

    public String getContent() {

        return content;
    }

    public void setContent(String content) {

        this.content = content;
    }

    public Collection<ValidationConfig> getInputValidations() {

        return inputValidations;
    }

    public void setInputValidations(Collection<ValidationConfig> inputValidations) {

        this.inputValidations = inputValidations;
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
