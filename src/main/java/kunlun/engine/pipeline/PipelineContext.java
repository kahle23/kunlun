/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.engine.pipeline;

/**
 * 管道引擎上下文的抽象声明.
 * <p>
 * 承载一次管道引擎执行的全程数据，流转方向固定为：
 * rawInput → convertedInput →（候选筛选与逐条求值）→ rawOutput → convertedOutput.
 * <p>
 * 除四个标准数据槽位外，各阶段的中间产物（如候选清单、逐条求值记录）
 * 直接经 {@code getStorage()} 读写.
 *
 * @author Kahle
 */
public interface PipelineContext extends kunlun.core.Context {

    /**
     * 获取场景码.
     *
     * @return 场景码
     */
    String getSceneCode();

    /**
     * 获取执行时的其他相关参数.
     *
     * @return 其他相关参数
     */
    Object[] getArguments();

    /**
     * 获取原始输入数据.
     *
     * @return 原始输入数据
     */
    Object getRawInput();

    /**
     * 获取引擎配置.
     *
     * @return 引擎配置
     */
    PipelineConfig getConfig();

    /**
     * 设置引擎配置.
     *
     * @param config 引擎配置
     */
    void setConfig(PipelineConfig config);

    /**
     * 获取转换后的输入数据.
     *
     * @return 转换后的输入数据
     */
    Object getConvertedInput();

    /**
     * 设置转换后的输入数据.
     *
     * @param input 转换后的输入数据
     */
    void setConvertedInput(Object input);

    /**
     * 获取原始输出数据（求值与聚合的产物，聚合前）.
     *
     * @return 原始输出数据
     */
    Object getRawOutput();

    /**
     * 设置原始输出数据.
     *
     * @param output 原始输出数据
     */
    void setRawOutput(Object output);

    /**
     * 获取转换后的输出数据.
     *
     * @return 转换后的输出数据
     */
    Object getConvertedOutput();

    /**
     * 设置转换后的输出数据.
     *
     * @param output 转换后的输出数据
     */
    void setConvertedOutput(Object output);

    /**
     * 获取错误信息对象.
     *
     * @return 错误信息对象
     */
    Throwable getError();

    /**
     * 设置错误信息对象.
     *
     * @param th 错误信息对象
     */
    void setError(Throwable th);

}
