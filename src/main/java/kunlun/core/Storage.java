/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.core;

/**
 * 为存储器提供最高层次的抽象.<br />
 *      该接口设计更倾向于对象存储和 Map.<br />
 *
 * @see <a href="https://en.wikipedia.org/wiki/Computer_data_storage">Computer data storage</a>
 * @see <a href="https://en.wikipedia.org/wiki/Data_storage">Data storage</a>
 * @see <a href="https://en.wikipedia.org/wiki/Object_storage">Object storage</a>
 * @author Kahle
 */
public interface Storage extends Strategy {

    /**
     * The high-level abstraction of data to be stored.
     * @author Kahle
     */
    interface Data {

        /**
         * Get the object key.
         * @return The object key
         */
        String getObjectKey();

        /**
         * Get the object content.
         * @return The object content
         */
        Object getObjectContent();

    }

}
