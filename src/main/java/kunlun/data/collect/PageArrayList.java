/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.data.collect;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * The array list with pagination.
 * @param <E> The type of elements in this list
 * @author Kahle
 */
public class PageArrayList<E> extends ArrayList<E> {
    private Integer pageCount;
    private Integer pageSize;
    private Integer pageNum;
    private Long total;
    private String scrollId;

    public PageArrayList() {

    }

    public PageArrayList(int initialCapacity) {

        super(initialCapacity);
    }

    public PageArrayList(Collection<? extends E> c) {
        super(c);
        if (c instanceof PageArrayList) {
            @SuppressWarnings("rawtypes")
            PageArrayList pl = (PageArrayList) c;
            this.setPageNum(pl.getPageNum());
            this.setPageSize(pl.getPageSize());
            this.setPageCount(pl.getPageCount());
            this.setTotal(pl.getTotal());
            this.setScrollId(pl.getScrollId());
        }
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

    public String getScrollId() {

        return scrollId;
    }

    public void setScrollId(String scrollId) {

        this.scrollId = scrollId;
    }

    public List<E> getData() {

        return this;
    }

    @Override
    public String toString() {
        return "PageArrayList{" +
                "pageCount=" + pageCount +
                ", pageSize=" + pageSize +
                ", pageNum=" + pageNum +
                ", total=" + total +
                ", scrollId='" + scrollId + '\'' +
                "} " + super.toString();
    }

}
