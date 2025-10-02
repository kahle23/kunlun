/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.core;

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

}
