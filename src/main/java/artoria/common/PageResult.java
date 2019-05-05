package artoria.common;

/**
 * Uniform page result output object.
 * @param <T> Data type
 * @author Kahle
 */
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

    public PageResult(Boolean success, String message) {

        super(success, message);
    }

    public Integer getPageNum() {

        return this.pageNum;
    }

    public void setPageNum(Integer pageNum) {

        this.pageNum = pageNum;
    }

    public Integer getPageSize() {

        return this.pageSize;
    }

    public void setPageSize(Integer pageSize) {

        this.pageSize = pageSize;
    }

    public Integer getPageCount() {

        return this.pageCount;
    }

    public void setPageCount(Integer pageCount) {

        this.pageCount = pageCount;
    }

    public Long getTotal() {

        return this.total;
    }

    public void setTotal(Long total) {

        this.total = total;
    }

}
