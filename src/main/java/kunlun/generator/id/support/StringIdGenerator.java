/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.generator.id.support;

import static kunlun.util.Assert.renderMessage;

/**
 * 抽象的字符串 ID 生成器.
 * @author Zerox
 */
public abstract class StringIdGenerator extends AbstractIdGenerator {

    /**
     * 预览将要生成的字符串 ID.
     * @param arguments ID 生成时的参数
     * @return 要预览的字符串 ID
     */
    @Override
    public String preview(Object... arguments) {
        throw new UnsupportedOperationException(renderMessage(
                "In \"%s\", the method \"preview\" is not supported! ", getClass().getName()
        ));
    }

    /**
     * 生成下一个字符串 ID.
     * @param arguments ID 生成时的参数
     * @return 生成的字符串 ID
     */
    @Override
    public abstract String next(Object... arguments);

}
