/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.common;

import java.util.LinkedHashMap;
import java.util.List;

import static kunlun.util.ObjectUtils.cast;

/**
 * The unified paging data output object.
 * @param <T> The data type
 * @author Kahle
 */
public class Page<T> extends LinkedHashMap<String, Object> {
    private static final String SCROLL_ID = "scrollId";
    private static final String PAGE_NUM  = "pageNum";
    private static final String PAGE_SIZE  = "pageSize";
    private static final String PAGE_COUNT = "pageCount";
    private static final String TOTAL = "total";
    private static final String DATA  = "data";

    public static <T> Page<T> of(Integer pageNum, Integer pageSize, Integer pageCount, Long total, List<T> data) {
        Page<T> page = new Page<T>();
        page.setPageNum(pageNum);
        page.setPageSize(pageSize);
        page.setPageCount(pageCount);
        page.setTotal(total);
        page.setData(data);
        return page;
    }

    public static <T> Page<T> of(String scrollId, Integer pageSize, List<T> data) {
        Page<T> page = new Page<T>();
        page.setScrollId(scrollId);
        page.setPageSize(pageSize);
        page.setData(data);
        return page;
    }

    public static <T> Page<T> of() {

        return new Page<T>();
    }


    public String getScrollId() {

        return (String) get(SCROLL_ID);
    }

    public void setScrollId(String scrollId) {

        put(SCROLL_ID, scrollId);
    }

    public Integer getPageNum() {

        return (Integer) get(PAGE_NUM);
    }

    public void setPageNum(Integer pageNum) {

        put(PAGE_NUM, pageNum);
    }

    public Integer getPageSize() {

        return (Integer) get(PAGE_SIZE);
    }

    public void setPageSize(Integer pageSize) {

        put(PAGE_SIZE, pageSize);
    }

    public Integer getPageCount() {

        return (Integer) get(PAGE_COUNT);
    }

    public void setPageCount(Integer pageCount) {

        put(PAGE_COUNT, pageCount);
    }

    public Long getTotal() {

        return (Long) get(TOTAL);
    }

    public void setTotal(Long total) {

        put(TOTAL, total);
    }

    public List<T> getData() {

        return cast(get(DATA));
    }

    public void setData(List<T> data) {

        put(DATA, data);
    }

}
