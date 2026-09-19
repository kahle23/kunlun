/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.engine.pipeline.base;

import kunlun.engine.pipeline.PipelineContext;
import kunlun.engine.pipeline.PipelineConfig;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 管道引擎上下文的简单实现.
 * <p>
 * 默认实现承载 {@code PipelineContext} 的全部槽位，
 * 中间产物等运行期数据直接经 {@code getStorage()} 读写.
 *
 * @author Kahle
 */
public class DefaultPipelineContext implements PipelineContext {
    /**
     * 上下文存储器：中间产物等运行期数据的存放处.
     */
    private final Map<String, Object> storage = new ConcurrentHashMap<String, Object>();
    private String sceneCode;
    private Object[] arguments;
    private PipelineConfig config;
    private Object rawInput;
    private Object convertedInput;
    private Object rawOutput;
    private Object convertedOutput;
    private Throwable error;

    public DefaultPipelineContext(String sceneCode, Object rawInput, Object[] arguments) {
        this.sceneCode = sceneCode;
        this.rawInput = rawInput;
        this.arguments = arguments;
    }

    public DefaultPipelineContext() {

    }

    @Override
    public Map<String, Object> getStorage() {

        return storage;
    }

    @Override
    public String getSceneCode() {

        return sceneCode;
    }

    public void setSceneCode(String sceneCode) {

        this.sceneCode = sceneCode;
    }

    @Override
    public Object[] getArguments() {

        return arguments;
    }

    public void setArguments(Object[] arguments) {

        this.arguments = arguments;
    }

    @Override
    public Object getRawInput() {

        return rawInput;
    }

    public void setRawInput(Object rawInput) {

        this.rawInput = rawInput;
    }

    @Override
    public PipelineConfig getConfig() {

        return config;
    }

    @Override
    public void setConfig(PipelineConfig config) {

        this.config = config;
    }

    @Override
    public Object getConvertedInput() {

        return convertedInput;
    }

    @Override
    public void setConvertedInput(Object input) {

        this.convertedInput = input;
    }

    @Override
    public Object getRawOutput() {

        return rawOutput;
    }

    @Override
    public void setRawOutput(Object output) {

        this.rawOutput = output;
    }

    @Override
    public Object getConvertedOutput() {

        return convertedOutput;
    }

    @Override
    public void setConvertedOutput(Object output) {

        this.convertedOutput = output;
    }

    @Override
    public Throwable getError() {

        return error;
    }

    @Override
    public void setError(Throwable th) {

        this.error = th;
    }

}
