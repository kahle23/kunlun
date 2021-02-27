package artoria.codec;

import artoria.logging.Logger;
import artoria.logging.LoggerFactory;
import org.junit.Test;

import java.nio.charset.Charset;

import static artoria.common.Constants.*;
import static org.junit.Assert.*;

public class Base64UtilsTest {
    private static Logger log = LoggerFactory.getLogger(Base64UtilsTest.class);
    private static Charset encoding = Charset.forName(UTF_8);
    private String data =
            "-->> 行路难！行路难！多歧路，今安在？ <<--\n" +
            "-->> 长风破浪会有时，直挂云帆济沧海。 <<--";
    private byte[] dataBytes = data.getBytes(encoding);

    @Test
    public void test1() {
        String encode = Base64Utils.encodeToString(dataBytes);
        assertTrue(encode.contains(PLUS));
        assertTrue(encode.contains(SLASH));
        log.info("Encode string: {}{}", NEWLINE, encode);
        byte[] decode = Base64Utils.decodeFromString(encode);
        String decodeStr = new String(decode, encoding);
        assertEquals(data, decodeStr);
        log.info("Decode string: {}{}", NEWLINE, decodeStr);
    }

    @Test
    public void test2() {
        String encode = Base64Utils.encodeToUrlSafeString(dataBytes);
        assertFalse(encode.contains(PLUS));
        assertFalse(encode.contains(SLASH));
        assertTrue(encode.contains(MINUS));
        assertTrue(encode.contains(UNDERLINE));
        log.info("Encode string: {}{}", NEWLINE, encode);
        byte[] decode = Base64Utils.decodeFromUrlSafeString(encode);
        String decodeStr = new String(decode, encoding);
        assertEquals(data, decodeStr);
        log.info("Decode string: {}{}", NEWLINE, decodeStr);
    }

    @Test
    public void test3() {
        String encode = Base64Utils.encodeToMimeString(dataBytes);
        assertTrue(encode.contains(PLUS));
        assertTrue(encode.contains(SLASH));
        assertTrue(encode.contains("\r\n"));
        log.info("Encode string: {}{}", NEWLINE, encode);
        byte[] decode = Base64Utils.decodeFromMimeString(encode);
        String decodeStr = new String(decode, encoding);
        assertEquals(data, decodeStr);
        log.info("Decode string: {}{}", NEWLINE, decodeStr);
    }

}
