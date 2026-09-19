/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.notification.model;

import java.io.Serializable;
import java.util.Map;

/**
 * 通知受理回执 —— {@link kunlun.notification.NotificationSender#send(java.util.Collection)} 与
 * {@link kunlun.notification.NotificationDeliverer#deliver(java.util.Collection)} 的批量返回值。
 * <p>
 * 回执只代表<b>本次调用受理成功</b>：同步通道（短信 / 邮件等）受理即送达；异步通道
 * （经 MQ / MySQL 中转）受理仅代表已入队 / 已落库，最终送达状态由各通道的回执机制另行告知。
 * 失败不经回执返回而是直接抛异常（Java 侧调用三方接口的惯例也是转成异常），
 * 故本对象不承载成败计数与汇总消息 —— 异步场景在受理时点本就无法得知。
 * <p>
 * <b>批量进、摘要出</b>是受理语义下唯一诚实的形状：传输层的批量就是一个动作
 * （N 条通知打进一条 MQ 消息 / 一次批量写入 / 一次网关提交），受理结果天然是一个，
 * 逐条结果要么不可知、要么只能编造占位符 —— 故不做多出参（{@code List<NotificationRt>} 形态）。
 * 同步通道确实拿到逐条回执时（如短信网关按手机号返回状态），按自定义 key 塞进
 * {@code details}；同步逐条语义的实现将来在具体实现类上扩展（扩展方法或能力接口），
 * 不改基础契约 —— 契约跟着最普遍的实现形态走，扩展跟着具体实现走。
 * <p>
 * 回执经 {@code NotificationProvider} 透传给发送 / 投递调用方：{@code send} 一对一路由，
 * 原样透传发送器回执；{@code deliver} 多通道扇出时返回<b>合并回执</b> —— {@code details}
 * 的 key 为通道名、value 为该通道投递器的回执（本类型嵌套一层、不拍平合并，
 * 避免各通道实现自定义的明细 key 相互覆盖）。
 * <p>
 * {@code details} 承载通道 / 传输层各自的回执明细（如短信回执 ID、MQ 消息 ID），
 * key 约定由实现自定义。当前仅此一字段仍保留对象包装而非直接返回 Map，
 * 为将来新增回执字段不改方法签名留余地。
 * <p>
 * 回执对象只做受理结果承载，不参与重试与补偿 —— 尽力而为语义下的失败明细由实现方自行记录。
 *
 * @author Kahle
 * @see kunlun.notification.NotificationSender
 * @see kunlun.notification.NotificationDeliverer
 */
public class NotificationRt implements Serializable {
    /**
     * 回执明细：承载通道 / 传输层各自的回执信息（如短信回执 ID、MQ 消息 ID），
     * key 约定由实现自定义。
     */
    private Map<String, Object> details;

    public NotificationRt() {

    }

    public NotificationRt(Map<String, Object> details) {

        this.details = details;
    }


    // region ======== 读取与写入（getter / setter） ========

    public Map<String, Object> getDetails() {

        return details;
    }

    public void setDetails(Map<String, Object> details) {

        this.details = details;
    }
    // endregion

}
