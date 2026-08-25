package kunlun.data.paging;

import kunlun.common.Paging;

/**
 * 分页请求处理器.
 * <p>计划与 {@link PageResultProcessor} 合并为一个分页适配器接口，理由：两者本是同一分页技术的一体两面，
 * 实现与注册永远成对出现（请求侧 startPage 的产物只有配套的结果侧才认识）、生命周期完全同步，
 * 且合并后空结果组装可在结果侧直接补默认 pageNum/pageSize（Page 无需反向依赖 PageUtil）；
 * 接口已随正式版发布、下游有直接接线点，需经桥接（新接口 extends 两者）过渡后大版本落地。
 * @author Kahle
 */
// todo 与 PageResultProcessor 合并为一个分页适配器接口（本次版本不改，理由见上方注释）
public interface PagingProcessor {

    Integer getDefaultPageNum();

    void setDefaultPageNum(Integer defaultPageNum);

    Integer getDefaultPageSize();

    void setDefaultPageSize(Integer defaultPageSize);

    Integer getMaxPageSize();

    void setMaxPageSize(Integer maxPageSize);

    Object process(Paging paging);

}
