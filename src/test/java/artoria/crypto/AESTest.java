package artoria.crypto;

import artoria.codec.Base64Utils;
import org.junit.Ignore;
import org.junit.Test;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

// ECB (Electronic Code Book)   not need iv
// CBC (Cipher Block Chaining)  need iv
// CFB (Cipher FeedBack)        need iv
// OFB (Output FeedBack)        need iv
// CTR (Counter)                need iv

// NoPadding     when encrypt need fill

// AES data Multiple 16
// AES Key length only is 16 or 24 or 32 (JDK not 32)
// AES Iv length 16
// SecretKeySpec key = new SecretKeySpec(inputKey, "AES");
// new IvParameterSpec(iv);
// "AES/ECB/NoPadding"
// "AES/ECB/PKCS5Padding"
// "AES/CBC/NoPadding"
// "AES/CBC/PKCS5Padding"

public class AESTest {
    private String algorithmName = "AES";
    private byte[] data = "Hello，Java！".getBytes();

    @Test
    public void ecbNoPaddingKey16() throws Exception {
        byte[] key = "IXUSMRSSFJQLHVK9".getBytes();
        String trsft = "AES/ECB/NoPadding";
        SecretKey secretKey = CipherUtils.parseSecretKey(algorithmName, key);

        Cipher encrypter = CipherUtils.getEncrypter(trsft, secretKey);
        // IllegalBlockSizeException: Input length not multiple of 16 bytes
        byte[] bytes = encrypter.doFinal(CipherUtils.fill(data, 16));
        System.out.println(Base64Utils.encodeToString(bytes));

        Cipher decrypter = CipherUtils.getDecrypter(trsft, secretKey);
        byte[] bytes1 = decrypter.doFinal(bytes);
        System.out.println(new String(bytes1));
    }

    @Ignore
    @Test
    public void ecbNoPaddingKey24() throws Exception {
        byte[] key = "IXUSMRSSFJQLHVK9U3NXA2N3".getBytes();
        String trsft = "AES/ECB/NoPadding";
        SecretKey secretKey = CipherUtils.parseSecretKey(algorithmName, key);

        Cipher encrypter = CipherUtils.getEncrypter(trsft, secretKey);
        // IllegalBlockSizeException: Input length not multiple of 16 bytes
        byte[] bytes = encrypter.doFinal(CipherUtils.fill(data, 16));
        System.out.println(Base64Utils.encodeToString(bytes));

        Cipher decrypter = CipherUtils.getDecrypter(trsft, secretKey);
        byte[] bytes1 = decrypter.doFinal(bytes);
        System.out.println(new String(bytes1));
    }

    @Ignore
    @Test
    public void ecbNoPaddingKey32() throws Exception {
        byte[] key = "IXUSMRSSFJQLHVK93L3HKK7Q5LLD42ZC".getBytes();
        String trsft = "AES/ECB/NoPadding";
        SecretKey secretKey = CipherUtils.parseSecretKey(algorithmName, key);

        Cipher encrypter = CipherUtils.getEncrypter(trsft, secretKey);
        // IllegalBlockSizeException: Input length not multiple of 16 bytes
        byte[] bytes = encrypter.doFinal(CipherUtils.fill(data, 16));
        System.out.println(Base64Utils.encodeToString(bytes));

        Cipher decrypter = CipherUtils.getDecrypter(trsft, secretKey);
        byte[] bytes1 = decrypter.doFinal(bytes);
        System.out.println(new String(bytes1));
    }

    @Test
    public void ecbPKCS5Padding() throws Exception {
        byte[] key = "IXUSMRSSFJQLHVK9".getBytes();
        String trsft = "AES/ECB/PKCS5Padding";
        SecretKey secretKey = CipherUtils.parseSecretKey(algorithmName, key);

        Cipher encrypter = CipherUtils.getEncrypter(trsft, secretKey);
        // IllegalBlockSizeException: Input length not multiple of 16 bytes
        byte[] bytes = encrypter.doFinal(CipherUtils.fill(data, 16));
        System.out.println(Base64Utils.encodeToString(bytes));

        Cipher decrypter = CipherUtils.getDecrypter(trsft, secretKey);
        byte[] bytes1 = decrypter.doFinal(bytes);
        System.out.println(new String(bytes1));
    }

    @Test
    public void cbcNoPadding() throws Exception {
        byte[] key = "IXUSMRSSFJQLHVK9".getBytes();
        byte[] iv = "XWQ6WCMAQAGFY0BR".getBytes();
        String trsft = "AES/CBC/NoPadding";
        SecretKey secretKey = CipherUtils.parseSecretKey(algorithmName, key);
        IvParameterSpec ivps = new IvParameterSpec(iv);

        Cipher encrypter = CipherUtils.getEncrypter(trsft, secretKey, ivps);
        // IllegalBlockSizeException: Input length not multiple of 16 bytes
        byte[] bytes = encrypter.doFinal(CipherUtils.fill(data, 16));
        System.out.println(Base64Utils.encodeToString(bytes));

        Cipher decrypter = CipherUtils.getDecrypter(trsft, secretKey, ivps);
        byte[] bytes1 = decrypter.doFinal(bytes);
        System.out.println(new String(bytes1));
    }

    @Test
    public void cbcPKCS5Padding() throws Exception {
        byte[] key = "IXUSMRSSFJQLHVK9".getBytes();
        byte[] iv = "XWQ6WCMAQAGFY0BR".getBytes();
        String trsft = "AES/CBC/PKCS5Padding";
        SecretKey secretKey = CipherUtils.parseSecretKey(algorithmName, key);
        IvParameterSpec ivps = new IvParameterSpec(iv);

        Cipher encrypter = CipherUtils.getEncrypter(trsft, secretKey, ivps);
        // IllegalBlockSizeException: Input length not multiple of 16 bytes
        byte[] bytes = encrypter.doFinal(CipherUtils.fill(data, 16));
        System.out.println(Base64Utils.encodeToString(bytes));

        Cipher decrypter = CipherUtils.getDecrypter(trsft, secretKey, ivps);
        byte[] bytes1 = decrypter.doFinal(bytes);
        System.out.println(new String(bytes1));
    }

}
