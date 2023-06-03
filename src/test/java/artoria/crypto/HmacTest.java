package artoria.crypto;

import artoria.codec.CodecUtils;
import artoria.logging.Logger;
import artoria.logging.LoggerFactory;
import artoria.util.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

import static artoria.codec.CodecUtils.HEX;
import static artoria.common.Constants.*;

public class HmacTest {
    private static final Logger log = LoggerFactory.getLogger(HmacTest.class);
    private static final File testFile = new File("src\\test\\resources\\test_read.txt");
    private static final Hmac hmd5 = new Hmac(HMAC_MD5);
    private static final Hmac hsha1 = new Hmac(HMAC_SHA1);
    private static final Hmac hsha256 = new Hmac(HMAC_SHA256);
    private static final Hmac hsha384 = new Hmac(HMAC_SHA384);
    private static final Hmac hsha512 = new Hmac(HMAC_SHA512);

    @Before
    public void init() throws Exception {
        hmd5.setSecretKey(KeyUtils.generateKey(HMAC_MD5, 10));
        hsha1.setSecretKey(KeyUtils.generateKey(HMAC_SHA1, 10));
        // Key length must be at least 40 bits
        hsha256.setSecretKey(KeyUtils.generateKey(HMAC_SHA256, 40));
        hsha384.setSecretKey(KeyUtils.generateKey(HMAC_SHA384, 40));
        hsha512.setSecretKey(KeyUtils.generateKey(HMAC_SHA512, 40));
    }

    @Test
    public void hmacString() throws Exception {
        String data = "12345";
        log.info(CodecUtils.encodeToString(HEX, hmd5.digest(data)));
        log.info(CodecUtils.encodeToString(HEX, hsha1.digest(data)));
        log.info(CodecUtils.encodeToString(HEX, hsha256.digest(data)));
        log.info(CodecUtils.encodeToString(HEX, hsha384.digest(data)));
        log.info(CodecUtils.encodeToString(HEX, hsha512.digest(data)));
    }

    @Test
    public void hashFile() throws Exception {
        log.info("Please insure file is exists. ");
        Assert.isTrue(testFile.exists(), "File are not find. ");
        log.info(CodecUtils.encodeToString(HEX, hmd5.digest(testFile)));
        log.info(CodecUtils.encodeToString(HEX, hsha1.digest(testFile)));
        log.info(CodecUtils.encodeToString(HEX, hsha256.digest(testFile)));
        log.info(CodecUtils.encodeToString(HEX, hsha384.digest(testFile)));
        log.info(CodecUtils.encodeToString(HEX, hsha512.digest(testFile)));
    }

}
