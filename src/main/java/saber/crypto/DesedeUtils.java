package saber.crypto;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.GeneralSecurityException;
import java.security.SecureRandom;

public abstract class DesedeUtils {
    private static final String ALGORITHM_NAME = "DESede";
    private static final String DESEDE_ECB_NOPADDING = "DESede/ECB/NoPadding";
    private static final String DESEDE_ECB_PKCS5PADDING = "DESede/ECB/PKCS5Padding";
    private static final String DESEDE_CBC_NOPADDING = "DESede/CBC/NoPadding";
    private static final String DESEDE_CBC_PKCS5PADDING = "DESede/CBC/PKCS5Padding";

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
        if (key.length != 24)
            throw new IllegalArgumentException("Wrong key size. Key length must is 24. ");
        SecretKey secretKey = new SecretKeySpec(key, ALGORITHM_NAME);
        Cipher cipher = Cipher.getInstance(DESEDE_ECB_NOPADDING);
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
        if (key.length != 24)
            throw new IllegalArgumentException("Wrong key size. Key length must is 24. ");
        SecretKey secretKey = new SecretKeySpec(key, ALGORITHM_NAME);
        Cipher cipher = Cipher.getInstance(DESEDE_ECB_PKCS5PADDING);
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
        if (key.length != 24)
            throw new IllegalArgumentException("Wrong key size. Key length must is 24. ");
        if (iv.length != 8)
            throw new IllegalArgumentException("Wrong IV length: must be 8 bytes long. ");
        SecretKey secretKey = new SecretKeySpec(key, ALGORITHM_NAME);
        Cipher cipher = Cipher.getInstance(DESEDE_CBC_NOPADDING);
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
        if (key.length != 24)
            throw new IllegalArgumentException("Wrong key size. Key length must is 24. ");
        if (iv.length != 8)
            throw new IllegalArgumentException("Wrong IV length: must be 8 bytes long. ");
        SecretKey secretKey = new SecretKeySpec(key, ALGORITHM_NAME);
        Cipher cipher = Cipher.getInstance(DESEDE_CBC_PKCS5PADDING);
        cipher.init(opmode, secretKey, new IvParameterSpec(iv));
        return cipher.doFinal(data);
    }

}
