/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.core;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 为上下文提供最高层次的抽象.<br />
 * @author Kahle
 */
public interface Context {

    /**
     * 获取上下文存储器（Runtime Data）.<br />
     * @return 上下文存储器
     */
    Map<String, Object> getStorage();


    /**
     * 抽象的上下文对象.<br />
     * @author Kahle
     */
    abstract class AbstractContext implements Context {
        private final Map<String, Object> storage;

        public AbstractContext(Map<String, Object> storage) {
            // 如果有其他特殊场景，可以走当前构造方法，可传入任何 Map 的实现对象
            // 再有更特殊情况，就只能自行实现接口了
            this.storage = storage;
        }

        public AbstractContext() {
            // 大部分场景，上下文对象是在一个线程的多个方法传递
            // 所以 LinkedHashMap 就作为默认构造方法的默认值了
            this(new LinkedHashMap<String, Object>());
        }

        @Override
        public Map<String, Object> getStorage() {

            return storage;
        }
    }

}
