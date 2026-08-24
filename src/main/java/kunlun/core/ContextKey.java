/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.core;

import kunlun.util.Assert;

import java.util.Map;

/**
 * 类型化的上下文键.<br />
 * <p>
 * 为 {@link Context#getStorage()} 的弱类型存储提供编译期类型约束的读写方式：
 * 键本身携带值的类型，读写经由 {@link #get(Map)} 与 {@link #put(Map, Object)}
 * 收口为 {@code Class.cast}，调用方无需再手写强转.
 * <pre>
 *     // 由各引擎家族声明自己的中间产物键（键即类型契约）:
 *     static final ContextKey&lt;List&lt;Evaluation&gt;&gt; EVALUATIONS =
 *             ContextKey.of("score-evaluations", List.class);
 *     ...
 *     List&lt;Evaluation&gt; evaluations = context.get(EVALUATIONS);
 * </pre>
 * <p>
 * 泛型的诚实性：受限于泛型擦除，{@code of("x", List.class)} 无法表达
 * {@code List<Element>} 的元素类型，容器键的精确性由调用点的泛型声明自行保证，
 * 类型不符会在读取时抛出 ClassCastException——
 * 与 {@link Action.Input} 一样，其价值在编译期契约与 IDE 提示，而非运行时安全.
 * <p>
 * 判等只看 {@code name}：同名即同键（同名不同类型视为装配错误）.
 *
 * @author Kahle
 */
public final class ContextKey<T> {
    /**
     * 键名（判等依据）.
     * */
    private final String name;
    /**
     * 值类型（读取时收口强转）.
     * */
    private final Class<T> type;

    private ContextKey(String name, Class<T> type) {
        this.name = Assert.notBlank(name);
        this.type = Assert.notNull(type);
    }

    /**
     * 以键名与值类型构建类型化上下文键.
     * @param name 键名
     * @param type 值类型
     * @param <T> 值类型
     * @return 类型化上下文键
     */
    public static <T> ContextKey<T> of(String name, Class<T> type) {

        return new ContextKey<T>(name, type);
    }

    /**
     * 获取键名.
     * @return 键名
     */
    public String getName() {

        return name;
    }

    /**
     * 获取值类型.
     * @return 值类型
     */
    public Class<T> getType() {

        return type;
    }

    /**
     * 从指定存储器中按键名读取值，并收口为键声明的类型.
     * @param storage 存储器
     * @return 键对应的值（不存在时为 Null）
     */
    public T get(Map<String, Object> storage) {
        Assert.notNull(storage, "Parameter \"storage\" must not null. ");
        Object value = storage.get(name);

        return value == null ? null : type.cast(value);
    }

    /**
     * 向指定存储器按键名写入值，返回旧值（同样收口为键声明的类型）.
     * @param storage 存储器
     * @param value 待写入的值
     * @return 旧值（不存在时为 Null）
     */
    public T put(Map<String, Object> storage, T value) {
        Assert.notNull(storage, "Parameter \"storage\" must not null. ");
        Object old = storage.put(name, value);

        return old == null ? null : type.cast(old);
    }

    @Override
    public boolean equals(Object object) {

        return this == object
                || object instanceof ContextKey && name.equals(((ContextKey<?>) object).name);
    }

    @Override
    public int hashCode() {

        return name.hashCode();
    }

    @Override
    public String toString() {

        return "ContextKey(name=" + name + ", type=" + type.getName() + ")";
    }

}
