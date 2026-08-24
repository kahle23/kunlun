/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.message.model;

import kunlun.message.MessageBus;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 消息总线上的消息单元 —— 生产者-消费者/发布-订阅模型的投递载荷，
 * 回答<i>「怎么送达」</i>，面向总线订阅者（设备、客户端、其他服务）。
 *
 * <p>本类继承自 {@link kunlun.message.MessageBus.Base}，与 {@link kunlun.message.MessageBus}
 * 共同构成消息总线契约：{@code MessageBus} 提供 {@code send}/{@code receive}/{@code subscribe}
 * 的总线能力，{@code Message} 则是流转于其上的消息单元，携带投递主题（{@code topic}，来自 Base）、
 * 投递属性（{@code properties}，来自 Base）、消息标识（{@code id}）与载荷（{@code body}）。
 *
 * <h3>定位</h3>
 * <ul>
 *   <li><b>用途：</b>作为总线上的载荷按 {@code topic} 投递/订阅，{@code properties}
 *       承载总线层投递属性（QoS、优先级、回执、重试策略等）。</li>
 *   <li><b>受众：</b>总线订阅者（设备 / 客户端 / 其他服务），不直接面向人阅读。</li>
 *   <li><b>生命周期：</b>即时投递，送达即完成；默认不持久、无已读状态
 *       （是否持久化由 {@code MessageBus} 实现决定，不在本模型上承载）。</li>
 *   <li><b>只管传输，不管语义：</b>标题、内容、图标、接收人等内容语义归属
 *       {@link kunlun.notification.model.Notification}；{@code Message} 只承载
 *       要送达的载荷本身，不关心它表达什么。</li>
 *   <li><b>独立于通知：</b>通知发送时<i>可</i>转成 {@code Message} 上总线投递
 *       （MQTT / WebSocket / Kafka 等皆为其一种 {@code MessageBus} 实现），
 *       但 {@code Message} 也可承载非通知类载荷（事件总线订阅、系统间数据同步、
 *       跨服务消息等）。它是独立载体，不是 Notification 的附属。</li>
 * </ul>
 *
 * <h3>三载体关系</h3>
 * <pre>
 *   Event（发生了什么）——订阅/转化——&gt; Notification（告诉谁）
 *   Notification——发送——&gt; Message（怎么送达：经 MessageBus 上总线投递）
 * </pre>
 * <ul>
 *   <li>{@link kunlun.data.Event} = 审计事实（长期、归档）。</li>
 *   <li>{@link kunlun.notification.model.Notification} = 用户消息（瞬时、已读/未读）。</li>
 *   <li>{@code Message} = 总线消息单元（投递载荷）。</li>
 * </ul>
 * 三者均经 {@code Action} 总线调度，但保持各自独立载体，不得合并。
 *
 * @author Kahle
 * @see kunlun.message.MessageBus
 * @see kunlun.data.Event
 * @see kunlun.notification.model.Notification
 */
public class Message extends MessageBus.Base {
    private String id;
    private Object body; // todo byte[] ? is ok ?

    public Message(String topic, Object body, Map<String, Object> properties) {
        super(topic, properties);
        this.body = body;
    }

    public Message(String topic, Object body) {
        super(topic, new LinkedHashMap<String, Object>());
        this.body = body;
    }

    public Message(String topic) {

        super(topic, new LinkedHashMap<String, Object>());
    }

    public Message() {

        this.setProperties(new LinkedHashMap<String, Object>());
    }

    public String getId() {

        return id;
    }

    public void setId(String id) {

        this.id = id;
    }

    public Object getBody() {

        return body;
    }

    public void setBody(Object body) {

        this.body = body;
    }

}
