/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.engine;

import kunlun.core.Engine;
import kunlun.core.function.Consumer;
import kunlun.util.Assert;

/**
 * 简单的通用引擎.<br />
 * <p>
 * 以"配置仓库 + 核心执行器"两件套装配即得可用的引擎：
 * config-loader 走 {@link RepositoryConfigLoader} 从仓库装载配置，
 * candidate-selector 与 result-aggregator 保持默认的无操作（核心执行器直接产出最终结果），
 * 需要筛选与聚合语义时，再经具名 setter 或管道替换对应阶段.
 * <pre>
 *     MapConfigRepository repository = new MapConfigRepository();
 *     repository.register("order-total", config);
 *     SimpleEngine engine = new SimpleEngine(repository, new MyExecutor());
 *     // 引擎即 Strategy，可直接持有调用；strategy 段即场景码.
 *     Object result = engine.execute("order-total", data);
 * </pre>
 *
 * @author Kahle
 */
public class SimpleEngine extends AbstractEngine {

    public SimpleEngine(ConfigRepository repository, Consumer<Engine.EngineContext> engineExecutor) {
        Assert.notNull(repository, "Parameter \"repository\" must not null. ");
        Assert.notNull(engineExecutor, "Parameter \"engineExecutor\" must not null. ");
        this.setConfigLoader(new RepositoryConfigLoader(repository));
        this.setEngineExecutor(engineExecutor);
    }

}
