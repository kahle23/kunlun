/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.engine;

import kunlun.core.Engine;
import kunlun.core.function.Consumer;
import kunlun.util.Assert;

/**
 * 配置仓库装载器.<br />
 * <p>
 * {@link ConfigRepository} 与管道 config-loader 阶段之间的胶水适配器：
 * 从上下文取场景码，经仓库装载配置并置入上下文，装载失败即抛出异常.
 *
 * @author Kahle
 */
public class RepositoryConfigLoader implements Consumer<Engine.EngineContext> {
    /**
     * 引擎配置仓库.
     * */
    private final ConfigRepository repository;

    public RepositoryConfigLoader(ConfigRepository repository) {

        this.repository = Assert.notNull(repository);
    }

    @Override
    public void accept(Engine.EngineContext context) {
        String sceneCode = context.getSceneCode();
        Engine.EngineConfig config = repository.load(sceneCode);
        Assert.notNull(config, "Load the engine config failure by scene code \""
                + sceneCode + "\". ");
        context.setConfig(config);
    }

}
