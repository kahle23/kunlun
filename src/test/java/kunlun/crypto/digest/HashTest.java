/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.crypto.digest;

import kunlun.logging.Logger;
import kunlun.logging.LoggerFactory;
import org.junit.Test;

import java.io.File;

import static kunlun.common.constant.Algorithms.*;

/**
 * The message digest tools Test.
 * @author Kahle
 */
public class HashTest {
    private static final Logger log = LoggerFactory.getLogger(HashTest.class);
    private static final File testFile = new File("src\\test\\resources\\test_read.txt");
    private static final Hash hash = new Hash();

    @Test
    public void hashString() {
        String data = "1234567890";
        log.info(hash.digestToHex(Hash.Cfg.of(MD2), data));
        log.info(hash.digestToHex(Hash.Cfg.of(MD5), data));
        log.info(hash.digestToHex(Hash.Cfg.of(SHA1), data));
        log.info(hash.digestToHex(Hash.Cfg.of(SHA256), data));
        log.info(hash.digestToHex(Hash.Cfg.of(SHA384), data));
        log.info(hash.digestToHex(Hash.Cfg.of(SHA512), data));
    }

    /*@Test
    public void hashFile() throws Exception {
        log.info("Please insure file is exists. ");
        Assert.isTrue(testFile.exists(), "File are not find. ");
        log.info(CodecUtils.encodeToString(HEX, md2.digest(testFile)));
        log.info(CodecUtils.encodeToString(HEX, md5.digest(testFile)));
        log.info(CodecUtils.encodeToString(HEX, sha1.digest(testFile)));
        log.info(CodecUtils.encodeToString(HEX, sha256.digest(testFile)));
        log.info(CodecUtils.encodeToString(HEX, sha384.digest(testFile)));
        log.info(CodecUtils.encodeToString(HEX, sha512.digest(testFile)));
    }*/

}
