package kunlun.flow.approval;

import java.util.Map;

/**
 * 审批单抽象对象.
 * @author Kahle
 */
public interface ApprovalTicket {

    /**
     * 获取业务类型
     * @return 业务类型
     */
    Object getBizType();

    /**
     * 获取业务ID
     * @return 业务ID
     */
    Object getBizId();

    /**
     * 获取业务编码
     * @return 业务编码
     */
    Object getBizNum();

    /**
     * 获取审批单名称.
     * @return 审批单名称
     */
    String getName();

    /**
     * 获取审批单内容.
     * @return 审批单内容
     */
    String getContent();

    /**
     * 获取其他数据（扩展数据）.
     * @return 其他数据（扩展数据）
     */
    Map<String, Object> getOthers();

}
