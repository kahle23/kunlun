package saber.crypto;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import java.security.GeneralSecurityException;
import java.security.SecureRandom;

public abstract class DesUtils {
    private static final String ALGORITHM_NAME = "DES";
    private static final String DES_ECB_NOPADDING = "DES/ECB/NoPadding";
    private static final String DES_ECB_PKCS5PADDING = "DES/ECB/PKCS5Padding";
    private static final String DES_CBC_NOPADDING = "DES/CBC/NoPadding";
    private static final String DES_CBC_PKCS5PADDING = "DES/CBC/PKCS5Padding";

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
        if (key.length < 8)
            throw new IllegalArgumentException("Wrong key size. Key length >= 8. ");
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(ALGORITHM_NAME);
        SecretKey secretKey = keyFactory.generateSecret(new DESKeySpec(key));
        Cipher cipher = Cipher.getInstance(DES_ECB_NOPADDING);
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
        if (key.length < 8)
            throw new IllegalArgumentException("Wrong key size. Key length >= 8. ");
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(ALGORITHM_NAME);
        SecretKey secretKey = keyFactory.generateSecret(new DESKeySpec(key));
        Cipher cipher = Cipher.getInstance(DES_ECB_PKCS5PADDING);
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
        if (key.length < 8)
            throw new IllegalArgumentException("Wrong key size. Key length >= 8. ");
        if (iv.length != 8)
            throw new IllegalArgumentException("Wrong IV length: must be 8 bytes long. ");
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(ALGORITHM_NAME);
        SecretKey secretKey = keyFactory.generateSecret(new DESKeySpec(key));
        Cipher cipher = Cipher.getInstance(DES_CBC_NOPADDING);
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
        if (key.length < 8)
            throw new IllegalArgumentException("Wrong key size. Key length >= 8. ");
        if (iv.length != 8)
            throw new IllegalArgumentException("Wrong IV length: must be 8 bytes long. ");
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(ALGORITHM_NAME);
        SecretKey secretKey = keyFactory.generateSecret(new DESKeySpec(key));
        Cipher cipher = Cipher.getInstance(DES_CBC_PKCS5PADDING);
        cipher.init(opmode, secretKey, new IvParameterSpec(iv));
        return cipher.doFinal(data);
    }

}
