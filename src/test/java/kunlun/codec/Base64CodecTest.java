/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.codec;

import kunlun.logging.Logger;
import kunlun.logging.LoggerFactory;
import org.junit.Test;

import java.nio.charset.Charset;

import static kunlun.codec.CodecUtils.BASE64;
import static kunlun.common.constant.Charsets.STR_UTF_8;
import static kunlun.common.constant.Symbols.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * The base64 codec tools Test.
 * @author Kahle
 */
public class Base64CodecTest {
    private static final Logger log = LoggerFactory.getLogger(Base64CodecTest.class);
    private static final Charset encoding = Charset.forName(STR_UTF_8);
    private final String data =
            "-->> 行路难！行路难！多歧路，今安在？ <<--\n" +
            "-->> 长风破浪会有时，直挂云帆济沧海。 <<--";
    private final byte[] dataBytes = data.getBytes(encoding);

    @Test
    public void test1() {
        String encode = CodecUtils.encodeToString(BASE64, dataBytes);
        assertTrue(encode.contains(PLUS));
        assertTrue(encode.contains(SLASH));
        log.info("Encode string: {}{}", NEWLINE, encode);
        byte[] decode = CodecUtils.decodeFromString(BASE64, encode);
        String decodeStr = new String(decode, encoding);
        assertEquals(data, decodeStr);
        log.info("Decode string: {}{}", NEWLINE, decodeStr);
    }

}
