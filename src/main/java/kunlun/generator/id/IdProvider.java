/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.generator.id;

import java.util.Map;

/**
 * The identifier provider.
 * @author Zerox
 */
public interface IdProvider {

    /**
     * Register common properties' information.
     * @param commonProperties The common properties
     */
    void registerCommonProperties(Map<?, ?> commonProperties);

    /**
     * Clears common properties' information.
     */
    void clearCommonProperties();

    /**
     * Get common properties' information.
     * @return The common properties
     */
    Map<String, Object> getCommonProperties();

    /**
     * Register the id generator.
     * @param name The id generator name
     * @param idGenerator The id generator
     */
    void registerGenerator(String name, IdGenerator idGenerator);

    /**
     * Deregister the id generator.
     * @param name The id generator name
     */
    void deregisterGenerator(String name);

    /**
     * Get the id generator by name.
     * @param name The id generator name
     * @return The id generator
     */
    IdGenerator getIdGenerator(String name);

    /**
     * 根据 ID 生成器的名称预览将要生成的ID.
     * @param name ID 生成器的名称
     * @param arguments ID 生成时的参数
     * @return 要预览的 ID
     */
    Object preview(String name, Object... arguments);

    /**
     * 根据 ID 生成器的名称生成下一个 ID.
     * @param name ID 生成器的名称
     * @param arguments ID 生成时的参数
     * @return 生成的 ID
     */
    Object next(String name, Object... arguments);

}
