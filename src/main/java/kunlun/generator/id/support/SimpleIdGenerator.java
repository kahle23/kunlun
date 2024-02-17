/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.generator.id.support;

import java.util.UUID;

import static kunlun.common.constant.Symbols.EMPTY_STRING;
import static kunlun.common.constant.Symbols.MINUS;

/**
 * The simple identifier generator based on uuid.
 * @author Kahle
 */
public class SimpleIdGenerator extends AbstractIdGenerator implements StringIdGenerator {
    private final boolean isSimple;

    public SimpleIdGenerator() {

        this(true);
    }

    public SimpleIdGenerator(boolean isSimple) {

        this.isSimple = isSimple;
    }

    @Override
    public String next(Object... arguments) {
        String uuid = UUID.randomUUID().toString();
        return isSimple ? uuid.replaceAll(MINUS, EMPTY_STRING) : uuid;
    }

}
