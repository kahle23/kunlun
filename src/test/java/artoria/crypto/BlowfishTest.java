package artoria.crypto;

import artoria.codec.Base64Utils;
import org.junit.Ignore;
import org.junit.Test;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

// Blowfish data Multiple 8
// Blowfish Key length <= 56
// Blowfish Iv length 8
// SecretKeySpec key = new SecretKeySpec(inputKey, "Blowfish");
// new IvParameterSpec(iv);
// JDK
// "Blowfish/ECB/NoPadding"
// "Blowfish/ECB/PKCS5Padding"
// "Blowfish/CBC/NoPadding"
// "Blowfish/CBC/PKCS5Padding"

public class BlowfishTest {
    private String algorithmName = "Blowfish";
    private byte[] data = "Hello，Java！".getBytes();

    @Ignore
    @Test
    public void ecbNoPadding() throws Exception {
        byte[] key = "YMHADK1XE12U1F925LZNJP3X21U5PIL5ZTOHU1B9CXOQ6449UTI3QQLA".getBytes();
        String trsft = "Blowfish/ECB/NoPadding";
        SecretKey secretKey = CipherUtils.parseSecretKey(algorithmName, key);

        Cipher encrypter = CipherUtils.getEncrypter(trsft, secretKey);
        byte[] bytes = encrypter.doFinal(CipherUtils.fill(data, 8));
        System.out.println(Base64Utils.encodeToString(bytes));

        Cipher decrypter = CipherUtils.getDecrypter(trsft, secretKey);
        byte[] bytes1 = decrypter.doFinal(bytes);
        System.out.println(new String(bytes1));
    }

    @Ignore
    @Test
    public void ecbPKCS5Padding() throws Exception {
        byte[] key = "YMHADK1XE12U1F925LZNJP3X21U5PIL5ZTOHU1B9CXOQ6449UTI3QQLA".getBytes();
        String trsft = "Blowfish/ECB/PKCS5Padding";
        SecretKey secretKey = CipherUtils.parseSecretKey(algorithmName, key);

        Cipher encrypter = CipherUtils.getEncrypter(trsft, secretKey);
        byte[] bytes = encrypter.doFinal(data);
        System.out.println(Base64Utils.encodeToString(bytes));

        Cipher decrypter = CipherUtils.getDecrypter(trsft, secretKey);
        byte[] bytes1 = decrypter.doFinal(bytes);
        System.out.println(new String(bytes1));
    }

    @Ignore
    @Test
    public void cbcNoPadding() throws Exception {
        byte[] key = "YMHADK1XE12U1F925LZNJP3X21U5PIL5ZTOHU1B9CXOQ6449UTI3QQLA".getBytes();
        byte[] iv = "J3CPV1FL".getBytes();
        String trsft = "Blowfish/CBC/NoPadding";
        SecretKey secretKey = CipherUtils.parseSecretKey(algorithmName, key);
        IvParameterSpec ivps = new IvParameterSpec(iv);

        Cipher encrypter = CipherUtils.getEncrypter(trsft, secretKey, ivps);
        byte[] bytes = encrypter.doFinal(CipherUtils.fill(data, 8));
        System.out.println(Base64Utils.encodeToString(bytes));

        Cipher decrypter = CipherUtils.getDecrypter(trsft, secretKey, ivps);
        byte[] bytes1 = decrypter.doFinal(bytes);
        System.out.println(new String(bytes1));
    }

    @Ignore
    @Test
    public void cbcPKCS5Padding() throws Exception {
        byte[] key = "YMHADK1XE12U1F925LZNJP3X21U5PIL5ZTOHU1B9CXOQ6449UTI3QQLA".getBytes();
        byte[] iv = "J3CPV1FL".getBytes();
        String trsft = "Blowfish/CBC/PKCS5Padding";
        SecretKey secretKey = CipherUtils.parseSecretKey(algorithmName, key);
        IvParameterSpec ivps = new IvParameterSpec(iv);

        Cipher encrypter = CipherUtils.getEncrypter(trsft, secretKey, ivps);
        byte[] bytes = encrypter.doFinal(data);
        System.out.println(Base64Utils.encodeToString(bytes));

        Cipher decrypter = CipherUtils.getDecrypter(trsft, secretKey, ivps);
        byte[] bytes1 = decrypter.doFinal(bytes);
        System.out.println(new String(bytes1));
    }

}
