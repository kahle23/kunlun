/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.event;

import kunlun.action.ActionUtil;
import kunlun.data.Event;
import kunlun.data.event.EventCollector;
import kunlun.action.event.support.SimpleEventCollector;
import org.junit.After;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

/**
 * The event util Test.
 * @author Kahle
 */
public class EventUtilTest {
    private final List<Registered> registeredConsumers = new ArrayList<Registered>();
    private final List<String> registeredCollectorNames = new ArrayList<String>();

    @After
    public void cleanup() {
        // 只摘除本测试自己挂载的消费者，保留预挂的默认消费者（如 LogEventConsumer）。
        for (Registered registered : registeredConsumers) {
            EventUtil.deregisterConsumer(registered.eventType, registered.consumer);
        }
        registeredConsumers.clear();
        for (String name : registeredCollectorNames) {
            EventUtil.deregisterCollector(name);
        }
        registeredCollectorNames.clear();
        // 恢复默认收集器的预挂状态，避免影响其他测试。
        EventUtil.deregisterCollector(EventProvider.DEFAULT_COLLECTOR_NAME);
        EventUtil.registerCollector(EventProvider.DEFAULT_COLLECTOR_NAME, new SimpleEventCollector());
    }

    private void register(String eventType, EventConsumer consumer) {
        EventUtil.registerConsumer(eventType, consumer);
        Registered registered = new Registered();
        registered.eventType = eventType;
        registered.consumer = consumer;
        registeredConsumers.add(registered);
    }

    private void registerCollector(String name, EventCollector collector) {
        EventUtil.registerCollector(name, collector);
        registeredCollectorNames.add(name);
    }

    @Test
    public void testDefaultCollectorAndConsumerPreRegistered() {
        assertNotNull("default collector should be pre-registered",
                EventUtil.getCollector(EventProvider.DEFAULT_COLLECTOR_NAME));
        List<EventConsumer> consumers = EventUtil.getConsumers(Event.ANY);
        assertTrue("default log consumer should be pre-registered on ANY",
                consumers != null && !consumers.isEmpty());
    }

    @Test
    public void testDefaultCollectorBridgesToConsumers() {
        RecordingConsumer consumer = new RecordingConsumer();
        register("test-default-bridge", consumer);
        EventUtil.collect(Event.of("test-default-bridge").appendMessage("via default collector"));
        assertEquals("default collector's push should bridge to consume", 1, consumer.names.size());
    }

    @Test
    public void testCollectAutoFillsTime() {
        Event event = Event.of("info:time").appendMessage("auto fill time");
        EventUtil.collect(event);
        assertTrue("time should be auto-filled by default collector's collect", event.getTime() != null);
    }

    @Test
    public void testCollectSwallowsInvalidEvent() {
        RecordingConsumer any = new RecordingConsumer();
        register(Event.ANY, any);
        // 事件名为空/空串：收集器内部校验失败被自吞，不影响调用方，也不会进入分发。
        EventUtil.collect(Event.of(""));
        EventUtil.collect(Event.of(null));
        assertEquals("invalid events should be swallowed before dispatch", 0, any.names.size());
    }

    @Test
    public void testCollectItselfDoesNotDispatchConsumers() {
        // 收集器只路由不消费：非桥接型收集器（RecordingCollector 不调用 consume）投递后消费者收不到
        RecordingCollector collector = new RecordingCollector();
        registerCollector("test-no-bridge", collector);
        RecordingConsumer consumer = new RecordingConsumer();
        register("test-no-bridge-type", consumer);
        Event event = Event.of("test-no-bridge-type");
        EventUtil.collect("test-no-bridge", event);
        assertSame("the named collector should be invoked", event, collector.lastPushed);
        assertEquals("collect itself must not dispatch consumers", 0, consumer.names.size());
        EventUtil.consume(event);
        assertEquals("consume dispatches consumers", 1, consumer.names.size());
    }

    @Test
    public void testConsumerDispatchByEventType() {
        RecordingConsumer first = new RecordingConsumer();
        RecordingConsumer second = new RecordingConsumer();
        RecordingConsumer other = new RecordingConsumer();
        register("test-type-1", first);
        register("test-type-1", second);
        register("test-type-2", other);
        EventUtil.consume(Event.of("test-type-1").appendMessage("hello"));
        assertEquals(1, first.names.size());
        assertEquals("test-type-1", first.names.get(0));
        assertEquals(1, second.names.size());
        assertEquals("consumer of other type must not receive", 0, other.names.size());
    }

    @Test
    public void testSameConsumerOnMultipleTypes() {
        RecordingConsumer consumer = new RecordingConsumer();
        register("test-multi-1", consumer);
        register("test-multi-2", consumer);
        EventUtil.consume(Event.of("test-multi-1"));
        EventUtil.consume(Event.of("test-multi-2"));
        assertEquals("one consumer mounted on two types should receive both", 2, consumer.names.size());
    }

    @Test
    public void testWildcardConsumer() {
        RecordingConsumer any = new RecordingConsumer();
        register(Event.ANY, any);
        EventUtil.consume(Event.of("test-any-1"));
        EventUtil.consume(Event.of("test-any-2"));
        assertEquals(2, any.names.size());
    }

    @Test
    public void testRegisterDedup() {
        RecordingConsumer consumer = new RecordingConsumer();
        register("test-dedup", consumer);
        register("test-dedup", consumer);
        EventUtil.consume(Event.of("test-dedup"));
        assertEquals("same consumer registered twice should receive once", 1, consumer.names.size());
    }

    @Test
    public void testConsumerExceptionIsolation() {
        EventConsumer bad = new EventConsumer() {
            @Override
            public void consume(Collection<Event> events) {
                throw new IllegalStateException("boom");
            }
        };
        RecordingConsumer good = new RecordingConsumer();
        register("test-isolation", bad);
        register("test-isolation", good);
        EventUtil.consume(Event.of("test-isolation"));
        assertEquals("other consumers must still receive", 1, good.names.size());
    }

    @Test
    public void testBatchDispatchGroupsByEventType() {
        BatchRecordingConsumer typed = new BatchRecordingConsumer();
        RecordingConsumer any = new RecordingConsumer();
        register(Event.CHANGE_LOG, typed);
        register(Event.ANY, any);
        EventUtil.consume(Arrays.asList(
                Event.of(Event.CHANGE_LOG), Event.of("info:other"), Event.of(Event.CHANGE_LOG)));
        assertEquals("same-type events should be dispatched as one batch", 1, typed.batchSizes.size());
        assertEquals(Integer.valueOf(2), typed.batchSizes.get(0));
        assertEquals("wildcard consumer should receive the whole batch", 3, any.names.size());
    }

    @Test
    public void testBatchCollectAutoFillsTime() {
        Event first = Event.of("info:batch-time");
        Event second = Event.of("info:batch-time");
        EventUtil.collect(Arrays.asList(first, second));
        assertTrue(first.getTime() != null);
        assertTrue(second.getTime() != null);
    }

    @Test
    public void testCollectorRouteByDefaultName() {
        RecordingCollector collector = new RecordingCollector();
        EventUtil.registerCollector(EventProvider.DEFAULT_COLLECTOR_NAME, collector);
        Event event = Event.of("test-collector-route");
        EventUtil.collect(event);
        assertSame(event, collector.lastProcessed);
        assertSame(event, collector.lastPushed);
        assertSame(collector, EventUtil.getCollector(EventProvider.DEFAULT_COLLECTOR_NAME));
        EventUtil.deregisterCollector(EventProvider.DEFAULT_COLLECTOR_NAME);
        assertNull(EventUtil.getCollector(EventProvider.DEFAULT_COLLECTOR_NAME));
    }

    @Test
    public void testCollectorRouteBySpecifiedName() {
        RecordingCollector collectorA = new RecordingCollector();
        RecordingCollector collectorB = new RecordingCollector();
        registerCollector("test-collector-a", collectorA);
        registerCollector("test-collector-b", collectorB);
        Event event = Event.of("test-named-route");
        EventUtil.collect("test-collector-a", event);
        assertSame("the named collector should be invoked", event, collectorA.lastPushed);
        assertNull("collectors of other names must not be invoked", collectorB.lastPushed);
        // 未注册的收集器名：跳过不抛异常。
        EventUtil.collect("not-registered-collector", Event.of("test-named-route"));
        assertNull(collectorB.lastPushed);
    }

    @Test
    public void testNullAndBlankCollectorNameRoutesToDefault() {
        RecordingCollector collector = new RecordingCollector();
        EventUtil.registerCollector(EventProvider.DEFAULT_COLLECTOR_NAME, collector);
        Event first = Event.of("test-null-name");
        EventUtil.collect(null, first);
        assertSame("null name should route to the default collector", first, collector.lastCollected);
        Event second = Event.of("test-blank-name");
        EventUtil.collect("  ", second);
        assertSame("blank name should route to the default collector", second, collector.lastCollected);
    }

    @Test
    public void testConsumeDoesNotInvokeCollectors() {
        RecordingCollector collector = new RecordingCollector();
        registerCollector("test-consume-no-collector", collector);
        RecordingConsumer consumer = new RecordingConsumer();
        register("test-consume-only", consumer);
        Event event = Event.of("test-consume-only");
        EventUtil.consume(event);
        assertEquals(1, consumer.names.size());
        assertNull("collectors must not be invoked by consume", collector.lastPushed);
    }

    @Test
    public void testMergeFromActionEntry() {
        RecordingConsumer consumer = new RecordingConsumer();
        register("test-merge", consumer);
        ActionUtil.execute(Event.of("test-merge").appendMessage("from new entry"));
        ActionUtil.execute(kunlun.data.Event.of("test-merge").appendMessage("from action entry"));
        assertEquals("both entries should reach the consumer", 2, consumer.names.size());
    }

    private static class Registered {
        private String eventType;
        private EventConsumer consumer;
    }

    private static class RecordingConsumer implements EventConsumer {
        private final List<String> names = new ArrayList<String>();

        @Override
        public void consume(Collection<Event> events) {
            for (Event event : events) {
                names.add(event.getName());
            }
        }
    }

    private static class BatchRecordingConsumer implements EventConsumer {
        private final List<Integer> batchSizes = new ArrayList<Integer>();

        @Override
        public void consume(Collection<Event> events) {
            batchSizes.add(events.size());
        }
    }

    private static class RecordingCollector implements EventCollector {
        private Event lastCollected;
        private Event lastProcessed;
        private Event lastPushed;

        @Override
        public void collect(Collection<Event> events) {
            if (events == null || events.isEmpty()) { return; }
            lastCollected = events.iterator().next();
            process(events);
            push(events);
        }

        @Override
        public void process(Event event) {
            lastProcessed = event;
        }

        @Override
        public void push(Event event) {
            lastPushed = event;
        }

        @Override
        public void process(Collection<Event> events) {
            for (Event event : events) {
                process(event);
            }
        }

        @Override
        public void push(Collection<Event> events) {
            for (Event event : events) {
                push(event);
            }
        }
    }

}
