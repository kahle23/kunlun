/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.engine;

import kunlun.core.ContextKey;
import kunlun.core.Engine;
import kunlun.core.function.Consumer;
import kunlun.core.function.Function;
import kunlun.exception.ExceptionUtil;
import kunlun.util.Assert;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

/**
 * 引擎的骨架实现.<br />
 * <p>
 * 引擎 = 执行管道 + 结果抽取：管道（{@link Engine.Pipeline}）持有依序执行的具名阶段，
 * 默认装配八个锚点阶段，可按键名做替换、前插、后插与移除——
 * 引擎的"生长"因此在"替换阶段"与"增删阶段"两个维度上都是自由的：
 * <pre>
 *     engine.getPipeline().addStageBefore(Engine.Stage.NAME_ENGINE_EXECUTOR, cacheStage);
 *     engine.getPipeline().replaceStage(Engine.Stage.NAME_RESULT_AGGREGATOR, myAggregator);
 * </pre>
 * 默认管道（顺序固定装配，内容全可换）：
 * <pre>
 *     config-loader → input-validator → input-converter → candidate-selector
 *     → engine-executor → result-aggregator → output-validator → output-converter
 * </pre>
 * 各锚点的默认内容：校验类为无操作，转换类为透传（{@link CommonConverter}），
 * candidate-selector 与 result-aggregator 退化为无操作（单执行器场景够用），
 * config-loader 与 engine-executor 为占位阶段（未替换就执行会抛出 IllegalStateException）.
 * <p>
 * execute 模板：构建上下文（场景码、输入、其他参数均进入 {@link Engine.EngineContext}）
 * → 依序执行管道 → 从上下文抽取返回值；异常置入上下文并包装抛出，finally 记录日志.
 * <p>
 * 生长一个新的引擎家族时，继承本类并按需替换阶段或定制管道即可，
 * 无需改动本类与 {@link Engine} 的任何代码.
 *
 * @author Kahle
 */
public abstract class AbstractEngine implements Engine {
    /**
     * 引擎执行管道：持有依序执行的具名阶段.
     * */
    private final Engine.Pipeline pipeline;
    /**
     * 日志记录阶段：在 finally 中执行，可读取上下文的错误信息.
     * */
    private Consumer<EngineContext> logRecorder;
    /**
     * 结果抽取阶段：管道执行完毕后从上下文抽取最终返回值.
     * */
    private Function<EngineContext, Object> resultExtractor;

    public AbstractEngine() {
        this.pipeline = buildPipeline();
        this.setLogRecorder(new Consumer.Empty<EngineContext>());
        this.setResultExtractor(new Function<EngineContext, Object>() {
            @Override
            public Object apply(EngineContext context) {

                return context.getConvertedOutput();
            }
        });
    }

    /**
     * 构建默认管道：八个锚点阶段按序装配，子类可覆写以定制初始管道.
     * @return 引擎执行管道
     */
    protected Engine.Pipeline buildPipeline() {
        SimplePipeline pipeline = new SimplePipeline();
        pipeline.addStage(new SimpleStage(Engine.Stage.NAME_CONFIG_LOADER,
                placeholder(Engine.Stage.NAME_CONFIG_LOADER)));
        pipeline.addStage(new SimpleStage(Engine.Stage.NAME_INPUT_VALIDATOR,
                new Consumer.Empty<EngineContext>()));
        pipeline.addStage(new SimpleStage(Engine.Stage.NAME_INPUT_CONVERTER,
                new CommonConverter(TRUE)));
        pipeline.addStage(new SimpleStage(Engine.Stage.NAME_CANDIDATE_SELECTOR,
                new Consumer.Empty<EngineContext>()));
        pipeline.addStage(new SimpleStage(Engine.Stage.NAME_ENGINE_EXECUTOR,
                placeholder(Engine.Stage.NAME_ENGINE_EXECUTOR)));
        pipeline.addStage(new SimpleStage(Engine.Stage.NAME_RESULT_AGGREGATOR,
                new Consumer.Empty<EngineContext>()));
        pipeline.addStage(new SimpleStage(Engine.Stage.NAME_OUTPUT_VALIDATOR,
                new Consumer.Empty<EngineContext>()));
        pipeline.addStage(new SimpleStage(Engine.Stage.NAME_OUTPUT_CONVERTER,
                new CommonConverter(FALSE)));
        return pipeline;
    }

    /**
     * 生成占位阶段：被调用即说明该锚点未被真实装配.
     * @param stageName 阶段的键名
     * @return 占位阶段
     */
    protected Consumer<EngineContext> placeholder(final String stageName) {

        return new Consumer<EngineContext>() {
            @Override
            public void accept(EngineContext context) {
                throw new IllegalStateException(
                        "The stage \"" + stageName + "\" has not been configured. ");
            }
        };
    }

    /**
     * 以同键名的阶段替换管道中的指定锚点.
     * @param stageName 锚点的键名
     * @param consumer 替换后的阶段行为
     */
    protected void replaceStage(String stageName, Consumer<EngineContext> consumer) {
        Assert.notBlank(stageName, "Parameter \"stageName\" must not blank. ");
        Assert.notNull(consumer, "Parameter \"consumer\" must not null. ");
        pipeline.replaceStage(stageName, new SimpleStage(stageName, consumer));
    }

    /**
     * 获取引擎执行管道.
     * @return 引擎执行管道
     */
    public Engine.Pipeline getPipeline() {

        return pipeline;
    }

    public Consumer<EngineContext> getConfigLoader() {

        return pipeline.getStage(Engine.Stage.NAME_CONFIG_LOADER);
    }

    public void setConfigLoader(Consumer<EngineContext> configLoader) {

        replaceStage(Engine.Stage.NAME_CONFIG_LOADER, configLoader);
    }

    public Consumer<EngineContext> getInputValidator() {

        return pipeline.getStage(Engine.Stage.NAME_INPUT_VALIDATOR);
    }

    public void setInputValidator(Consumer<EngineContext> inputValidator) {

        replaceStage(Engine.Stage.NAME_INPUT_VALIDATOR, inputValidator);
    }

    public Consumer<EngineContext> getInputConverter() {

        return pipeline.getStage(Engine.Stage.NAME_INPUT_CONVERTER);
    }

    public void setInputConverter(Consumer<EngineContext> inputConverter) {

        replaceStage(Engine.Stage.NAME_INPUT_CONVERTER, inputConverter);
    }

    public Consumer<EngineContext> getCandidateSelector() {

        return pipeline.getStage(Engine.Stage.NAME_CANDIDATE_SELECTOR);
    }

    public void setCandidateSelector(Consumer<EngineContext> candidateSelector) {

        replaceStage(Engine.Stage.NAME_CANDIDATE_SELECTOR, candidateSelector);
    }

    public Consumer<EngineContext> getEngineExecutor() {

        return pipeline.getStage(Engine.Stage.NAME_ENGINE_EXECUTOR);
    }

    public void setEngineExecutor(Consumer<EngineContext> engineExecutor) {

        replaceStage(Engine.Stage.NAME_ENGINE_EXECUTOR, engineExecutor);
    }

    public Consumer<EngineContext> getResultAggregator() {

        return pipeline.getStage(Engine.Stage.NAME_RESULT_AGGREGATOR);
    }

    public void setResultAggregator(Consumer<EngineContext> resultAggregator) {

        replaceStage(Engine.Stage.NAME_RESULT_AGGREGATOR, resultAggregator);
    }

    public Consumer<EngineContext> getOutputValidator() {

        return pipeline.getStage(Engine.Stage.NAME_OUTPUT_VALIDATOR);
    }

    public void setOutputValidator(Consumer<EngineContext> outputValidator) {

        replaceStage(Engine.Stage.NAME_OUTPUT_VALIDATOR, outputValidator);
    }

    public Consumer<EngineContext> getOutputConverter() {

        return pipeline.getStage(Engine.Stage.NAME_OUTPUT_CONVERTER);
    }

    public void setOutputConverter(Consumer<EngineContext> outputConverter) {

        replaceStage(Engine.Stage.NAME_OUTPUT_CONVERTER, outputConverter);
    }

    public Consumer<EngineContext> getLogRecorder() {

        return logRecorder;
    }

    public void setLogRecorder(Consumer<EngineContext> logRecorder) {

        this.logRecorder = Assert.notNull(logRecorder);
    }

    public Function<EngineContext, Object> getResultExtractor() {

        return resultExtractor;
    }

    public void setResultExtractor(Function<EngineContext, Object> resultExtractor) {

        this.resultExtractor = Assert.notNull(resultExtractor);
    }

    /**
     * 以入参构建引擎上下文对象.
     * @param sceneCode 场景码
     * @param input 输入对象
     * @param arguments 其他相关参数
     * @return 引擎上下文对象
     */
    protected EngineContext buildContext(String sceneCode, Object input, Object[] arguments) {

        return new EngineContextImpl(Assert.notBlank(sceneCode), input, arguments);
    }

    @Override
    public Object execute(String strategy, Object input, Object[] arguments) {
        // 构建上下文.
        EngineContext context = buildContext(strategy, input, arguments);
        Assert.state(context != null, "Build the context failure! ");
        try {
            // 依序执行管道中的阶段（默认八个锚点，可增删插排替换）.
            pipeline.execute(context);
            // 从上下文抽取最终返回值.
            return getResultExtractor().apply(context);
        }
        catch (Exception e) {
            context.setError(e);
            throw ExceptionUtil.wrap(e);
        }
        finally { getLogRecorder().accept(context); }
    }

    /**
     * 通用转换器.<br />
     * <p>
     * 默认实现为透传：将 raw 数据直接作为 converted 数据，
     * 各引擎家族可替换为脚本转换等实现.
     *
     * @author Kahle
     */
    public static class CommonConverter implements Consumer<EngineContext> {
        /**
         * 是否处理输入（为 false 时处理输出）.
         * */
        private final boolean processInput;

        public CommonConverter(boolean processInput) {

            this.processInput = processInput;
        }

        @Override
        public void accept(EngineContext context) {
            if (processInput) {
                context.setConvertedInput(context.getRawInput());
            }
            else {
                context.setConvertedOutput(context.getRawOutput());
            }
        }
    }

    /**
     * 引擎上下文的简单实现.<br />
     * <p>
     * 类型化读写委托给 {@link ContextKey}（底层走存储器），
     * 中间产物经由 ContextKey 声明即成为一等公民.
     *
     * @author Kahle
     */
    public static class EngineContextImpl implements EngineContext {
        /**
         * 上下文存储器：中间产物等运行期数据的存放处.
         * */
        private final Map<String, Object> storage = new ConcurrentHashMap<String, Object>();
        private String sceneCode;
        private Object[] arguments;
        private EngineConfig config;
        private Object rawInput;
        private Object convertedInput;
        private Object rawOutput;
        private Object convertedOutput;
        private Throwable error;

        public EngineContextImpl(String sceneCode, Object rawInput, Object[] arguments) {
            this.sceneCode = sceneCode;
            this.rawInput = rawInput;
            this.arguments = arguments;
        }

        public EngineContextImpl() {

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
        public EngineConfig getConfig() {

            return config;
        }

        @Override
        public void setConfig(EngineConfig config) {

            this.config = config;
        }

        @Override
        public Object getRawInput() {

            return rawInput;
        }

        public void setRawInput(Object rawInput) {

            this.rawInput = rawInput;
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
        public <T> T get(ContextKey<T> key) {
            Assert.notNull(key, "Parameter \"key\" must not null. ");

            return key.get(getStorage());
        }

        @Override
        public <T> T put(ContextKey<T> key, T value) {
            Assert.notNull(key, "Parameter \"key\" must not null. ");

            return key.put(getStorage(), value);
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

}
