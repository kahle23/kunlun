/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.engine.pipeline;

import kunlun.core.function.Consumer;
import kunlun.util.Assert;

import java.util.List;

/**
 * 引擎执行管道的抽象声明.
 * <p>
 * 有序阶段的容器：阶段 = 键名 + 行为（{@code Consumer}），按加入顺序依序执行，
 * 支持以键名为锚点做追加、前插、后插、替换与移除——
 * 引擎的"生长"因此在"替换阶段"与"增删阶段"两个维度上都是自由的.
 * 键名与行为的配对经 {@link kunlun.engine.pipeline.Pipeline.Stage} 条目承载（管道实现的内部簿记），
 * 阶段行为就是普通的 Consumer，不与任何键名绑定（同一行为可落位于不同键名）.
 * <p>
 * 定制管道时应以 STAGE_* 常量（而非裸字符串）定位锚点：
 * <pre>
 *     Pipeline pipeline = engine.getPipeline();
 *     pipeline.insertBeforeStage(Pipeline.STAGE_ENGINE_EXECUTOR, "cache", cacheStage);
 *     pipeline.replaceStage(Pipeline.STAGE_RESULT_AGGREGATOR, myAggregator);
 * </pre>
 * <p>
 * 变更类方法的防错语义：追加在键名已存在时抛出 IllegalArgumentException，
 * 插入 / 替换 / 移除在锚点（键名）不存在时抛出，
 * 让装配错误在装配期暴露而不是执行期；getStageNames 返回快照副本.
 * <p>
 * 实现应保证变更与执行的并发安全.
 * <p>
 * 管道是从声明派生的产物：不可变实现每次构建新的（构造逻辑固定），
 * 热更实现查库装配加缓存，全由实现方决定；对某次取到的管道实例的变更只影响该实例，
 * 按输入走不同流程应在阶段内部读取上下文分支来表达.
 *
 * @author Kahle
 */
public interface Pipeline {

    // region ======== 阶段的维护与执行 ========
    /**
     * 以"键名 + 行为"追加具名阶段到管道末尾.
     *
     * @param name 阶段的键名
     * @param consumer 阶段行为
     */
    void appendStage(String name, Consumer<PipelineContext> consumer);

    /**
     * 在锚点阶段之前插入具名阶段.
     *
     * @param anchorName 锚点阶段的键名
     * @param name 新阶段的键名
     * @param consumer 阶段行为
     */
    void insertBeforeStage(String anchorName, String name, Consumer<PipelineContext> consumer);

    /**
     * 在锚点阶段之后插入具名阶段.
     *
     * @param anchorName 锚点阶段的键名
     * @param name 新阶段的键名
     * @param consumer 阶段行为
     */
    void insertAfterStage(String anchorName, String name, Consumer<PipelineContext> consumer);

    /**
     * 替换指定键名的阶段（新行为以该键名落位）.
     *
     * @param name 阶段的键名
     * @param consumer 替换后的阶段行为
     */
    void replaceStage(String name, Consumer<PipelineContext> consumer);

    /**
     * 移除指定键名的阶段.
     *
     * @param name 阶段的键名
     */
    void removeStage(String name);

    /**
     * 获取全部阶段键名的快照副本（不可变，按执行顺序排列）.
     *
     * @return 全部阶段键名的快照副本
     */
    List<String> getStageNames();

    /**
     * 依序执行管道中的全部阶段.
     *
     * @param context 管道引擎上下文
     */
    void execute(PipelineContext context);
    // endregion ======== 阶段的维护与执行 ========


    // region ======== 阶段条目 ========
    /**
     * 管道阶段的条目：键名与行为的配对载体.
     * <p>
     * 管道实现内部的簿记结构；实现方可继承扩展（如携带额外元数据），
     * 也可不覆写直接使用——接口成员只能公开，
     * 此处以公开嵌套类充当扩展点，对不关心的调用方无感.
     *
     * @author Kahle
     */
    class Stage {
        /**
         * 阶段行为.
         */
        private final Consumer<PipelineContext> consumer;
        /**
         * 键名（锚点寻址与查重的依据）.
         */
        private final String name;

        /**
         * 以键名与行为构建阶段条目（入参判空）.
         *
         * @param name 键名
         * @param consumer 阶段行为
         */
        public Stage(String name, Consumer<PipelineContext> consumer) {
            this.consumer = Assert.notNull(consumer, "Parameter \"consumer\" must not null. ");
            this.name = Assert.notBlank(name, "Parameter \"name\" must not blank. ");
        }

        /**
         * 获取键名.
         *
         * @return 键名
         */
        public String getName() {

            return name;
        }

        /**
         * 获取阶段行为.
         *
         * @return 阶段行为
         */
        public Consumer<PipelineContext> getConsumer() {

            return consumer;
        }
    }
    // endregion ======== 阶段条目 ========


    // region ======== 锚点常量：按默认管道的执行顺序声明 ========
    /**
     * 锚点名：配置装载阶段，按场景码装载引擎配置.
     */
    String STAGE_CONFIG_LOADER = "config-loader";
    /**
     * 锚点名：预处理校验阶段，核心求值前校验输入数据（入参校验）.
     */
    String STAGE_PREPROCESS_VALIDATOR = "preprocess-validator";
    /**
     * 锚点名：预处理阶段，核心求值前对输入做加工（转换、校验、赋值等）.
     */
    String STAGE_PREPROCESSOR = "preprocessor";
    /**
     * 锚点名：候选筛选阶段，从配置声明的一组规则中筛出本次参与的候选.
     */
    String STAGE_CANDIDATE_SELECTOR = "candidate-selector";
    /**
     * 锚点名：核心求值阶段，对候选规则逐条求值或执行.
     */
    String STAGE_ENGINE_EXECUTOR = "engine-executor";
    /**
     * 锚点名：结果聚合阶段，将逐条求值的产物聚合为单一结果.
     */
    String STAGE_RESULT_AGGREGATOR = "result-aggregator";
    /**
     * 锚点名：后处理阶段，核心求值后对输出做加工（转换、格式化、清洗等）.
     */
    String STAGE_POSTPROCESSOR = "postprocessor";
    /**
     * 锚点名：后处理校验阶段，后处理完成后校验最终输出（出参校验）.
     */
    String STAGE_POSTPROCESS_VALIDATOR = "postprocess-validator";
    // endregion ======== 锚点常量：按默认管道的执行顺序声明 ========

}
