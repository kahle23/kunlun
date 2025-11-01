package kunlun.util;

import kunlun.common.Page;
import kunlun.common.Paging;
import kunlun.common.constant.Nil;
import kunlun.data.Dict;
import kunlun.data.paging.PageResultProcessor;
import kunlun.data.paging.PagingProcessor;
import kunlun.data.paging.SerialNumberFiller;
import kunlun.data.paging.support.PageResultProcessorImpl;
import kunlun.data.paging.support.PagingProcessorImpl;
import kunlun.data.paging.support.SerialNumberFillerImpl;

import java.util.Collection;
import java.util.List;

import static kunlun.util.Assert.notNull;

public class PageUtil {
    private static SerialNumberFiller serialNumberFiller;
    private static PagingProcessor pagingProcessor;
    private static PageResultProcessor pageResultProcessor;

    public static SerialNumberFiller getSerialNumberFiller() {
        if (serialNumberFiller != null) { return serialNumberFiller; }
        synchronized (PageUtil.class) {
            if (serialNumberFiller != null) { return serialNumberFiller; }
            PageUtil.setSerialNumberFiller(new SerialNumberFillerImpl());
            return serialNumberFiller;
        }
    }

    public static void setSerialNumberFiller(SerialNumberFiller serialNumberFiller) {

        PageUtil.serialNumberFiller = notNull(serialNumberFiller);
    }

    public static PagingProcessor getPagingProcessor() {
        if (pagingProcessor != null) { return pagingProcessor; }
        synchronized (PageUtil.class) {
            if (pagingProcessor != null) { return pagingProcessor; }
            PageUtil.setPagingProcessor(new PagingProcessorImpl());
            return pagingProcessor;
        }
    }

    public static void setPagingProcessor(PagingProcessor pagingProcessor) {

        PageUtil.pagingProcessor = notNull(pagingProcessor);
    }

    public static PageResultProcessor getPageResultProcessor() {
        if (pageResultProcessor != null) { return pageResultProcessor; }
        synchronized (PageUtil.class) {
            if (pageResultProcessor != null) { return pageResultProcessor; }
            PageUtil.setPageResultProcessor(new PageResultProcessorImpl());
            return pageResultProcessor;
        }
    }

    public static void setPageResultProcessor(PageResultProcessor pageResultProcessor) {

        PageUtil.pageResultProcessor = notNull(pageResultProcessor);
    }



    public static String getDefaultSerialNumberFieldName() {

        return getSerialNumberFiller().getDefaultSerialNumberFieldName();
    }

    public static void setDefaultSerialNumberFieldName(String defaultSerialNumberFieldName) {

        getSerialNumberFiller().setDefaultSerialNumberFieldName(defaultSerialNumberFieldName);
    }

    public static <T> void fillSerialNumber(Collection<T> data, int pageIndex, int pageSize) {

        fillSerialNumber(data, Nil.STR, pageIndex, pageSize);
    }

    public static <T> void fillSerialNumber(Collection<T> data, String fieldName, int pageIndex, int pageSize) {

        getSerialNumberFiller().fill(data, fieldName, pageIndex, pageSize);
    }

    public static Integer getDefaultPageNum() {

        return getPagingProcessor().getDefaultPageNum();
    }

    public static void setDefaultPageNum(Integer defaultPageNum) {

        getPagingProcessor().setDefaultPageNum(defaultPageNum);
    }

    public static Integer getDefaultPageSize() {

        return getPagingProcessor().getDefaultPageSize();
    }

    public static void setDefaultPageSize(Integer defaultPageSize) {

        getPagingProcessor().setDefaultPageSize(defaultPageSize);
    }


    public static Object startPage(Integer pageNum, Integer pageSize) {

        return PageUtil.startPage(pageNum, pageSize, true, null);
    }

    public static Object startPage(Integer pageNum, Integer pageSize, boolean doCount) {

        return PageUtil.startPage(pageNum, pageSize, doCount, null);
    }

    public static Object startPage(Integer pageNum, Integer pageSize, String orderBy) {

        return PageUtil.startPage(pageNum, pageSize, true, orderBy);
    }

    public static Object startPage(Integer pageNum, Integer pageSize, boolean doCount, String orderBy) {
        Paging paging = Paging.of(pageNum, pageSize
                , Dict.of("doCount", doCount).set("orderBy", orderBy));
        return getPagingProcessor().process(paging);
    }

    public static <T> Page<T> handleResult(List<T> data) {

        return getPageResultProcessor().process(data);
    }

    public static <F, T> Page<T> handleResult(List<F> data, Class<T> clazz) {

        return getPageResultProcessor().process(data, clazz);
    }

    public static <F, T> Page<T> handleResult(Page<F> data, Class<T> clazz) {

        return getPageResultProcessor().process(data, clazz);
    }

}
