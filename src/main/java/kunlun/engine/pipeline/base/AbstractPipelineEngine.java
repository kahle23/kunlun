/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.engine.pipeline.base;

import kunlun.core.function.Consumer;
import kunlun.core.function.Function;
import kunlun.engine.pipeline.Pipeline;
import kunlun.engine.pipeline.PipelineContext;
import kunlun.engine.pipeline.PipelineEngine;
import kunlun.exception.ExceptionUtil;
import kunlun.util.Assert;

/**
 * 管道引擎的骨架实现.
 * <p>
 * 引擎 = 执行管道 + 结果抽取：管道由子类实现 {@link kunlun.engine.pipeline.PipelineEngine#getPipeline()} 提供，
 * 管道策略全由实现方决定——不可变用法每次构建新的（构造逻辑固定），
 * 复杂用法查库装配加缓存（热更失效），实现可参考类内的注释模板.
 * <p>
 * execute 模板：构建上下文（场景码、输入、其他参数均进入 {@code PipelineContext}）
 * → 依序执行管道 → 从上下文抽取返回值；异常置入上下文并包装抛出，finally 记录日志.
 * <p>
 * 定制口子：实现 {@link kunlun.engine.pipeline.PipelineEngine#getPipeline()}（必须）、
 * {@link #setLogRecorder(Consumer)} 与 {@link #setResultExtractor(Function)}.
 *
 * @author Kahle
 */
public abstract class AbstractPipelineEngine implements PipelineEngine {
    /**
     * 结果抽取器：管道执行完毕后从上下文抽取最终返回值.
     */
    private Function<PipelineContext, Object> resultExtractor;
    /**
     * 日志记录器：在 finally 中执行，可读取上下文的错误信息.
     */
    private Consumer<PipelineContext> logRecorder;

    public AbstractPipelineEngine() {
        this.logRecorder = new Consumer.Empty<PipelineContext>();
        this.resultExtractor = new Function<PipelineContext, Object>() {
            @Override
            public Object apply(PipelineContext context) {

                return context.getConvertedOutput();
            }
        };
    }

    // getPipeline() 方法的实现可参考下面的注释模板：
    // 其核心是 config-loader 阶段——它会加载场景码对应的规则（声明）并置入上下文，
    // 其余锚点（入参校验、预处理、候选筛选、核心求值、结果聚合、后处理、出参校验）按需装配；
    // 管道策略全由实现方决定：不可变用法每次构建新的，复杂用法查库装配加缓存（热更失效）.
    /*
    @Override
    public Pipeline getPipeline() {
        DefaultPipeline pipeline = new DefaultPipeline();
        pipeline.appendStage(Pipeline.STAGE_CONFIG_LOADER, new Consumer<PipelineContext>() {
            @Override
            public void accept(PipelineContext context) {
                // 核心一步：按场景码装载对应的规则（声明）置入上下文，来源由实现方决定.
                context.setConfig(loadConfig(context.getSceneCode()));
            }
        });
        pipeline.appendStage(Pipeline.STAGE_PREPROCESS_VALIDATOR, new CommonValidator(Boolean.TRUE));
        pipeline.appendStage(Pipeline.STAGE_PREPROCESSOR, new CommonProcessor(Boolean.TRUE));
        pipeline.appendStage(Pipeline.STAGE_CANDIDATE_SELECTOR, new Consumer.Empty<PipelineContext>());
        pipeline.appendStage(Pipeline.STAGE_ENGINE_EXECUTOR, new Consumer.Empty<PipelineContext>());
        pipeline.appendStage(Pipeline.STAGE_RESULT_AGGREGATOR, new Consumer.Empty<PipelineContext>());
        pipeline.appendStage(Pipeline.STAGE_POSTPROCESSOR, new CommonProcessor(Boolean.FALSE));
        pipeline.appendStage(Pipeline.STAGE_POSTPROCESS_VALIDATOR, new CommonValidator(Boolean.FALSE));
        return pipeline;
    }
    */

    /**
     * 以入参构建引擎上下文对象.
     *
     * @param sceneCode 场景码
     * @param input 输入对象
     * @param arguments 其他相关参数
     * @return 引擎上下文对象
     */
    protected PipelineContext buildContext(String sceneCode, Object input, Object[] arguments) {

        return new DefaultPipelineContext(
                Assert.notBlank(sceneCode, "Parameter \"sceneCode\" must not blank. "), input, arguments);
    }

    public Function<PipelineContext, Object> getResultExtractor() {

        return resultExtractor;
    }

    public void setResultExtractor(Function<PipelineContext, Object> resultExtractor) {

        this.resultExtractor = Assert.notNull(resultExtractor, "Parameter \"resultExtractor\" must not null. ");
    }

    public Consumer<PipelineContext> getLogRecorder() {

        return logRecorder;
    }

    public void setLogRecorder(Consumer<PipelineContext> logRecorder) {

        this.logRecorder = Assert.notNull(logRecorder, "Parameter \"logRecorder\" must not null. ");
    }

    @Override
    public Object execute(String strategy, Object input, Object[] arguments) {
        // 构建上下文.
        PipelineContext context = buildContext(strategy, input, arguments);
        Assert.state(context != null, "Build the context failure! ");
        try {
            // 取本次执行的管道（本执行期间结构稳定）.
            Pipeline pipeline = getPipeline();
            // 依序执行管道中的阶段.
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

}
