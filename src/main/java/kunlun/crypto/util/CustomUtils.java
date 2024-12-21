/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.crypto.util;

import kunlun.crypto.cipher.SymmetricCipher;
import kunlun.crypto.digest.Hmac;
import kunlun.util.Assert;

import static kunlun.common.constant.Algorithms.*;
import static kunlun.common.constant.Numbers.*;
import static kunlun.crypto.util.KeyUtils.parseSecretKey;

/**
 * The custom encryption and decryption and message digest tools.
 * @author Kahle
 */
public class CustomUtils {
    public static final byte[] PI_KEY_64 = new byte[]{ 51,46,49,52,49,53,57,50,54,53,51,53,56,57,55,57
            ,51,50,51,56,52,54,50,54,52,51,51,56,51,50,55,57,53,48,50,56,56,52,49,57,55,49,54,57,51,57
            ,57,51,55,53,49,48,53,56,50,48,57,55,52,57,52,52,53,57 };
    public static final byte[] PI_KEY_32 = new byte[]{ 51,46,49,52,49,53,57,50,54,53,51,53,56,57,55,57
            ,51,50,51,56,52,54,50,54,52,51,51,56,51,50,55,57 };
    public static final byte[] PI_KEY_16 = new byte[]{ 51,46,49,52,49,53,57,50,54,53,51,53,56,57,55,57 };
    public static final byte[] PI_KEY_8 = new byte[]{ 51,46,49,52,49,53,57,50 };

    private static final SymmetricCipher.Cfg AES_CFG;
    private static final SymmetricCipher.Cfg DES_CFG;
    private static final Hmac.Cfg H_SHA512_CFG;
    private static final Hmac.Cfg H_SHA256_CFG;
    private static final Hmac.Cfg H_SHA1_CFG;

    private static SymmetricCipher cipher = new SymmetricCipher();
    private static Hmac hmac = new Hmac();

    static {
        String aesTrn = "AES/ECB/PKCS5Padding";
        String desTrn = "DES/ECB/PKCS5Padding";
        AES_CFG = SymmetricCipher.Cfg.of(aesTrn, parseSecretKey(AES, PI_KEY_16));
        DES_CFG = SymmetricCipher.Cfg.of(desTrn, parseSecretKey(DES, PI_KEY_8));
        H_SHA512_CFG = Hmac.Cfg.of(HMAC_SHA512, parseSecretKey(HMAC_SHA512, PI_KEY_64));
        H_SHA256_CFG = Hmac.Cfg.of(HMAC_SHA256, parseSecretKey(HMAC_SHA256, PI_KEY_32));
        H_SHA1_CFG   = Hmac.Cfg.of(HMAC_SHA1,   parseSecretKey(HMAC_SHA1, PI_KEY_16));
    }

    public static SymmetricCipher getSymmetricCipher() {

        return cipher;
    }

    public static void setSymmetricCipher(SymmetricCipher symmetricCipher) {
        Assert.notNull(symmetricCipher, "Parameter \"symmetricCipher\" must not null. ");
        CustomUtils.cipher = symmetricCipher;
    }

    public static Hmac getHmac() {

        return hmac;
    }

    public static void setHmac(Hmac hmac) {
        Assert.notNull(hmac, "Parameter \"hmac\" must not null. ");
        CustomUtils.hmac = hmac;
    }

    public static byte[] addSalt(byte[] data) {
        int dataLen = data.length, arrLen = dataLen + SIXTEEN;
        int destPos = dataLen + EIGHT;
        byte[] newData = new byte[arrLen];
        System.arraycopy(PI_KEY_8, ZERO, newData, ZERO, EIGHT);
        System.arraycopy(data, ZERO, newData, EIGHT, dataLen);
        System.arraycopy(PI_KEY_8, ZERO, newData, destPos, EIGHT);
        return newData;
    }

    public static byte[] removeSalt(byte[] data) {
        int arrLen = data.length - SIXTEEN;
        byte[] newData = new byte[arrLen];
        System.arraycopy(data, EIGHT, newData, ZERO, arrLen);
        return newData;
    }

    public static byte[] aesEncrypt(byte[] data) {

        return cipher.encrypt(AES_CFG, data);
    }

    public static byte[] aesDecrypt(byte[] data) {

        return cipher.decrypt(AES_CFG, data);
    }

    public static byte[] desEncrypt(byte[] data) {

        return cipher.encrypt(DES_CFG, data);
    }

    public static byte[] desDecrypt(byte[] data) {

        return cipher.decrypt(DES_CFG, data);
    }

    public static byte[] hmacSha512(byte[] data) {

        return hmac.digest(H_SHA512_CFG, data);
    }

    public static byte[] hmacSha256(byte[] data) {

        return hmac.digest(H_SHA256_CFG, data);
    }

    public static byte[] hmacSha1(byte[] data) {

        return hmac.digest(H_SHA1_CFG, data);
    }

}
