package kunlun.data.paging.support;

import kunlun.common.Page;
import kunlun.data.Dict;
import kunlun.data.bean.BeanUtil;
import kunlun.data.paging.PageResultProcessor;

import java.util.List;

public class PageResultProcessorImpl implements PageResultProcessor {

    @Override
    public <T> Page<T> process(List<T> data) {

        throw new UnsupportedOperationException();
    }

    @Override
    public <F, T> Page<T> process(List<F> data, Class<T> clazz) {

        throw new UnsupportedOperationException();
    }

    @Override
    public <F, T> Page<T> process(Page<F> data, Class<T> clazz) {
        if (data == null) { return Page.of(); }
        String scrollId = data.getScrollId();
        Integer pageNum = data.getPageNum();
        Integer pageSize = data.getPageSize();
        Integer pageCount = data.getPageCount();
        Dict others = data.getOthers();
        List<T> tList = BeanUtil.beanToBeanInList(data.getData(), clazz);
        Page<T> page = Page.of(pageNum, pageSize, pageCount, data.getTotal(), tList);
        page.setScrollId(scrollId);
        page.setOthers(others);
        return page;
    }

}
