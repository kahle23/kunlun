/*
 * Copyright (c) 2019. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.data.sort;

import java.io.Serializable;

/**
 * 统一的自定义排序项（存储无关）.
 * <p>排序项的集合顺序即排序优先级；字段名默认为实体属性名（驼峰），
 * 跨表/表达式字段由各存储实现的列解析钩子处理（见 {@link Sorter}）。
 *
 * @author Kahle
 */
public class SortField implements Serializable {

    /**
     * 构建排序项的便捷方法.
     *
     * @param field 字段名
     * @param order 排序方向（asc / desc）
     * @return 排序项
     */
    public static SortField of(String field, String order) {
        SortField item = new SortField();
        item.setField(field);
        item.setOrder(order);
        return item;
    }

    /**
     * 字段名（默认为实体属性名，驼峰）
     */
    private String field;
    /**
     * 排序方向：asc / desc（忽略大小写，兼容 ascending / descending；缺省按正序处理）
     */
    private String order;

    public String getField() {

        return field;
    }

    public void setField(String field) {

        this.field = field;
    }

    public String getOrder() {

        return order;
    }

    public void setOrder(String order) {

        this.order = order;
    }
}
