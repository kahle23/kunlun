/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.util;

import kunlun.logging.Logger;
import kunlun.logging.LoggerFactory;
import org.junit.Test;

/**
 * The boolean tools Test.
 * @author Kahle
 */
public class BooleanUtilTest {
    private static final Logger log = LoggerFactory.getLogger(BooleanUtilTest.class);

    @Test
    public void parseBooleanTest() {
        log.info("\"0\" parseBoolean: {}", BooleanUtil.parseBoolean("0"));
        log.info("\"1\" parseBoolean: {}", BooleanUtil.parseBoolean("1"));
        log.info("\"YeS\" parseBoolean: {}", BooleanUtil.parseBoolean("YeS"));
        log.info("\"nO\" parseBoolean: {}", BooleanUtil.parseBoolean("nO"));
        log.info("\"y\" parseBoolean: {}", BooleanUtil.parseBoolean("y"));
        log.info("\"N\" parseBoolean: {}", BooleanUtil.parseBoolean("N"));
        log.info("\"T\" parseBoolean: {}", BooleanUtil.parseBoolean("T"));
        log.info("\"f\" parseBoolean: {}", BooleanUtil.parseBoolean("f"));
        log.info("\"oN\" parseBoolean: {}", BooleanUtil.parseBoolean("oN"));
        log.info("\"oFf\" parseBoolean: {}", BooleanUtil.parseBoolean("oFf"));
        log.info("\"trUe\" parseBoolean: {}", BooleanUtil.parseBoolean("trUe"));
        log.info("\"faLse\" parseBoolean: {}", BooleanUtil.parseBoolean("faLse"));
        log.info("\"AAaAA\" parseBoolean: {}", BooleanUtil.parseBoolean("AAaAA"));
        log.info("\"rrr\" parseBoolean: {}", BooleanUtil.parseBoolean("rrr"));
        log.info("\"null\" parseBoolean: {}", BooleanUtil.parseBoolean(null));
        log.info("\"\" parseBoolean: {}", BooleanUtil.parseBoolean(""));
    }

    @Test
    public void valueOfTest() {
        log.info("\"0\" valueOf: {}", BooleanUtil.valueOf("0"));
        log.info("\"1\" valueOf: {}", BooleanUtil.valueOf("1"));
        log.info("\"YeS\" valueOf: {}", BooleanUtil.valueOf("YeS"));
        log.info("\"nO\" valueOf: {}", BooleanUtil.valueOf("nO"));
        log.info("\"y\" valueOf: {}", BooleanUtil.valueOf("y"));
        log.info("\"N\" valueOf: {}", BooleanUtil.valueOf("N"));
        log.info("\"T\" valueOf: {}", BooleanUtil.valueOf("T"));
        log.info("\"f\" valueOf: {}", BooleanUtil.valueOf("f"));
        log.info("\"oN\" valueOf: {}", BooleanUtil.valueOf("oN"));
        log.info("\"oFf\" valueOf: {}", BooleanUtil.valueOf("oFf"));
        log.info("\"trUe\" valueOf: {}", BooleanUtil.valueOf("trUe"));
        log.info("\"faLse\" valueOf: {}", BooleanUtil.valueOf("faLse"));
        log.info("\"AAaAA\" valueOf: {}", BooleanUtil.valueOf("AAaAA"));
        log.info("\"rrr\" valueOf: {}", BooleanUtil.valueOf("rrr"));
        log.info("\"null\" valueOf: {}", BooleanUtil.valueOf(null));
        log.info("\"\" valueOf: {}", BooleanUtil.valueOf(""));
    }

}
