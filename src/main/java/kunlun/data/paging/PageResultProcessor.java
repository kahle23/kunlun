package kunlun.data.paging;

import kunlun.common.Page;

import java.util.List;

/**
 * 分页结果处理器.
 * @author Kahle
 */
public interface PageResultProcessor {

    <T> Page<T> process(List<T> data);

    <F, T> Page<T> process(List<F> data, Class<T> clazz);

    <F, T> Page<T> process(Page<F> data, Class<T> clazz);

}
