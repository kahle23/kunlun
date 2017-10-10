package saber.util.crypto;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.GeneralSecurityException;
import java.security.SecureRandom;

public abstract class BlowfishUtils {
    private static final String BLOWFISH_ALGORITHM_NAME = "Blowfish";
    private static final String BLOWFISH_ECB_NOPADDING = "Blowfish/ECB/NoPadding";
    private static final String BLOWFISH_ECB_PKCS5PADDING = "Blowfish/ECB/PKCS5Padding";
    private static final String BLOWFISH_CBC_NOPADDING = "Blowfish/CBC/NoPadding";
    private static final String BLOWFISH_CBC_PKCS5PADDING = "Blowfish/CBC/PKCS5Padding";

    private static byte[] doFinal(Cipher cipher, byte[] data, int opmode, int fill)
            throws GeneralSecurityException {
        if (opmode == Cipher.ENCRYPT_MODE) {
            int len = data.length; // Input length multiple of [fill] bytes
            byte[] finalData = new byte[len + fill - len % fill];
            System.arraycopy(data, 0, finalData, 0, len);
            return cipher.doFinal(finalData);
        } else {
            return cipher.doFinal(data);
        }
    }

    public static byte[] encryptEcbNoPadding(byte[] data, byte[] key)
            throws GeneralSecurityException {
        return ecbNoPadding(data, key, Cipher.ENCRYPT_MODE);
    }

    public static byte[] decryptEcbNoPadding(byte[] data, byte[] key)
            throws GeneralSecurityException {
        return ecbNoPadding(data, key, Cipher.DECRYPT_MODE);
    }

    public static byte[] ecbNoPadding(byte[] data, byte[] key, int opmode)
            throws GeneralSecurityException {
        if (key.length > 56)
            throw new IllegalArgumentException("Key too long (> 448 bits (56 bytes)). ");
        SecretKey secretKey = new SecretKeySpec(key, BLOWFISH_ALGORITHM_NAME);
        Cipher cipher = Cipher.getInstance(BLOWFISH_ECB_NOPADDING);
        cipher.init(opmode, secretKey, new SecureRandom());
        return doFinal(cipher, data, opmode, 8);
    }

    public static byte[] encryptEcbPkcs5Padding(byte[] data, byte[] key)
            throws GeneralSecurityException {
        return ecbPkcs5Padding(data, key, Cipher.ENCRYPT_MODE);
    }

    public static byte[] decryptEcbPkcs5Padding(byte[] data, byte[] key)
            throws GeneralSecurityException {
        return ecbPkcs5Padding(data, key, Cipher.DECRYPT_MODE);
    }

    public static byte[] ecbPkcs5Padding(byte[] data, byte[] key, int opmode)
            throws GeneralSecurityException {
        if (key.length > 56)
            throw new IllegalArgumentException("Key too long (> 448 bits (56 bytes)). ");
        SecretKey secretKey = new SecretKeySpec(key, BLOWFISH_ALGORITHM_NAME);
        Cipher cipher = Cipher.getInstance(BLOWFISH_ECB_PKCS5PADDING);
        cipher.init(opmode, secretKey, new SecureRandom());
        return cipher.doFinal(data);
    }

    public static byte[] encryptCbcNoPadding(byte[] data, byte[] key, byte[] iv)
            throws GeneralSecurityException {
        return cbcNoPadding(data, key, iv, Cipher.ENCRYPT_MODE);
    }

    public static byte[] decryptCbcNoPadding(byte[] data, byte[] key, byte[] iv)
            throws GeneralSecurityException {
        return cbcNoPadding(data, key, iv, Cipher.DECRYPT_MODE);
    }

    public static byte[] cbcNoPadding(byte[] data, byte[] key, byte[] iv, int opmode)
            throws GeneralSecurityException {
        if (key.length > 56)
            throw new IllegalArgumentException("Key too long (> 448 bits (56 bytes)). ");
        if (iv.length != 8)
            throw new IllegalArgumentException("Wrong IV length: must be 8 bytes long. ");
        SecretKey secretKey = new SecretKeySpec(key, BLOWFISH_ALGORITHM_NAME);
        Cipher cipher = Cipher.getInstance(BLOWFISH_CBC_NOPADDING);
        cipher.init(opmode, secretKey, new IvParameterSpec(iv));
        return doFinal(cipher, data, opmode, 8);
    }

    public static byte[] encryptCbcPkcs5Padding(byte[] data, byte[] key, byte[] iv)
            throws GeneralSecurityException {
        return cbcPkcs5Padding(data, key, iv, Cipher.ENCRYPT_MODE);
    }

    public static byte[] decryptCbcPkcs5Padding(byte[] data, byte[] key, byte[] iv)
            throws GeneralSecurityException {
        return cbcPkcs5Padding(data, key, iv, Cipher.DECRYPT_MODE);
    }

    public static byte[] cbcPkcs5Padding(byte[] data, byte[] key, byte[] iv, int opmode)
            throws GeneralSecurityException {
        if (key.length > 56)
            throw new IllegalArgumentException("Key too long (> 448 bits (56 bytes)). ");
        if (iv.length != 8)
            throw new IllegalArgumentException("Wrong IV length: must be 8 bytes long. ");
        SecretKey secretKey = new SecretKeySpec(key, BLOWFISH_ALGORITHM_NAME);
        Cipher cipher = Cipher.getInstance(BLOWFISH_CBC_PKCS5PADDING);
        cipher.init(opmode, secretKey, new IvParameterSpec(iv));
        return cipher.doFinal(data);
    }

}
