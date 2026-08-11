/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.common;

import kunlun.common.constant.Nil;
import kunlun.data.Dict;

import java.io.Serializable;

/**
 * The simple paging info.
 * @author Kahle
 */
public class Paging implements Serializable {

    // region ======== 静态构建方法 ========

    public static Paging of(Integer pageNum, Integer pageSize, Dict pageParams) {
        Paging paging = new Paging();
        paging.setPageNum(pageNum);
        paging.setPageSize(pageSize);
        paging.setPageParams(pageParams);
        return paging;
    }

    public static Paging of(Integer pageNum, Integer pageSize) {

        return of(pageNum, pageSize, Nil.<Dict>g());
    }

    public static Paging of() {

        return new Paging();
    }
    // endregion


    private Integer pageNum;
    private Integer pageSize;
    private Dict    pageParams;


    /*public Paging() {

        this(ONE, TEN);
    }

    public Paging(Integer pageNum, Integer pageSize) {
        this.pageSize = pageSize;
        this.pageNum = pageNum;
    }*/

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

    public Dict getPageParams() {

        return pageParams;
    }

    public void setPageParams(Dict pageParams) {

        this.pageParams = pageParams;
    }

    @Override
    public String toString() {
        return "Paging{" +
                "pageNum=" + pageNum +
                ", pageSize=" + pageSize +
                ", pageParams=" + pageParams +
                '}';
    }

}
