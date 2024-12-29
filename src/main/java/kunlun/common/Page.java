/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.common;

import java.io.Serializable;
import java.util.List;

/**
 * The unified paging data output object.
 * @param <T> The data type
 * @author Kahle
 */
public class Page<T> implements Serializable {
    private String  scrollId;
    private Integer pageNum;
    private Integer pageSize;
    private Integer pageCount;
    private Long    total;
    private List<T> data;

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

    public static <T> Page<T> of(List<T> data) {
        Page<T> page = new Page<T>();
        page.setData(data);
        return page;
    }

    public static <T> Page<T> of() {

        return new Page<T>();
    }


    public String getScrollId() {

        return scrollId;
    }

    public void setScrollId(String scrollId) {

        this.scrollId = scrollId;
    }

    public Integer getPageNum() {

        return pageNum;
    }

    public void setPageNum(Integer pageNum) {

        this.pageNum = pageNum;
    }

    public Integer getPageSize() {

        return pageSize;
    }

    public void setPageSize(Integer pageSize) {

        this.pageSize = pageSize;
    }

    public Integer getPageCount() {

        return pageCount;
    }

    public void setPageCount(Integer pageCount) {

        this.pageCount = pageCount;
    }

    public Long getTotal() {

        return total;
    }

    public void setTotal(Long total) {

        this.total = total;
    }

    public List<T> getData() {

        return data;
    }

    public void setData(List<T> data) {

        this.data = data;
    }

}
