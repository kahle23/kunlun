/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.engine.pipeline.limit;

import kunlun.core.function.Consumer;
import kunlun.engine.pipeline.Pipeline;
import kunlun.engine.pipeline.PipelineContext;
import kunlun.engine.pipeline.base.AbstractPipelineEngine;
import kunlun.engine.pipeline.base.CommonProcessor;
import kunlun.engine.pipeline.base.CommonValidator;
import kunlun.engine.pipeline.base.DefaultPipeline;

/**
 * 限制引擎的骨架实现.
 * <p>
 * 按 {@link AbstractPipelineEngine} 的注释模板装配八锚点管道，
 * 冻结限制引擎的执行框架；维度匹配、策略路由、结果聚合等细节全部下放给子类——
 * 子类只需实现四个 getXxx 口子（各自返回对应锚点的阶段行为）：
 * <ul>
 *     <li>{@link #getConfigLoader()}：配置装载阶段——按场景码装载限制配置（声明）
 *     置入上下文，来源与缓存由子类决定（查库 + 缓存、读文件皆可）；</li>
 *     <li>{@link #getCandidateSelector()}：候选筛选阶段——从配置声明的一组规则中
 *     筛出本次参与的候选（启停过滤、优先级排序等细节归子类）；</li>
 *     <li>{@link #getRuleExecutor()}：核心求值阶段——对候选规则逐条求值
 *     （维度取值匹配、策略路由 Java 实现或脚本，归子类）；</li>
 *     <li>{@link #getResultAggregator()}：结果聚合阶段——将逐条求值的产物聚合为
 *     单一校验结果（组合方式、拦截效果、提示语渲染，归子类）.</li>
 * </ul>
 * 通用实现（候选筛选 / 核心求值 / 结果聚合三阶段：启停与优先级筛选、
 * 维度取值匹配、组合与效果裁决、提示语占位符渲染）由下游配套库的
 * limit 家族提供（如 baibao 的 {@code baibao.engine.pipeline.limit}），
 * 子类直接装配使用，也可继承覆写或另行实现.
 * <p>
 * 入参/出参校验与预/后处理四个锚点直接复用基座默认阶段
 * （{@link CommonValidator} / {@link CommonProcessor}，消费配置上的校验与脚本字段，
 * 未声明即无操作/透传），不设口子——要换行为，
 * 经 {@code getPipeline()} 对具体锚点做替换或增删即可（管道的既有定制机制）.
 * <p>
 * 限制校验是 check 类语义：超限以结果对象表达（子类在核心求值或结果聚合阶段
 * 把结果置入上下文的输出槽位），不以异常表达——异常只留给装配与程序错误；
 * 执行留痕经 {@link AbstractPipelineEngine#setLogRecorder(Consumer)} 挂载
 * （finally 中执行，可读取上下文的错误信息与 storage 中的逐规则结果）.
 * <p>
 * 定制口子：实现四个 getXxx（必须）、
 * {@link AbstractPipelineEngine#setLogRecorder(Consumer)} 与
 * {@link AbstractPipelineEngine#setResultExtractor(kunlun.core.function.Function)}.
 *
 * @author Kahle
 */
public abstract class AbstractLimitEngine extends AbstractPipelineEngine {

    /**
     * 构建限制引擎的执行管道：八锚点依序装配.
     * <p>
     * 不可变用法：每次构建新的（阶段共八个，构建开销可忽略）；
     * 四个口子在装配期各调用一次（返回 null 会在装配期即抛出），
     * 返回的阶段行为在每次执行时运行，应无状态或每次新建.
     *
     * @return 限制引擎的执行管道
     */
    @Override
    public Pipeline getPipeline() {
        DefaultPipeline pipeline = new DefaultPipeline();
        pipeline.appendStage(Pipeline.STAGE_CONFIG_LOADER, getConfigLoader());
        pipeline.appendStage(Pipeline.STAGE_PREPROCESS_VALIDATOR, new CommonValidator(Boolean.TRUE));
        pipeline.appendStage(Pipeline.STAGE_PREPROCESSOR, new CommonProcessor(Boolean.TRUE));
        pipeline.appendStage(Pipeline.STAGE_CANDIDATE_SELECTOR, getCandidateSelector());
        pipeline.appendStage(Pipeline.STAGE_ENGINE_EXECUTOR, getRuleExecutor());
        pipeline.appendStage(Pipeline.STAGE_RESULT_AGGREGATOR, getResultAggregator());
        pipeline.appendStage(Pipeline.STAGE_POSTPROCESSOR, new CommonProcessor(Boolean.FALSE));
        pipeline.appendStage(Pipeline.STAGE_POSTPROCESS_VALIDATOR, new CommonValidator(Boolean.FALSE));
        return pipeline;
    }

    // region ======== 子类的四个实现口子 ========
    /**
     * 构建配置装载阶段：按场景码装载限制配置（声明）置入上下文.
     * <p>
     * 场景码从 {@code context.getSceneCode()} 取，装载结果经
     * {@code context.setConfig(...)} 置入；来源与缓存策略由子类决定.
     * 场景未配置时应置入"规则集为空"的配置对象而非跳过置入——
     * 基座的校验与处理阶段要求配置非 null 且为 {@code AbstractPipelineConfig} 类型.
     *
     * @return 配置装载阶段行为（不可为 null）
     */
    protected abstract Consumer<PipelineContext> getConfigLoader();

    /**
     * 构建候选筛选阶段：从配置声明的一组规则中筛出本次参与的候选.
     * <p>
     * 候选载体与筛选细节（启停过滤、优先级排序等）由子类决定，
     * 筛选产物通常经 {@code context.getStorage()} 传递给核心求值阶段
     * （存储键由所选实现约定）.
     *
     * @return 候选筛选阶段行为（不可为 null）
     */
    protected abstract Consumer<PipelineContext> getCandidateSelector();

    /**
     * 构建核心求值阶段：对候选规则逐条求值或执行.
     * <p>
     * 求值细节（维度取值匹配、策略路由 Java 实现或脚本）由子类决定；
     * 逐条结果通常经 {@code context.getStorage()} 传递给结果聚合阶段，
     * 单规则失败按 check 类语义记入结果而非抛出异常
     * （存储键由所选实现约定）.
     *
     * @return 核心求值阶段行为（不可为 null）
     */
    protected abstract Consumer<PipelineContext> getRuleExecutor();

    /**
     * 构建结果聚合阶段：将逐条求值的产物聚合为单一校验结果.
     * <p>
     * 聚合语义（规则间组合、拦截效果、提示语渲染）由子类决定，
     * 最终结果置入上下文的输出槽位（{@code setRawOutput}），
     * 经基座后处理阶段透传后由结果抽取器返回.
     *
     * @return 结果聚合阶段行为（不可为 null）
     */
    protected abstract Consumer<PipelineContext> getResultAggregator();
    // endregion ======== 子类的四个实现口子 ========

}
