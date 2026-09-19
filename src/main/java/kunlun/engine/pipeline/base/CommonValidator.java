/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.engine.pipeline.base;

import kunlun.core.function.Consumer;
import kunlun.data.bean.BeanUtil;
import kunlun.data.json.JsonUtil;
import kunlun.data.validation.ValidatorUtil;
import kunlun.data.validation.support.ValidationConfig;
import kunlun.engine.pipeline.PipelineContext;
import kunlun.polyglot.PolyglotUtil;
import kunlun.util.CollUtil;
import kunlun.util.StrUtil;

import java.util.Arrays;
import java.util.Collection;

/**
 * 通用校验器：骨架默认管道中预处理校验/后处理校验阶段的默认实现.
 * <p>
 * 参照 {@code kunlun.action.invoke.InvokeAction.CommonValidator} 复刻：
 * 两半校验逻辑同置一类，由构造参数选择走哪一半，
 * 各半逻辑拆在 protected 方法（{@link #doPreprocessValidate} 与
 * {@link #doPostprocessValidate}）中，可单独覆写.
 * 逐条求值校验表达式并校验，未声明校验配置即无操作；
 * 上下文以 Bean 转 Map 后作为表达式的求值环境.
 * <p>
 * 对校验配置字符串的解析约定是本实现自己的选择——按
 * {@code kunlun.data.validation.support.ValidationConfig} 的 JSON 数组解析；
 * 其他形态（DSL、表达式串）由替换本阶段的实现自行解析.
 * <p>
 * 要求引擎配置继承 {@link kunlun.engine.pipeline.base.AbstractPipelineConfig}，不适用时可经
 * {@code getPipeline()} 替换或移除对应校验阶段.
 *
 * @author Kahle
 */
public class CommonValidator implements Consumer<PipelineContext> {
    /**
     * 是否做预处理校验（为 false 时做后处理校验）.
     */
    private final boolean preprocess;

    public CommonValidator(boolean preprocess) {

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
                    "The CommonValidator requires the config extends AbstractPipelineConfig. ");
        }
        return (AbstractPipelineConfig) config;
    }

    protected void throwValidationException(boolean validate, String message) {
        if (!validate) {
            throw new IllegalArgumentException(message);
        }
    }

    protected void validate(String scriptName, Collection<ValidationConfig> configs, Object data) {
        if (CollUtil.isEmpty(configs)) { return; }
        data = BeanUtil.beanToMap(data);
        for (ValidationConfig config : configs) {
            String expression = config.getExpression();
            String validator = config.getValidator();
            String message = config.getMessage();
            if (StrUtil.isBlank(expression)) { continue; }
            Object eval = PolyglotUtil.eval(scriptName, expression, data);
            boolean passed = ValidatorUtil.validateToBoolean(validator, eval);
            throwValidationException(passed, message);
        }
    }

    /**
     * 预处理校验（入参校验）：解析预处理校验配置并逐条校验，未声明即无操作.
     *
     * @param context 管道引擎上下文
     */
    protected void doPreprocessValidate(PipelineContext context) {
        AbstractPipelineConfig config = requireConfig(context);
        // 本实现的解析约定：校验配置字符串 = ValidationConfig 的 JSON 数组.
        String validationConfigs = config.getPreprocessValidationConfigs();
        if (StrUtil.isBlank(validationConfigs)) { return; }
        ValidationConfig[] configs = JsonUtil.parseObject(validationConfigs, ValidationConfig[].class);
        validate(config.getScriptEngineName(), Arrays.asList(configs), context);
    }

    /**
     * 后处理校验（出参校验）：解析后处理校验配置并逐条校验，未声明即无操作.
     *
     * @param context 管道引擎上下文
     */
    protected void doPostprocessValidate(PipelineContext context) {
        AbstractPipelineConfig config = requireConfig(context);
        // 本实现的解析约定：校验配置字符串 = ValidationConfig 的 JSON 数组.
        String validationConfigs = config.getPostprocessValidationConfigs();
        if (StrUtil.isBlank(validationConfigs)) { return; }
        ValidationConfig[] configs = JsonUtil.parseObject(validationConfigs, ValidationConfig[].class);
        validate(config.getScriptEngineName(), Arrays.asList(configs), context);
    }

    @Override
    public void accept(PipelineContext context) {
        if (preprocess) { doPreprocessValidate(context); }
        else { doPostprocessValidate(context); }
    }

}
