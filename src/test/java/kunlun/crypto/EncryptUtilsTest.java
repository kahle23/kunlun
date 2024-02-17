/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.crypto;

import kunlun.codec.CodecUtils;
import kunlun.file.Text;
import kunlun.logging.Logger;
import kunlun.logging.LoggerFactory;
import org.junit.Test;

import java.io.IOException;

import static kunlun.codec.CodecUtils.BASE64;

public class EncryptUtilsTest {
    private static final Logger log = LoggerFactory.getLogger(EncryptUtilsTest.class);
    private static final String data = "Hello, Java! ";

    @Test
    public void test1() {
        byte[] encrypt = EncryptUtils.encrypt(data.getBytes());
        log.info("Encrypt base64: {}", CodecUtils.encodeToString(BASE64, encrypt));
        byte[] decrypt = EncryptUtils.decrypt(encrypt);
        log.info("Decrypt string: {}", decrypt != null ? new String(decrypt) : null);
    }

    @Test
    public void test2() throws IOException {
        Text text = new Text();
        text.readFromClasspath("logging.properties");
        byte[] encrypt = EncryptUtils.encrypt(text.writeToByteArray());
        log.info("Encrypt base64: {}", CodecUtils.encodeToString(BASE64, encrypt));
        byte[] decrypt = EncryptUtils.decrypt(encrypt);
        log.info("Decrypt string: {}", decrypt != null ? new String(decrypt) : null);
    }

    @Test
    public void test3() {
        byte[] bytes = EncryptUtils.digest(data.getBytes());
        log.info("Digest base64: {}", CodecUtils.encodeToString(BASE64, bytes));
    }

    @Test
    public void test4() {
        byte[] bytes = EncryptUtils.digest256(data.getBytes());
        log.info("Digest 256 base64: {}", CodecUtils.encodeToString(BASE64, bytes));
    }

    @Test
    public void test5() {
        byte[] bytes = EncryptUtils.digest512(data.getBytes());
        log.info("Digest 512 base64: {}", CodecUtils.encodeToString(BASE64, bytes));
    }

}
