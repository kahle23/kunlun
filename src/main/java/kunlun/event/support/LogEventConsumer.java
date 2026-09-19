/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.event.support;

import kunlun.data.Event;
import kunlun.event.EventConsumer;
import kunlun.logging.Logger;
import kunlun.logging.LoggerFactory;
import kunlun.time.DateUtil;

import java.util.Collection;

import static kunlun.common.constant.Numbers.FIVE_HUNDRED;
import static kunlun.common.constant.Numbers.ZERO;
import static kunlun.common.constant.Symbols.NEWLINE;

/**
 * 日志事件消费者 —— 将事件摘要打印到日志系统的通用消费者，可自由组合挂载：
 * <ul>
 *   <li>默认由 {@code SimpleEventProvider} 预挂到 {@link kunlun.data.Event#ANY}（任意类型都打印），
 *       承接原简单收集器的日志兜底职责；</li>
 *   <li>挂载类型由注册处决定，也可单独挂到某一事件类型（如排障时仅为变更日志追加打印），
 *       或与其他消费者、收集器并存（如 MySQL 收集器落库 + 本消费者打印双路）。</li>
 * </ul>
 *
 * @author Kahle
 */
public class LogEventConsumer implements EventConsumer {
    private static final Logger log = LoggerFactory.getLogger(LogEventConsumer.class);

    /**
     * 打印单条事件摘要（消息超过 500 字符时截断显示）。
     *
     * @param event 待打印的事件记录
     */
    protected void show(Event event) {
        if (event == null) { return; }
        String message = String.valueOf(event.getMessage());
        if (message.length() > FIVE_HUNDRED) {
            message = message.substring(ZERO, FIVE_HUNDRED) + " ...";
        }
        String content = NEWLINE +
                "---- Begin Event ----" + NEWLINE +
                "Name:           " + event.getName() + NEWLINE +
                "Time:           " + DateUtil.format(event.getTime()) + NEWLINE +
                "UserId:         " + event.getUserId() + NEWLINE +
                "Message:        " + message + NEWLINE +
                "Consumer:       " + getClass().getName() + NEWLINE +
                "---- End Event ----" + NEWLINE;
        log.info(content);
    }

    @Override
    public void consume(Collection<Event> events) {
        if (events == null) { return; }
        for (Event event : events) {
            show(event);
        }
    }

}
