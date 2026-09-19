/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.engine.pipeline;

import kunlun.core.Engine;

/**
 * 管道家族的面：以"具名阶段的执行管道"为执行器官的复合型引擎.
 * <p>
 * 引擎四要素在本家族的落地（根契约见 {@link kunlun.core.Engine}）：
 * <ul>
 *     <li>声明 → {@link kunlun.engine.pipeline.PipelineConfig}：一个场景码对应一份，按场景码装载；</li>
 *     <li>上下文 → {@link kunlun.engine.pipeline.PipelineContext}：承载一次执行的全程数据；</li>
 *     <li>执行 → {@link kunlun.engine.pipeline.Pipeline}：持有依序执行的有名阶段，
 *     可按键名做替换、前插、后插与移除；</li>
 *     <li>管理 → 由引擎实现自定：config-loader 阶段按场景码装载对应的规则.</li>
 * </ul>
 * <p>
 * 管道的维护不设具名 setter，统一经 {@link #getPipeline()} 拿到管道后调用其维护方法：
 * <pre>
 *     Pipeline pipeline = engine.getPipeline();
 *     pipeline.insertBeforeStage(Pipeline.STAGE_ENGINE_EXECUTOR, "cache", cacheStage);
 *     pipeline.replaceStage(Pipeline.STAGE_RESULT_AGGREGATOR, myAggregator);
 * </pre>
 * <p>
 * 继承层次与"生长"方式（家族仅为示意，本库只冻结根契约与本接口）：
 * <pre>
 *     Strategy
 *       ▲
 *     Engine（根契约，kunlun.core）
 *       ▲
 *     PipelineEngine（本接口）
 *       ▲
 *     └─ AbstractPipelineEngine（执行模板骨架，base 子包，内附管道装配注释模板）
 * </pre>
 * 生长出管道家族的新引擎时，继承骨架、实现 getPipeline 即可，
 * 无需改动本接口与 {@link kunlun.core.Engine} 的任何代码.
 * 家族内的层次：基座实现在 {@code base} 子包，
 * 基于骨架生长的具体引擎（如规则引擎、风控引擎）以本包的子包生长（如 {@code rule}、{@code risk}）.
 *
 * @author Kahle
 */
public interface PipelineEngine extends Engine {

    /**
     * 获取引擎执行管道：结构的唯一出处，由引擎实现提供.
     * <p>
     * 管道的维护（追加、前插、后插、替换与移除阶段）经由此入口进行，
     * 变更只影响取到的实例；实现策略全由实现方决定——
     * 不可变用法每次构建新的（构造逻辑固定），
     * 复杂用法查库装配加缓存（热更失效），骨架内附注释模板可参考.
     *
     * @return 引擎执行管道
     */
    Pipeline getPipeline();

}
