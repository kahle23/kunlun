/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.util;

import kunlun.logging.Logger;
import kunlun.logging.LoggerFactory;
import org.junit.Test;

import static kunlun.common.constant.Charsets.*;
import static org.junit.Assert.assertEquals;

public class CharsetUtilTest {
    private static final Logger log = LoggerFactory.getLogger(CharsetUtilTest.class);

    @Test
    public void testParse() {
        assertEquals(GB2312, CharsetUtil.parse(STR_GB2312));
        assertEquals(UTF_8, CharsetUtil.parse(STR_UTF_8));
        assertEquals(GBK, CharsetUtil.parse(STR_GBK));
    }

    @Test
    public void testConvert() {
        String data = CharsetUtil.convert("世界，你好！", STR_UTF_8, STR_ISO_8859_1);
        log.info(CharsetUtil.convert(data, STR_ISO_8859_1, STR_UTF_8));
    }

}
