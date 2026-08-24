/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.engine;

import kunlun.core.Engine;
import kunlun.core.function.Consumer;
import kunlun.util.Assert;

/**
 * 具名阶段的简单实现.<br />
 * <p>
 * 以"键名 + 行为"两个字段实现 {@link Engine.Stage}：
 * 行为直接委托持有的 {@link Consumer}，无额外逻辑.
 * 锚点常量（NAME_*）定义在 {@link Engine.Stage} 上.
 *
 * @author Kahle
 */
public class SimpleStage implements Engine.Stage {
    /**
     * 键名（管道中定位锚点的依据）.
     * */
    private final String name;
    /**
     * 阶段行为.
     * */
    private final Consumer<Engine.EngineContext> consumer;

    /**
     * 以键名与行为构建具名阶段.
     * @param name 键名
     * @param consumer 阶段行为
     */
    public SimpleStage(String name, Consumer<Engine.EngineContext> consumer) {
        this.name = Assert.notBlank(name);
        this.consumer = Assert.notNull(consumer);
    }

    @Override
    public String getName() {

        return name;
    }

    /**
     * 获取阶段行为.
     * @return 阶段行为
     */
    public Consumer<Engine.EngineContext> getConsumer() {

        return consumer;
    }

    @Override
    public void accept(Engine.EngineContext context) {

        consumer.accept(context);
    }

    @Override
    public String toString() {

        return "SimpleStage(name=" + name + ", consumer=" + consumer + ")";
    }

}
