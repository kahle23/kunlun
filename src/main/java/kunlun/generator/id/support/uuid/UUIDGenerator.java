/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.generator.id.support.uuid;

import kunlun.generator.id.support.StringIdGenerator;

import java.util.UUID;

import static kunlun.common.constant.Symbols.EMPTY_STRING;
import static kunlun.common.constant.Symbols.MINUS;

/**
 * UUID 生成器.
 * @author Kahle
 */
public class UUIDGenerator extends StringIdGenerator {
    private final boolean isSimple;

    public UUIDGenerator() {

        this(true);
    }

    public UUIDGenerator(boolean isSimple) {

        this.isSimple = isSimple;
    }

    @Override
    public String next(Object... arguments) {
        String uuid = UUID.randomUUID().toString();
        return isSimple ? uuid.replaceAll(MINUS, EMPTY_STRING) : uuid;
    }

}
