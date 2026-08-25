/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.engine.pipeline.base;

import kunlun.engine.pipeline.PipelineConfig;

import java.io.Serializable;
import java.util.Map;

/**
 * 管道引擎配置的通用基类.
 * <p>
 * 为 {@link kunlun.engine.pipeline.PipelineConfig} 提供字段化的默认载体（参照
 * {@code kunlun.action.invoke.InvokeAction.AbstractConfig} 的形态）：
 * 除声明通用属性（场景码、描述、属性表）外，
 * 还携带脚本与校验相关的公共字段，供 {@link kunlun.engine.pipeline.base.CommonValidator} 与
 * {@link kunlun.engine.pipeline.base.CommonProcessor} 等默认阶段消费；
 * 各管道引擎的配置类继承本类再补充自己的声明字段（如规则集、公式集）.
 * <p>
 * 字段按默认管道的执行阶段分组排列：
 * <pre>
 *     config-loader → preprocess-validator → preprocessor → candidate-selector
 *     → engine-executor → result-aggregator → postprocessor → postprocess-validator
 * </pre>
 * 即：装载声明 → 入参校验 → 预处理 → 核心求值（候选筛选、逐条求值、结果聚合）
 * → 后处理 → 出参校验.
 *
 * @author Kahle
 */
public abstract class AbstractPipelineConfig implements PipelineConfig, Serializable {
    /**
     * 场景码（声明的标识）.
     */
    private String sceneCode;
    /**
     * 描述信息.
     */
    private String description;
    /**
     * 脚本引擎名称（输入/输出转换脚本与校验表达式的求值引擎）.
     */
    private String scriptEngineName;
    /**
     * 文本渲染器名称（基座默认阶段不消费，供具体引擎使用）.
     */
    private String rendererName;
    /**
     * 预处理校验配置（入参校验）：字符串载体，如何解析由具体实现方决定
     * （默认实现 {@link kunlun.engine.pipeline.base.CommonValidator} 按 ValidationConfig 的 JSON 数组解析）.
     *
     * @see kunlun.data.validation.support.ValidationConfig
     */
    private String preprocessValidationConfigs;
    /**
     * 预处理脚本：核心求值前对输入做加工（转换、校验、赋值等）.
     */
    private String preprocessScript;
    /**
     * 后处理脚本：核心求值后对输出做加工（转换、格式化、清洗等）.
     */
    private String postprocessScript;
    /**
     * 后处理校验配置（出参校验）：字符串载体，如何解析由具体实现方决定
     * （默认实现 {@link kunlun.engine.pipeline.base.CommonValidator} 按 ValidationConfig 的 JSON 数组解析）.
     *
     * @see kunlun.data.validation.support.ValidationConfig
     */
    private String postprocessValidationConfigs;
    /**
     * 属性表：其他配置.
     */
    private Map<String, Object> attributes;

    @Override
    public String getSceneCode() {

        return sceneCode;
    }

    public void setSceneCode(String sceneCode) {

        this.sceneCode = sceneCode;
    }

    @Override
    public String getDescription() {

        return description;
    }

    public void setDescription(String description) {

        this.description = description;
    }

    public String getScriptEngineName() {

        return scriptEngineName;
    }

    public void setScriptEngineName(String scriptEngineName) {

        this.scriptEngineName = scriptEngineName;
    }

    public String getRendererName() {

        return rendererName;
    }

    public void setRendererName(String rendererName) {

        this.rendererName = rendererName;
    }

    public String getPreprocessValidationConfigs() {

        return preprocessValidationConfigs;
    }

    public void setPreprocessValidationConfigs(String preprocessValidationConfigs) {

        this.preprocessValidationConfigs = preprocessValidationConfigs;
    }

    public String getPreprocessScript() {

        return preprocessScript;
    }

    public void setPreprocessScript(String preprocessScript) {

        this.preprocessScript = preprocessScript;
    }

    public String getPostprocessScript() {

        return postprocessScript;
    }

    public void setPostprocessScript(String postprocessScript) {

        this.postprocessScript = postprocessScript;
    }

    public String getPostprocessValidationConfigs() {

        return postprocessValidationConfigs;
    }

    public void setPostprocessValidationConfigs(String postprocessValidationConfigs) {

        this.postprocessValidationConfigs = postprocessValidationConfigs;
    }

    @Override
    public Map<String, Object> getAttributes() {

        return attributes;
    }

    public void setAttributes(Map<String, Object> attributes) {

        this.attributes = attributes;
    }

}
