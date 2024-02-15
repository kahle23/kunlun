package artoria.codec.support;

import artoria.logging.Logger;
import artoria.logging.LoggerFactory;
import org.junit.Test;

import java.nio.charset.Charset;

import static artoria.common.constant.Charsets.STR_UTF_8;
import static artoria.common.constant.Numbers.MINUS_ONE;
import static artoria.common.constant.Symbols.*;
import static java.lang.Boolean.TRUE;
import static org.junit.Assert.*;

/**
 * The base64 codec Test.
 * @author Kahle
 */
public class Base64Test {
    private static final Logger log = LoggerFactory.getLogger(Base64Test.class);
    private static final Charset encoding = Charset.forName(STR_UTF_8);
    private final String data =
            "-->> 行路难！行路难！多歧路，今安在？ <<--\n" +
            "-->> 长风破浪会有时，直挂云帆济沧海。 <<--";
    private final byte[] dataBytes = data.getBytes(encoding);
    private final Base64 mimeBase64 = new Base64(TRUE, MINUS_ONE, null);
    private final Base64 urlBase64 = new Base64(TRUE);
    private final Base64 base64 = new Base64();

    @Test
    public void test1() {
        String encode = base64.encodeToString(dataBytes);
        assertTrue(encode.contains(PLUS));
        assertTrue(encode.contains(SLASH));
        log.info("Encode string: {}{}", NEWLINE, encode);
        byte[] decode = base64.decodeFromString(encode);
        String decodeStr = new String(decode, encoding);
        assertEquals(data, decodeStr);
        log.info("Decode string: {}{}", NEWLINE, decodeStr);
    }

    @Test
    public void test2() {
        String encode = urlBase64.encodeToString(dataBytes);
        assertFalse(encode.contains(PLUS));
        assertFalse(encode.contains(SLASH));
        assertTrue(encode.contains(MINUS));
        assertTrue(encode.contains(UNDERLINE));
        log.info("Encode string: {}{}", NEWLINE, encode);
        byte[] decode = urlBase64.decodeFromString(encode);
        String decodeStr = new String(decode, encoding);
        assertEquals(data, decodeStr);
        log.info("Decode string: {}{}", NEWLINE, decodeStr);
    }

    @Test
    public void test3() {
        String encode = mimeBase64.encodeToString(dataBytes);
        assertTrue(encode.contains(PLUS));
        assertTrue(encode.contains(SLASH));
        assertTrue(encode.contains("\r\n"));
        log.info("Encode string: {}{}", NEWLINE, encode);
        byte[] decode = mimeBase64.decodeFromString(encode);
        String decodeStr = new String(decode, encoding);
        assertEquals(data, decodeStr);
        log.info("Decode string: {}{}", NEWLINE, decodeStr);
    }

}
