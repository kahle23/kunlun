package com.apyhs.artoria.crypto;

import com.apyhs.artoria.codec.Base64;
import org.junit.Test;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

public class DESedeTest {

    private String algorithmName = "DESede";
    private byte[] data = "Hello，Java！".getBytes();

    @Test
    public void ecbNoPadding() throws Exception {
        byte[] key = "XASA4BKBHXLTUAC3G8L76EQ0".getBytes();
        String trsft = "DESede/ECB/NoPadding";
        SecretKey secretKey = CipherUtils.parseSecretKey(algorithmName, key);

        Cipher encrypter = CipherUtils.getEncrypter(trsft, secretKey);
        byte[] bytes = encrypter.doFinal(CipherUtils.fill(data, 8));
        System.out.println(Base64.encodeToString(bytes));

        Cipher decrypter = CipherUtils.getDecrypter(trsft, secretKey);
        byte[] bytes1 = decrypter.doFinal(bytes);
        System.out.println(new String(bytes1));
    }

    @Test
    public void ecbPKCS5Padding() throws Exception {
        byte[] key = "XASA4BKBHXLTUAC3G8L76EQ0".getBytes();
        String trsft = "DESede/ECB/PKCS5Padding";
        SecretKey secretKey = CipherUtils.parseSecretKey(algorithmName, key);

        Cipher encrypter = CipherUtils.getEncrypter(trsft, secretKey);
        byte[] bytes = encrypter.doFinal(data);
        System.out.println(Base64.encodeToString(bytes));

        Cipher decrypter = CipherUtils.getDecrypter(trsft, secretKey);
        byte[] bytes1 = decrypter.doFinal(bytes);
        System.out.println(new String(bytes1));
    }

    @Test
    public void cbcNoPadding() throws Exception {
        byte[] key = "XASA4BKBHXLTUAC3G8L76EQ0".getBytes();
        byte[] iv = "HESN0G1Q".getBytes();
        String trsft = "DESede/CBC/NoPadding";
        SecretKey secretKey = CipherUtils.parseSecretKey(algorithmName, key);
        IvParameterSpec ivps = new IvParameterSpec(iv);

        Cipher encrypter = CipherUtils.getEncrypter(trsft, secretKey, ivps);
        byte[] bytes = encrypter.doFinal(CipherUtils.fill(data, 8));
        System.out.println(Base64.encodeToString(bytes));

        Cipher decrypter = CipherUtils.getDecrypter(trsft, secretKey, ivps);
        byte[] bytes1 = decrypter.doFinal(bytes);
        System.out.println(new String(bytes1));
    }

    @Test
    public void cbcPKCS5Padding() throws Exception {
        byte[] key = "XASA4BKBHXLTUAC3G8L76EQ0".getBytes();
        byte[] iv = "HESN0G1Q".getBytes();
        String trsft = "DESede/CBC/PKCS5Padding";
        SecretKey secretKey = CipherUtils.parseSecretKey(algorithmName, key);
        IvParameterSpec ivps = new IvParameterSpec(iv);

        Cipher encrypter = CipherUtils.getEncrypter(trsft, secretKey, ivps);
        byte[] bytes = encrypter.doFinal(data);
        System.out.println(Base64.encodeToString(bytes));

        Cipher decrypter = CipherUtils.getDecrypter(trsft, secretKey, ivps);
        byte[] bytes1 = decrypter.doFinal(bytes);
        System.out.println(new String(bytes1));
    }

}

// DESede data Multiple 8
// DESede Key length 24
// DESede Iv length 8
// SecretKeySpec key = new SecretKeySpec(inputKey, "DESede");
// new IvParameterSpec(iv);
// JDK
// "DESede/ECB/NoPadding"
// "DESede/ECB/PKCS5Padding"
// "DESede/CBC/NoPadding"
// "DESede/CBC/PKCS5Padding"
