/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.core;

import kunlun.core.function.Consumer;

import java.util.List;
import java.util.Map;

/**
 * 为 engine 提供最高层次的抽象.<br />
 * <p>
 * 什么是 engine？
 * 表示由场景码（scene code）驱动、对一组"规则"进行求值或执行，
 * 并将求值结果聚合为单一结果的引擎，如脚本引擎、规则引擎、风控引擎、计算引擎等.
 * <p>
 * engine 是 strategy 的语义升级：{@link Strategy#execute} 的 strategy 段被解释为场景码，
 * 一次执行不再是"单点调用"，而是"配置装载 → 候选筛选 → 逐条求值 → 结果聚合".
 * engine 只依赖 {@link Strategy} 而不依赖 Action，
 * 引擎实例本身即 Strategy，可被任意调用体系直接持有；
 * 需要接入 ActionManager 与 ActionUtil 的既有路由时，由装配方做一次 action 适配即可
 * （command 的 strategy 段即场景码，如 "score-engine.order-total" 的 "order-total"）.
 * <p>
 * 继承层次与"生长"方式（家族仅为示意，本库只冻结顶层）：
 * <pre>
 *     Strategy
 *       ▲
 *     Engine（本接口）
 *       ▲
 *     ├─ ScriptEngine    执行型: 场景码 → 脚本定义 → 受管执行
 *     ├─ RuleEngine      决策型: 场景码 → 规则集   → 决策结果
 *     │    └─ RiskEngine 风控细化: 决策结果 → 风险决策（等级与处置）
 *     └─ ComputeEngine   计算型: 场景码 → 公式集   → 计算结果
 * </pre>
 * 生长出一个新的引擎家族只需三步，核心零改动：
 * <ol>
 * <li>词汇层：定义 XxxEngine extends Engine，及其 Config 与结果模型等词汇接口，
 * 中间产物以 {@link ContextKey} 声明（键即类型契约）；</li>
 * <li>骨架层（可选）：定义 AbstractXxxEngine extends kunlun.engine.AbstractEngine，
 * 特化个别阶段插槽，或经 {@link Pipeline} 增删插排阶段；</li>
 * <li>落地层（业务侧）：提供 Config 子类与各阶段实现，装配后交由调用体系持有即接入.</li>
 * </ol>
 *
 * @author Kahle
 */
public interface Engine extends Strategy {

    /**
     * 引擎配置的抽象声明.<br />
     * <p>
     * 一个场景码对应一份引擎配置，配置是"场景码背后那组规则"的声明载体，
     * 由各引擎家族按需扩展（如规则集、公式集、脚本定义），
     * 通常在执行期的 config-loader 阶段按场景码装载并置入 {@link EngineContext}，
     * 配置的来源由 {@link kunlun.engine.ConfigRepository} 声明.
     *
     * @author Kahle
     */
    interface EngineConfig {

        /**
         * 获取场景码.
         * @return 场景码
         */
        String getSceneCode();

        /**
         * 获取描述信息.
         * @return 描述信息
         */
        String getDescription();

        /**
         * 获取是否启用（为 Null 时按启用处理，由实现方约定）.
         * @return 是否启用
         */
        Boolean getEnabled();

        /**
         * 获取配置版本.
         * @return 配置版本
         */
        String getVersion();

        /**
         * 获取其他配置.
         * @return 其他配置
         */
        Map<String, Object> getOtherConfigs();

    }

    /**
     * 引擎上下文的抽象声明.<br />
     * <p>
     * 承载一次引擎执行的全程数据，流转方向固定为：
     * rawInput → convertedInput →（候选筛选与逐条求值）→ rawOutput → convertedOutput.
     * <p>
     * 除四个标准数据槽位外，各阶段的中间产物（如候选清单、逐条求值记录）
     * 经 {@link ContextKey} 走 {@link #get} 与 {@link #put} 类型化读写，
     * 底层存储复用 {@link Context#getStorage()}.
     *
     * @author Kahle
     */
    interface EngineContext extends Context {

        /**
         * 获取场景码.
         * @return 场景码
         */
        String getSceneCode();

        /**
         * 获取执行时的其他相关参数.
         * @return 其他相关参数
         */
        Object[] getArguments();

        /**
         * 获取引擎配置.
         * @return 引擎配置
         */
        EngineConfig getConfig();

        /**
         * 设置引擎配置.
         * @param config 引擎配置
         */
        void setConfig(EngineConfig config);

        /**
         * 获取原始输入数据.
         * @return 原始输入数据
         */
        Object getRawInput();

        /**
         * 获取转换后的输入数据.
         * @return 转换后的输入数据
         */
        Object getConvertedInput();

        /**
         * 设置转换后的输入数据.
         * @param input 转换后的输入数据
         */
        void setConvertedInput(Object input);

        /**
         * 获取原始输出数据（求值与聚合的产物，聚合前）.
         * @return 原始输出数据
         */
        Object getRawOutput();

        /**
         * 设置原始输出数据.
         * @param output 原始输出数据
         */
        void setRawOutput(Object output);

        /**
         * 获取转换后的输出数据.
         * @return 转换后的输出数据
         */
        Object getConvertedOutput();

        /**
         * 设置转换后的输出数据.
         * @param output 转换后的输出数据
         */
        void setConvertedOutput(Object output);

        /**
         * 以类型化键读取中间产物（不存在时为 Null）.
         * @param key 类型化键
         * @param <T> 中间产物的类型
         * @return 键对应的中间产物
         */
        <T> T get(ContextKey<T> key);

        /**
         * 以类型化键写入中间产物，返回旧值.
         * @param key 类型化键
         * @param value 待写入的中间产物
         * @param <T> 中间产物的类型
         * @return 旧值（不存在时为 Null）
         */
        <T> T put(ContextKey<T> key, T value);

        /**
         * 获取错误信息对象.
         * @return 错误信息对象
         */
        Throwable getError();

        /**
         * 设置错误信息对象.
         * @param th 错误信息对象
         */
        void setError(Throwable th);

    }

    /**
     * 引擎管道中的具名阶段.<br />
     * <p>
     * 阶段 = 键名 + 行为：键名是 {@link Pipeline} 中定位锚点（替换、前插、后插、移除）的依据，
     * 行为是作用于 {@link EngineContext} 的 {@link Consumer}.
     * <p>
     * 本接口持有的 NAME_* 常量是 {@link kunlun.engine.AbstractEngine} 默认管道的八个锚点名，
     * 定制管道时应以这些常量（而非裸字符串）定位锚点：
     * <pre>
     *     pipeline.addStageBefore(Stage.NAME_ENGINE_EXECUTOR, cacheStage);
     *     pipeline.replaceStage(Stage.NAME_RESULT_AGGREGATOR, myAggregator);
     * </pre>
     *
     * @author Kahle
     */
    interface Stage extends Consumer<EngineContext> {
        /**
         * 锚点名：配置装载阶段，按场景码装载引擎配置.
         * */
        String NAME_CONFIG_LOADER = "config-loader";
        /**
         * 锚点名：输入校验阶段，按配置校验输入数据.
         * */
        String NAME_INPUT_VALIDATOR = "input-validator";
        /**
         * 锚点名：输入转换阶段，将原始输入转换为求值所需的输入.
         * */
        String NAME_INPUT_CONVERTER = "input-converter";
        /**
         * 锚点名：候选筛选阶段，从配置声明的一组规则中筛出本次参与的候选.
         * */
        String NAME_CANDIDATE_SELECTOR = "candidate-selector";
        /**
         * 锚点名：核心求值阶段，对候选规则逐条求值或执行.
         * */
        String NAME_ENGINE_EXECUTOR = "engine-executor";
        /**
         * 锚点名：结果聚合阶段，将逐条求值的产物聚合为单一结果.
         * */
        String NAME_RESULT_AGGREGATOR = "result-aggregator";
        /**
         * 锚点名：输出校验阶段，按配置校验输出数据.
         * */
        String NAME_OUTPUT_VALIDATOR = "output-validator";
        /**
         * 锚点名：输出转换阶段，将聚合结果转换为最终输出.
         * */
        String NAME_OUTPUT_CONVERTER = "output-converter";

        /**
         * 获取键名.
         * @return 键名
         */
        String getName();

    }

    /**
     * 引擎执行管道的抽象声明.<br />
     * <p>
     * 有序的 {@link Stage} 容器：阶段按加入顺序依序执行，
     * 支持以键名为锚点做追加、前插、后插、替换与移除——
     * 引擎的"生长"因此在"替换阶段"与"增删阶段"两个维度上都是自由的.
     * <p>
     * 变更类方法（add / replace / remove）在键名或锚点不存在时抛出 IllegalArgumentException，
     * 让装配错误在装配期暴露而不是执行期；
     * 查询类方法（getStage）找不到时返回 Null，getStages 返回快照副本.
     * <p>
     * 实现应保证变更与执行的并发安全（参照 SimpleActionManager 的注册与执行可并发）.
     *
     * @author Kahle
     */
    interface Pipeline {

        /**
         * 追加阶段到管道末尾.
         * @param stage 待追加的阶段
         */
        void addStage(Stage stage);

        /**
         * 在锚点阶段之前插入阶段.
         * @param anchorName 锚点阶段的键名
         * @param stage 待插入的阶段
         */
        void addStageBefore(String anchorName, Stage stage);

        /**
         * 在锚点阶段之后插入阶段.
         * @param anchorName 锚点阶段的键名
         * @param stage 待插入的阶段
         */
        void addStageAfter(String anchorName, Stage stage);

        /**
         * 替换同键名的阶段（新阶段的键名须与 name 一致）.
         * @param name 待替换阶段的键名
         * @param stage 替换后的阶段
         * @return 被替换的旧阶段
         */
        Stage replaceStage(String name, Stage stage);

        /**
         * 移除指定键名的阶段.
         * @param name 待移除阶段的键名
         * @return 被移除的阶段
         */
        Stage removeStage(String name);

        /**
         * 获取指定键名的阶段（找不到时为 Null）.
         * @param name 阶段的键名
         * @return 键名对应的阶段
         */
        Stage getStage(String name);

        /**
         * 获取全部阶段的快照副本（不可变，按执行顺序排列）.
         * @return 全部阶段的快照副本
         */
        List<Stage> getStages();

        /**
         * 依序执行管道中的全部阶段.
         * @param context 引擎上下文
         */
        void execute(EngineContext context);

    }

}
