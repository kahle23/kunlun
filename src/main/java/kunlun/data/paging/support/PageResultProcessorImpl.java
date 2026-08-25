package kunlun.data.paging.support;

import kunlun.common.Page;
import kunlun.common.constant.Nil;
import kunlun.data.Dict;
import kunlun.data.bean.BeanUtil;
import kunlun.data.paging.PageResultProcessor;

import java.util.Collections;
import java.util.List;

import static kunlun.common.constant.Numbers.ONE;
import static kunlun.common.constant.Numbers.ZERO;

public class PageResultProcessorImpl implements PageResultProcessor {

    /**
     * 创建"空结果"的分页对象：data 为空集合，total、pageCount 为 0.
     * <p>（pageNum、pageSize 不预填：默认值属 PagingProcessor 职责，有页面上下文的调用方自行设置；
     * protected 供继承类复用）
     */
    protected <T> Page<T> createPage() {

        return createPage(Collections.<T>emptyList());
    }

    /**
     * 组装普通 List（非分页）的查询结果：total 为条数、pageCount 为 1（0 条时为 0），
     * 即"非分页结果 = 单页全量".
     * <p>（pageNum、pageSize 不预填，约定同 {@link #createPage()}；protected 供继承类复用）
     */
    protected <T> Page<T> createPage(List<T> data) {

        return Page.of(Nil.INT, Nil.INT, data.isEmpty() ? ZERO : ONE, (long) data.size(), data);
    }

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
        if (data == null) { return createPage(); }
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
