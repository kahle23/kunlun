/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.crypto.cipher;

import kunlun.codec.CodecUtils;
import kunlun.crypto.util.BouncyCastleSupport;
import kunlun.logging.Logger;
import kunlun.logging.LoggerFactory;
import org.junit.Test;

import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static kunlun.codec.CodecUtils.BASE64;
import static kunlun.common.constant.Algorithms.AES;
import static kunlun.crypto.util.KeyUtils.parseIv;
import static kunlun.crypto.util.KeyUtils.parseSecretKey;

/**
 * DESede/ECB/ZeroPadding(NoPadding)
 * DESede/ECB/PKCS5Padding
 * DESede/CBC/ZeroPadding(NoPadding)
 * DESede/CBC/PKCS5Padding
 */
public class DESedeTest extends BouncyCastleSupport {
    private static final Logger log = LoggerFactory.getLogger(DESedeTest.class);
    private static final SymmetricCipher cipher = new SymmetricCipher();
    private static final byte[] data = "Hello, Java!".getBytes();
    // Wrong keySize: must be equal to 112 or 168
    private static final SecretKey key = parseSecretKey(AES, "TesTestKeyTestKeyTestKey".getBytes());
    // Wrong IV length: must be 8 bytes long
    private static final IvParameterSpec iv = parseIv("TeTestIv".getBytes());

    /*static {
        SecretKey secretKey = KeyUtils.generateKey(DESEDE, 168);
        key = secretKey.getEncoded();
    }*/

    private void testEncryptAndDecrypt(String transformation, boolean needIv) {
        log.info("Start test {}", transformation);
        SymmetricCipher.Cfg cfg = SymmetricCipher.Cfg.of(transformation, key);
        if (needIv) { cfg.setIv(iv); }
        byte[] bytes = cipher.encrypt(cfg, data);
        log.info("Encrypt: {}", CodecUtils.encodeToString(BASE64, bytes));
        log.info("Decrypt: {}", cipher.decryptToString(cfg, bytes));
        log.info("End test {}", transformation);
    }

    @Test
    public void testEcbZeroPadding() {

        testEncryptAndDecrypt("DESede/ECB/ZeroPadding", FALSE);
    }

    @Test
    public void testEcbPKCS5Padding() {

        testEncryptAndDecrypt("DESede/ECB/PKCS5Padding", FALSE);
    }

    @Test
    public void testCbcZeroPadding() {

        testEncryptAndDecrypt("DESede/CBC/ZeroPadding", TRUE);
    }

    @Test
    public void testCbcPKCS5Padding() {

        testEncryptAndDecrypt("DESede/CBC/PKCS5Padding", TRUE);
    }

}
