package com.github.kahlkn.artoria.crypto;

import com.github.kahlkn.artoria.codec.Base64;
import org.junit.Test;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;

public class DESTest {

    private String algorithmName = "DES";
    private byte[] data = "Hello，Java！".getBytes();

    @Test
    public void ecbNoPaddingKey8() throws Exception {
        // Key must is 8, if using SecretKeySpec.
        byte[] key = "TAB2QNW4".getBytes();
        String trsft = "DES/ECB/NoPadding";
        SecretKey secretKey = CipherUtils.parseSecretKey(algorithmName, key);

        Cipher encrypter = CipherUtils.getEncrypter(trsft, secretKey);
        byte[] bytes = encrypter.doFinal(CipherUtils.fill(data, 8));
        System.out.println(Base64.encodeToString(bytes));

        Cipher decrypter = CipherUtils.getDecrypter(trsft, secretKey);
        byte[] bytes1 = decrypter.doFinal(bytes);
        System.out.println(new String(bytes1));
    }

    @Test
    public void ecbNoPaddingKeyGt8() throws Exception {
        // The result equal ecbNoPaddingKey8()
        // So DESKeySpec just get before 8 length
        byte[] key = "TAB2QNW4UKPHY".getBytes();
        String trsft = "DES/ECB/NoPadding";
        SecretKey secretKey = CipherUtils.parseSecretKey(algorithmName, new DESKeySpec(key));

        Cipher encrypter = CipherUtils.getEncrypter(trsft, secretKey);
        byte[] bytes = encrypter.doFinal(CipherUtils.fill(data, 8));
        System.out.println(Base64.encodeToString(bytes));

        Cipher decrypter = CipherUtils.getDecrypter(trsft, secretKey);
        byte[] bytes1 = decrypter.doFinal(bytes);
        System.out.println(new String(bytes1));
    }

    @Test
    public void ecbPKCS5Padding() throws Exception {
        byte[] key = "TAB2QNW4".getBytes();
        String trsft = "DES/ECB/PKCS5Padding";
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
        byte[] key = "TAB2QNW4".getBytes();
        byte[] iv = "WLBSQ8CG".getBytes();
        String trsft = "DES/CBC/NoPadding";
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
        byte[] key = "TAB2QNW4".getBytes();
        byte[] iv = "WLBSQ8CG".getBytes();
        String trsft = "DES/CBC/PKCS5Padding";
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

// DES data Multiple 8
// DES Key length = 8 (using DESKeySpec can >= 8, but result is equal)
// DES Iv length 8
// SecretKey key = SecretKeyFactory.getInstance("DES").generateSecret(new DESKeySpec(inputKey));
// new IvParameterSpec(iv);
// JDK
// "DES/ECB/NoPadding"
// "DES/ECB/PKCS5Padding"
// "DES/CBC/NoPadding"
// "DES/CBC/PKCS5Padding"
