package saber.util.crypto;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.GeneralSecurityException;
import java.security.SecureRandom;

public abstract class AesUtils {
    private static final String ALGORITHM_NAME = "AES";
    private static final String AES_ECB_NOPADDING = "AES/ECB/NoPadding";
    private static final String AES_ECB_PKCS5PADDING = "AES/ECB/PKCS5Padding";
    private static final String AES_CBC_NOPADDING = "AES/CBC/NoPadding";
    private static final String AES_CBC_PKCS5PADDING = "AES/CBC/PKCS5Padding";

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
        if (key.length != 16 && key.length != 24 && key.length != 32)
            throw new IllegalArgumentException("Wrong key size. Key length only is 16 or 24 or 32. ");
        Cipher cipher = Cipher.getInstance(AES_ECB_NOPADDING);
        SecretKeySpec secretKey = new SecretKeySpec(key, ALGORITHM_NAME);
        cipher.init(opmode, secretKey, new SecureRandom());
        return doFinal(cipher, data, opmode, 16);
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
        if (key.length != 16 && key.length != 24 && key.length != 32)
            throw new IllegalArgumentException("Wrong key size. Key length only is 16 or 24 or 32. ");
        Cipher cipher = Cipher.getInstance(AES_ECB_PKCS5PADDING);
        SecretKeySpec secretKey = new SecretKeySpec(key, ALGORITHM_NAME);
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
        if (key.length != 16 && key.length != 24 && key.length != 32)
            throw new IllegalArgumentException("Wrong key size. Key length only is 16 or 24 or 32. ");
        if (iv.length != 16)
            throw new IllegalArgumentException("Wrong IV length: must be 16 bytes long. ");
        Cipher cipher = Cipher.getInstance(AES_CBC_NOPADDING);
        SecretKeySpec secretKey = new SecretKeySpec(key, ALGORITHM_NAME);
        cipher.init(opmode, secretKey, new IvParameterSpec(iv));
        return doFinal(cipher, data, opmode, 16);
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
        if (key.length != 16 && key.length != 24 && key.length != 32)
            throw new IllegalArgumentException("Wrong key size. Key length only is 16 or 24 or 32. ");
        if (iv.length != 16)
            throw new IllegalArgumentException("Wrong IV length: must be 16 bytes long. ");
        Cipher cipher = Cipher.getInstance(AES_CBC_PKCS5PADDING);
        SecretKeySpec secretKey = new SecretKeySpec(key, ALGORITHM_NAME);
        cipher.init(opmode, secretKey, new IvParameterSpec(iv));
        return cipher.doFinal(data);
    }

}
