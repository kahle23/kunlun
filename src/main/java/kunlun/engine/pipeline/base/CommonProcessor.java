/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.engine.pipeline.base;

import kunlun.core.function.Consumer;
import kunlun.data.bean.BeanUtil;
import kunlun.engine.pipeline.PipelineContext;
import kunlun.polyglot.PolyglotUtil;
import kunlun.util.StrUtil;

/**
 * 通用处理器：骨架默认管道中预处理/后处理阶段的默认实现.
 * <p>
 * 参照 {@code kunlun.action.invoke.InvokeAction.CommonConverter} 复刻：
 * 预处理与后处理的默认逻辑同置一类，由构造参数选择走哪一半，
 * 各半逻辑拆在 protected 方法（{@link #doPreprocess} 与 {@link #doPostprocess}）中，
 * 可单独覆写；若默认逻辑不合用，大概率是整体重写，替换对应阶段即可.
 * <p>
 * 配置（{@link kunlun.engine.pipeline.base.AbstractPipelineConfig}）声明了脚本时，
 * 以上下文（Bean 转 Map）为求值环境执行脚本得到加工结果
 * （转换、校验、赋值等皆可）；未声明脚本时退化为透传.
 * <p>
 * 要求引擎配置继承 {@link kunlun.engine.pipeline.base.AbstractPipelineConfig}，不适用时可经
 * {@code getPipeline()} 替换或移除对应阶段.
 *
 * @author Kahle
 */
public class CommonProcessor implements Consumer<PipelineContext> {
    /**
     * 是否做预处理（为 false 时做后处理）.
     */
    private final boolean preprocess;

    public CommonProcessor(boolean preprocess) {

        this.preprocess = preprocess;
    }

    /**
     * 取引擎配置，并要求其为 {@link kunlun.engine.pipeline.base.AbstractPipelineConfig} 类型.
     *
     * @param context 管道引擎上下文
     * @return 引擎配置（已确保类型）
     * @throws IllegalStateException 引擎配置不是 AbstractPipelineConfig 类型时
     */
    protected AbstractPipelineConfig requireConfig(PipelineContext context) {
        Object config = context.getConfig();
        if (!(config instanceof AbstractPipelineConfig)) {
            throw new IllegalStateException(
                    "The CommonProcessor requires the config extends AbstractPipelineConfig. ");
        }
        return (AbstractPipelineConfig) config;
    }

    /**
     * 预处理：声明了预处理脚本则求值加工，否则透传.
     *
     * @param context 管道引擎上下文
     */
    protected void doPreprocess(PipelineContext context) {
        AbstractPipelineConfig config = requireConfig(context);
        String preprocessScript = config.getPreprocessScript();
        if (StrUtil.isNotBlank(preprocessScript)) {
            // 以上下文为求值环境执行预处理脚本.
            context.setConvertedInput(PolyglotUtil.eval(
                    config.getScriptEngineName(), preprocessScript, BeanUtil.beanToMap(context)));
        }
        else {
            // 未声明脚本时透传.
            context.setConvertedInput(context.getRawInput());
        }
    }

    /**
     * 后处理：声明了后处理脚本则求值加工，否则透传.
     *
     * @param context 管道引擎上下文
     */
    protected void doPostprocess(PipelineContext context) {
        AbstractPipelineConfig config = requireConfig(context);
        String postprocessScript = config.getPostprocessScript();
        if (StrUtil.isNotBlank(postprocessScript)) {
            // 以上下文为求值环境执行后处理脚本.
            context.setConvertedOutput(PolyglotUtil.eval(
                    config.getScriptEngineName(), postprocessScript, BeanUtil.beanToMap(context)));
        }
        else {
            // 未声明脚本时透传.
            context.setConvertedOutput(context.getRawOutput());
        }
    }

    @Override
    public void accept(PipelineContext context) {
        if (preprocess) { doPreprocess(context); }
        else { doPostprocess(context); }
    }

}
