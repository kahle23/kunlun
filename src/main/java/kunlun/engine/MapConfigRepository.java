/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.engine;

import kunlun.core.Engine;
import kunlun.util.Assert;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 基于内存 Map 的引擎配置仓库.<br />
 * <p>
 * 以并发 Map 维护"场景码 → 引擎配置"注册表，注册与装载可在多线程环境下并发进行.
 * 适用于配置写死在代码里的小型场景，或作为测试与演示的默认仓库.
 *
 * @author Kahle
 */
public class MapConfigRepository implements ConfigRepository {
    /**
     * 配置注册表：场景码 → 引擎配置.
     * */
    private final Map<String, Engine.EngineConfig> configs =
            new ConcurrentHashMap<String, Engine.EngineConfig>();

    /**
     * 注册引擎配置（同场景码重复注册则覆盖旧的）.
     * @param sceneCode 场景码
     * @param config 引擎配置
     */
    public void register(String sceneCode, Engine.EngineConfig config) {
        Assert.notBlank(sceneCode, "Parameter \"sceneCode\" must not blank. ");
        Assert.notNull(config, "Parameter \"config\" must not null. ");
        configs.put(sceneCode, config);
    }

    /**
     * 注销场景码对应的引擎配置.
     * @param sceneCode 场景码
     */
    public void deregister(String sceneCode) {
        Assert.notBlank(sceneCode, "Parameter \"sceneCode\" must not blank. ");
        configs.remove(sceneCode);
    }

    @Override
    public Engine.EngineConfig load(String sceneCode) {
        Assert.notBlank(sceneCode, "Parameter \"sceneCode\" must not blank. ");

        return configs.get(sceneCode);
    }

}
