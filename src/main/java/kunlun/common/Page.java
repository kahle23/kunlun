/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.common;

import kunlun.data.Dict;

import java.io.Serializable;
import java.util.List;

/**
 * 统一的分页数据输出对象.
 * <p>维护约定：本类定位是纯数据对象，现有 of(...) 构建方法与 setter 已能满足设值需求，
 * 如非必要不要继续为本类新增构建方法；"空结果""默认值"之类的组装语义应在调用者侧实现。
 * @param <T> 分页数据的类型
 * @author Kahle
 */
public class Page<T> implements Serializable {


    // region ======== 分页数据对象静态构建方法 ========

    public static <T> Page<T> of(Integer pageNum, Integer pageSize, Integer pageCount, Long total, List<T> data) {
        Page<T> page = new Page<T>();
        page.setPageNum(pageNum);
        page.setPageSize(pageSize);
        page.setPageCount(pageCount);
        page.setTotal(total);
        page.setData(data);
        return page;
    }

    public static <T> Page<T> of(String scrollId, Integer pageSize, List<T> data) {
        Page<T> page = new Page<T>();
        page.setScrollId(scrollId);
        page.setPageSize(pageSize);
        page.setData(data);
        return page;
    }

    public static <T> Page<T> of(List<T> data) {
        Page<T> page = new Page<T>();
        page.setData(data);
        return page;
    }

    public static <T> Page<T> of() {

        return new Page<T>();
    }
    // endregion


    // region ======== 分页数据对象属性和方法 ========
    /**
     * 滚动 ID
     */
    private String  scrollId;
    /**
     * 页码
     */
    private Integer pageNum;
    /**
     * 每页条数
     */
    private Integer pageSize;
    /**
     * 页数
     */
    private Integer pageCount;
    /**
     * 总条数
     */
    private Long    total;
    /**
     * 分页数据
     */
    private List<T> data;
    /**
     * 其他扩展数据
     */
    private Dict    others;

    public Page() {

        this.others = Dict.of();
    }

    public String getScrollId() {

        return scrollId;
    }

    public void setScrollId(String scrollId) {

        this.scrollId = scrollId;
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

    public Integer getPageCount() {

        return pageCount;
    }

    public void setPageCount(Integer pageCount) {

        this.pageCount = pageCount;
    }

    public Long getTotal() {

        return total;
    }

    public void setTotal(Long total) {

        this.total = total;
    }

    public List<T> getData() {

        return data;
    }

    public void setData(List<T> data) {

        this.data = data;
    }

    public Dict getOthers() {

        return others;
    }

    public void setOthers(Dict others) {

        this.others = others;
    }
    // endregion


    // region ======== 分页查询对象抽象类 ========
    /**
     * 分页相关查询对象.
     * @author Kahle
     */
    public static abstract class Query implements Serializable {
        /**
         * 是否分页
         */
        private boolean paged = true;
        /**
         * 滚动 ID
         */
        private String  scrollId;
        /**
         * 滚动分页的排序（默认为：false 倒序，true 为升序）<br />
         * （注意：滚动分页除了基于滚动ID的条件进行排序外，不能有别的排序条件）<br />
         * （当非滚动排序时，此字段为空，由此可以作为是否增加额外排序字段的控制器）<br />
         */
        private Boolean scrollByAsc;
        /**
         * 页码
         */
        private Integer pageNum;
        /**
         * 每页条数
         */
        private Integer pageSize;

        public boolean isPaged() {

            return paged;
        }

        public void setPaged(boolean paged) {

            this.paged = paged;
        }

        public String getScrollId() {

            return scrollId;
        }

        public void setScrollId(String scrollId) {

            this.scrollId = scrollId;
        }

        public Boolean getScrollByAsc() {

            return scrollByAsc;
        }

        public void setScrollByAsc(Boolean scrollByAsc) {

            this.scrollByAsc = scrollByAsc;
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
    }
    // endregion

}
