/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.engine;

import kunlun.core.Engine;
import kunlun.util.Assert;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 简单的引擎执行管道.<br />
 * <p>
 * 内部持有写时复制（copy-on-write）的阶段列表：
 * 变更操作（add / replace / remove）以 synchronized 保证"查找 + 修改"的原子性，
 * 执行与查询走快照遍历，注册与执行可在多线程环境下并发进行.
 * <p>
 * 阶段数量通常在十个以内，按键名的线性查找足够，不额外维护索引.
 *
 * @author Kahle
 */
public class SimplePipeline implements Engine.Pipeline {
    /**
     * 阶段列表（写时复制，按执行顺序排列）.
     * */
    private final List<Engine.Stage> stages = new CopyOnWriteArrayList<Engine.Stage>();

    @Override
    public synchronized void addStage(Engine.Stage stage) {
        Assert.notNull(stage, "Parameter \"stage\" must not null. ");
        assertNotExists(stage.getName());
        stages.add(stage);
    }

    @Override
    public synchronized void addStageBefore(String anchorName, Engine.Stage stage) {
        Assert.notBlank(anchorName, "Parameter \"anchorName\" must not blank. ");
        Assert.notNull(stage, "Parameter \"stage\" must not null. ");
        assertNotExists(stage.getName());
        int index = indexOfOrThrow(anchorName);
        stages.add(index, stage);
    }

    @Override
    public synchronized void addStageAfter(String anchorName, Engine.Stage stage) {
        Assert.notBlank(anchorName, "Parameter \"anchorName\" must not blank. ");
        Assert.notNull(stage, "Parameter \"stage\" must not null. ");
        assertNotExists(stage.getName());
        int index = indexOfOrThrow(anchorName);
        stages.add(index + 1, stage);
    }

    @Override
    public synchronized Engine.Stage replaceStage(String name, Engine.Stage stage) {
        Assert.notBlank(name, "Parameter \"name\" must not blank. ");
        Assert.notNull(stage, "Parameter \"stage\" must not null. ");
        if (!name.equals(stage.getName())) {
            throw new IllegalArgumentException("The stage name \"" + stage.getName()
                    + "\" is not equals to \"" + name + "\". ");
        }
        int index = indexOfOrThrow(name);

        return stages.set(index, stage);
    }

    @Override
    public synchronized Engine.Stage removeStage(String name) {
        Assert.notBlank(name, "Parameter \"name\" must not blank. ");
        int index = indexOfOrThrow(name);

        return stages.remove(index);
    }

    @Override
    public Engine.Stage getStage(String name) {
        Assert.notBlank(name, "Parameter \"name\" must not blank. ");
        for (Engine.Stage stage : stages) {
            if (stage.getName().equals(name)) { return stage; }
        }
        return null;
    }

    @Override
    public List<Engine.Stage> getStages() {

        return Collections.unmodifiableList(new ArrayList<Engine.Stage>(stages));
    }

    @Override
    public void execute(Engine.EngineContext context) {
        Assert.notNull(context, "Parameter \"context\" must not null. ");
        for (Engine.Stage stage : stages) {
            // 快照遍历（写时复制），变更与执行可并发.
            stage.accept(context);
        }
    }

    protected void assertNotExists(String stageName) {
        if (getStage(stageName) != null) {
            throw new IllegalArgumentException("The stage \"" + stageName + "\" already exists. ");
        }
    }

    protected int indexOfOrThrow(String stageName) {
        for (int i = 0; i < stages.size(); i++) {
            if (stages.get(i).getName().equals(stageName)) { return i; }
        }
        throw new IllegalArgumentException("The stage \"" + stageName + "\" could not be found. ");
    }

}
