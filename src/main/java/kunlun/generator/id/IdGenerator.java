/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.generator.id;

import kunlun.generator.Generator;

import java.util.Map;

/**
 * ID 生成器的接口.
 * @author Zerox
 */
public interface IdGenerator extends Generator {

    /**
     * Get the common properties of the settings.
     * @return The common properties that is set
     */
    Map<Object, Object> getCommonProperties();

    /**
     * Set common properties for the id generator.
     * @param properties The properties to be set
     */
    void setCommonProperties(Map<?, ?> properties);

    /**
     * 预览将要生成的 ID.
     * @param arguments ID 生成时的参数
     * @return 要预览的 ID
     */
    Object preview(Object... arguments);

    /**
     * 生成下一个 ID.
     * 在大部分场景下，“参数”不是必须的.
     * 返回值在实现类中是确定的，不是字符串就是数字.
     * @param arguments ID 生成时的参数
     * @return 生成的 ID
     */
    Object next(Object... arguments);

    /**
     * ID 生成器的上下文接口.
     * @author Zerox
     */
    interface Context extends kunlun.core.Context.Ct {

        /**
         * 获取 ID 生成时的参数.
         * @return ID 生成时的参数
         */
        Object[] getArguments();

        /**
         * 获取 ID 生成器的配置.
         * @return ID 生成器的配置
         */
        Object getConfig();

    }

}
