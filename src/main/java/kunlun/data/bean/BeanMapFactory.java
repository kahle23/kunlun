/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.data.bean;

/**
 * The bean map factory.
 * @author Kahle
 */
public interface BeanMapFactory {

    /**
     * Get the bean map instance.
     * @param bean The bean object (nullable)
     * @return The bean map instance
     */
    BeanMap getInstance(Object bean);

}
