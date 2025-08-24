/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.core;

import java.util.Map;

/**
 * Provide the highest level of abstraction for context.
 * @author Kahle
 */
public interface Context {

//    /**
//     * Get the runtime data (for data transfer among multiple "handlers").
//     * getRuntimeData
//     * @return The runtime data
//     */
//    Map<String, Object> getStorage();

    interface Ct extends Context {

        /**
         * 获取上下文存储器
         * @return 上下文存储器
         */
        Map<String, Object> getStorage();

    }

    /**
     * 抽象的上下文对象.
     * @author Zerox
     */
    abstract class Act implements Context.Ct {
        private final Map<String, Object> storage;

        protected Act(Map<String, Object> storage) {

            this.storage = storage;
        }

        @Override
        public Map<String, Object> getStorage() {

            return storage;
        }
    }

}
