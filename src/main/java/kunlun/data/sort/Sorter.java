/*
 * Copyright (c) 2019. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.data.sort;

import java.util.List;
import java.util.Map;

/**
 * 排序器：把调用方传入的自定义排序参数（{@link SortField} 列表）应用到排序目标对象上.
 * <p>纯 JDK 契约（本接口不依赖任何具体存储的类型），配合 {@link SortUtil} 门面形成多实现体系：
 * MyBatis-Plus 实现见 {@code baibao.db.jdbc.mybatisplus.MyBatisPlusSorter}，
 * 未来 ES 等其他存储可另行实现并经 SPI（META-INF/services）注册，调用方与排序参数对象保持不变。
 * <p>它是存储无关的通用抽象——"排序目标"不限于数据库查询条件：
 * 数据库的条件构造器（Wrapper）、ES 的查询构建器、甚至纯内存的集合排序上下文，都可以作为实现的路由目标。
 * <p>实现约定：
 * <ul>
 * <li>字段解析必须走白名单（如实体元数据），拒绝直接拼接外部传入的列名，防止 SQL 注入；</li>
 * <li>排序项按列表顺序逐项追加，顺序即排序优先级；</li>
 * <li>单项字段解析失败（不在白名单）只跳过该项并告警，不中断其余项；</li>
 * <li>未传排序参数（列表为空）时无操作，由业务的默认排序兜底。</li>
 * </ul>
 *
 * @author Kahle
 */
public interface Sorter {

    /**
     * 是否支持该排序目标对象（按类型路由）.
     * <p>各实现用 instanceof 判断各自支持的排序目标类型，例如 MyBatis-Plus 实现支持
     * {@code MPJLambdaWrapper}，ES 实现支持 ES 的查询构建对象。
     *
     * @param target 排序目标对象
     * @return 支持返回 true
     */
    boolean supports(Object target);

    /**
     * 应用自定义排序.
     *
     * @param target 排序目标对象（如 MyBatis-Plus 的 {@code MPJLambdaWrapper}）
     * @param entityClass 排序字段名所归属的类型（字段名解析的元数据来源）：数据库场景为实体类型
     *                    （解析属性到列名的白名单），ES 场景为文档模型类型，纯内存场景为待排序元素的类型
     *                    （兼作泛型擦除后拿不到的元素类型信息）
     * @param sortFields 调用方传入的排序参数（可为空，为空时无操作）
     * @param options 通用扩展选项（命名选项包，可为 null）：key 的语义由各实现与调用方自行约定——
     *                 谁使用某个 key，谁负责其含义与校验；MyBatis-Plus 实现约定
     *                 {@code MyBatisPlusSorter.OPTION_COLUMN_MAPPING} 对应的 Map 为
     *                 "字段名 → 列名/列表达式"的自定义映射（虚拟/计算排序键、跨表重名消歧），命中优先于自动白名单
     * @return 有任意一项排序实际生效返回 true
     */
    boolean applySort(Object target, Class<?> entityClass, List<SortField> sortFields, Map<String, Object> options);

}
