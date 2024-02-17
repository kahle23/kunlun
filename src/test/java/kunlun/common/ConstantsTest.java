/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.common;

import kunlun.logging.Logger;
import kunlun.logging.LoggerFactory;
import org.junit.Test;

import static kunlun.common.constant.Charsets.STR_DEFAULT_CHARSET;
import static kunlun.common.constant.Env.*;
import static kunlun.common.constant.Symbols.*;
import static kunlun.common.constant.Words.GET;

/**
 * The constants Test.
 * @author Kahle
 */
public class ConstantsTest {
    private static final Logger log = LoggerFactory.getLogger(ConstantsTest.class);

    @Test
    public void test1() {
        log.info(NEWLINE);
        log.info(FILE_SEPARATOR);
        log.info(PATH_SEPARATOR);
        log.info(COMPUTER_NAME);
        log.info(ROOT_PATH);
        log.info(CLASSPATH);
        log.info(HOST_NAME);
        log.info(STR_DEFAULT_CHARSET);
        log.info(GET);
    }

}
