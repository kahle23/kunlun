/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.db.jdbc;

import java.util.Collection;

/**
 * 字段值清除器：按指定属性名集合，把对应字段值清空（典型为置 NULL）。
 * <p>框架无关：本接口只定义"清空指定字段值"的契约，service/wrapper 以 Object 传入，
 * 由具体实现识别其底层框架（如 ORM、JDBC）并完成实际清空。
 * <p>典型场景之一：MyBatis-Plus 默认 FieldStrategy.NOT_NULL 会跳过 null 字段，
 * 导致 updateById 无法置空；用本清除器经 UpdateWrapper.set(col,null) 绕开该策略。
 * 非 MyBatis-Plus 框架若有同类置空需求，提供本接口的实现即可。
 * @author Kahle
 */
public interface FieldValueClearer {

    /**
     * 执行型：按 id 清空指定字段（实体类由实现自行从 service 推断）。
     * @param service 目标数据访问对象（实现自行识别，如 MyBatis-Plus 的 IService）
     * @param id 业务数据主键
     * @param fields 待清空属性名集合（驼峰），前端可控
     * @return 是否执行了清空更新
     */
    boolean clear(Object service, Object id, Collection<String> fields);

    /**
     * 执行型：按 id 清空指定字段（显式实体类，推断不到时用）。
     */
    boolean clear(Object service, Class<?> entityClass, Object id, Collection<String> fields);

    /**
     * 追加型：往调用者已构建的 wrapper 追加清空子句，不执行，返回该 wrapper（便于链式）。
     * @param wrapper 调用者已构建的更新条件对象（实现自行识别，如 MyBatis-Plus 的 UpdateWrapper）
     */
    Object appendClearFields(Object wrapper, Class<?> entityClass, Collection<String> fields);

}
