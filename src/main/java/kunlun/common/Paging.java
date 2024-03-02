/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.common;

import java.io.Serializable;

import static kunlun.common.constant.Numbers.ONE;
import static kunlun.common.constant.Numbers.TEN;

/**
 * The simple paging info.
 * @author Kahle
 */
@Deprecated
public class Paging implements Serializable {
    private Integer pageNum;
    private Integer pageSize;

    public Paging() {

        this(ONE, TEN);
    }

    public Paging(Integer pageNum, Integer pageSize) {
        this.pageSize = pageSize;
        this.pageNum = pageNum;
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

    @Override
    public String toString() {
        return "Paging{" +
                "pageNum=" + pageNum +
                ", pageSize=" + pageSize +
                '}';
    }

}
