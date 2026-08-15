/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.core;

/**
 * 为 action 提供最高层次的抽象.<br />
 * <p>
 * 什么是 action？
 * 表示用于执行特定功能的操作、动作或任务.
 *
 * @author Kahle
 */
public interface Action extends Strategy {

    /**
     * 带类型声明的 action 输入标记接口.<br />
     * <p>
     * 类型参数 R 为该输入对应 action 的返回类型，调用方通过泛型约束即可直接得到返回类型
     * （当输入以 raw 方式使用时，R 为 Object）.
     * <p>
     * 绑定粒度为"一个输入类 → 一个返回类型"，与 shortcut 路由的粒度一致；
     * 泛型擦除之下运行时仍是强转，其价值在编译期契约与 IDE 提示，而非运行时安全.
     *
     * @param <R> 该输入对应 action 的返回类型
     * @author Kahle
     */
    interface Input<R> {

    }

}
