/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.action.invoke.jdbc;

import kunlun.data.validation.support.ValidationConfig;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;

/**
 * The jdbc invoke configuration.
 * @author Kahle
 */
public class JdbcInvokeConfig implements Serializable {
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
     * The jdbc input conversion script.
     */
    private String input;
    /**
     * The jdbc execute type: 0 unknown, 1 nothing, 2 update (insert or update), 3 batch update (insert or update)
     *  , 4 single value query, 5 single object query, 6 object array query, 7 object array paging query (page number)
     *  , 8 object array paging query (scroll id)
     */
    private Integer executeType;
    /**
     * The jdbc execute sql.
     */
    private String sql;
    /**
     * The jdbc output conversion script.
     */
    private String output;
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

    public String getInput() {

        return input;
    }

    public void setInput(String input) {

        this.input = input;
    }

    public Integer getExecuteType() {

        return executeType;
    }

    public void setExecuteType(Integer executeType) {

        this.executeType = executeType;
    }

    public String getSql() {

        return sql;
    }

    public void setSql(String sql) {

        this.sql = sql;
    }

    public String getOutput() {

        return output;
    }

    public void setOutput(String output) {

        this.output = output;
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
