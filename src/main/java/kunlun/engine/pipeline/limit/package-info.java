/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

/**
 * 限制引擎家族包（kunlun 侧）：执行骨架.
 * <p>
 * 只冻结"执行框架"——八锚点装配骨架与四个 getXxx 口子
 * （配置装载、候选筛选、核心求值、结果聚合）；
 * 限制领域的共享词汇（场景壳 → 规则 → 维度阈值的数据形态）与通用求值实现
 * （启停与优先级筛选、维度取值匹配、组合与效果裁决、提示语占位符渲染）
 * 由下游配套库的 limit 家族承载（如 baibao 的 {@code baibao.engine.pipeline.limit}，
 * 其 model / support 子包与 {@code LimitStrategy} 契约）——
 * 业务项目继承本骨架、实现四个口子（核心三阶段直接装配通用实现）即得一个限制引擎.
 * <p>
 * 组成：{@code AbstractLimitEngine}（骨架实现，按基座注释模板装配八锚点：
 * 配置装载在 config-loader 阶段调 {@code getConfigLoader}，
 * 入参/出参校验与预/后处理复用基座 CommonValidator / CommonProcessor，
 * 核心三阶段调 {@code getCandidateSelector} / {@code getRuleExecutor} /
 * {@code getResultAggregator}）.
 * <p>
 * 执行时序（锚点顺序即 {@code Pipeline} 的 STAGE_* 常量声明顺序）：
 * <pre>
 *   config-loader → preprocess-validator → preprocessor → candidate-selector
 *   → engine-executor → result-aggregator → postprocessor → postprocess-validator
 * </pre>
 * 限制校验是 check 类语义：超限以结果对象表达、不以异常表达；
 * 配置装载与执行留痕（写执行日志表）归下游，留痕经基座的 logRecorder 口子挂载
 * （finally 中执行，从上下文 storage 读逐规则结果）.
 */
package kunlun.engine.pipeline.limit;
