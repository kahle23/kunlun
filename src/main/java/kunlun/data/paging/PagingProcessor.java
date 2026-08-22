package kunlun.data.paging;

import kunlun.common.Paging;

/**
 * 分页请求处理器.
 * @author Kahle
 */
public interface PagingProcessor {

    Integer getDefaultPageNum();

    void setDefaultPageNum(Integer defaultPageNum);

    Integer getDefaultPageSize();

    void setDefaultPageSize(Integer defaultPageSize);

    Integer getMaxPageSize();

    void setMaxPageSize(Integer maxPageSize);

    Object process(Paging paging);

}
