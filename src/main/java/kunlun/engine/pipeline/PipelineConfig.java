/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.engine.pipeline;

import java.util.Map;

/**
 * 管道家族的引擎配置（声明载体）.
 * <p>
 * 一个场景码对应一份引擎配置，配置是"场景码背后那组规则"的声明载体，
 * 由各管道引擎按需扩展（如规则集、公式集、脚本定义），
 * 通常在执行期的 config-loader 阶段按场景码装载并置入 {@link kunlun.engine.pipeline.PipelineContext}，
 * 声明的来源由引擎实现决定.
 *
 * @author Kahle
 */
public interface PipelineConfig {

    /**
     * 获取场景码.
     * <p>
     * 场景码是声明（配置）的标识，也是 execute 的 strategy 段.
     * 之所以叫"场景"码：引擎实例通常只有一个，而业务场景有许多——
     * 一个场景码对应一份声明（一组规则、公式或脚本），
     * 调用方以场景码说明"本次执行发生在哪个业务场景"，
     * 引擎按场景码装载对应的声明：场景变则声明变，引擎不变.
     *
     * @return 场景码
     */
    String getSceneCode();

    /**
     * 获取描述信息.
     *
     * @return 描述信息
     */
    String getDescription();

    /**
     * 获取属性表（其他配置）.
     *
     * @return 属性表
     */
    Map<String, Object> getAttributes();

}
