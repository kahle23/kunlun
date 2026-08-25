package kunlun.data.paging;

import kunlun.common.Page;

import java.util.List;

/**
 * 分页结果处理器.
 * <p>计划与 {@link PagingProcessor} 合并为一个分页适配器接口，理由：两者本是同一分页技术的一体两面，
 * 实现与注册永远成对出现、生命周期完全同步，且合并后空结果组装可在本侧直接补默认
 * pageNum/pageSize（Page 无需反向依赖 PageUtil）；接口已随正式版发布、下游有直接接线点，
 * 需经桥接（新接口 extends 两者）过渡后大版本落地。
 * @author Kahle
 */
// todo 与 PagingProcessor 合并为一个分页适配器接口（本次版本不改，理由见上方注释）
public interface PageResultProcessor {

    <T> Page<T> process(List<T> data);

    <F, T> Page<T> process(List<F> data, Class<T> clazz);

    <F, T> Page<T> process(Page<F> data, Class<T> clazz);

}
