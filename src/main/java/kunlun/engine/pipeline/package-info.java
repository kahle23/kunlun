/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

/**
 * 管道家族包：以"具名阶段的执行管道"为执行器官的引擎家族
 * （家族生长约定：每个引擎家族生长为 {@code kunlun.engine} 下的一个子包，
 * 面、词汇与机制同包，根契约见 {@code kunlun.core.Engine}）.
 * <p>
 * 组成（对应引擎四要素的管道家族落地）：
 * <ul>
 *   <li>{@code PipelineEngine}：家族的面，管道维护统一经 {@code getPipeline()} 进行；</li>
 *   <li>{@code Pipeline}：管道（内嵌 {@code Stage} 阶段条目扩展点，
 *       STAGE_* 八锚点常量在 {@code Pipeline} 上）；</li>
 *   <li>{@code PipelineContext}：管道引擎上下文（承载一次执行的全程数据）；</li>
 *   <li>{@code PipelineConfig}：管道引擎配置（声明载体，一场景码一份）；</li>
 *   <li>{@code base}：基座——{@code AbstractPipelineEngine}（执行模板骨架，参照
 *       {@code kunlun.action.invoke.AbstractInvokeAction} 的形态，定制口子：
 *       实现 {@code getPipeline()}（必须，参考类内注释模板——核心是 config-loader
 *       装载场景码对应的规则；管道策略——每次新建 / 查库 + 缓存——全由实现方决定）、
 *       日志与结果抽取 setter）、
 *       {@code DefaultPipelineContext}（上下文默认实现）、
 *       {@code AbstractPipelineConfig}（配置通用基类，复刻自
 *       {@code InvokeAction.AbstractConfig}）、{@code CommonValidator}
 *       （按配置的校验集合校验）、{@code CommonProcessor}
 *       （预处理/后处理脚本求值，无脚本即透传）、
 *       {@code DefaultPipeline}（默认管道，阶段条目 {@code Pipeline.Stage}
 *       可继承扩展）.</li>
 * </ul>
 * 注释模板的管道（锚点顺序即 STAGE_* 常量的声明顺序）：
 * <pre>
 *   config-loader → preprocess-validator → preprocessor → candidate-selector
 *   → engine-executor → result-aggregator → postprocessor → postprocess-validator
 * </pre>
 * 家族内的层次：{@code base} 为基座，基于骨架生长的具体引擎
 * （规则引擎、风控引擎等）以本包的子包生长（如 {@code rule}、{@code risk}）.
 * 业务的管道引擎继承骨架即可，本包通常不直接暴露给调用方.
 */
package kunlun.engine.pipeline;
