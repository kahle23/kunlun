package kunlun.data.paging.support;

import kunlun.common.Paging;
import kunlun.data.paging.PagingProcessor;
import kunlun.util.Assert;

public class PagingProcessorImpl implements PagingProcessor {
    private Integer defaultPageSize = 20;
    private Integer defaultPageNum = 1;

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
    public Object process(Paging paging) {

        throw new UnsupportedOperationException();
    }
}
