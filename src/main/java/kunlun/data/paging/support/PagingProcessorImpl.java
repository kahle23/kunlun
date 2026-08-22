package kunlun.data.paging.support;

import kunlun.common.Paging;
import kunlun.data.paging.PagingProcessor;
import kunlun.util.Assert;

import static kunlun.common.constant.Numbers.ONE;

public class PagingProcessorImpl implements PagingProcessor {
    private Integer defaultPageSize = 20;
    private Integer defaultPageNum = 1;
    /**
     * 分页大小的上限（防御性封顶，防止误传或恶意的超大 pageSize 拖垮内存），默认 100000.
     */
    private Integer maxPageSize = 100000;

    @Override
    public Integer getDefaultPageSize() {

        return defaultPageSize;
    }

    @Override
    public void setDefaultPageSize(Integer defaultPageSize) {
        Assert.notNull(defaultPageNum, "Parameter \"defaultPageNum\" must not null. ");
        this.defaultPageSize = defaultPageSize;
    }

    @Override
    public Integer getDefaultPageNum() {

        return defaultPageNum;
    }

    @Override
    public void setDefaultPageNum(Integer defaultPageNum) {
        Assert.notNull(defaultPageSize, "Parameter \"defaultPageSize\" must not null. ");
        this.defaultPageNum = defaultPageNum;
    }

    @Override
    public Integer getMaxPageSize() {

        return maxPageSize;
    }

    @Override
    public void setMaxPageSize(Integer maxPageSize) {
        Assert.notNull(maxPageSize);
        if (maxPageSize < ONE) {
            throw new IllegalArgumentException("The max page size must be greater than 0. ");
        }
        this.maxPageSize = maxPageSize;
    }

    @Override
    public Object process(Paging paging) {

        throw new UnsupportedOperationException();
    }
}
