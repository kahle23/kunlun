/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.flow.approval.biz;

import kunlun.flow.approval.ApprovalTicket;

import java.util.Map;

/**
 * 审批流程的业务处理器的抽象接口.
 * @author Kahle
 */
public interface BizProcessor {

    // region ======== config ========

    /**
     * 该流程业务处理器的所处命名空间（namespace）.
     * @return 所处命名空间（namespace），可以为空
     */
    String getNamespace();

    /**
     * 获取流程的业务 Key.
     * @return 流程的业务 Key
     */
    String getBusinessKey();

    /**
     * 获取流程模型的 Key.
     * @return 流程模型的 Key
     */
    String getFlowModelKey();

    // endregion ======== config ========


    // region ======== build ========

    /**
     * 构建审批单内容.
//     * @param businessId businessId
     * @param ticket ticket
     * @return TicketText
     */
//    Pair<String, String> buildTicketText(String businessId, Map<String, Object> ticket);
    <T extends ApprovalTicket> T buildTicketContent(T ticket);

    // endregion


    // region ======== 审批单相关方法 ========

    /**
     * 在审批单 创建前 的处理逻辑.
     * @param businessId businessId
     * @param ticket ticket
     */
    void beforeTicketCreate(String businessId, Map<String, Object> ticket);

    /**
     * 在审批单 创建后 的处理逻辑.
     * @param businessId businessId
     * @param ticket ticket
     */
    void afterTicketCreate(String businessId, Map<String, Object> ticket);

    /**
     * 在审批单 提交前 的处理逻辑.
     * @param businessId businessId
     * @param ticket ticket
     */
    void beforeTicketSubmit(String businessId, Map<String, Object> ticket);

    /**
     * 在审批单 提交后 的处理逻辑.
     * @param businessId businessId
     * @param ticket ticket
     */
    void afterTicketSubmit(String businessId, Map<String, Object> ticket);

    /**
     * 在审批单 取消前 的处理逻辑.
     * @param businessId businessId
     * @param ticket ticket
     */
    void beforeTicketCancel(String businessId, Map<String, Object> ticket);

    /**
     * 在审批单 取消后 的处理逻辑.
     * @param businessId businessId
     * @param ticket ticket
     */
    void afterTicketCancel(String businessId, Map<String, Object> ticket);

    // endregion


    // region ======== 审批操作相关方法 ========

    /**
     * 在审批 操作前 的处理逻辑.
     * @param businessId businessId
     * @param ticket ticket
     * @param approvalData approvalData
     */
    void beforeApprovalOperation(String businessId, Map<String, Object> ticket, Map<String, Object> approvalData);

    /**
     * 在审批 操作后 的处理逻辑.
     * @param businessId businessId
     * @param ticket ticket
     * @param approvalData approvalData
     */
    void afterApprovalOperation(String businessId, Map<String, Object> ticket, Map<String, Object> approvalData);

    // endregion


    // region ======== 审批完成相关方法 ========

    /**
     * 在审批 完成时 的处理逻辑.
     * @param businessId businessId
     * @param ticket ticket
     */
    void onApprovalCompleted(String businessId, Map<String, Object> ticket);

    // endregion


    /**
     * The business processor manager.
     * @author Kahle
     */
    interface Manager {

        /**
         * register
         * @param processor processor
         */
        void register(BizProcessor processor);

        /**
         * deregister
         * @param namespace namespace
         * @param businessKey businessKey
         */
        void deregister(String namespace, String businessKey);

        /**
         * get BusinessProcessor
         * @param namespace namespace
         * @param businessKey businessKey
         * @return BusinessProcessor
         */
        BizProcessor get(String namespace, String businessKey);

        /**
         * getOrThrow BusinessProcessor
         * @param namespace namespace
         * @param businessKey businessKey
         * @return BusinessProcessor
         */
        BizProcessor getOrThrow(String namespace, String businessKey);

    }

}
