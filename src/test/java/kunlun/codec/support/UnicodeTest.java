/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.codec.support;

import kunlun.logging.Logger;
import kunlun.logging.LoggerFactory;
import org.junit.Test;

/**
 * The unicode codec Test.
 * @author Kahle
 */
public class UnicodeTest {
    private static final Logger log = LoggerFactory.getLogger(UnicodeTest.class);
    private static final Unicode unicode = new Unicode();

    @Test
    public void test1() {
        String encode = unicode.encode("Hello，Java! ");
        log.info(encode);
        log.info(unicode.decode(encode));
    }

    @Test
    public void test2() {
        String data = "\\u003ctable cellpadding=0 cellspacing=0 class=\\u0027tableTitle\\u0027\\u003e\\u003ctr\\u003e\\u003ctd style=" +
                "\\u0027text-align:center;font-size:14px;font-weight:bold;color:#003399;\\u0027\\u003e\\u003cspan id=\\u0027HistoryName\\u0027\\u003e";
        log.info(unicode.decode(data));
    }

}
