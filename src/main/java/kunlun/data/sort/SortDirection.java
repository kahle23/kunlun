/*
 * Copyright (c) 2019. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.data.sort;

/**
 * 统一的排序方向（存储无关）.
 *
 * @author Kahle
 */
public enum SortDirection {

    /**
     * 正序
     */
    ASC,

    /**
     * 倒序
     */
    DESC;


    /**
     * 是否正序.
     *
     * @return 正序返回 true
     */
    public boolean isAsc() {

        return this == ASC;
    }

    /**
     * 宽松解析排序方向：desc / descending（忽略大小写）解析为倒序，
     * 其余取值（含空白）按正序处理.
     *
     * @param direction 方向字符串（asc / desc / ascending / descending）
     * @return 排序方向
     */
    public static SortDirection of(String direction) {
        if (direction == null) { return ASC; }
        String value = direction.trim();
        if ("desc".equalsIgnoreCase(value) || "descending".equalsIgnoreCase(value)) { return DESC; }
        return ASC;
    }

}
