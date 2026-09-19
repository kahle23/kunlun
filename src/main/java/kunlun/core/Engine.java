/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.core;

/**
 * 为 engine 提供最高层次的抽象.<br />
 * <p>
 * 什么是 engine（引擎）？
 * 引擎是解释"声明"的运行时：行为不写死在调用代码里，
 * 而是描述在可管理的数据（声明）中——引擎装载声明、结合上下文执行解释、产出结果，
 * 换行为改声明（数据），不改代码.
 * 脚本引擎（声明 = 脚本定义）、规则引擎（声明 = 规则集）、
 * 流程引擎（声明 = 流程定义）、编排引擎（声明 = 编排规则）皆是此结构的同构实现，
 * JDK 内置的 {@code javax.script.ScriptEngine} 即是一例：
 * 声明 = 脚本文本，上下文 = Bindings，管理 = ScriptEngineManager 的注册与发现.
 * <p>
 * 引擎的解剖学（四要素，概念定义；类型由各家族落地）：
 * <ul>
 *     <li>声明（declaration）：行为的载体，数据形态，如各家族的 Config；</li>
 *     <li>上下文（context）：执行期的输入与中间产物，如各家族的 Context；</li>
 *     <li>执行（execution）：解释"声明 × 上下文 → 结果"，即本接口的 execute 方法；</li>
 *     <li>管理（management）：声明的装载、注册与版本，由各家族的配置装载口子承担.</li>
 * </ul>
 * 判断一个场景是否需要引擎的口诀：行为要变化时，是改数据还是改代码？
 * 改数据（规则、脚本、公式、编排）→ 引擎场景；改代码 → 工具/服务.
 * <p>
 * 本接口是引擎家族的根契约：execute 继承自 {@link Strategy}，
 * 其中 strategy 段即场景码（声明的标识）、input 与 arguments 即上下文.
 * 引擎实例本身即 Strategy，可被任意持有 Strategy 的调用体系直接持有.
 * <p>
 * 家族的生长约定：每个引擎家族生长为 {@code kunlun.engine} 下的一个子包，
 * 面、词汇与机制同包——当前第一支为管道家族（{@code kunlun.engine.pipeline}），
 * 未来的 script、rule 等家族照此生长，本接口零改动.
 * 将来引擎出现跨家族的通用上下文等共享语义时，以本接口的内部类形式挂靠，
 * core 层不引入没有明确使用场景的类型.
 *
 * @author Kahle
 */
public interface Engine extends Strategy {
}
