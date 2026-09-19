/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.engine.pipeline;

import kunlun.core.function.Consumer;
import kunlun.engine.pipeline.base.DefaultPipeline;
import kunlun.engine.pipeline.base.DefaultPipelineContext;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * The default pipeline test.
 *
 * @author Kahle
 */
public class DefaultPipelineTest {

    /**
     * 构建把自身键名记录进 order 的阶段行为（用于断言执行顺序）.
     */
    private static Consumer<PipelineContext> record(final String name, final List<String> order) {
        return new Consumer<PipelineContext>() {
            @Override
            public void accept(PipelineContext context) {

                order.add(name);
            }
        };
    }

    @Test
    public void testAppendStageAndGetStageNames() {
        DefaultPipeline pipeline = new DefaultPipeline();
        pipeline.appendStage("a", new Consumer.Empty<PipelineContext>());
        pipeline.appendStage("b", new Consumer.Empty<PipelineContext>());
        pipeline.appendStage("c", new Consumer.Empty<PipelineContext>());
        assertEquals(Arrays.asList("a", "b", "c"), pipeline.getStageNames());
    }

    @Test
    public void testAppendDuplicateNameThrows() {
        DefaultPipeline pipeline = new DefaultPipeline();
        pipeline.appendStage("a", new Consumer.Empty<PipelineContext>());
        try {
            pipeline.appendStage("a", new Consumer.Empty<PipelineContext>());
            fail("Expected IllegalArgumentException for duplicate stage name. ");
        }
        catch (IllegalArgumentException ignore) { }
    }

    @Test
    public void testInsertBeforeStage() {
        DefaultPipeline pipeline = new DefaultPipeline();
        pipeline.appendStage("a", new Consumer.Empty<PipelineContext>());
        pipeline.appendStage("c", new Consumer.Empty<PipelineContext>());
        pipeline.insertBeforeStage("c", "b", new Consumer.Empty<PipelineContext>());
        assertEquals(Arrays.asList("a", "b", "c"), pipeline.getStageNames());
    }

    @Test
    public void testInsertAfterStage() {
        DefaultPipeline pipeline = new DefaultPipeline();
        pipeline.appendStage("a", new Consumer.Empty<PipelineContext>());
        pipeline.appendStage("d", new Consumer.Empty<PipelineContext>());
        pipeline.insertAfterStage("a", "b", new Consumer.Empty<PipelineContext>());
        pipeline.insertAfterStage("b", "c", new Consumer.Empty<PipelineContext>());
        assertEquals(Arrays.asList("a", "b", "c", "d"), pipeline.getStageNames());
    }

    @Test
    public void testInsertWithMissingAnchorThrows() {
        DefaultPipeline pipeline = new DefaultPipeline();
        try {
            pipeline.insertBeforeStage("missing", "b", new Consumer.Empty<PipelineContext>());
            fail("Expected IllegalArgumentException for missing anchor. ");
        }
        catch (IllegalArgumentException ignore) { }
    }

    @Test
    public void testReplaceStage() {
        List<String> order = new ArrayList<String>();
        DefaultPipeline pipeline = new DefaultPipeline();
        pipeline.appendStage("a", record("a-1", order));
        pipeline.appendStage("b", record("b-1", order));
        pipeline.replaceStage("b", record("b-2", order));
        // 替换后键名集合不变，执行的是新行为.
        assertEquals(Arrays.asList("a", "b"), pipeline.getStageNames());
        pipeline.execute(new DefaultPipelineContext());
        assertEquals(Arrays.asList("a-1", "b-2"), order);
    }

    @Test
    public void testReplaceMissingThrows() {
        DefaultPipeline pipeline = new DefaultPipeline();
        try {
            pipeline.replaceStage("missing", new Consumer.Empty<PipelineContext>());
            fail("Expected IllegalArgumentException for missing stage name. ");
        }
        catch (IllegalArgumentException ignore) { }
    }

    @Test
    public void testRemoveStage() {
        List<String> order = new ArrayList<String>();
        DefaultPipeline pipeline = new DefaultPipeline();
        pipeline.appendStage("a", record("a", order));
        pipeline.appendStage("b", record("b", order));
        pipeline.removeStage("a");
        assertEquals(Arrays.asList("b"), pipeline.getStageNames());
        pipeline.execute(new DefaultPipelineContext());
        assertEquals(Arrays.asList("b"), order);
    }

    @Test
    public void testRemoveMissingThrows() {
        DefaultPipeline pipeline = new DefaultPipeline();
        try {
            pipeline.removeStage("missing");
            fail("Expected IllegalArgumentException for missing stage name. ");
        }
        catch (IllegalArgumentException ignore) { }
    }

    @Test
    public void testExecuteInDeclarationOrder() {
        List<String> order = new ArrayList<String>();
        DefaultPipeline pipeline = new DefaultPipeline();
        pipeline.appendStage("first", record("first", order));
        pipeline.appendStage("second", record("second", order));
        pipeline.appendStage("third", record("third", order));
        pipeline.execute(new DefaultPipelineContext());
        assertEquals(Arrays.asList("first", "second", "third"), order);
    }

    @Test
    public void testGetStageNamesReturnsUnmodifiableSnapshot() {
        DefaultPipeline pipeline = new DefaultPipeline();
        pipeline.appendStage("a", new Consumer.Empty<PipelineContext>());
        List<String> names = pipeline.getStageNames();
        // 快照副本：修改底层管道不影响已取出的列表.
        pipeline.appendStage("b", new Consumer.Empty<PipelineContext>());
        assertEquals(Arrays.asList("a"), names);
        try {
            names.add("c");
            fail("Expected UnsupportedOperationException. ");
        }
        catch (UnsupportedOperationException ignore) { }
    }

    @Test
    public void testAppendBlankNameOrNullConsumerThrows() {
        DefaultPipeline pipeline = new DefaultPipeline();
        try {
            pipeline.appendStage(" ", new Consumer.Empty<PipelineContext>());
            fail("Expected IllegalArgumentException for blank stage name. ");
        }
        catch (IllegalArgumentException ignore) { }
        try {
            pipeline.appendStage("a", null);
            fail("Expected IllegalArgumentException for null consumer. ");
        }
        catch (IllegalArgumentException ignore) { }
    }

}
