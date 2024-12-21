/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.crypto.cipher;

import kunlun.codec.CodecUtils;
import kunlun.crypto.util.BouncyCastleSupport;
import kunlun.crypto.util.KeyUtils;
import kunlun.logging.Logger;
import kunlun.logging.LoggerFactory;
import org.junit.Test;

import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static kunlun.codec.CodecUtils.BASE64;
import static kunlun.common.constant.Algorithms.DES;
import static kunlun.crypto.util.KeyUtils.parseIv;
import static kunlun.crypto.util.KeyUtils.parseSecretKey;

/**
 * The DES encryption and decryption tools Test.
 *
 * Test transformation:
 *  DES/ECB/ZeroPadding(NoPadding)
 *  DES/ECB/PKCS5Padding
 *  DES/CBC/ZeroPadding(NoPadding)
 *  DES/CBC/PKCS5Padding
 *
 * @author Kahle
 */
public class DESTest extends BouncyCastleSupport {
    private static final Logger log = LoggerFactory.getLogger(DESTest.class);
    private static final SymmetricCipher cipher = new SymmetricCipher();
    private static final byte[] data = "Hello, Java!".getBytes();
    // Wrong keySize: must be equal to 56 (length is 8)
    private static final SecretKey key = parseSecretKey(DES, "TTestKey".getBytes());
    // Wrong IV length: must be 8 bytes long
    private static final IvParameterSpec iv = parseIv("TeTestIv".getBytes());

    /*static {
        SecretKey secretKey = KeyUtils.generateKey("DES", 56);
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

        testEncryptAndDecrypt("DES/ECB/ZeroPadding", FALSE);
    }

    @Test
    public void testEcbPKCS5Padding() {

        testEncryptAndDecrypt("DES/ECB/PKCS5Padding", FALSE);
    }

    @Test
    public void testCbcZeroPadding() {

        testEncryptAndDecrypt("DES/CBC/ZeroPadding", TRUE);
    }

    @Test
    public void testCbcPKCS5Padding() {

        testEncryptAndDecrypt("DES/CBC/PKCS5Padding", TRUE);
    }

    /**
     * The length of the DES key only supports 8.
     * The DES key length of the arguments when using the KeyGenerator is only 56.
     * Error info:"Wrong keySize: must be equal to 56".
     * So if the key length is correct, the encryption process is fine.
     * So the following code is only a backup.
     * @see KeyUtils#parseSecretKey(String, byte[])
     */
    /*public static SecretKey parseSecretKey(String algorithm, byte[] key) throws GeneralSecurityException {
        if (DES.equalsIgnoreCase(algorithm)) {
            KeySpec keySpec = new DESKeySpec(key);
            SecretKeyFactory factory = SecretKeyFactory.getInstance(algorithm);
            return factory.generateSecret(keySpec);
        }
        else {
            return new SecretKeySpec(key, algorithm);
        }
    }*/

}
