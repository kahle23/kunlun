/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.crypto.digest;

import kunlun.crypto.util.KeyUtils;
import kunlun.logging.Logger;
import kunlun.logging.LoggerFactory;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.security.Key;

import static kunlun.common.constant.Algorithms.*;

/**
 * The hmac tools Test.
 * @author Kahle
 */
public class HmacTest {
    private static final Logger log = LoggerFactory.getLogger(HmacTest.class);
    private static final File testFile = new File("src\\test\\resources\\test_read.txt");
    private static final Hmac hmac = new Hmac();
    private Key hmd5Key;
    private Key hsha1Key;
    private Key hsha256Key;
    private Key hsha384Key;
    private Key hsha512Key;

    @Before
    public void init() {
        hmd5Key = KeyUtils.genKey(HMAC_MD5, 10);
        hsha1Key = KeyUtils.genKey(HMAC_SHA1, 10);
        // Key length must be at least 40 bits
        hsha256Key = KeyUtils.genKey(HMAC_SHA256, 40);
        hsha384Key = KeyUtils.genKey(HMAC_SHA384, 40);
        hsha512Key = KeyUtils.genKey(HMAC_SHA512, 40);
    }

    @Test
    public void hmacString() {
        String data = "12345";
        log.info(hmac.digestToHex(Hmac.Cfg.of(HMAC_MD5).setKey(hmd5Key), data));
        log.info(hmac.digestToHex(Hmac.Cfg.of(HMAC_SHA1).setKey(hsha1Key), data));
        log.info(hmac.digestToHex(Hmac.Cfg.of(HMAC_SHA256).setKey(hsha256Key), data));
        log.info(hmac.digestToHex(Hmac.Cfg.of(HMAC_SHA384).setKey(hsha384Key), data));
        log.info(hmac.digestToHex(Hmac.Cfg.of(HMAC_SHA512).setKey(hsha512Key), data));
    }

    /*@Test
    public void hashFile() throws Exception {
        log.info("Please insure file is exists. ");
        Assert.isTrue(testFile.exists(), "File are not find. ");
        log.info(CodecUtils.encodeToString(HEX, hmd5.digest(testFile)));
        log.info(CodecUtils.encodeToString(HEX, hsha1.digest(testFile)));
        log.info(CodecUtils.encodeToString(HEX, hsha256.digest(testFile)));
        log.info(CodecUtils.encodeToString(HEX, hsha384.digest(testFile)));
        log.info(CodecUtils.encodeToString(HEX, hsha512.digest(testFile)));
    }*/

}
