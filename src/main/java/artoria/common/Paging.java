package artoria.common;

import java.io.Serializable;

import static artoria.common.constant.Numbers.ONE;
import static artoria.common.constant.Numbers.TEN;

/**
 * The simple paging info.
 * @author Kahle
 */
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
