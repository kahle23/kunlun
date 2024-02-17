/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.common;

/**
 * The uniform page result output object.
 * @param <T> The data type
 * @author Kahle
 */
@Deprecated
public class PageResult<T> extends Result<T> {
    private Integer pageNum;
    private Integer pageSize;
    private Integer pageCount;
    private Long total;

    public PageResult() {

        super();
    }

    public PageResult(T data) {

        super(data);
    }

    public PageResult(String code, String message) {

        super(code, message);
    }

    public PageResult(Boolean success, String code, String message) {

        super(success, code, message);
    }

    public PageResult(Boolean success, String code, String message, T data) {

        super(success, code, message, data);
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

}
