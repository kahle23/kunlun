/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.core;

import kunlun.core.Context.AbstractContext;

import java.util.Map;

/**
 * 为策略提供最高层次的抽象.<br />
 * @author Kahle
 */
public interface Strategy {

    /**
     * 执行指定策略的逻辑.<br />
     * @param strategy  策略名称
     * @param input     策略执行时的主要输入对象
     * @param arguments 策略执行时的其他相关参数
     * @return 策略执行后返回的结果或者 Null
     */
    Object execute(String strategy, Object input, Object[] arguments);


    /**
     * 抽象的策略上下文对象.<br />
     * @author Kahle
     */
    abstract class StrategyContext extends AbstractContext {
        private String strategy;
        private Object input;
        private Object[] arguments;
        private Object output;

        public StrategyContext(Map<String, Object> storage) {

            super(storage);
        }

        public StrategyContext() {

        }

        public String getStrategy() {

            return strategy;
        }

        public void setStrategy(String strategy) {

            this.strategy = strategy;
        }

        public Object getInput() {

            return input;
        }

        public void setInput(Object input) {

            this.input = input;
        }

        public Object[] getArguments() {

            return arguments;
        }

        public void setArguments(Object[] arguments) {

            this.arguments = arguments;
        }

        public Object getOutput() {

            return output;
        }

        public void setOutput(Object output) {

            this.output = output;
        }
    }

}
