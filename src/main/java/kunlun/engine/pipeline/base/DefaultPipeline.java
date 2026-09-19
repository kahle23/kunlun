/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.engine.pipeline.base;

import kunlun.core.function.Consumer;
import kunlun.engine.pipeline.Pipeline;
import kunlun.engine.pipeline.PipelineContext;
import kunlun.util.Assert;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import static kunlun.common.constant.Numbers.ZERO;

/**
 * 简单的引擎执行管道：管道的默认实现.
 * <p>
 * 内部持有写时复制（copy-on-write）的阶段条目列表：
 * 变更操作以 synchronized 保证"查找 + 修改"的原子性，
 * 执行与查询走快照遍历，装配与执行可在多线程环境下并发进行.
 * <p>
 * 阶段数量通常在十个以内，按键名的线性查找足够，不额外维护索引.
 *
 * @author Kahle
 */
public class DefaultPipeline implements Pipeline {
    /**
     * 阶段条目列表（写时复制，按执行顺序排列）.
     */
    private final List<Stage> stages = new CopyOnWriteArrayList<Stage>();

    protected void assertNotExists(String stageName) {
        for (Stage entry : stages) {
            if (entry.getName().equals(stageName)) {
                throw new IllegalArgumentException("The stage \"" + stageName + "\" already exists. ");
            }
        }
    }

    protected int indexOfOrThrow(String stageName) {
        for (int i = ZERO; i < stages.size(); i++) {
            if (stages.get(i).getName().equals(stageName)) { return i; }
        }
        throw new IllegalArgumentException("The stage \"" + stageName + "\" could not be found. ");
    }

    @Override
    public synchronized void appendStage(String name, Consumer<PipelineContext> consumer) {
        Assert.notBlank(name, "Parameter \"name\" must not blank. ");
        Assert.notNull(consumer, "Parameter \"consumer\" must not null. ");
        assertNotExists(name);
        stages.add(new Stage(name, consumer));
    }

    @Override
    public synchronized void insertBeforeStage(String anchorName, String name, Consumer<PipelineContext> consumer) {
        Assert.notBlank(anchorName, "Parameter \"anchorName\" must not blank. ");
        Assert.notBlank(name, "Parameter \"name\" must not blank. ");
        Assert.notNull(consumer, "Parameter \"consumer\" must not null. ");
        assertNotExists(name);
        int index = indexOfOrThrow(anchorName);
        stages.add(index, new Stage(name, consumer));
    }

    @Override
    public synchronized void insertAfterStage(String anchorName, String name, Consumer<PipelineContext> consumer) {
        Assert.notBlank(anchorName, "Parameter \"anchorName\" must not blank. ");
        Assert.notBlank(name, "Parameter \"name\" must not blank. ");
        Assert.notNull(consumer, "Parameter \"consumer\" must not null. ");
        assertNotExists(name);
        int index = indexOfOrThrow(anchorName);
        stages.add(index + 1, new Stage(name, consumer));
    }

    @Override
    public synchronized void replaceStage(String name, Consumer<PipelineContext> consumer) {
        Assert.notBlank(name, "Parameter \"name\" must not blank. ");
        Assert.notNull(consumer, "Parameter \"consumer\" must not null. ");
        int index = indexOfOrThrow(name);
        stages.set(index, new Stage(name, consumer));
    }

    @Override
    public synchronized void removeStage(String name) {
        Assert.notBlank(name, "Parameter \"name\" must not blank. ");
        int index = indexOfOrThrow(name);
        stages.remove(index);
    }

    @Override
    public List<String> getStageNames() {
        List<String> names = new ArrayList<String>(stages.size());
        for (Stage entry : stages) { names.add(entry.getName()); }

        return Collections.unmodifiableList(names);
    }

    @Override
    public void execute(PipelineContext context) {
        Assert.notNull(context, "Parameter \"context\" must not null. ");
        for (Stage entry : stages) {
            // 快照遍历（写时复制），变更与执行可并发.
            entry.getConsumer().accept(context);
        }
    }

}
