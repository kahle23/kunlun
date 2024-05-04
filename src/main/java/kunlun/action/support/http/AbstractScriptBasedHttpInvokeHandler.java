/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.action.support.http;

import kunlun.action.support.script.AbstractScriptBasedInvokeHandler;
import kunlun.data.Dict;
import kunlun.data.bean.BeanUtils;
import kunlun.data.json.JsonUtils;
import kunlun.data.tuple.KeyValue;
import kunlun.data.tuple.KeyValueImpl;
import kunlun.data.validation.support.ValidationConfig;
import kunlun.logging.Logger;
import kunlun.logging.LoggerFactory;
import kunlun.util.Assert;
import kunlun.util.ClassUtils;
import kunlun.util.ObjectUtils;
import kunlun.util.StringUtils;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import static kunlun.common.constant.Numbers.*;
import static kunlun.util.ObjectUtils.cast;

/**
 * The abstract script-based http invoke action handler.
 * @author Kahle
 */
public abstract class AbstractScriptBasedHttpInvokeHandler extends AbstractScriptBasedInvokeHandler {
    private static final Logger log = LoggerFactory.getLogger(AbstractScriptBasedHttpInvokeHandler.class);

    @Override
    protected void validateInput(InvokeContext context) {
        HttpInvokeConfig config = (HttpInvokeConfig) context.getConfig();
        Collection<ValidationConfig> validations = config.getInputValidations();
        validate(config.getScriptEngine(), context, validations);
    }

    @Override
    protected void convertInput(InvokeContext context) {
        // Get config.
        HttpInvokeConfig config = (HttpInvokeConfig) context.getConfig();
        String  scriptEngine = config.getScriptEngine();
        Integer inputType = config.getInputType();
        Assert.notNull(inputType, "The input type must not null! ");
        // Create converted input object.
        ConvertedInput convertedInput = new ConvertedInput();
        context.setConvertedInput(convertedInput);
        // Charset and method.
        String charset = config.getCharset();
        if (StringUtils.isNotBlank(charset)) {
            convertedInput.setCharset(charset);
        }
        convertedInput.setMethod(config.getMethod());
        // Url.
        Object eval = eval(scriptEngine, config.getUrl(), context);
        convertedInput.setUrl(String.valueOf(eval));
        // Headers.
        List<?> headers = (List<?>) eval(scriptEngine, config.getHeaders(), context);
        if (!ObjectUtils.isEmpty(headers)) {
            Collection<KeyValue<String, String>> collection =
                    cast(BeanUtils.beanToBeanInList(headers, KeyValueImpl.class));
            convertedInput.setHeaders(collection);
        }
        // Params.
        // input type: 0 unknown, 1 no content, 2 form-www, 3 form-data, 4 json
        convertedInput.setInputType(inputType);
        if (inputType == TWO || inputType == THREE) {
            List<?> parametersObj = (List<?>) eval(scriptEngine, config.getParameters(), context);
            if (!ObjectUtils.isEmpty(parametersObj)) {
                Collection<KeyValue<String, Object>> collection =
                        cast(BeanUtils.beanToBeanInList(parametersObj, KeyValueImpl.class));
                convertedInput.setParameters(collection);
            }
        }
        // Body.
        if (inputType == FOUR) {
            Object bodyObj = eval(scriptEngine, config.getBody(), context);
            if (bodyObj != null) {
                convertedInput.setBody(
                    bodyObj instanceof String ? bodyObj : JsonUtils.toJsonString(bodyObj)
                );
            }
        }
        // End.
    }

    @Override
    protected void validateOutput(InvokeContext context) {
        HttpInvokeConfig config = (HttpInvokeConfig) context.getConfig();
        Collection<ValidationConfig> validations = config.getOutputValidations();
        validate(config.getScriptEngine(), context, validations);
    }

    @Override
    protected void convertOutput(InvokeContext context) {
        // Get config and get script engine name.
        HttpInvokeConfig config = (HttpInvokeConfig) context.getConfig();
        Class<?> expectedClass = context.getExpectedClass();
        String   scriptEngine = config.getScriptEngine();
        Integer  outputType = config.getOutputType();
        String   output = config.getOutput();
        Assert.notNull(outputType, "The output type must not null! ");
        // Handle converted output.
        RawOutput rawOutput = (RawOutput) context.getRawOutput();
        if (outputType == ONE) {
            context.setConvertedOutput(null);
        }
        else if (outputType == TWO) {
            context.setConvertedOutput(rawOutput.getRawString());
        }
        else if (outputType == THREE) {
            // RawObject must init in doInvoke.
            context.setConvertedOutput(rawOutput.getRawObject());
        }
        else {
            throw new UnsupportedOperationException("The output type is unsupported! ");
        }
        // Eval output conversion script.
        if (StringUtils.isNotBlank(output)) {
            Object outputObj = eval(scriptEngine, output, context);
            context.setConvertedOutput(outputObj);
        }
        // Convert converted output.
        if (!ClassUtils.isSimpleValueType(expectedClass)) {
            Object convertedOutput = context.getConvertedOutput();
            if (convertedOutput instanceof Collection) {
            }
            else if (convertedOutput!=null&&convertedOutput.getClass().isArray()) {
            }
            else if (convertedOutput instanceof Map && Map.class.isAssignableFrom(expectedClass)) {
                context.setConvertedOutput(Dict.of((Map<?, ?>) convertedOutput));
            }
            else if (convertedOutput != null && !expectedClass.isAssignableFrom(convertedOutput.getClass())) {
                context.setConvertedOutput(BeanUtils.beanToBean(convertedOutput, expectedClass));
            }
            else { /* Do nothing. */ }
        }
    }

    /**
     * The converted input object.
     * @author Kahle
     */
    public static class ConvertedInput implements Serializable {
        /**
         * The http charset.
         */
        private String charset;
        /**
         * The http method: 1 get, 2 post
         */
        private Integer method;
        /**
         * The http url.
         */
        private String  url;
        /**
         * The http headers.
         */
        private Collection<KeyValue<String, String>> headers;
        /**
         * The http input type: 0 unknown, 1 no content, 2 form-www, 3 form-data, 4 json
         */
        private Integer inputType;
        /**
         * The http parameters.
         */
        private Collection<KeyValue<String, Object>> parameters;
        /**
         * The http body.
         */
        private Object body;

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

        public Collection<KeyValue<String, String>> getHeaders() {

            return headers;
        }

        public void setHeaders(Collection<KeyValue<String, String>> headers) {

            this.headers = headers;
        }

        public Integer getInputType() {

            return inputType;
        }

        public void setInputType(Integer inputType) {

            this.inputType = inputType;
        }

        public Collection<KeyValue<String, Object>> getParameters() {

            return parameters;
        }

        public void setParameters(Collection<KeyValue<String, Object>> parameters) {

            this.parameters = parameters;
        }

        public Object getBody() {

            return body;
        }

        public void setBody(Object body) {

            this.body = body;
        }
    }

    /**
     * The raw output object.
     * @author Kahle
     */
    public static class RawOutput implements Serializable {
        /**
         * The raw output's time.
         */
        private String time;
        /**
         * The http charset.
         */
        private String charset;
        /**
         * The http headers.
         */
        private Collection<KeyValue<String, String>> headers;
        /**
         * The http status code.
         */
        private Integer statusCode;
        /**
         * The http output type: 0 unknown，1 no content, 2 raw data, 3 json
         */
        private Integer outputType;
        /**
         * The http raw string.
         */
        private String rawString;
        /**
         * The http raw object.
         */
        private Object rawObject;

        public String getTime() {

            return time;
        }

        public void setTime(String time) {

            this.time = time;
        }

        public String getCharset() {

            return charset;
        }

        public void setCharset(String charset) {

            this.charset = charset;
        }

        public Collection<KeyValue<String, String>> getHeaders() {

            return headers;
        }

        public void setHeaders(Collection<KeyValue<String, String>> headers) {

            this.headers = headers;
        }

        public Integer getStatusCode() {

            return statusCode;
        }

        public void setStatusCode(Integer statusCode) {

            this.statusCode = statusCode;
        }

        public Integer getOutputType() {

            return outputType;
        }

        public void setOutputType(Integer outputType) {

            this.outputType = outputType;
        }

        public String getRawString() {

            return rawString;
        }

        public void setRawString(String rawString) {

            this.rawString = rawString;
        }

        public Object getRawObject() {

            return rawObject;
        }

        public void setRawObject(Object rawObject) {

            this.rawObject = rawObject;
        }
    }

}
