package artoria.action.handler.http;

import artoria.data.validation.support.ValidationConfig;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;

/**
 * The http invoke configuration.
 * @author Kahle
 */
public class HttpInvokeConfig implements Serializable {
    /**
     * The script engine name.
     */
    private String scriptEngine;
    /**
     * The http charset.
     */
    private String charset;
    /**
     * The http method: 1 get, 2 post, 3 put, 4 delete
     */
    private Integer method;
    /**
     * The http url (data script).
     */
    private String url;
    /**
     * The http headers (data script).
     * The expected java type is "Collection<KeyValue<String, String>>".
     */
    private String headers;
    /**
     * The http input type: 0 unknown, 1 no content, 2 form-www, 3 form-data, 4 json
     */
    private Integer inputType;
    /**
     * The http parameters (data script).
     * The expected java type is "Collection<KeyValue<String, String>>".
     */
    private String parameters;
    /**
     * The http body (data script).
     */
    private String body;
    /**
     * The input data validation configs.
     */
    private Collection<ValidationConfig> inputValidations;
    /**
     * The http output type: 0 unknown，1 no content, 2 raw data, 3 json
     */
    private Integer outputType;
    /**
     * The http output conversion script.
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
    private Map<Object, Object> otherConfigs;

    public String getScriptEngine() {

        return scriptEngine;
    }

    public void setScriptEngine(String scriptEngine) {

        this.scriptEngine = scriptEngine;
    }

    public String getCharset() {

        return charset;
    }

    public void setCharset(String charset) {

        this.charset = charset;
    }

    public Integer getMethod() {

        return method;
    }

    public void setMethod(Integer method) {

        this.method = method;
    }

    public String getUrl() {

        return url;
    }

    public void setUrl(String url) {

        this.url = url;
    }

    public String getHeaders() {

        return headers;
    }

    public void setHeaders(String headers) {

        this.headers = headers;
    }

    public Integer getInputType() {

        return inputType;
    }

    public void setInputType(Integer inputType) {

        this.inputType = inputType;
    }

    public String getParameters() {

        return parameters;
    }

    public void setParameters(String parameters) {

        this.parameters = parameters;
    }

    public String getBody() {

        return body;
    }

    public void setBody(String body) {

        this.body = body;
    }

    public Collection<ValidationConfig> getInputValidations() {

        return inputValidations;
    }

    public void setInputValidations(Collection<ValidationConfig> inputValidations) {

        this.inputValidations = inputValidations;
    }

    public Integer getOutputType() {

        return outputType;
    }

    public void setOutputType(Integer outputType) {

        this.outputType = outputType;
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

    public Map<Object, Object> getOtherConfigs() {

        return otherConfigs;
    }

    @SuppressWarnings("unchecked")
    public void setOtherConfigs(Map<?, ?> otherConfigs) {

        this.otherConfigs = (Map<Object, Object>) otherConfigs;
    }

}
