/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.data.collect;

import kunlun.data.Dict;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * The array list with pagination.
 * @param <E> The type of elements in this list
 * @author Kahle
 */
public class PageArrayList<E> extends ArrayList<E> {
    private String  scrollId;
    private Integer pageNum;
    private Integer pageSize;
    private Integer pageCount;
    private Long    total;
    private Dict    others = Dict.of();

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
            this.setScrollId(pl.getScrollId());
            this.setPageNum(pl.getPageNum());
            this.setPageSize(pl.getPageSize());
            this.setPageCount(pl.getPageCount());
            this.setTotal(pl.getTotal());
            this.setOthers(pl.getOthers());
        }
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

    public List<E> getData() {

        return this;
    }

    public Dict getOthers() {

        return others;
    }

    public void setOthers(Dict others) {

        this.others = others;
    }

    @Override
    public String toString() {
        return "PageArrayList{" +
                "scrollId='" + scrollId + '\'' +
                ", pageNum=" + pageNum +
                ", pageSize=" + pageSize +
                ", pageCount=" + pageCount +
                ", total=" + total +
                ", others=" + others +
                "} " + super.toString();
    }

}
