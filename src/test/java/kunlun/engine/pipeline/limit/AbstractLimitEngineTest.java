/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.engine.pipeline.limit;

import kunlun.core.function.Consumer;
import kunlun.engine.pipeline.Pipeline;
import kunlun.engine.pipeline.PipelineConfig;
import kunlun.engine.pipeline.PipelineContext;
import kunlun.engine.pipeline.base.AbstractPipelineConfig;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * The abstract limit engine test.
 *
 * @author Kahle
 */
public class AbstractLimitEngineTest {

    /**
     * 测试配置：仅满足基座阶段对 AbstractPipelineConfig 的类型要求（未声明校验与脚本）.
     */
    private static class TestLimitConfig extends AbstractPipelineConfig {

    }

    /**
     * 测试引擎：记录执行顺序；配置装载置入配置，候选筛选校验配置并写 storage，
     * 核心求值消费候选产物、读取透传后的输入并置输出，聚合透传.
     */
    private static class TestLimitEngine extends AbstractLimitEngine {
        /**
         * 执行顺序记录（配置装载与核心三阶段各记一笔）.
         */
        private final List<String> order = new ArrayList<String>();
        /**
         * 配置装载阶段要置入的配置.
         */
        private final PipelineConfig config;
        /**
         * 核心求值阶段读到的转换后输入.
         */
        private Object convertedInputSeen;

        private TestLimitEngine(PipelineConfig config) {

            this.config = config;
        }

        @Override
        protected Consumer<PipelineContext> getConfigLoader() {
            return new Consumer<PipelineContext>() {
                @Override
                public void accept(PipelineContext context) {
                    order.add("loadConfig:" + context.getSceneCode());
                    context.setConfig(config);
                }
            };
        }

        @Override
        protected Consumer<PipelineContext> getCandidateSelector() {
            return new Consumer<PipelineContext>() {
                @Override
                public void accept(PipelineContext context) {
                    order.add("candidate-selector");
                    // 候选筛选应能读到配置装载阶段置入的配置.
                    if (context.getConfig() != config) {
                        throw new IllegalStateException("The config is not loaded. ");
                    }
                    context.getStorage().put("candidates", "rule-1");
                }
            };
        }

        @Override
        protected Consumer<PipelineContext> getRuleExecutor() {
            return new Consumer<PipelineContext>() {
                @Override
                public void accept(PipelineContext context) {
                    order.add("engine-executor");
                    // 核心求值消费候选筛选的产物，读取预处理透传后的输入.
                    convertedInputSeen = context.getConvertedInput();
                    Object candidate = context.getStorage().get("candidates");
                    context.setRawOutput("LIMIT-RESULT:" + candidate);
                }
            };
        }

        @Override
        protected Consumer<PipelineContext> getResultAggregator() {
            return new Consumer<PipelineContext>() {
                @Override
                public void accept(PipelineContext context) {

                    order.add("result-aggregator");
                }
            };
        }
    }

    @Test
    public void testGetPipelineAssemblesEightAnchors() {
        AbstractLimitEngine engine = new TestLimitEngine(new TestLimitConfig());
        Pipeline pipeline = engine.getPipeline();
        // 八锚点依 STAGE_* 常量的声明顺序装配.
        assertEquals(Arrays.asList(
                Pipeline.STAGE_CONFIG_LOADER,
                Pipeline.STAGE_PREPROCESS_VALIDATOR,
                Pipeline.STAGE_PREPROCESSOR,
                Pipeline.STAGE_CANDIDATE_SELECTOR,
                Pipeline.STAGE_ENGINE_EXECUTOR,
                Pipeline.STAGE_RESULT_AGGREGATOR,
                Pipeline.STAGE_POSTPROCESSOR,
                Pipeline.STAGE_POSTPROCESS_VALIDATOR), pipeline.getStageNames());
        // 不可变用法：每次构建新的管道.
        assertNotSame(pipeline, engine.getPipeline());
    }

    @Test
    public void testExecuteRunsStagesInOrderAndReturnsResult() {
        TestLimitEngine engine = new TestLimitEngine(new TestLimitConfig());
        Object result = engine.execute("purchase.demand.ready-precheck", "input-1", null);
        // 配置装载在 config-loader 阶段执行期调用，核心三阶段依序执行.
        assertEquals(Arrays.asList(
                "loadConfig:purchase.demand.ready-precheck",
                "candidate-selector", "engine-executor", "result-aggregator"), engine.order);
        // 预处理未声明脚本即透传：核心求值读到的就是原始输入.
        assertEquals("input-1", engine.convertedInputSeen);
        // 核心求值置入的输出经后处理透传，由默认结果抽取器返回.
        assertEquals("LIMIT-RESULT:rule-1", result);
    }

    @Test
    public void testStageFailureSetsErrorAndInvokesLogRecorder() {
        final List<PipelineContext> recorded = new ArrayList<PipelineContext>();
        AbstractLimitEngine engine = new TestLimitEngine(new TestLimitConfig()) {
            @Override
            protected Consumer<PipelineContext> getRuleExecutor() {
                // 核心求值阶段抛出，模拟阶段执行失败.
                return new Consumer<PipelineContext>() {
                    @Override
                    public void accept(PipelineContext context) {

                        throw new IllegalStateException("mock stage failure. ");
                    }
                };
            }
        };
        engine.setLogRecorder(new Consumer<PipelineContext>() {
            @Override
            public void accept(PipelineContext context) {

                recorded.add(context);
            }
        });
        try {
            engine.execute("scene-1", "input-1", null);
            fail("Expected the stage failure to be rethrown. ");
        }
        catch (RuntimeException ignore) { }
        // 留痕在 finally 中执行：记录器收到上下文，且错误已置入.
        assertEquals(1, recorded.size());
        Throwable error = recorded.get(0).getError();
        assertTrue(error instanceof IllegalStateException);
    }

    @Test
    public void testNullStageFailsFast() {
        AbstractLimitEngine engine = new TestLimitEngine(new TestLimitConfig()) {
            @Override
            protected Consumer<PipelineContext> getCandidateSelector() {
                // 口子返回 null，装配期即应暴露.
                return null;
            }
        };
        try {
            engine.getPipeline();
            fail("Expected IllegalArgumentException for null stage. ");
        }
        catch (IllegalArgumentException ignore) { }
    }

}
