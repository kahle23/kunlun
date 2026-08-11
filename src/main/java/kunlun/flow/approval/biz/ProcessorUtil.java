/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.flow.approval.biz;

import kunlun.common.constant.Nil;
import kunlun.logging.Logger;
import kunlun.logging.LoggerFactory;
import kunlun.util.Assert;
import kunlun.util.StrUtil;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static kunlun.common.constant.Symbols.EMPTY_STRING;
import static kunlun.common.constant.Symbols.MINUS;

/**
 * The processor tools.
 * @author Kahle
 */
public class ProcessorUtil {
    private static final Logger log = LoggerFactory.getLogger(ProcessorUtil.class);
    private static BizProcessor.Manager processorManager;

    public static BizProcessor.Manager getProcessorManager() {
        if (processorManager != null) { return processorManager; }
        synchronized (ProcessorUtil.class) {
            if (processorManager != null) { return processorManager; }
            ProcessorUtil.setProcessorManager(new ProcessorManager());
            return processorManager;
        }
    }

    public static void setProcessorManager(BizProcessor.Manager processorManager) {
        Assert.notNull(processorManager, "Parameter \"processorManager\" must not null. ");
        log.debug("Set processor manager: {}", processorManager.getClass().getName());
        ProcessorUtil.processorManager = processorManager;
    }

    public static void register(BizProcessor processor) {

        getProcessorManager().register(processor);
    }

    public static void deregister(String namespace, String businessKey) {

        getProcessorManager().deregister(namespace, businessKey);
    }

    public static void deregister(String businessKey) {

        getProcessorManager().deregister(Nil.STR, businessKey);
    }

    public static BizProcessor get(String namespace, String businessKey) {

        return getProcessorManager().get(namespace, businessKey);
    }

    public static BizProcessor get(String businessKey) {

        return getProcessorManager().get(Nil.STR, businessKey);
    }

    public static BizProcessor getOrThrow(String namespace, String businessKey) {

        return getProcessorManager().getOrThrow(namespace, businessKey);
    }

    public static BizProcessor getOrThrow(String businessKey) {

        return getProcessorManager().getOrThrow(Nil.STR, businessKey);
    }


    /**
     * ProcessorManagerImpl
     * @author Kahle
     */
    public static class ProcessorManager implements BizProcessor.Manager {
        private static final Logger log = LoggerFactory.getLogger(ProcessorManager.class);
        protected final Map<String, BizProcessor> processors;

        public ProcessorManager(Map<String, BizProcessor> processors) {

            this.processors = Assert.notNull(processors);
        }

        public ProcessorManager() {

            this(new ConcurrentHashMap<String, BizProcessor>());
        }

        protected String buildMapKey(String namespace, String businessKey) {
            Assert.notBlank(businessKey, "Parameter \"businessKey\" must not blank. ");
            namespace = StrUtil.isNotBlank(namespace) ? namespace : EMPTY_STRING;
            return namespace + MINUS + businessKey;
        }

        public void register(BizProcessor processor) {
            Assert.notNull(processor, "Parameter \"processor\" must not null. ");
            String mapKey = buildMapKey(processor.getNamespace(), processor.getBusinessKey());
            String className = processor.getClass().getName();
            processors.put(mapKey, processor);
            log.debug("Register the business processor \"{}\" to \"{}\". ", className, mapKey);
        }

        public void deregister(String namespace, String businessKey) {
            String mapKey = buildMapKey(namespace, businessKey);
            BizProcessor remove = processors.remove(mapKey);
            if (remove != null) {
                String className = remove.getClass().getName();
                log.debug("Deregister the business processor \"{}\" from \"{}\". ", className, mapKey);
            }
        }

        public BizProcessor get(String namespace, String businessKey) {

            return processors.get(buildMapKey(namespace, businessKey));
        }

        public BizProcessor getOrThrow(String namespace, String businessKey) {
            BizProcessor processor = get(namespace, businessKey);
            Assert.notNull(processor, "The corresponding " +
                    "business processor could not be found by namespace and business key. ");
            return processor;
        }
    }

}
