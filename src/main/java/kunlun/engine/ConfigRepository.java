/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.engine;

import kunlun.core.Engine;

/**
 * 引擎配置仓库的抽象声明.<br />
 * <p>
 * 声明"配置从哪来"：引擎执行期的 config-loader 阶段按场景码从这里装载
 * {@link Engine.EngineConfig}，引擎本身不关心配置的存储介质.
 * <p>
 * 默认实现 {@link MapConfigRepository} 是内存仓库；
 * 落地方可替换为数据库、配置中心、文件等实现，
 * 有热更新需求的实现可在此基础上演进版本查询与重载语义.
 *
 * @author Kahle
 */
public interface ConfigRepository {

    /**
     * 按场景码装载引擎配置.
     * @param sceneCode 场景码
     * @return 引擎配置（不存在时为 Null）
     */
    Engine.EngineConfig load(String sceneCode);

}
