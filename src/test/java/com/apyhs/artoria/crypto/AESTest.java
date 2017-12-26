package com.apyhs.artoria.crypto;

import com.apyhs.artoria.codec.Base64;
import com.apyhs.artoria.exception.ReflectionException;
import com.apyhs.artoria.util.ClassUtils;
import com.apyhs.artoria.util.ReflectUtils;
import org.junit.Test;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.Provider;
import java.security.Security;

// ECB (Electronic Code Book)   false
// CBC (Cipher Block Chaining)  true
// CFB (Cipher FeedBack)        true
// OFB (Output FeedBack)        true
// CTR (Counter)                true

// nopadding

public class AESTest {

    private String algorithmName = "AES";
    private byte[] data = "Hello，Java！".getBytes();
    //    private byte[] key = "IXUSMRSSFJQLHVK93L3HKK7Q5LLD42ZC".getBytes();

    @Test
    public void test_ECB_NoPadding() throws Exception {
        byte[] key = "IXUSMRSSFJQLHVK9".getBytes();
        String trsft = "AES/ECB/NoPadding";
        SecretKeySpec secretKey = Keys.parseSecretKey(algorithmName, key);
        Cipher cipher = Ciphers.getEncrypter(trsft, secretKey);
        // IllegalBlockSizeException: Input length not multiple of 16 bytes
        byte[] bytes = cipher.doFinal(Ciphers.fill(data, 16));
        System.out.println(Base64.encodeToString(bytes));
    }

    @Test
    public void test_CBC_NoPadding() throws Exception {
        byte[] key = "IXUSMRSSFJQLHVK9".getBytes();
        byte[] iv = "XWQ6WCMAQAGFY0BR".getBytes();
        String trsft = "AES/CBC/NoPadding";
        SecretKeySpec secretKey = Keys.parseSecretKey(algorithmName, key);
        IvParameterSpec ivps = new IvParameterSpec(iv);
        Cipher cipher = Ciphers.getEncrypter(trsft, secretKey, ivps);
        // IllegalBlockSizeException: Input length not multiple of 16 bytes
        byte[] bytes = cipher.doFinal(Ciphers.fill(data, 16));
        System.out.println(Base64.encodeToString(bytes));
    }

}

// AES data Multiple 16
// AES Key length only is 16 or 24 or 32 (JDK not 32)
// AES Iv length 16
// SecretKeySpec key = new SecretKeySpec(inputKey, "AES");
// new IvParameterSpec(iv);
// JDK
// "AES/ECB/NoPadding"
// "AES/ECB/PKCS5Padding"
// "AES/CBC/NoPadding"
// "AES/CBC/PKCS5Padding"
